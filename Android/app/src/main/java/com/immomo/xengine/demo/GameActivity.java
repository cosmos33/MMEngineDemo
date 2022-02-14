package com.immomo.xengine.demo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.momo.xeengine.IXEngine;
import com.momo.xeengine.game.IXGameView;
import com.momo.xeengine.game.XEGameView;
import com.momo.xeengine.script.ScriptBridge;

public final class GameActivity extends AppCompatActivity implements IXGameView.Callback {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    private XEGameView gameView;
    private GameHandler gameHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameHandler = new GameHandler(this);
        gameView = findViewById(R.id.gameView);
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
        engine.addLibraryPath("/sdcard/demo");
        engine.getScriptBridge().regist(gameHandler, "GameHandler");
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
//        finish();
        });

    }

    @Override
    public void onRenderSizeChanged(int i, int i1) {

    }

    //引擎so下载进度，仅针对陌陌客户端
    @Override
    public void onEngineDynamicLinkLibraryDownloadProcess(int percent, double speed) {

    }

    //同步Bridge方法
    public String onGameOver(String arg) {
        runOnUiThread(() -> {

            AlertDialog alert = new AlertDialog.Builder(GameActivity.this).create();
            alert.setTitle("游戏结束");
            alert.setMessage(arg);

            //添加"确定"按钮
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "是的", (arg0, arg1) -> finish());
            alert.show();
        });
        return null;
    }

    //异步Bridge方法
    public void func2(String arg, ScriptBridge.Callback callback) {
        callback.call("Java异步回调");
    }
}