package com.example.rma_2_anis_karic;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class Manager extends AndroidViewModel {

    private static final String TAG = "MANAGER";//DEBUG TAG
    private final Board board = new Board();

    //LIVE DATA
    private final MutableLiveData<HashMap<Long, Tile>> liveTiles = new MutableLiveData<HashMap<Long, Tile>>();
    private final MutableLiveData<Integer> liveScore = new MutableLiveData<>();
    private final MutableLiveData<Integer> liveHiScore = new MutableLiveData<>();

    //SOUND EFFECTS
    private final AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
    private final SoundPool soundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(audioAttributes).build();
    private final int soundClick = soundPool.load(this.getApplication(), R.raw.click, 1);
    private final int soundWin = soundPool.load(this.getApplication(), R.raw.win, 1);
    private final int soundLose = soundPool.load(this.getApplication(), R.raw.lose, 1);

    //BLOCK FLAG
    private boolean isBlocked = false;

    //CONSTRUCTOR
    public Manager(@NonNull Application application) {
        super(application);
    }

    //LIVEDATA GET
    public LiveData<HashMap<Long, Tile>> getTiles() {
        return liveTiles;
    }
    public LiveData<Integer> getScore() {
        return liveScore;
    }
    public LiveData<Integer> getHiScore() {
        return liveHiScore;
    }

    //VIEW BUTTON COMMANDS
    public void newGame() {
        board.clear();
        board.addNewTiles();
        board.addNewTiles();
        board.addNewTiles();
        board.addNewTiles();
        board.addNewTiles();
        board.addNewTiles(1024);
        board.addNewTiles(512);
        board.addNewTiles(256);
        board.addNewTiles(128);
        board.addNewTiles(32);
        board.addNewTiles(64);

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("gameState", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

        updateTiles();
        liveScore.setValue(board.getScore());
        liveHiScore.setValue(board.getHiScore());
        unblockGrid();
    }
    public void undo() {
        board.loadSaveState();

        updateTiles();
        liveScore.setValue(board.getScore());
        liveHiScore.setValue(board.getHiScore());
    }

    //GRID SWIPE GESTURE HANDLERS
    public void swipeRightHandler() {
        if (isBlocked) return;
        List<List<Tile>> previousGridImage = board.getGridImage();
        board.moveRight();

        if (board.gridChanged(previousGridImage)){
            board.addNewTiles();
            playClick();
        }

        updateTiles();
        liveScore.setValue(board.getScore());
        liveHiScore.setValue(board.getHiScore());
    }
    public void swipeLeftHandler() {
        if (isBlocked) return;
        List<List<Tile>> previousGridImage = board.getGridImage();
        board.moveLeft();

        if (board.gridChanged(previousGridImage)){
            board.addNewTiles();
            playClick();
        }

        updateTiles();
        liveScore.setValue(board.getScore());
        liveHiScore.setValue(board.getHiScore());
    }
    public void swipeUpHandler() {
        if (isBlocked) return;
        List<List<Tile>> previousGridImage = board.getGridImage();
        board.moveUp();

        if (board.gridChanged(previousGridImage)){
            board.addNewTiles();
            playClick();
        }

        updateTiles();
        liveScore.setValue(board.getScore());
        liveHiScore.setValue(board.getHiScore());
    }
    public void swipeDownHandler() {
        if (isBlocked) return;
        List<List<Tile>> previousGridImage = board.getGridImage();
        board.moveDown();

        if (board.gridChanged(previousGridImage)){
            board.addNewTiles();
            playClick();
        }

        updateTiles();
        liveScore.setValue(board.getScore());
        liveHiScore.setValue(board.getHiScore());
    }

    //DATA STRUCTURE CONVERTER
    private void updateTiles() {
        HashMap<Long, Tile> tiles = new HashMap<>();

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                Tile currentTile = board.getGrid().get(row).get(col);
                tiles.put(currentTile.getId() ,currentTile);
            }
        }
        liveTiles.setValue(tiles);
        setGameState();

        Log.d(TAG, board.toString());
    }

    //STATE SAVING
    public void setGameState(){
        GameState fullState = board.createGameState();
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("gameState", Context.MODE_PRIVATE);

        sharedPreferences.edit().putString("fullState", new Gson().toJson(fullState)).apply();
    }
    public GameState getGameState(){
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("gameState", Context.MODE_PRIVATE);

        String json = sharedPreferences.getString("fullState",null);

        if (json == null) return null;
        else return new Gson().fromJson(json, GameState.class);
    }
    public void loadGameState(GameState gameState){
        board.restoreGameState(gameState);
        updateTiles();
    }

    //GAME END
    public int checkGameEnd(){
        if (board.checkWin()) return 1;
        if (board.checkLose()) return 2;
        return 0;
    }

    //SOUND PLAYING
    public void playWin(){
        soundPool.play(soundWin,1f,1f,2,0,1);
    }
    public void playLose(){
        soundPool.play(soundLose, 1f, 1f, 2, 0, 1);
    }
    public void playClick(){
        soundPool.play(soundClick, 1f, 1f, 1, 0, 1);
    }

    public void blockGrid(){isBlocked = true;}
    public void unblockGrid(){isBlocked = false;}

}
