package com.example.hook;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;

public class HookCallback implements Handler.Callback {

    private Handler mBase;

    public HookCallback(Handler handler) {
        this.mBase = handler;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            // ActivityThread 里面 "LAUNCH_ACTIVITY"这个字段的值是100
            //本来使用反射的方式获取是最好的，这里是为了简便直接的使用硬编码
            case 100:
                handleLauncherActivity(msg);
                break;

        }
        mBase.handleMessage(msg);
        return true;
    }

    private void handleLauncherActivity(Message msg) {
        //这里简单起见，直接取出TargetActivity
        Object object = msg.obj;

        //把替身恢复成真身
        Intent intent = (Intent) RefInvoke.getFieldObject(object,"intent");
//        HookHelper.hookPackageManager();
        Intent targetIntent = intent.getParcelableExtra(HookHelper.EXTRA_TARGET_INTENT);
        intent.setComponent(targetIntent.getComponent());
//        TextLogUtil.textLog("把替身恢复成真身:"+targetIntent);
    }
}
