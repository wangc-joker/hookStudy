package com.example.hook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class HookActivity extends AppCompatActivity {

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("cong","onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("cong","onResume");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hook);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("cong","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("cong","onDestroy");
    }
}
