package com.example.rma_2_anis_karic;

import android.util.Log;

import java.util.Arrays;
import java.util.function.IntPredicate;

public class Board {

    private final int[][] grid;
    private int score;
    private int hiScore = 0;
    private int[][] saveState;
    private static final String TAG = "BOARD";

    public Board() {
        this.grid = new int[4][4];
        this.score = 0;
    }

    public int[][] getSaveState() {
        return saveState;
    }

    private void setSaveState(int[][] saveState) {
        this.saveState = saveState;
    }

    public int loadSaveState() {
        if (saveState != null) {
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < 4; j++)
                    grid[i][j] = saveState[i][j];

            return 0;
        } else return 1;
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

    public void clear() {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                grid[i][j] = 0;

        score = 0;
    }

    public int addNewTiles() {
        //count empty tiles in grid
        int emptyTileCounter = 0;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (grid[i][j] == 0) emptyTileCounter++;

        if (emptyTileCounter == 0) return 1;

        //choose random empty tile
        int tileOfChoice = ((int) (Math.random() * 100000)) % emptyTileCounter;

        //choose value randomly 2->90%, 4->10%
        int tileValue;
        if (((int) (Math.random() * 10)) % 10 == 0) tileValue = 4;
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

    public void moveLeft() {
        //for each row
        for (int rowIndex = 0; rowIndex < 4; rowIndex++) {

            //extract values/tiles
            int[] rowValues = filterEmpty(grid[rowIndex]);

            //if there is no, go to next row
            if (rowValues.length == 0) continue;

            //otherwise, merge same values
            rowValues = merge(rowValues);

            //extends the values to a full row, by filling in zeroes
            rowValues = extend(rowValues);

            //apply the change
            grid[rowIndex] = rowValues;


            setSaveState(grid);

            Log.d(TAG, Arrays.toString(grid[rowIndex]));
        }
    }

    public void moveRight() {
        //for each row
        for (int rowIndex = 0; rowIndex < 4; rowIndex++) {

            //extract values/tiles
            int[] rowValues = filterEmpty(grid[rowIndex]);

            //if there is no, go to next row
            if (rowValues.length == 0) continue;

            /*
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
             */

            //reverse merge, right to left
            rowValues = merge(reverse(rowValues));

            //extends the values to a full row, by filling in zeroes
            rowValues = extend(rowValues);

            //reverses the full row, alligning it from the right
            rowValues = reverse(rowValues);

            //applies the change
            grid[rowIndex] = rowValues;


            setSaveState(grid);

            Log.d(TAG, Arrays.toString(grid[rowIndex]));
        }
    }

    public void moveUp() {
        //for each row
        for (int columnIndex = 0; columnIndex < 4; columnIndex++) {

            //extract values/tiles
            int[] columnValues = getColumn(columnIndex);

            columnValues = filterEmpty(columnValues);

            //if there is no, go to next row
            if (columnValues.length == 0) continue;

            //otherwise, merge same values
            columnValues = merge(columnValues);

            //extends the values to a full row, by filling in zeroes
            columnValues = extend(columnValues);

            //applies the change
            setColumn(columnIndex, columnValues);

        }

        setSaveState(grid);

        Log.d(TAG, toString());
    }

    public void moveDown() {
        //for each row
        for (int columnIndex = 0; columnIndex < 4; columnIndex++) {

            //extract values/tiles
            int[] columnValues = getColumn(columnIndex);

            columnValues = filterEmpty(columnValues);

            //if there is no, go to next row
            if (columnValues.length == 0) continue;

            /*
            //otherwise, merge same values
            for (int i = columnValues.length - 2; i >= 0; i--){
                if (columnValues[i] == columnValues[i+1]){
                    columnValues[i+1] += columnValues[i];
                    addScore(columnValues[i+1]);
                    columnValues[i] = 0;
                    columnValues = filterEmpty(columnValues);
                }
            }
            */

            //reverse merge, bottom to top
            columnValues = merge(reverse(columnValues));

            //extends the values to a full row, by filling in zeroes
            columnValues = extend(columnValues);

            //reverses the full row, alligning it from the right
            columnValues = reverse(columnValues);

            //applies the change
            setColumn(columnIndex, columnValues);
        }

        setSaveState(grid);

        Log.d(TAG, toString());
    }

    @Override
    public String toString() {
        StringBuilder fullGrid = new StringBuilder();

        for (int i = 0; i < 4; i++)
            fullGrid.append("Board{" + "grid=").append(Arrays.toString(grid[i])).append("}\n");

        return fullGrid.toString();
    }

    private int[] merge(int[] values) {
        for (int i = 1; i < values.length; i++) {
            if (values[i - 1] == values[i]) {
                values[i - 1] += values[i];
                addScore(values[i - 1]);
                values[i] = 0;
                values = filterEmpty(values);
            }
        }
        return values;
    }

    private int[] extend(int[] values) {
        int[] fullRow = new int[4];

        for (int i = 0; i < fullRow.length; i++) {
            if (i < values.length)
                fullRow[i] = values[i];
            else fullRow[i] = 0;
        }

        return fullRow;
    }

    private int[] reverse(int[] arr) {
        int[] result = new int[arr.length];
        for (int i = 0; i < arr.length; i++)
            result[arr.length - 1 - i] = arr[i];

        return result;
    }

    private int[] filterEmpty(int[] values) {
        return Arrays.stream(values).filter(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value > 0;
            }
        }).toArray();
    }

    private int[] getColumn(int columnIndex) {
        int[] columnValues = new int[4];
        for (int rowIndex = 0; rowIndex < 4; rowIndex++) {
            columnValues[rowIndex] = grid[rowIndex][columnIndex];
        }

        return columnValues;
    }

    private void setColumn(int columnIndex, int[] columnValues) {
        for (int rowIndex = 0; rowIndex < columnValues.length; rowIndex++) {
            grid[rowIndex][columnIndex] = columnValues[rowIndex];
        }
    }
}
