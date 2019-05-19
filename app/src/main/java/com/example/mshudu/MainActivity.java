package com.example.mshudu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button undo;
    private Button count_time;
    private Button new_game;
    private Button back;
    private ShuDuView view;
    public Chronometer chronometer;
    long rangeTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view=(ShuDuView) findViewById(R.id.view);

        chronometer = (Chronometer) this.findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        undo=findViewById(R.id.undo);
        count_time =findViewById(R.id.count_time);
        new_game=findViewById(R.id.new_game);
        back=findViewById(R.id.back);
        undo.setOnClickListener(this);
        count_time.setOnClickListener(this);
        new_game.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.undo:
                view.undo();
                break;
            case R.id.count_time:
                rangeTime = SystemClock.elapsedRealtime() - chronometer.getBase();
                Log.d("timeeeeeeeeeeeee", rangeTime + "");
                chronometer.stop();
                AlertDialog spause_dialog = new AlertDialog.Builder(this)
                        .setTitle("暂停")
                        .setMessage("游戏已暂停，请点击下方按钮继续")
                        .setPositiveButton("继续", new DialogInterface.OnClickListener() {//添加"继续"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                chronometer.setBase(SystemClock.elapsedRealtime()-rangeTime);
                                chronometer.start();
                                Toast.makeText(MainActivity.this, "这是确定按钮", Toast.LENGTH_SHORT).show();
                            }
                        }) .create();
                spause_dialog.show();

                break;
            case R.id.new_game:
                view.new_game();
                break;
            case R.id.quit:

                break;

        }
    }
}
