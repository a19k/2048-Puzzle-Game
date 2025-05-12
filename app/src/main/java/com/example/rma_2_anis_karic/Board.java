package com.example.rma_2_anis_karic;

import android.util.Log;

import java.util.Arrays;

public class Board {

    private final int[][] grid;
    private final int dimension;
    private static final String TAG = "BOARD";

    public Board(int dimension) {
        this.grid = new int[dimension][dimension];
        this.dimension = dimension;
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getDimension() {
        return dimension;
    }

    public void clear(){
        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++)
                grid[i][j] = 0;
    }

    public void addNewTiles(){
        //count empty tiles in grid
        int emptyTileCounter = 0;
        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++)
                if (grid[i][j] == 0) emptyTileCounter++;

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
    }

    @Override
    public String toString() {
        StringBuilder fullGrid = new StringBuilder();

        for(int i = 0; i < dimension; i++)
            fullGrid.append("Board{" + "grid=").append(Arrays.toString(grid[i])).append('}');

        return fullGrid.toString();
    }
}
