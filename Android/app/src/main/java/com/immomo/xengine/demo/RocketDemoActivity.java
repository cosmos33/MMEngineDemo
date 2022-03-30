package com.immomo.xengine.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.immomo.xengine.demo.model.GameConfig;
import com.momo.xeengine.IXEngine;
import com.momo.xeengine.game.IXGameView;
import com.momo.xeengine.game.XEGameView;

import java.util.ArrayList;
import java.util.List;

/**
 * 定制火箭的DEMO
 */
public class RocketDemoActivity extends AppCompatActivity implements IXGameView.Callback {

    /**
     * 游戏内Bridge对象的名称，该名称由游戏脚本规定，不可更改。
     */
    private static final String GAME_HANDLER_NAME = "LiveGameHandler";

    /**
     * 游戏渲染View，引擎提供。
     */
    private XEGameView gameView;

    private final Gson gson = new Gson();

    /**
     * 火箭配置样式，这个demo中配置为空代表当前需要启动编辑模式
     */
    private String rocketConfig = null;

    private ViewGroup viewGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        viewGroup = findViewById(R.id.viewGroup);
        playGame();
    }

    private void playGame() {
        gameView = new XEGameView(this);
        viewGroup.addView(gameView);

        if (rocketConfig == null) {
            //火箭的配置界面，这个界面一般都是用半屏的
            //所以我这里简单设置了一个topMargin
            //当然这里可以根据业务需要自行处理，我只是举例说明如何设置半屏显示
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) gameView.getLayoutParams();
            layoutParams.topMargin = 500;
            gameView.setLayoutParams(layoutParams);
        }
        gameView.setBackgroundColor(Color.TRANSPARENT);
        gameView.setRenderViewType(XEGameView.TYPE_TEXTURE_VIEW);
        //渲染缩放比：越高显示效果越细腻，对手机的性能开销也就越大，默认为1。
        gameView.setRenderScale(1.f);
        //帧率：越高越流畅，对手机的性能开销也就越大，默认为30。
        gameView.setPreferredFramesPerSecond(30);
        gameView.setCallback(this);
        gameView.start();
    }

    private void stopGame() {
        gameView.stop();
        viewGroup.removeView(gameView);
        gameView = null;
    }

    @Override
    public void onRenderViewCreate(View view) {
        //渲染试图创建的回调，业务一般无需处理
    }

    @Override
    public void onStart(IXEngine engine) {
        //添加素材路径。火箭的素材就在这个目录里面
        engine.addLibraryPath(getExternalFilesDir("") + "/rocket");
        //添加头像的目录 这里我是随便写的，实际使用时需要根据业务进行替换。
        //getAvatars方法返回的头像需要在这个目录下。
        engine.addLibraryPath(getExternalFilesDir("") + "/avatars");
        //注册Bridge。
        engine.getScriptBridge().regist(this, GAME_HANDLER_NAME);

        //根据不同的模式启动不同的脚本。
        if (rocketConfig != null) {
            engine.getScriptEngine().startGameScriptFile("giftApp");
            engine.getScriptEngine().getScriptBridge().call(GAME_HANDLER_NAME, "gameInfo", rocketConfig);
        } else {
            //游戏配置，可以不传不传就是默认的配置。
            GameConfig gameConfig = new GameConfig();
//            gameConfig.setLanguage("en");
            //启动游戏脚本（编辑模式）
            engine.getScriptEngine().startGameScriptFile("app", gson.toJson(gameConfig));
        }

    }

    @Override
    public void onStartFailed(String s) {
        runOnUiThread(() -> Toast.makeText(this, "引擎启动失败:" + s, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onRenderSizeChanged(int i, int i1) {
        //渲染大小改变的回调，业务上一般用不到。
    }

    @Override
    public void onEngineDynamicLinkLibraryDownloadProcess(int i, double v) {
        //引擎SO动态下载进度回调，依赖于客户端中实现的下载器。
        //若未启用，则无需处理。
    }

    /**
     * 游戏结束的回调，发射模式回调。
     *
     * @param args 无需处理
     * @return 无需处理，返回空即可
     */
    @Keep
    public String removeGame(String args) {
        runOnUiThread(() -> {
            Toast.makeText(this, "火箭发射播放完成！！！", Toast.LENGTH_SHORT).show();
            stopGame();
        });
        return null;
    }

    /**
     * 发射火箭
     *
     * @param args 火箭的样式json
     * @return
     */
    @Keep
    public String startRocket(String args) {
        rocketConfig = args;
        runOnUiThread(() -> {
            //结束火箭编辑
            stopGame();
            //开启火箭发射
            playGame();

        });
        return null;
    }

    /**
     * 获取自定义的配置
     * @param args 无需处理
     * @return
     */
    @Keep
    public String getAvatars(String args) {
        List<String> avatars = new ArrayList<>();
        avatars.add("1.png");//主播头像
        avatars.add("2.png");//自己头像
        return gson.toJson(avatars);
    }
}
