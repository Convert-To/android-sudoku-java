package com.zhengtao.sudoku.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Pair;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Toast;

import com.zhengtao.sudoku.game.Cell;
import com.zhengtao.sudoku.utils.DataUtils;
import com.zhengtao.sudoku.view.SudokuView;
import com.zhengtao.sudoku.viewmodel.PlaySudokuViewModel;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.zhengtao.sudoku.R;

public class PlaySudokuActivity extends AppCompatActivity implements SudokuView.OnTouchListener {
    private PlaySudokuViewModel viewModel;
    private Chronometer mChronometer;
    private List<Integer> numButtons = Arrays.asList(R.id.deleteButton,
            R.id.oneButton, R.id.twoButton, R.id.threeButton,
            R.id.fourButton, R.id.fiveButton, R.id.sixButton,
            R.id.sevenButton, R.id.eightButton, R.id.nineButton );
    private SudokuView sudokuView;
    private int difficulty = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_sudoku);

        // 初始化控件
        initView();
        // 初始化计时器 并开始计时
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
        // 注册监听器
        sudokuView.registerListener(this);
        // 获取ViewModel
        viewModel = new ViewModelProvider(this).get(PlaySudokuViewModel.class);
        // 获取主界面传入的难度参数，和是否继续参数
        Intent intent = this.getIntent();
        this.difficulty = intent.getExtras().getInt("difficulty");
        // 开始谜题，根据主界面传入的参数生成谜题或读取谜题
        viewModel.sudokuGame.startPuzzle(this,difficulty,intent.getExtras().getBoolean("resume"));
        // 观察sudokuGa中的LiveData数据，如果数据发生改变，则执行updateSelectedCellUI()方法
        viewModel.sudokuGame.selectedCellLiveData.observe(this,integerIntegerPair -> { updateSelectedCellUI(integerIntegerPair); });
        viewModel.sudokuGame.cellsLiveData.observe(this, cells -> { updateCells(cells); });
    }

    private void initView() {
        mChronometer = findViewById(R.id.mChronometer);
        sudokuView = findViewById(R.id.sudokuBoardView);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.deleteButton:
            case R.id.oneButton:
            case R.id.twoButton:
            case R.id.threeButton:
            case R.id.fourButton:
            case R.id.fiveButton:
            case R.id.sixButton:
            case R.id.sevenButton:
            case R.id.eightButton:
            case R.id.nineButton: viewModel.sudokuGame.handleInput(numButtons.indexOf(v.getId()));break;
            case R.id.checkButton: checkSudoku();break;
            case R.id.refreshButton:viewModel.sudokuGame.refreshCells(difficulty);break;
            case R.id.showButton:viewModel.sudokuGame.showCellAns();break;
            case R.id.undoButton:viewModel.sudokuGame.undo();break;
        }
    }
    
    private void checkSudoku() {
        if (!viewModel.sudokuGame.checkSudoku()) {
            Toast.makeText(PlaySudokuActivity.this,"不对哦 再检查一下吧",Toast.LENGTH_SHORT).show();
            return;
        }
        mChronometer.stop();
        long time = SystemClock.elapsedRealtime() - mChronometer.getBase();
        AlertDialog.Builder builder = new AlertDialog.Builder(PlaySudokuActivity.this);
        builder.setTitle("回答正确");
        builder.setMessage(convertSecToTimeString(time));
        //检查全部正确的话 播放准备好的音效
        //MediaPlayer.create(PlaySudokuActivity.this, R.raw.halo_reach_grunt_birthday_party_sound).start();
        builder.setPositiveButton("再来一局", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.sudokuGame.refreshCells(difficulty);
                mChronometer.setBase(SystemClock.elapsedRealtime());
                mChronometer.start();
            }
        });
        builder.show();
    }

    private String convertSecToTimeString(long time) {
        long min = TimeUnit.MILLISECONDS.toMinutes(time);
        long sec = TimeUnit.MILLISECONDS.toSeconds(time) % 60;
        return min>0?String.format("用时 %d分%d秒",min,sec):String.format("用时 %d秒",sec) ;
    }

    private void updateSelectedCellUI(Pair<Integer, Integer> integerIntegerPair) {
        sudokuView.updateSelectedCellUI(integerIntegerPair.first,integerIntegerPair.second);
    }

    private void updateCells(List<Cell> cells) {
        if (cells != null) { sudokuView.updateCells(cells); }
    }


    @Override
    public void onCellTouched(int row, int col) { viewModel.sudokuGame.updateSelectedCell(row, col); }

    @Override
    protected void onPause() {
        DataUtils.setPuzzleData(this,viewModel.sudokuGame.getCells());
        DataUtils.setAnswerData(this,viewModel.sudokuGame.getPuzzleAnswer());
        mChronometer.stop();
        super.onPause();
    }


}