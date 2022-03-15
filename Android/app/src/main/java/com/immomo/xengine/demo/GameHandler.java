package com.immomo.xengine.demo;

import android.app.Activity;
import android.widget.Toast;

public final class GameHandler {

    private Activity activity;

    public GameHandler(Activity activity) {
        this.activity = activity;
    }

    public String removeGame(String msg) {
        activity.runOnUiThread(() -> {
            Toast.makeText(activity, "removeGame:" + msg, Toast.LENGTH_SHORT).show();
        });
        return null;
    }
}
