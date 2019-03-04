package com.example.hook;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;

import com.example.hook.utils.TextLogUtil;

import java.lang.reflect.Proxy;

public final class HookHelper {

    public static final String EXTRA_TARGET_INTENT = "extra_target_intent";

    public static void hookActivityManager() {
        try {
            //获取AMN 的gGetDefault的单例，gDefault
            Object mInstanceField = null;
            Object gDefault = null;
            if (Build.VERSION.SDK_INT > 26) {
                gDefault = RefInvoke.getStaticFieldObject("android.app.ActivityManager","IActivityManagerSingleton");
            } else {
                gDefault = RefInvoke.getStaticFieldObject("android.app.ActivityManagerNative","gDefaut");
            }
            mInstanceField = RefInvoke.getFieldObject("android.util.Singleton",gDefault,"mInstance");
            Class<?> iActivityManagerInterface = Class.forName("android.app.IActivityManager");

            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class<?>[]{iActivityManagerInterface}
            ,new HookAMProxy(mInstanceField));

            RefInvoke.setFieldObject("android.util.Singleton",gDefault,"mInstance",proxy);
        } catch (Exception e) {
            throw new RuntimeException("Hook Failed",e);
        }
    }

    public static void hookPackageManager() {
        try {
            //获取ActivityThread对象
            Object currentActivityThread = RefInvoke.getStaticFieldObject(
                    "android.app.ActivityThread",
                    "sCurrentActivityThread"
            );

            //获取ActivityThread里面原始的sPackageManager
            Object sPackageManager = RefInvoke.getFieldObject(
                    "android.app.ActivityThread",
                    currentActivityThread,
                    "sPackageManager"
            );

            //获取ActivityThread里面原始的sPackageManager
            Class<?> iPackageManagerInterface = Class.forName("android.content.pm.IPackageManager");

            Object proxy = Proxy.newProxyInstance(
                    iPackageManagerInterface.getClassLoader(),
                    new Class<?>[]{iPackageManagerInterface},
                    new HookPMProxy(sPackageManager)
            );


            //替换掉ActivityThread 里面的sPackageManager字段
            RefInvoke.setFieldObject(
                    currentActivityThread,"sPackageManager",proxy);

            //替换ApplicationPackageManager里面的mPM
//            PackageManager pm = context.getPackageManager();
////            RefInvoke.setFieldObject(pm,"mPM",proxy);
        } catch (Exception e) {
            TextLogUtil.textLog(e.getMessage());
        }
    }

    public static void hookInstrumentation(Activity activity) {
        Instrumentation instrumentation = (Instrumentation)RefInvoke.getFieldObject(Activity.class,activity,"mInstrumentation");

        Instrumentation evilIns = new EvilInstrumentation(instrumentation);
        RefInvoke.setFieldObject(Activity.class,activity,"mInstrumentation",evilIns);
    }

    public static void hookMH() {
        Object currentActivityTread = RefInvoke.getStaticFieldObject("android.app.ActivityThread"
        ,"sCurrentActivityThread");

        Handler mH = (Handler)RefInvoke.getFieldObject("android.app.ActivityThread",currentActivityTread,"mH");

        RefInvoke.setFieldObject(Handler.class,mH,"mCallback",new HookCallback(mH));
    }

    public static void hookActivityThreadInsrtumentation() {
        //获取当前activityThread对象
        Object currentActivityThread = RefInvoke.invokeStaticMethod(
                "android.app.ActivityThread","currentActivityThread",null,null);

        Instrumentation instrumentation = (Instrumentation) RefInvoke.getFieldObject(
                currentActivityThread,"mInstrumentation"
        );

        Instrumentation proxy = new EvilInstrumentation(instrumentation);

        RefInvoke.setFieldObject(currentActivityThread,"mInstrumentation",proxy);

    }

}
