package com.example.mshudu;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RankList extends Activity {
    private MySQLiteOpenHelper dbHelper=null;
    private List<Map<String, Object>> searchList = new ArrayList<Map<String, Object>>();
    private SimpleAdapter searchadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranklist);
        dbHelper = new MySQLiteOpenHelper(this);
        initData();

    }
    void initData(){
        String sql = "select finishtime,lasttime from tb_mycontacts where isfinished=? order by finishtime ASC";
        searchList= dbHelper.selectList(sql,new String[]{"true"});
        searchadapter = new SimpleAdapter(getApplicationContext(), searchList, R.layout.ranklist_item,
                new String[] { "finishtime", "lasttime"},
                new int[] { R.id.finish_time, R.id.last_time });
        ListView listView=(ListView)findViewById(R.id.rank_list);
        listView.setAdapter(searchadapter);
    }

}
