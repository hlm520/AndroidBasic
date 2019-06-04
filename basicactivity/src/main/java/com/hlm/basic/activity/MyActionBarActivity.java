package com.hlm.basic.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hlm.basic.R;
import com.hlm.basic.file.FilePlay;
import com.hlm.basic.file.Index;
import com.hlm.basic.file.StoreGain;

import java.io.File;

public class MyActionBarActivity extends AppCompatActivity {

    private StoreGain sg = null;

    protected void setMyActionBar() {
        CharSequence title = getTitle();
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        View mActionBarView = LayoutInflater.from(this).inflate(R.layout.my_bar, null);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setCustomView(mActionBarView, lp);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            ((TextView) findViewById(R.id.label)).setText(title);
        }
    }

    protected void setMyActionBar(View bar) {
        CharSequence title = getTitle();
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setCustomView(bar, lp);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            setTitle(title);
        }
    }

    protected void setMyActionBar(String title) {
        setMyActionBar();
        setTitle(title);
    }

    protected void setTitle(String title) {
        if (title != null && !title.equals("") && findViewById(R.id.label) != null)
            ((TextView) findViewById(R.id.label)).setText(title);
    }

    protected void setDefaultHelp(String help) {
        String path = "help/" + getPackageName() + "/Default_Help.x";
        if (!(new File("sdcard/" + path)).exists()) {
            sg = new FilePlay().setGoal(Index.SD).setPath(path);
            sg.storeString(help);
        }
    }

    protected void setHelpListener(String help) {
        String path;
        if (help != null && !help.equals("")) {
            setListener(help);
            path = "help/" + getPackageName() + "/" + getLocalClassName() + "_Help.x";
            if (!(new File("sdcard/" + path)).exists()) {
                sg = new FilePlay().setGoal(Index.SD).setPath(path);
                sg.storeString(help);
                setListener(help);
            }
        } else {
            path = "help/" + getPackageName() + "/Default_Help.x";
            if ((new File("sdcard/" + path)).exists()) {
                sg = new FilePlay().setGoal(Index.SD).setPath(path);
                setListener(sg.gainString());
            }
        }
    }

    private void setListener(final String help) {
        if (findViewById(R.id.help) != null)
            findViewById(R.id.help).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MyActionBarActivity.this, help, Toast.LENGTH_LONG).show();
                }
            });
    }

}
