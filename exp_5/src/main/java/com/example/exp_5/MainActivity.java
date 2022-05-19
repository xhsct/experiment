package com.example.exp_5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    private ImageView imgView;
    private Button btn_download;
    private String URL = "https://img.zcool.cn/community/038746a55de90940000017140d86a2f.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgView = findViewById(R.id.img);
        btn_download = findViewById(R.id.downLoad);


        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownLoadImg downLoadImg = new DownLoadImg();
                downLoadImg.execute();

            }
        });

    }

    private class DownLoadImg extends AsyncTask<Void,Void, Bitmap>{

        AlertDialog tips;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tips = new AlertDialog.Builder(MainActivity.this).setTitle("提示信息").setMessage("下载中...").create();
            tips.show();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            tips.cancel();
            imgView.setImageBitmap(bitmap);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bitmap = null;
            try {
                //对资源链接
                URL url = new URL(URL);
                //打开输入流
                InputStream inputStream = url.openStream();
                //对网上资源进行下载并转换为位图图片
                bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }

}