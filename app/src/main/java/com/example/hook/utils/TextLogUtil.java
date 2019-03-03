package com.example.hook.utils;

import android.os.Message;

import com.example.hook.MainActivity;

public class TextLogUtil {

    public static void textLog(String msgText) {
        Message msg = MainActivity.getInstance().handler.obtainMessage();
        msg.obj = "\n"+msgText;
        MainActivity.getInstance().handler.sendMessage(msg);
    }

}
