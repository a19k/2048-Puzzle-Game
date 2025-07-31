package com.example.rma_2_anis_karic;

import java.util.Objects;

public class Tile {

    private final long id;
    private int row;
    private int col;
    private int value;

    private static long ID_COUNTER = 0;

    public Tile(int row, int col, int value) {
        this.id = ID_COUNTER++;
        this.row = row;
        this.col = col;
        this.value = value;
    }

    public long getId() {
        return id;
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


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return id == tile.id && row == tile.row && col == tile.col && value == tile.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, row, col, value);
    }

    @Override
    public String toString() {
        return "Tile{" +
                "id=" + id +
                ", row=" + row +
                ", col=" + col +
                ", value=" + value +
                '}';
    }
}
