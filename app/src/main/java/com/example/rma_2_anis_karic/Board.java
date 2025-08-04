package com.example.rma_2_anis_karic;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final List<List<Tile>> grid;
    private int score;
    private int hiScore = 0;

    private List<List<Tile>> saveState;
    private int scoreSave;
    private int hiScoreSave;

    private static final String TAG = "BOARD";//DEBUG TAG

    public Board() {
        this.grid = getNewGrid();
        addNewTiles();
        addNewTiles();
        this.saveState = getGridImage();

        this.score = 0;
    }//CONSTRUCTOR

    //GET
    public List<List<Tile>> getGrid() {
        return grid;
    }
    public int getScore() {
        return score;
    }
    public int getHiScore() {
        return hiScore;
    }

    //SCORE / HISCORE UPDATE
    public void addScore(int score) {
        this.score += score;
        if (this.score > this.hiScore) this.hiScore = this.score;
    }

    //SAVESTATE
    private void setSaveState(List<List<Tile>> previousGridImage,int previousScore,int previousHiScore) {
        if (!gridChanged(previousGridImage)) return;

        scoreSave = previousScore;
        hiScoreSave = previousHiScore;

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                saveState.get(i).get(j).setValue( previousGridImage.get(i).get(j).getValue() );
    }
    public List<List<Tile>> getSaveState() {
        return saveState;
    }
    public void loadSaveState() {
        score = scoreSave;
        hiScore = hiScoreSave;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                grid.get(i).get(j).setValue(saveState.get(i).get(j).getValue());
            }
        }
    }

    //VIEWMODEL COMMANDS
    public void clear() {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                grid.get(i).get(j).setValue(0);

        score = 0;
    }
    public int addNewTiles() {
        //count empty tiles in grid
        int emptyTileCounter = 0;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (grid.get(i).get(j).getValue() == 0) emptyTileCounter++;

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
                if (grid.get(i).get(j).getValue() == 0) {
                    if (emptyTileCounter == tileOfChoice) grid.get(i).get(j).setValue(tileValue);
                    emptyTileCounter++;
                }
        }

        Log.d(TAG, this.toString());

        return 0;
    }

    //MOVE
    public void moveLeft() {
        List<List<Tile>> gridImage = getGridImage();
        int score = getScore();
        int hiscore = getHiScore();

        //for each row
        for (int rowIndex = 0; rowIndex < 4; rowIndex++) {

            //extract values/tiles
            List<Tile> rowValues = grid.get(rowIndex);

            //if there is no, go to next row
            if (rowValues.size() == 0) continue;

            //otherwise, merge same values
            merge(rowValues);

            //apply the change
            grid.set(rowIndex, rowValues);
        }
        setSaveState(gridImage,score,hiscore);
        Log.d(TAG, toString());
    }
    public void moveRight() {
        List<List<Tile>> gridImage = getGridImage();
        int score = getScore();
        int hiscore = getHiScore();

        //for each row
        for (int rowIndex = 0; rowIndex < 4; rowIndex++) {

            //extract values/tiles
            List<Tile> rowValues = grid.get(rowIndex);

            //if there is no, go to next row
            if (rowValues.size() == 0) continue;

            //reverse the tile order
            rowValues = reverse(rowValues);

            //merge same value tiles
            merge(rowValues);

            //reverses again, aligning tiles to the right
            rowValues = reverse(rowValues);

            //applies the change
            grid.set(rowIndex, rowValues);
        }
        setSaveState(gridImage,score,hiscore);
        Log.d(TAG, toString());
    }
    public void moveUp() {
        List<List<Tile>> gridImage = getGridImage();
        int score = getScore();
        int hiscore = getHiScore();

        //for each row
        for (int columnIndex = 0; columnIndex < 4; columnIndex++) {

            //extract values/tiles
            List<Tile> columnValues = getColumn(columnIndex);

            //if there is no, go to next row
            if (columnValues.size() == 0) continue;

            //otherwise, merge same values
            merge(columnValues);

            //applies the change
            setColumn(columnIndex, columnValues);

        }
        //update row and column fields
        setSaveState(gridImage,score,hiscore);

        Log.d(TAG, toString());
    }
    public void moveDown() {
        List<List<Tile>> gridImage = getGridImage();
        int score = getScore();
        int hiscore = getHiScore();

        //for each row
        for (int columnIndex = 0; columnIndex < 4; columnIndex++) {

            //extract values/tiles
            List<Tile> columnValues = getColumn(columnIndex);

            //if there is no, go to next row
            if (columnValues.size() == 0) continue;

            //reverse the tile order
            columnValues = reverse(columnValues);

            //merge same value tiles
            merge(columnValues);

            //reverses again, aligning tiles to the right
            columnValues = reverse(columnValues);

            //applies the change
            setColumn(columnIndex, columnValues);
        }
        setSaveState(gridImage,score,hiscore);
        Log.d(TAG, toString());
    }

    //MOVE UTILITIES
    private void merge(List<Tile> tiles) {
        floatEmptyTilesToEnd(tiles);

        for (int i = 1; i < tiles.size(); i++) {
            Tile current = tiles.get(i);
            Tile previous = tiles.get(i - 1);

            if (current.getValue() != 0  && previous.getValue() == current.getValue()) {
                previous.setValue(previous.getValue() * 2);
                addScore(previous.getValue());
                current.setValue(0);
            }
        }
        floatEmptyTilesToEnd(tiles);

    }
    private List<Tile> reverse(List<Tile> normal) {
        List<Tile> reverse = new ArrayList<>(4);

        for (int i = 0; i < normal.size(); i++)
            reverse.add(normal.get(normal.size()-1-i));


        return reverse;
    }
    private void floatEmptyTilesToEnd(List<Tile> tiles){
        int i = 0;
        int size = tiles.size();
        while (i < size){
            if (tiles.get(i).getValue() == 0){
                tiles.add(tiles.remove(i));
                size--;
            }
            else i++;
        }
    }

    //COLUMN SHORTCUTS
    private List<Tile> getColumn(int columnIndex) {
        List<Tile> columnValues = new ArrayList<Tile>(4);

        for (int rowIndex = 0; rowIndex < 4; rowIndex++)
            columnValues.add(this.grid.get(rowIndex).get(columnIndex));

        return columnValues;
    }
    private void setColumn(int columnIndex, List<Tile> columnValues) {
        for (int rowIndex = 0; rowIndex < columnValues.size(); rowIndex++) {
            grid.get(rowIndex).set(columnIndex, columnValues.get(rowIndex));
        }
    }

    //GRID CHANGE DETECTION
    private List<List<Tile>> copyGrid(List<List<Tile>> original){
        List<List<Tile>> copy = new ArrayList<>(4);

        for (int i = 0; i < 4; i++) {
            copy.add(new ArrayList<>(4));
            for (int j = 0; j < 4; j++) {
                Tile old = original.get(i).get(j);
                copy.get(i).add(new Tile(i, j, old.getValue()));

            }
        }
        return copy;
    }//DEEP COPY OF ANY LIST<LIST<TILE>> GRID
    public List<List<Tile>> getGridImage(){
        return copyGrid(this.grid);
    }//deep copy of THE GRID(current)
    public boolean gridChanged(List<List<Tile>> previousGridImage) {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (grid.get(i).get(j).getValue() != previousGridImage.get(i).get(j).getValue()) return true;

        return false;
    }//checks for diff between current and given grid, for use with previous grid states
    private List<List<Tile>> getNewGrid(){
        List<List<Tile>> blank = new ArrayList<List<Tile>>(4);

        for (int i = 0; i < 4; i++) {
            blank.add(new ArrayList<>(4));
            for (int j = 0; j < 4; j++) {
                blank.get(i).add(new Tile(i, j, 0));
            }
        }

        return blank;
    }//creates a new grid with new ids

    @Override
    public String toString() {
        StringBuilder fullGrid = new StringBuilder();

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                fullGrid.append("Board{" + "grid=").append(grid.get(i).get(j).toString()).append("}\n");

        return fullGrid.toString();
    }//TOSTRING
}
