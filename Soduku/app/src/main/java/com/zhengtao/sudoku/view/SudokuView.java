package com.zhengtao.sudoku.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorRes;

import com.zhengtao.sudoku.R;
import com.zhengtao.sudoku.game.Cell;

import java.util.ArrayList;
import java.util.List;

public class SudokuView extends View {
    // 尺寸
    private int size = 9;
    // 尺寸平方根
    private int sqrtSize = 3;
    // 单元格大小
    private float cellSizePixels = 0F;
    // 选中的行
    private int selectedRow = 0;
    // 选中的列
    private int selectedCol = 0;
    // 单元格集合
    private List<Cell> cells;
    // 监听器
    private SudokuView.OnTouchListener listener;

    // region 定义画笔
    // 粗画笔
    private Paint thickLinePaint = new Paint(){
        {
            setStrokeWidth(8f);
            setStyle(Paint.Style.STROKE);
            setColor(getResources().getColor(R.color.black));
        }
    };
    // 细画笔
    private Paint thinLinePaint = new Paint(){
        {
            setStrokeWidth(2f);
            setStyle(Paint.Style.STROKE);
            setColor(getResources().getColor(R.color.black));
        }
    };
    // 选中单元格 画笔
    private Paint selectedCellPaint= new Paint(){
        {
            setStyle(Style.FILL_AND_STROKE);
            setColor(getResources().getColor(R.color.cellLight));
        }
    };
    // 冲突单元格 画笔
    private Paint conflictingCellPaint= new Paint(){
        {
            setStyle(Style.FILL_AND_STROKE);
            setColor(getResources().getColor(R.color.cellDark));
        }
    };
    // 输入文字画笔 醒目颜色 + 加粗
    private Paint textPaint = new Paint(){
        {
            setStyle(Paint.Style.FILL_AND_STROKE);
            setTypeface(Typeface.DEFAULT_BOLD);
            setColor(getResources().getColor(R.color.design_default_color_primary));
        }
    };
    // 初始文字画笔
    private Paint startingTextPaint= new Paint(){
        {
            setStyle(Paint.Style.FILL_AND_STROKE);
            setColor(getResources().getColor(R.color.black));
        }
    };
    // endregion

    public SudokuView(Context context) {
        super(context);
    }
    public SudokuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public SudokuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置View正方形显示
     *
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizePixels = Math.min(widthMeasureSpec,heightMeasureSpec);
        setMeasuredDimension(sizePixels,sizePixels);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //根据屏幕宽度设置单元格和绘制字体的大小
        updateMeasurements(getWidth());
        //填充整个表格
        fillCells(canvas);
        //绘制线
        drawLines(canvas);
        //绘制文本
        drawText(canvas);

