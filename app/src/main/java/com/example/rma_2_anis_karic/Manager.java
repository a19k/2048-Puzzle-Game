package com.example.rma_2_anis_karic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class Manager extends ViewModel {

    private final Board board = new Board();

    private final MutableLiveData<List<Tile>> liveTiles = new MutableLiveData<>();
    private final MutableLiveData<Integer> liveScore = new MutableLiveData<>();
    private final MutableLiveData<Integer> liveHiScore = new MutableLiveData<>();

    public Manager() {
        newGame();
    }//CONSTRUCTOR

    //LIVEDATA GET
    public LiveData<List<Tile>> getTiles() {
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
        int[][] previousGridImage = board.getGridImage();
        board.moveRight();

        if (board.gridChanged(previousGridImage))
            board.addNewTiles();

        updateTiles();
        liveScore.setValue(board.getScore());
        liveHiScore.setValue(board.getHiScore());
    }
    public void swipeLeftHandler() {
        int[][] previousGridImage = board.getGridImage();
        board.moveLeft();

        if (board.gridChanged(previousGridImage))
            board.addNewTiles();

        updateTiles();
        liveScore.setValue(board.getScore());
        liveHiScore.setValue(board.getHiScore());
    }
    public void swipeUpHandler() {
        int[][] previousGridImage = board.getGridImage();
        board.moveUp();

        if (board.gridChanged(previousGridImage))
            board.addNewTiles();

        updateTiles();
        liveScore.setValue(board.getScore());
        liveHiScore.setValue(board.getHiScore());
    }
    public void swipeDownHandler() {
        int[][] previousGridImage = board.getGridImage();
        board.moveDown();

        if (board.gridChanged(previousGridImage))
            board.addNewTiles();

        updateTiles();
        liveScore.setValue(board.getScore());
        liveHiScore.setValue(board.getHiScore());
    }

    //DATA STRUCTURE CONVERTER
    private void updateTiles() {
        int[][] grid = board.getGrid();
        List<Tile> tiles = new ArrayList<>();

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                    tiles.add(new Tile(row, col,grid[row][col]));
            }
        }
        liveTiles.setValue(tiles);
    }

}
