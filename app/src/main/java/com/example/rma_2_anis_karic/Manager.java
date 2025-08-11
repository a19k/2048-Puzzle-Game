package com.example.rma_2_anis_karic;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.List;

public class Manager extends ViewModel {

    private static final String TAG = "MANAGER";//DEBUG TAG
    private final Board board = new Board();

    private final MutableLiveData<HashMap<Long, Tile>> liveTiles = new MutableLiveData<HashMap<Long, Tile>>();
    private final MutableLiveData<Integer> liveScore = new MutableLiveData<>();
    private final MutableLiveData<Integer> liveHiScore = new MutableLiveData<>();

    public Manager() {
        updateTiles();
    }//CONSTRUCTOR

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

        updateTiles();
        liveScore.setValue(board.getScore());
        liveHiScore.setValue(board.getHiScore());
    }
    public void undo() {
        board.loadSaveState();

        updateTiles();
        liveScore.setValue(board.getScore());
        liveHiScore.setValue(board.getHiScore());
    }

    //GRID SWIPE GESTURE HANDLERS
    public void swipeRightHandler() {
        List<List<Tile>> previousGridImage = board.getGridImage();
        board.moveRight();

        if (board.gridChanged(previousGridImage))
            board.addNewTiles();

        updateTiles();
        liveScore.setValue(board.getScore());
        liveHiScore.setValue(board.getHiScore());
    }
    public void swipeLeftHandler() {
        List<List<Tile>> previousGridImage = board.getGridImage();
        board.moveLeft();

        if (board.gridChanged(previousGridImage))
            board.addNewTiles();

        updateTiles();
        liveScore.setValue(board.getScore());
        liveHiScore.setValue(board.getHiScore());
    }
    public void swipeUpHandler() {
        List<List<Tile>> previousGridImage = board.getGridImage();
        board.moveUp();

        if (board.gridChanged(previousGridImage))
            board.addNewTiles();

        updateTiles();
        liveScore.setValue(board.getScore());
        liveHiScore.setValue(board.getHiScore());
    }
    public void swipeDownHandler() {
        List<List<Tile>> previousGridImage = board.getGridImage();
        board.moveDown();

        if (board.gridChanged(previousGridImage))
            board.addNewTiles();

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

        Log.d(TAG, board.toString());
    }

}
