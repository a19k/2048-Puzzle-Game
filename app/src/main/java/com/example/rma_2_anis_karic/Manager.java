package com.example.rma_2_anis_karic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class Manager extends ViewModel {

    private final Board board = new Board();

    private final MutableLiveData<List<List<Integer>>> liveGrid = new MutableLiveData<>();
    private final MutableLiveData<Integer> liveScore = new MutableLiveData<>();
    private final MutableLiveData<Integer> liveHiScore = new MutableLiveData<>();

    public Manager() {
        newGame();
    }

    private void newGame(){
        board.clear();
        board.addNewTiles();
        board.addNewTiles();
        board.addNewTiles();
        board.addNewTiles();
        board.addNewTiles();
        board.addNewTiles();
        board.addNewTiles();
        board.addNewTiles();
        board.addNewTiles();
        board.addNewTiles();
        board.addNewTiles();
        board.addNewTiles();
        board.addNewTiles();
        board.addNewTiles();
        board.addNewTiles();
        board.addNewTiles();
        board.moveUp();
        liveGrid.setValue(convert2DArrayTo2DList(board.getGrid()));
        liveScore.setValue(board.getScore());
        liveHiScore.setValue(board.getHiScore());
    }


    public LiveData<List<List<Integer>>> getGrid(){
        return liveGrid;
    }

    public LiveData<Integer> getScore() {
        return liveScore;
    }

    public LiveData<Integer> getHiScore() {
        return liveHiScore;
    }

    private List<List<Integer>> convert2DArrayTo2DList(int [][] grid){
        List<List<Integer>> convertedGrid = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                row.add(grid[i][j]);
            }
            convertedGrid.add(row);
        }

        return convertedGrid;
    }


}
