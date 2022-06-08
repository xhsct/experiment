package com.example.keshe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haibin.calendarview.CalendarView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class add_list extends AppCompatActivity {
    public static final int REQUEST_CODE_TAKE = 1; //获取相片
    public static final int REQUEST_CODE_CHOOSE = 1; //选择相片
    public EditText title_list;
    public EditText content_list;
    public String curdata;
    public Calendar calendar;
    public ImageButton data;
    public ImageButton camera;
    private Uri imageUri;
    private ImageView imageView;
    private String imageBase64;
    private String ID;
    Intent intent;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        imageView = findViewById(R.id.imageview);
        // 创建一个实例对象
        calendar = Calendar.getInstance();
        title_list = findViewById(R.id.title_list);
        content_list = findViewById(R.id.content_list);
        TextView textView = findViewById(R.id.data);
        intent =  getIntent();
//        Log.d("Add", "onCreate: "+intent.getStringExtra("image1"));
        if(intent.getStringExtra("type").equals("0")){
            curdata = intent.getStringExtra("curdata");
            textView.setText(intent.getStringExtra("show_data"));
        }
        else if (intent.getStringExtra("type").equals("1")){
            MySqliteOpenHelper helper = new MySqliteOpenHelper(this, MySqliteOpenHelper.SQlite.DB_NAME, null, 1);
            db = helper.getReadableDatabase();
            ID = intent.getStringExtra("id");
            curdata = intent.getStringExtra("time");
            String []args=new String[]{intent.getStringExtra("id")};
            Cursor cursor = db.query("todolist",null,"id=?",args,null,null,null);
            while (cursor.moveToNext()) {
                // 如果查询到的图片信息不是空，那就通过base64toimage转化出来
                @SuppressLint("Range")
                String image = cursor.getString(cursor.getColumnIndex(MySqliteOpenHelper.SQlite.image));
                Log.d("image", image+"");
                if (image != null){
                    imageView.setImageBitmap(ImageUtil.base64ToImage(image));
                }
            }
            db.close();
            // 将得到的时间转换为想要显示的形式
            StringBuffer show_data = new StringBuffer(curdata);
            show_data.insert(8,"日");
            show_data.insert(6,"月");
            show_data.insert(4,"年");
            textView.setText(show_data);
            title_list.setText(intent.getStringExtra("title"));
            content_list.setText(intent.getStringExtra("content"));
        }
        data = findViewById(R.id.select_data);
        // 修改日历控件
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                        setTitle(year + "-" + monthOfYear + "-" + dayOfMonth);
                DatePickerDialog dialog = new DatePickerDialog(add_list.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        monthOfYear += 1;
//                        setTitle(year + "-" + monthOfYear + "-" + dayOfMonth);
                        curdata = "";
                        String data = year + "年";
                        curdata += year + "";
                        Log.e("data", "0" + monthOfYear);
                        if (monthOfYear / 10 == 0) {
                            data += "0" + monthOfYear + "月";
                            curdata += "0" + monthOfYear;
                        } else {
                            data += monthOfYear + "月";
                            curdata += monthOfYear + "";
                        }
                        if (dayOfMonth / 10 == 0) {
                            data += "0" + dayOfMonth + "日";
                            curdata += "0" + dayOfMonth;
                        } else {
                            data += dayOfMonth + "日";
                            curdata += dayOfMonth + "";
                        }
                        textView.setText(data);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.show();
            }
        });
        camera = findViewById(R.id.camera);
        // 照相功能
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(add_list.this, Manifest.permission.CAMERA )== PackageManager.PERMISSION_GRANTED){
                    //真正去拍照
                    toTake();
                }else {
                    //取申请权限
                    ActivityCompat.requestPermissions(add_list.this,new String[]{Manifest.permission.CAMERA},1);

                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                toTake();
            }else {
                Toast.makeText(this, "没有获得权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void toTake(){
        // 创建一个文件流，用来存储拍到的照片
        File imageTemp = new File(getExternalCacheDir(),"imageOut.jepg");
        if(imageTemp.exists()){
            imageTemp.delete();
        }
        try {
            imageTemp.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT > 24){
            // contentProvider 这个在里AndroidManifest中设置
            imageUri = FileProvider.getUriForFile(this,"com.example.keshe.fileprovider",imageTemp);
        }else {
            imageUri = Uri.fromFile(imageTemp);
        }
        // 隐式调用
        Intent intent_1 = new Intent();
        intent_1.setAction("android.media.action.IMAGE_CAPTURE");
        intent_1.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent_1, REQUEST_CODE_TAKE);

    }
    // 回调到imageView中并显示
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_TAKE){
            if(resultCode == RESULT_OK){
                //获取拍摄的照片
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(bitmap);
                    String imagetoBase64 = ImageUtil.imageToBase64(bitmap);
                    // 将得到的图片转为string类型，以便后面存入数据库中
                    imageBase64 = imagetoBase64;

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 添加功能
    public void tianjia(View view) {
        String title = title_list.getText().toString();
        String content = content_list.getText().toString();
        MySqliteOpenHelper helper = new MySqliteOpenHelper(this, MySqliteOpenHelper.SQlite.DB_NAME, null, 1);
        Date date = new Date(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        String format = dateFormat.format(date);
        db = helper.getWritableDatabase();
        //识别是添加还是编辑
        if(intent.getStringExtra("type").equals("0")) {
            db.execSQL("insert into todolist values(null,?,?,?,?,'start',?)"
                    , new String[]{curdata, title, content, imageBase64, format});
            db.close();
            if (intent.getStringExtra("return").equals("1")){
                startActivity(new Intent(this, show_list.class));
            }else {
            startActivity(new Intent(this, MainActivity.class));}
            finish();
        }else if(intent.getStringExtra("type").equals("1")){
            Log.e("title", title+"");
            db.execSQL("update todolist set title=?,content=?,now_time=?,image=?,time=? where id=?"
                    , new String[]{title,content,format,imageBase64,curdata,ID});
            db.close();
            startActivity(new Intent(this,show_list.class));
            finish();
        }

    }

    //取消键
    public void cancel(View view) {
        if(intent.getStringExtra("type").equals("0")){
            if (intent.getStringExtra("return").equals("1")){
                startActivity(new Intent(this, show_list.class));
            }else {
                startActivity(new Intent(this, MainActivity.class));}
            finish();
        }
        else if (intent.getStringExtra("type").equals("1")){
            startActivity(new Intent(this,show_list.class));
            finish();
        }


    }
}