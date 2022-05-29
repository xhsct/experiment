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
        Log.d("Add", "onCreate: "+intent.getStringExtra("image1"));
        if(intent.getStringExtra("type").equals("0")){
            curdata = intent.getStringExtra("curdata");
            textView.setText(curdata);
        }
        else if (intent.getStringExtra("type").equals("1")){
            MySqliteOpenHelper helper = new MySqliteOpenHelper(this, MySqliteOpenHelper.SQlite.DB_NAME, null, 1);
            db = helper.getReadableDatabase();
         //   String querySql="select * from todolist where id= ? ";
            Log.d("Main", "onCreate: "+intent.getStringExtra("id"));
            String []args=new String[]{intent.getStringExtra("id")};
            Cursor cursor = db.query("todolist",null,"id=?",args,null,null,null);
            while (cursor.moveToNext()) {
                @SuppressLint("Range")
                String image = cursor.getString(cursor.getColumnIndex(MySqliteOpenHelper.SQlite.image));
                imageView.setImageBitmap(ImageUtil.base64ToImage(image));
            }
            db.close();
            textView.setText(intent.getStringExtra("time"));
            title_list.setText(intent.getStringExtra("title"));
            content_list.setText(intent.getStringExtra("content"));

            Log.d("Add", "onCreate: "+intent.getExtras().get("image"));
//            imageView.setImageBitmap(ImageUtil.base64ToImage(intent.getStringExtra("image")));
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
            // contentProvider
            imageUri = FileProvider.getUriForFile(this,"com.example.keshe.fileprovider",imageTemp);
        }else {
            imageUri = Uri.fromFile(imageTemp);
        }
        Intent intent_1 = new Intent();
        intent_1.setAction("android.media.action.IMAGE_CAPTURE");
        intent_1.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent_1, REQUEST_CODE_TAKE);

    }

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
                    imageBase64 = imagetoBase64;

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
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
        db.execSQL("insert into todolist values(null,?,?,?,?,'0',?)"
                , new String[]{curdata,title,content,imageBase64,format});
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