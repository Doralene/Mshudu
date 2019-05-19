package com.example.mshudu;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.mshudu.PhotoUtil.compressScale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MySQLiteOpenHelper dbHelper = null;
    private Button undo;
    private Button count_time;
    private Button new_game;
    private Button back;
    private Button camera;
    private Button answer;
    private ShuDuView view;
    public Chronometer chronometer;
    private ImageView imageView;

    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private static final int REQUEST_PERMISSION = 4;  //权限请求
    static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private PhotoUtil photoUtil;

    private Uri imageUri;

//    private static final String TAG = "main";
//    private static final String FILE_PATH = "/img/syscamera.jpg";
    long rangeTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view=(ShuDuView) findViewById(R.id.view);
        dbHelper = new MySQLiteOpenHelper(this);
        chronometer = (Chronometer) this.findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        undo=findViewById(R.id.undo);
        count_time =findViewById(R.id.count_time);
        new_game=findViewById(R.id.new_game);
        back=findViewById(R.id.back);
        camera=findViewById(R.id.camera);
        answer=findViewById(R.id.answer);
        answer.setOnClickListener(this);
        undo.setOnClickListener(this);
        count_time.setOnClickListener(this);
        new_game.setOnClickListener(this);
        back.setOnClickListener(this);
        camera.setOnClickListener(this);
        imageView=findViewById(R.id.img);
        mPermissionsChecker = new PermissionsChecker(this);
        photoUtil = new PhotoUtil(this);
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
                chronometer.stop();
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                break;
            case R.id.camera:
                tack_photo();
                break;
            case R.id.answer:
                try {
                    answerShudu();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.back:

                String initdata="";
                String gamedata="";
                String isfinished="";
                String lasttime="";
                long finishtime=0;
                initdata=view.getInit();
                gamedata=view.getNow();

                if(view.finishGame())
                {
                    isfinished="true";
                    finishtime=SystemClock.elapsedRealtime()-chronometer.getBase();
                }
                else{
                    isfinished="false";
                    finishtime=0;
                }
                Date t = new Date();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                lasttime=df.format(t);
                String sql = "delete from tb_mycontacts where isfinished=?";
                dbHelper.execData(sql, new Object[] { "false" });
                 sql ="insert into tb_mycontacts(initdata, gamedata,isfinished,lasttime,finishtime)values(?,?,?,?,?)";
                boolean flag = dbHelper.execData(sql, new Object[] { initdata, gamedata,isfinished,lasttime,finishtime});
                Log.d("init",initdata);
                Log.d("game",gamedata);
                Log.d("isfinish",isfinished);
                Log.d("last",lasttime);
                Log.d("finishTime",finishtime+" ");
                if (flag) {
                    Toast.makeText(getApplicationContext(),"保存成功！",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"保存失败！",Toast.LENGTH_SHORT).show();
                }
                break;


        }
    }

    private void tack_photo(){
        //检查权限(6.0以上做权限判断)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                startPermissionsActivity();
            } else {
                //打开相机
                imageUri = photoUtil.takePhoto(0);
            }
        } else {
            imageUri = photoUtil.takePhoto(0);
        }
    }
    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_PERMISSION,
                PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                imageView.setImageBitmap(compressScale(bitmap));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 1) {
            //从相册选择
            String imgPath = photoUtil.getCallPhoto(data);
            if (TextUtils.isEmpty(imgPath)) {
                bitmap = BitmapFactory.decodeFile(imgPath);
                imageView.setImageBitmap(compressScale(bitmap));
            }
        }
    }

    private void answerShudu() throws InterruptedException {
        SudokuXAnalyse sudokuXAnalyse=new SudokuXAnalyse(view.getAllSuduNumber());
        int [][] result=sudokuXAnalyse.getAns();
        view.setData(result);
    }
}
