package com.zhengtao.sudoku.game;

public class Cell {
    private int row;   // 行
    private int col;   // 列
    private int value; // 值
    private boolean isStartingCell; // 是初始单元格
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

    public void setValue(int col) {
        this.value = col;
    }

    public boolean isStartingCell() {
        return isStartingCell;
    }

    public void setStartingCell(boolean startingCell) {
        isStartingCell = startingCell;
    }

    public Cell(int row, int col, int value , boolean isStartingCell) {
        this.row = row;
        this.col = col;
        this.value = value;
        this.isStartingCell = isStartingCell;
    }
}

