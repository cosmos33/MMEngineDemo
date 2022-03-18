package com.immomo.xengine.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.momo.xeengine.XEnginePreferences;

public final class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 1;
    private static String[] PERMISSIONS = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.RECORD_AUDIO"
    };

    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int storagePermission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            int audioPermission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.RECORD_AUDIO");
            if (storagePermission != PackageManager.PERMISSION_GRANTED || audioPermission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS, REQUEST_PERMISSIONS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置引擎的上下文
        XEnginePreferences.setApplicationContext(this.getApplicationContext());
        //设置引擎license 与包名绑定，请联系陌陌获得。
        XEnginePreferences.setLicense("OjkAHm4HIlGJQc0OL+mtBqDdho7DB8OuLmBwSLaBTncpR3BX3+tjJvX14pWFkfTIty9mIT+bF6O49HQUVYWD6vpWM6rKEqXzCNO9++1YD/hvinWqdoXv41mrLAfU7UgD7W8mXhJgVLdyMwkPOhehZ6kpRIIhzvIeiJdL1j7M00E=");
        verifyStoragePermissions(this);
    }

    public void playGame(View v) {
        startActivity(new Intent(this, GameActivity.class));
    }
}