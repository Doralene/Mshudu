package com.example.mshudu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Stack;

/**
 * Created by Administrator on 2016/3/13.
 */
public class ShuDuView extends View {
    private float width;//单元格的宽度
    //按钮的起始位置
    private float btnStartX;
    private float btnStartY;
    private float btnWidth;//按钮宽度
    private float btnHeight;//按钮高度
    private int numberX;
    private int numberY;

    private Paint bgPaint = new Paint();//背景色画笔
    private Paint darkPaint = new Paint();//深色画笔
    private Paint hilitePaint = new Paint();//白色画笔
    private Paint lightPaint = new Paint();//浅色画笔
    private Paint numPaint = new Paint();//初始数字画笔
    private Paint newNumPaint = new Paint();//用户输入的数字画笔
    private Paint btnPaint = new Paint();//按钮画笔

    private Context mContext;
    private Game game = new Game();
    private Canvas canvas;

    private int[][] allSuduNumber;
    private Stack stack=new Stack();

    public ShuDuView(Context context) {
        super(context);
        mContext=context;
    }
    public ShuDuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
     }

    public ShuDuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
         super(context, attrs, defStyleAttr);
        mContext=context;
     }

    public ShuDuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext=context;
     }

    @Override
    /**
     * 当View的宽高改变的时候执行
     * w与h 当前View的宽和高
     * oldw与oldh 变动前的宽和高
     */
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width = w>h?h/9f:w/9f;
        if (w > h) {
            this.width = h/9f;
            this.btnWidth = (w-width*9)*3/5;
            this.btnHeight = (w-width*9)*1/4;
            this.btnStartX = width*9 + btnWidth/3;
            this.btnStartY = btnHeight/2;
        } else {
            this.width = w/9f;
            this.btnWidth = w*3/5;
            this.btnHeight = (h-width*9)*1/4;
            this.btnStartX = btnWidth/3;
            this.btnStartY = width*9 + btnHeight/2;
        }
        btnHeight/=2;
        btnWidth/=2;
        btnStartX-=50;
        btnStartY-=100;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    //当Android系统需要绘制一个View对象时，就会调用该对象的onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        bgPaint.setColor(getResources().getColor(R.color.backGround));
        canvas.drawRect(0, 0, getWidth(), getHeight(), bgPaint);
        darkPaint.setStrokeWidth(2);
        darkPaint.setColor(getResources().getColor(R.color.darkGray));
        hilitePaint.setStrokeWidth(2);
        hilitePaint.setColor(getResources().getColor(R.color.hiliteGray));
        lightPaint.setStrokeWidth(2);
        lightPaint.setColor(getResources().getColor(R.color.lightGray));
        setBackGround(canvas);
        refreshNumbers(canvas);

        this.canvas = canvas;

        super.onDraw(canvas);
    }

    private void setBackGround(Canvas canvas) {
        for (int i = 0; i<10; i++) {
            //绘制横线
            canvas.drawLine(0, i*width, 9*width, i*width, i%3==0?darkPaint:lightPaint);
            canvas.drawLine(0, i*width+2, 9*width, i*width+2, hilitePaint);
            //绘制纵线
            canvas.drawLine(i*width, 0, i*width, 9*width, i%3==0?darkPaint:lightPaint);
            canvas.drawLine(i*width+2, 0, i*width+2, 9*width, hilitePaint);
        }
    }

    private void refreshNumbers(Canvas canvas) {
        //初始数字设置
        numPaint.setColor(Color.BLACK);
//        numPaint.setStyle(Paint.Style.STROKE);//设置空心
        numPaint.setTextSize(width * 0.75f);//设置文本大小为单元格宽度的四分之三
        numPaint.setTextAlign(Paint.Align.CENTER);//设置水平方向居中
        //用户输入的数字设置
        newNumPaint.setColor(Color.BLUE);
//        newNumPaint.setStyle(Paint.Style.STROKE);
        newNumPaint.setTextSize(width * 0.75f);
        newNumPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fm = numPaint.getFontMetrics();
        float x = width/2;
        float y = width/2 - (fm.ascent + fm.descent)/2;
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++)
                canvas.drawText(game.getNumStr(i, j), i * width + x, j * width + y, game.isAbleToEdit(i, j)? newNumPaint: numPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.d("touch",event.getX()+","+event.getY());
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(event);
        }
        //在网格区
        if(event.getY()<=width*9){
            numberX = (int)(event.getX()/width);
            numberY = (int)(event.getY()/width);
            if (!game.isAbleToEdit(numberX, numberY)) {
                return super.onTouchEvent(event);
            }
            int used[] = game.getUsedNumbers(numberX, numberY);
            for (int i=0; i<used.length; i++) {
                Log.i("Game", String.valueOf(used[i]));
            }

            KeysDialog keysDialog = new KeysDialog(getContext(), used, this, R.style.dialog, (int)(width*3), (int)(width*3));

            Window dialogWindow = keysDialog.getWindow();
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();//获取对话框当前的参数值
            dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);//设置原始坐标为左上角
            //设置对话框相对于原始坐标的位置
            layoutParams.x = (int)((numberX - 1) * width);
            layoutParams.y = (int)((numberY - 1) * width);

            keysDialog.show();
        }
            return true;
    }

    public void new_game(){
        game = new Game();
        invalidate();
    }
    public void setNumber(int number) {
        if (game.setNumberIfValid(numberX, numberY, number)) {
            stack.push(number);
            stack.push(numberY);
            stack.push(numberX);
            invalidate();//刷新界面，重新调用onDraw方法
        }
    }
    public void undo(){
        if(stack.empty()){
            Toast.makeText(mContext.getApplicationContext(), "不能再撤销了！！！", Toast.LENGTH_SHORT).show();
        }else {
            numberX = Integer.parseInt(stack.pop().toString());
            numberY = Integer.parseInt(stack.pop().toString());
            int number = Integer.parseInt(stack.pop().toString());
            game.reduNumber(numberX, numberY, number);
            invalidate();
        }
    }
    public void quit(){
        allSuduNumber=game.getAllNumber();
        String s="";
        for(int i=0;i<9;i++)
            for(int j=0;j<9;j++)
            {
                s+=allSuduNumber[j][i]+",";
            }
        s=s.substring(0,s.length()-1);//把最后的逗号去掉
        System.out.println(s);
        Log.d("tuichu","11111111111111");
    }
    public boolean finishGame(){
        return game.isFinishedGame();
    }
    public String getInit(){
        return game.getInitdata();
    }
    public String getNow(){
        return game.getGamedata();
    }

    public int[][] getAllSuduNumber(){
        return game.getAllNumber();
    }

    public  void setData(int data[][]){
        game.setGamedata(data);
        invalidate();//刷新界面，重新调用onDraw方法
    }
}
