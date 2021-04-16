package com.zhengtao.sudoku.game;

import android.content.Context;
import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import com.zhengtao.sudoku.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

public class SudokuGame {
    public MutableLiveData<Pair<Integer, Integer>> selectedCellLiveData = new MutableLiveData<>();
    public MutableLiveData<List<Cell>> cellsLiveData = new MutableLiveData<>();

    private int size = 9; // 大小
    private int selectedRow = -1; // 选中行
    private int selectedCol = -1; // 选中列
    private List<Cell> cells = new ArrayList<>(); // 存储谜题的单元格集合
    private int[][] puzzleAnswer; // 谜题答案
    private List<Cell> usedCells = new ArrayList<>(); // 存储使用过的单元格
    private Board board = null;



    /**
     * 开始
     */
    public void startPuzzle (Context context ,int diff,boolean resume) {
        puzzleAnswer = resume ? DataUtils.getAnswerData(context) : Puzzle.getAnswer();
        cells = resume ? DataUtils.getPuzzleData(context) : Puzzle.generatePuzzle(diff);

        board = new Board(size, cells);
        selectedCellLiveData.postValue(new Pair(selectedRow, selectedCol));
        cellsLiveData.postValue(board.cells);
    }

    /**
     * 更新所选单元格
     *
     * @param row
     *              选中行
     * @param col
     *              选中列
     */
    public void updateSelectedCell(int row, int col) {
        selectedRow = row;
        selectedCol = col;

        selectedCellLiveData.postValue(new Pair(selectedRow, selectedCol));
    }
    /**
     * 输入处理
     *
     * @param num
     *              数字键盘对应的数字
     */
    public void handleInput(int num) {
        if (selectedRow == -1 || selectedCol == -1) return;
        Cell cell = board.getCell(selectedRow,selectedCol);
        if (cell.isStartingCell()) return;
        if (num!=0 || cell.getValue() !=0)usedCells.add(new Cell(selectedRow,selectedCol,cell.getValue(),false));
        cell.setValue(num);
        cell.setStartingCell(false);
        cellsLiveData.postValue(board.cells);
    }
    /**
     * 刷新单元格
     */
    public void refreshCells(int diff) {
        puzzleAnswer = Puzzle.getAnswer();
        cells = Puzzle.generatePuzzle(diff);

        board = new Board(size, cells);
        selectedRow = -1;
        selectedCol = -1;
        cellsLiveData.postValue(board.cells);
        selectedCellLiveData.postValue(new Pair(selectedRow,selectedCol));
    }
    /**
     * 检查数独与答案是否一致
     *
     * @return boolean
     *              正确返回true 错误返回false
     */
    public boolean checkSudoku() {
        //遍历当前数独表格 的每个单元格
        for (Cell cell:board.cells) {
            //比对答案 如果单元格的数据 等于0 或者 跟答案不一样 返回false
            if  (cell.getValue() == 0 || cell.getValue() != puzzleAnswer[cell.getRow()][cell.getCol()]) {
                return false;
            }
        }
        //如果没有错误返回真
        return true;
    }

    /**
     * 显示当前所选单元格对应的正确数值
     */
    public void showCellAns() {
        if (selectedRow == -1 || selectedCol == -1) return;
        Cell cell = board.getCell(selectedRow,selectedCol);
        usedCells.add(new Cell(selectedRow,selectedCol,cell.getValue(),false));
        cell.setValue(puzzleAnswer[selectedRow][selectedCol]);
        cellsLiveData.postValue(board.cells);
    }
    /**
     * 撤销操作
     */
    public void undo() {
        int index = usedCells.size() - 1;
        if (index < 0) return;
        Cell cell = board.getCell(usedCells.get(index).getRow(),usedCells.get(index).getCol());
        cell.setValue(usedCells.get(index).getValue());
        usedCells.remove(index);
        cellsLiveData.postValue(board.cells);
    }
    public List<Cell> getCells() {
        return cells;
    }

    public int[][] getPuzzleAnswer() { return puzzleAnswer; }
}