        super.onDraw(canvas);
    }

    /**
     * 根据屏幕宽设置单元格和绘制字体的大小
     *
     * @param width
     *              传入当前屏幕宽度
     */
    private void updateMeasurements(int width) {
        cellSizePixels = width / size;;
        textPaint.setTextSize(cellSizePixels / 1.5F);
        startingTextPaint.setTextSize(cellSizePixels / 1.5F);
    }

    /**
     * 填充单元格
     *
     * @param canvas
     *              画布
     * @return
     */
    private void fillCells(Canvas canvas) {
        //获取当先所选单元格对应的索引
        int index = selectedRow * 9  +  selectedCol;
        if(cells != null) {
            //遍历整个数独表格
            for (Cell cell:cells) {
                //获取每个单元格的行，列
                int r = cell.getRow();
                int c = cell.getCol();
                if (r == selectedRow && c == selectedCol) {
                    //System.out.println(r);
                    // 填充用户当前所选的单元格
                    fillCell(canvas, r, c, selectedCellPaint);
                } else if (index >= 0 && cells.get(index).getValue() == cell.getValue() && cell.getValue() != 0){
                    // 填充与用户所选单元格内数字相同的单元格
                    fillCell(canvas, r, c, selectedCellPaint);
                } else if (r == selectedRow || c == selectedCol) {
                    // 填充用户所选单元格所在的列 和 行
                    fillCell(canvas, r, c, conflictingCellPaint);
                } else if (r / sqrtSize == selectedRow / sqrtSize && c / sqrtSize == selectedCol / sqrtSize) {
                    // 填充用户所选单元格行列外且在一个宫内的单元格
                    fillCell(canvas, r, c, conflictingCellPaint);
                }
            }
        }
    }
    /**
     * 根据作标填充所选单元格
     *
     * @param canvas
     *              画布
     * @param r
     *              行
     * @param c
     *              列
     * @param paint
     *              画笔
     */
    private void fillCell(Canvas canvas, int r, int c, Paint paint) {
        // left：矩形的左边位置。top：矩形的上边位置。
        // right：矩形的右边位置。bottom：矩形的下边位置。
        canvas.drawRect(
                c*cellSizePixels,
                r*cellSizePixels,
                (c+1)*cellSizePixels,
                (r+1)*cellSizePixels,
                paint);
    }
    /**
     * 绘制线条
     *
     * @param canvas
     *              画布
     */
    private void drawLines(Canvas canvas) {
        // 绘制边框
        canvas.drawRect(0,0,getWidth(),getWidth(),thickLinePaint);
        // 绘制线条
        for (int i = 1; i < size; i++){
            Paint paintToUse = (i % sqrtSize == 0 ?thickLinePaint:thinLinePaint);
            canvas.drawLine(
                    i*cellSizePixels,
                    0,
                    i*cellSizePixels,
                    getHeight(),
                    paintToUse);
            canvas.drawLine(
                    0,
                    i*cellSizePixels,
                    getWidth(),
                    i*cellSizePixels,
                    paintToUse);
        }
    }
    /**
     * 绘制数字
     *
     * @param canvas
     *              画布
     */
    private void drawText(Canvas canvas) {
        if (cells != null) {
            for (Cell cell:cells) {
                int row = cell.getRow();
                int col = cell.getCol();
                String num = cell.getValue() == 0?"":Integer.toString(cell.getValue());
                Paint paintToUse = cell.isStartingCell()?startingTextPaint:textPaint;

                Rect textBounds = new Rect();
                paintToUse.getTextBounds(num,0,num.length(),textBounds);
                float numWidth = paintToUse.measureText(num);
                float numHeight = textBounds.height();

                canvas.drawText(
                        num,
                        (col * cellSizePixels) + cellSizePixels / 2 - numWidth / 2,
                        (row * cellSizePixels) + cellSizePixels / 2 + numHeight / 2,
                        paintToUse);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            handleTouchEvent(event.getX(),event.getY());
            return true;
        } else
            return false;
    }

    /**
     * 处理触摸事件
     *
     * @param x
     *              用户触摸点坐标x
     * @param y
     *              用户触摸点坐标y
     */
    private void handleTouchEvent(float x, float y) {
        int possibleSelectedRow = (int)(y / cellSizePixels);
        int possibleSelectedCol = (int)(x / cellSizePixels);
        if ( this.listener != null) {
            this.listener.onCellTouched(possibleSelectedRow, possibleSelectedCol);
        }
    }
    /**
     * 更新所选单元格UI
     *
     * @param row
     *              行
     * @param col
     *              列
     */
    public void updateSelectedCellUI(int row, int col) {
        this.selectedRow = row;
        this.selectedCol = col;
        invalidate();
    }
    /**
     * 更新cells
     *
     * @param cells
     *              新的cells
     */
    public void updateCells (List<Cell> cells) {
        this.cells = cells;
        invalidate();
    }

    public void registerListener(SudokuView.OnTouchListener listener) {
        this.listener = listener;
    }
    public interface OnTouchListener {
        void onCellTouched(int row, int col);
    }
}
