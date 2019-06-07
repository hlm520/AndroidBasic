package com.hlm.basic;

import android.Manifest;
import android.os.Bundle;

import com.hlm.basic.activity.BasicActivity;

public class MainActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSelfActionBarWithTitle(R.layout.action_bar_view,R.id.tv_title,"Main");

        new Thread() {
            private Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    setDefaultHelp("This is default help!");
                    setHelpListener("This is main listener");
                }
            };
            @Override
            public void run() {
                if (isRequested(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    runnable.run();
                } else {
                    request(runnable, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
        }.start();
    }
}
