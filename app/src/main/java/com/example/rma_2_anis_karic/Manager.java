package com.example.rma_2_anis_karic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class Manager extends ViewModel {

    private final Board board = new Board(4);

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

        for (int i = 0; i < board.getDimension(); i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < board.getDimension(); j++) {
                row.add(grid[i][j]);
            }
            convertedGrid.add(row);
        }

        return convertedGrid;
    }


}
