package com.example.rma_2_anis_karic;

import java.util.Arrays;

public class Board {

    private int[][] grid;
    private int dimension;

    public Board(int dimension) {
        this.grid = new int[dimension][dimension];
        this.dimension = dimension;
    }

    public int[][] getGrid() {
        return grid;
    }

    public void clear(){
        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++)
                grid[i][j] = 0;
    }

    private void addNewTiles(){
        int emptyTileCounter = 0;
        for(int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++)
                if (grid[i][j] == 0) emptyTileCounter++;

        int tileOfChoice = (int) ((Math.random() * 100));
    }

    @Override
    public String toString() {
        return "Board{" +
                "grid=" + Arrays.toString(grid) +
                '}';
    }
}
