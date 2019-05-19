package com.example.mshudu;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class History extends Activity {
    private MySQLiteOpenHelper dbHelper=null;
    private List<Map<String, Object>> searchList = new ArrayList<Map<String, Object>>();
    private SimpleAdapter searchadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historylist);
        dbHelper = new MySQLiteOpenHelper(this);
        initData();

    }
    void initData(){
        String sql = "select isfinished,lasttime,finishtime from tb_mycontacts  order by lasttime DESC";
        searchList= dbHelper.selectList(sql,null);
        searchadapter = new SimpleAdapter(getApplicationContext(), searchList, R.layout.history_item,
                new String[] { "isfinished", "lasttime","finishtime"},
                new int[] { R.id.isfinished, R.id.last_time,R.id.finish_time });
        ListView listView=(ListView)findViewById(R.id.history_list);
        listView.setAdapter(searchadapter);
    }

}
