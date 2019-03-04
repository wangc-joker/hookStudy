package com.example.hook;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.example.hook.utils.TextLogUtil;

public class EvilInstrumentation extends Instrumentation {

    private static final String TAG = "EvilInstrumentation";

    //ActivityThread中的原始对象
    Instrumentation mBase;

    public EvilInstrumentation(Instrumentation instrumentation) {
        this.mBase = instrumentation;
    }

    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {

        Class[] p1 = {
                Context.class,
                IBinder.class,
                IBinder.class,
                Activity.class,
                Intent.class,
                int.class,
                Bundle.class
        };

        Object[] v1 = {
                who,
                contextThread,
                token,
                target,
                intent,
                requestCode,
                options
        };
        return (ActivityResult)RefInvoke.invokeInstanceMethod(mBase,"execStartActivity",p1,v1);
    }

    public Activity newActivity(ClassLoader cl, String className,
                                Intent intent)
            throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {

        Intent rawIntent = intent.getParcelableExtra(HookHelper.EXTRA_TARGET_INTENT);
        if (rawIntent == null) {
            return mBase.newActivity(cl,className,intent);
        }
        Log.d("cong","className:"+className+" rawItent:"+rawIntent);
        String newClassName = rawIntent.getComponent().getClassName();
        return mBase.newActivity(cl,newClassName,null);
    }

    public void callActivityOnCreate(Activity activity, Bundle icicle) {
         TextLogUtil.textLog("我hook 了 callActivityOnCreate");
         mBase.callActivityOnCreate(activity,icicle);
    }

}
