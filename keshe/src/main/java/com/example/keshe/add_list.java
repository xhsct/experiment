package com.example.keshe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.haibin.calendarview.CalendarView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class add_list extends AppCompatActivity {
    public EditText title_list;
    public EditText content_list;
    public String curdata;
    public Calendar calendar;
    public ImageButton data;
    public ImageButton camera;
    private Uri ImageUri;
    private ImageView imageView;
    Intent intent;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        imageView = findViewById(R.id.imageview);
        calendar = Calendar.getInstance();
        title_list = findViewById(R.id.title_list);
        content_list = findViewById(R.id.content_list);
        TextView textView = findViewById(R.id.data);
        intent =  getIntent();
        if(intent.getStringExtra("type").equals("0")){
            curdata = intent.getStringExtra("curdata");
            textView.setText(curdata);
        }
        else if (intent.getStringExtra("type").equals("1")){
            textView.setText(intent.getStringExtra("time"));
            title_list.setText(intent.getStringExtra("title"));
            content_list.setText(intent.getStringExtra("content"));
        }
        data = findViewById(R.id.select_data);
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(add_list.this, new DatePickerDialog.OnDateSetListener(){

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        monthOfYear += 1;
                        setTitle(year + "-" + monthOfYear + "-" + dayOfMonth);
                        curdata = "";
                        String data = year+"";
                        curdata += year+"";
                        Log.e("data", "0"+monthOfYear);
                        if (monthOfYear/10 == 0){
                            data += "0"+monthOfYear+"月";
                            curdata += "0"+monthOfYear;
                        }
                        else {
                            data += monthOfYear+"月";
                            curdata += monthOfYear+"";
                        }
                        if (dayOfMonth/10 == 0){
                            data += "0"+dayOfMonth+"日";
                            curdata += "0"+dayOfMonth;
                        }
                        else {
                            data += dayOfMonth+"日";
                            curdata += dayOfMonth+"";
                        }
                        textView.setText(data);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        camera = findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File outputImage = new File(getExternalFilesDir(null),"outputImage.jpg");
                try {
                    //创建一个文件，等待输入流
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //第二个参数与provider的authorities属性一致
                ImageUri = FileProvider.getUriForFile(add_list.this,"shanzang",outputImage);
                //直接使用隐式Intent的方式去调用相机，就不需要再去申请相机权限
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                //指定拍照的输出地址，当向intent传入MEdiaStore.Exter_OUTPUT参数后，表明这是一个存储动作，相机拍摄到的图片会直接存储到相应路径，不会缓存在内存中
                intent.putExtra(MediaStore.EXTRA_OUTPUT,ImageUri);
                //第二个参数为requestCode，他的值必须大于等于0，否则就不会回调
                startActivityForResult(intent,1);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK)
                try {
                    //拿到相机存储在指定路径的图片，而后将其转化为bitmap格式，然后显示在界面上
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(ImageUri));
                    imageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
        }
    }

    public void tianjia(View view) {
        String title = title_list.getText().toString();
        String content = content_list.getText().toString();
        MySqliteOpenHelper helper = new MySqliteOpenHelper(this, MySqliteOpenHelper.SQlite.DB_NAME, null, 1);
        Date date = new Date(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        String format = dateFormat.format(date);
        db = helper.getWritableDatabase();
        db.execSQL("insert into todolist values(null,?,?,?,null,'0',?)"
                , new String[]{curdata,title,content,format});
        db.close();
        startActivity(new Intent(this,MainActivity.class));

    }

    public void cancel(View view) {
        if(intent.getStringExtra("type").equals("0")){
            startActivity(new Intent(this,MainActivity.class));
        }
        else if (intent.getStringExtra("type").equals("1")){
            startActivity(new Intent(this,show_list.class));
        }

    }
}