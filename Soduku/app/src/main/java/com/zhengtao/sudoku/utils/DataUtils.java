package com.zhengtao.sudoku.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhengtao.sudoku.game.Cell;

import java.util.ArrayList;
import java.util.List;

public class DataUtils {
    private static final String FILE_NAME_PUZZLE = "puzzledata";
    private static final String FILE_NAME_ANS = "ansdata";
    private static final String KEY_PUZZLE = "puzzle";
    private static final String KEY_ANS = "answer";


    public static void setPuzzleData (Context context, List<Cell> puzzle) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME_PUZZLE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (puzzle == null || puzzle.size() <= 0) return;
        String puzzleJson = new Gson().toJson(puzzle);
        editor.clear();
        editor.putString(KEY_PUZZLE,puzzleJson);
        editor.commit();
    }
    public static void setAnswerData (Context context, int[][] ans) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME_ANS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (ans == null || ans.length <= 0 ) return;
        String ansJson = new Gson().toJson(ans);
        editor.clear();
        editor.putString(KEY_ANS,ansJson);
        editor.commit();
    }

    public static List<Cell> getPuzzleData (Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME_PUZZLE,Context.MODE_PRIVATE);
        String strJson = sp.getString(KEY_PUZZLE,null);
        //System.out.println("This is data at getPuzzleData in DataUtils "+strJson);
        return strJson==null?null:new Gson().fromJson(strJson, new TypeToken<List<Cell>>() {}.getType());
    }
    public static int[][] getAnswerData (Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME_ANS,Context.MODE_PRIVATE);
        String strJson = sp.getString(KEY_ANS,null);
        //System.out.println("This is data at getAnsData in DataUtils "+strJson);
        return strJson==null?null:new Gson().fromJson(strJson, new TypeToken<int[][]>() {}.getType());
    }
}
