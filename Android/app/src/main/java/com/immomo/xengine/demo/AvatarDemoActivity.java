package com.immomo.xengine.demo;

import android.os.Bundle;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.momo.xeengine.IXEngine;
import com.momo.xeengine.game.IXGameView;
import com.momo.xeengine.game.XEGameView;

public class AvatarDemoActivity extends AppCompatActivity implements IXGameView.Callback {

    private XEGameView gameView;
    private ViewGroup viewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        viewGroup = findViewById(R.id.viewGroup);
        gameView = new XEGameView(this);
        viewGroup.addView(gameView);
        gameView.setRenderViewType(XEGameView.TYPE_TEXTURE_VIEW);
        gameView.setRenderScale(1.f);
        gameView.setPreferredFramesPerSecond(30);
        gameView.setCallback(this);
        gameView.start();
    }

    //渲染视图创建回调
    @Override
    public void onRenderViewCreate(View view) {
        if (view instanceof SurfaceView) {
            SurfaceView renderView = (SurfaceView) view;
        } else if (view instanceof TextureView) {
            TextureView renderView = (TextureView) view;
        }
        //do something if needed
    }

    //引擎启动回调
    @Override
    public void onStart(IXEngine engine) {
        engine.getLogger().setLogEnable(true);

        engine.addLibraryPath(getExternalFilesDir("") + "/avatarDemo");
        engine.getScriptBridge().regist(this, "ClientBridge");
        engine.getScriptEngine().startGameScriptFile("app");
    }

    //引擎启动失败回调
    //可能导致引擎启动失败的原因：
    //1. 引擎启动前没有设置全局的上下文对象
    //2. 如果有so打包时排出，改为运行时下载的场景。可能为so下载失败。或so版本与客户端不匹配
    @Override
    public void onStartFailed(String errorMsg) {
        runOnUiThread(() -> {
            Toast.makeText(this, "引擎启动失败 " + errorMsg, Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onRenderSizeChanged(int i, int i1) {

    }

    @Override
    public void onEngineDynamicLinkLibraryDownloadProcess(int percent, double speed) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
