package com.example.rma_2_anis_karic;

import android.util.Log;

import java.util.Arrays;

public class Board {

    private final int[][] grid;
    private final int dimension;
    private int score;
    private int hiScore = 0;
    private static final String TAG = "BOARD";

    public Board(int dimension) {
        this.grid = new int[dimension][dimension];
        this.dimension = dimension;
        this.score = 0;
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getDimension() {
        return dimension;
    }

    public int getScore() {
        return score;
    }

    public int getHiScore() {
        return hiScore;
    }

    public void clear(){
        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++)
                grid[i][j] = 0;

        score = 0;
    }

    public int addNewTiles(){
        //count empty tiles in grid
        int emptyTileCounter = 0;
        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++)
                if (grid[i][j] == 0) emptyTileCounter++;

        if(emptyTileCounter == 0) return 1;

        //choose random empty tile
        int tileOfChoice =((int)(Math.random() * 100000)) % emptyTileCounter;

        //choose value randomly 2->90%, 4->10%
        int tileValue;
        if(((int)(Math.random() * 10)) % 10 == 0) tileValue = 4;
        else tileValue = 2;

        //find the chosen tile and assign the new value
        emptyTileCounter = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++)
                if (grid[i][j] == 0) {
                    if (emptyTileCounter == tileOfChoice) grid[i][j] = tileValue;
                    emptyTileCounter++;
                }
        }

        Log.d(TAG, this.toString());
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder fullGrid = new StringBuilder();

        for(int i = 0; i < dimension; i++)
            fullGrid.append("Board{" + "grid=").append(Arrays.toString(grid[i])).append('}');

        return fullGrid.toString();
    }
}
