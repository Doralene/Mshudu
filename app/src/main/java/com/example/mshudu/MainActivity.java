package com.example.mshudu;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button undo;
    private Button count_time;
    private Button new_game;
    private Button quit;
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
        quit=findViewById(R.id.quit);
        undo.setOnClickListener(this);
        count_time.setOnClickListener(this);
        new_game.setOnClickListener(this);
        quit.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.undo:
                view.undo();
                break;
            case R.id.count_time:
                if (count_time.getText().equals("暂停")) {
                    rangeTime = SystemClock.elapsedRealtime() - chronometer.getBase();
                    Log.d("timeeeeeeeeeeeee", rangeTime + "");
                    chronometer.stop();
                    count_time.setText("继续");
                }
                else if (count_time.getText().equals("继续")){
                    chronometer.setBase(SystemClock.elapsedRealtime()-rangeTime);
                    chronometer.start();
                    count_time.setText("暂停");
                }
                break;
            case R.id.new_game:
                view.new_game();
                break;
            case R.id.quit:

                break;

        }
    }
}
