package com.example.rma_2_anis_karic;

import android.util.Log;

import java.util.Arrays;
import java.util.function.IntPredicate;

public class Board {

    private final int[][] grid;
    private int score;
    private int hiScore = 0;
    private static final String TAG = "BOARD";

    public Board() {
        this.grid = new int[4][4];
        this.score = 0;
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int score) {
        this.score += score;
        if (this.score > this.hiScore) this.hiScore = this.score;
    }

    public int getHiScore() {
        return hiScore;
    }

    public void clear(){
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                grid[i][j] = 0;

        score = 0;
    }

    public int addNewTiles(){
        //count empty tiles in grid
        int emptyTileCounter = 0;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
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
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++)
                if (grid[i][j] == 0) {
                    if (emptyTileCounter == tileOfChoice) grid[i][j] = tileValue;
                    emptyTileCounter++;
                }
        }

        Log.d(TAG, this.toString());

        return 0;
    }

    public void moveRight(){
        //for each row
        for (int rowIndex = 0; rowIndex < 4; rowIndex++){

            //extract values/tiles
            int[] rowValues = filterEmpty(grid[rowIndex]);

            //if there is no, go to next row
            if (rowValues.length == 0) continue;

            //otherwise, merge same values
            for (int i = rowValues.length - 2; i >= 0; i--){
                if (rowValues[i] == rowValues[i+1]){
                    rowValues[i+1] += rowValues[i];
                    addScore(rowValues[i+1]);
                    rowValues[i] = 0;
                    rowValues = filterEmpty(rowValues);
                }
            }

            //fill up the rest of the row with empty slots/zeroes
            for (int i = 0; i < 4; i++){
                if (i < 4 - rowValues.length)
                    grid[rowIndex][i] = 0;
                else
                    grid[rowIndex][i] = rowValues[i - (4 - rowValues.length)];
            }

            Log.d(TAG, Arrays.toString(grid[rowIndex]));
        }
    }
    public void moveLeft(){
        //for each row
        for (int rowIndex = 0; rowIndex < 4; rowIndex++){

            //extract values/tiles
            int[] rowValues = filterEmpty(grid[rowIndex]);

            //if there is no, go to next row
            if (rowValues.length == 0) continue;

            //otherwise, merge same values
            for (int i = 1; i < rowValues.length; i++){
                if (rowValues[i-1] == rowValues[i]){
                    rowValues[i-1] += rowValues[i];
                    addScore(rowValues[i-1]);
                    rowValues[i] = 0;
                    rowValues = filterEmpty(rowValues);
                }
            }

            //fill up the rest of the row with empty slots/zeroes
            for (int i = 0; i < 4; i++){
                if (i < rowValues.length)
                    grid[rowIndex][i] = rowValues[i];
                else
                    grid[rowIndex][i] = 0;
            }

            Log.d(TAG, Arrays.toString(grid[rowIndex]));
        }
    }
    public void moveDown(){
        //for each row
        for (int columnIndex = 0; columnIndex < 4; columnIndex++){

            //extract values/tiles
            int[] columnValues = new int[4];
            for (int rowIndex = 0; rowIndex < 4; rowIndex++){
                    columnValues[rowIndex] = grid[rowIndex][columnIndex];
            }
            columnValues = filterEmpty(columnValues);

            //if there is no, go to next row
            if (columnValues.length == 0) continue;

            //otherwise, merge same values
            for (int i = 1; i < columnValues.length; i++){
                if (columnValues[i-1] == columnValues[i]){
                    columnValues[i-1] += columnValues[i];
                    addScore(columnValues[i-1]);
                    columnValues[i] = 0;
                    columnValues = filterEmpty(columnValues);
                }
            }

            //clear the entire column
            for (int rowIndex = 0; rowIndex < 4; rowIndex++){
                grid[rowIndex][columnIndex] = 0;
            }

            for (int rowIndex = 0; rowIndex < columnValues.length; rowIndex++){
                grid[rowIndex][columnIndex] = columnValues[rowIndex];
            }

        }
        Log.d(TAG, toString());
    }
    public void moveUp(){
        //for each row
        for (int columnIndex = 0; columnIndex < 4; columnIndex++){

            //extract values/tiles
            int[] columnValues = new int[4];
            for (int rowIndex = 0; rowIndex < 4; rowIndex++){
                columnValues[rowIndex] = grid[rowIndex][columnIndex];
            }
            columnValues = filterEmpty(columnValues);

            //if there is no, go to next row
            if (columnValues.length == 0) continue;

            //otherwise, merge same values
            for (int i = columnValues.length - 2; i >= 0; i--){
                if (columnValues[i] == columnValues[i+1]){
                    columnValues[i+1] += columnValues[i];
                    addScore(columnValues[i+1]);
                    columnValues[i] = 0;
                    columnValues = filterEmpty(columnValues);
                }
            }

            //clear the entire column
            for (int rowIndex = 0; rowIndex < 4; rowIndex++){
                grid[rowIndex][columnIndex] = 0;
            }

            int i = 0;
            for (int rowIndex = columnValues.length-1; rowIndex >= 0; rowIndex--){
                grid[3-i++][columnIndex] = columnValues[rowIndex];
            }

        }
        Log.d(TAG, toString());
    }

    @Override
    public String toString() {
        StringBuilder fullGrid = new StringBuilder();

        for(int i = 0; i < 4; i++)
            fullGrid.append("Board{" + "grid=").append(Arrays.toString(grid[i])).append("}\n");

        return fullGrid.toString();
    }

    private int[] filterEmpty(int[] row){
        return Arrays.stream(row).filter(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value > 0;
            }
        }).toArray();
    }
}
