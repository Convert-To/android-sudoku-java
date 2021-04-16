package com.zhengtao.sudoku.game;

import java.util.List;

public class Board {
    // 大小
    int size;
    // 单元格集合
    List<Cell> cells;
    public Board(int size, List<Cell> cells) {
        this.size = size;
        this.cells = cells;
    }
    // 根据传入的行列计算索引 获取cells中对应索引的单元个Cell
    public Cell getCell (int row, int col) {
        return this.cells.get(row * size + col);
    }

}
