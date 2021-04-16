package com.zhengtao.sudoku.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhengtao.sudoku.R;
import com.zhengtao.sudoku.utils.DataUtils;

public class MainActivity extends AppCompatActivity {
    private Button startButton,resumeButton;
    private TextView difficultyTextView;
    private static int[] difficulty = {5,9,15};
    private static String[] difficultyText = {"Easy","Normal","Hard"};
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        if (DataUtils.getPuzzleData( this)!= null) {
            resumeButton.setVisibility(View.VISIBLE);
            startButton.setText("New Game");
        }
    }

    private void initView() {
        difficultyTextView = findViewById(R.id.difficultyTextView);
        startButton = findViewById(R.id.startButton);
        resumeButton = findViewById(R.id.resumeButton);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.leftButton:
                index = index-1<0?2:index-1;
                difficultyTextView.setText(difficultyText[index]);
                break;
            case R.id.rightButton:
                index = index+1>2?0:index+1;
                difficultyTextView.setText(difficultyText[index]);
                break;
            case R.id.startButton:
            case R.id.resumeButton:
                Intent intent = new Intent(MainActivity.this,PlaySudokuActivity.class);
                intent.putExtra("difficulty",difficulty[index]);
                intent.putExtra("resume",v.getId() == R.id.resumeButton);
                startActivity(intent);
                break;
        }
    }
    @Override
    protected void onResume() {
        if (DataUtils.getPuzzleData(this) != null) {
            resumeButton.setVisibility(View.VISIBLE);
            startButton.setText("New Game");
        }

        super.onResume();
    }
}