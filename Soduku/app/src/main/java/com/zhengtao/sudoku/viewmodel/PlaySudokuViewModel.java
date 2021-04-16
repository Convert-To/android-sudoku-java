package com.zhengtao.sudoku.viewmodel;

import androidx.lifecycle.ViewModel;

import com.zhengtao.sudoku.game.SudokuGame;

public class PlaySudokuViewModel extends ViewModel {
    public SudokuGame sudokuGame = new SudokuGame();
}