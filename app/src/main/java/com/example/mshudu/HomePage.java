package com.example.mshudu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG="Sudoku";
    private Button continue_btn;
    private Button new_game_btn;
    private Button rank_list_btn;
    private Button history_btn;
    private Button about_btn;
    private Button quit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        init();


    }

    void init(){
        continue_btn=findViewById(R.id.continue_game);
        new_game_btn=findViewById(R.id.new_game);
        rank_list_btn=findViewById(R.id.rank_list);
        history_btn=findViewById(R.id.history);
        about_btn=findViewById(R.id.about);
        quit=findViewById(R.id.quit);
        continue_btn.setOnClickListener(this);
        new_game_btn.setOnClickListener(this);
        rank_list_btn.setOnClickListener(this);
        history_btn.setOnClickListener(this);
        about_btn.setOnClickListener(this);
        quit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()){
            case R.id.new_game:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.continue_game:
//                startActivity(new Intent(this));
                break;
            case R.id.rank_list:

                break;
            case R.id.history:

                break;
            case R.id.about:
                startActivity(new Intent(this,About.class));
                break;
            case R.id.quit:

                break;
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
//        Music.stop(this);
    }


    /**
     * 关于raw，及获取音乐文件的方法
     */
    @Override
    protected void onResume() {
        super.onResume();
        //Music.play(this,R.raw.);
        }


}
