package com.example.rma_2_anis_karic;

import java.util.Objects;

public class Tile {

    private int row;
    private int col;
    private int value;
    private boolean isNew;
    private boolean justMerged;
    private int previousRow;
    private int previousCol;

    public Tile(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
        isNew = true;
        justMerged = false;
    }

    public int getRow() {
        return row;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public int getCol() {
        return col;
    }
    public void setCol(int col) {
        this.col = col;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    public boolean isNew() {
        return isNew;
    }
    public void setNew(boolean aNew) {
        isNew = aNew;
    }
    public boolean isJustMerged() {
        return justMerged;
    }
    public void setJustMerged(boolean justMerged) {
        this.justMerged = justMerged;
    }
    public int getPreviousRow() {
        return previousRow;
    }
    public void setPreviousRow(int previousRow) {
        this.previousRow = previousRow;
    }
    public int getPreviousCol() {
        return previousCol;
    }
    public void setPreviousCol(int previousCol) {
        this.previousCol = previousCol;
    }

    public void resetFlags() {
        isNew = false;
        justMerged = false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return row == tile.row && col == tile.col && value == tile.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, value);
    }
}
