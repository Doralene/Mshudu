package com.example.mshudu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class FirstActivity extends AppCompatActivity {

    private Button back;
    private Button new_game;
    private ShuDuView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_keys);


    }
}