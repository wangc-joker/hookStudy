package com.example.hook;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    private static MainActivity mainActivity;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
//        HookHelper.hookActivityManager();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);
        mainActivity = this;
        HookHelper.hookActivityManager();
        HookHelper.hookMH();
        setAction();
    }

    public void setAction() {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,HookActivity.class));
            }
        });
    }

    public void updateLog(String msg) {
//        String text = "\n"+tag+":"+msg;
        textView.append(msg);
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            updateLog((String)msg.obj);
        }
    };

    public static MainActivity getInstance(){
        return mainActivity;
    }
}
