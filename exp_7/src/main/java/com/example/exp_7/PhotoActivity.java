package com.example.exp_7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.view.LifecycleCameraController;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PhotoActivity extends AppCompatActivity {

    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    PreviewView viewFinder;
    Button photo_btn_preview, cancel;
    LifecycleCameraController cameraControl = null;
    Executor executor = null;
    private static final String FILENAME_FORMATE = "yyyy-MM-DD-HH-mm-ss";
    private CameraSelector cameraSelectorId = CameraSelector.DEFAULT_BACK_CAMERA;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        viewFinder = findViewById(R.id.viewFinder);
        cancel = findViewById(R.id.cancel);
        photo_btn_preview = findViewById(R.id.photo_btn_preview);

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS);
        }


        photo_btn_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cameraControl != null) {

                    String mFileName = "picture-" + UUID.randomUUID().toString() + ".jpg";
                    File photoFile = new File(getExternalCacheDir(), mFileName);
                    ImageCapture.OutputFileOptions outputFileOptions =
                            new ImageCapture.OutputFileOptions.Builder(photoFile).build();
                    cameraControl.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback() {
                        @Override
                        public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                            Log.d("ttt", "saved:" + outputFileResults);
                            Log.d("ttt", "mFileName:" + mFileName);
                            Intent intent = new Intent();
                            intent.putExtra("photoName",photoFile.getPath());
                            setResult(RESULT_OK,intent);
                            finish();
                        }

                        @Override
                        public void onError(@NonNull ImageCaptureException exception) {
                            int ttt = Log.d("ttt", "onError:" + exception);
                        }
                    });
                }
            }
        });


    }


    private void startCamera() {
        executor = Executors.newSingleThreadExecutor();
        // 绑定生命周期的相机控制器
        cameraControl = new LifecycleCameraController(this);
        //这里参数其实是lifecycleOwner,由于在activity中则直接用this
        cameraControl.bindToLifecycle(this);
        cameraControl.setCameraSelector(cameraSelectorId);
        cameraControl.getInitializationFuture().addListener(() ->
                viewFinder.setController(cameraControl), ContextCompat.getMainExecutor(this));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean allPermissionsGranted() {

        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}