package com.example.hook;

import android.content.ComponentName;
import android.content.Intent;

import com.example.hook.utils.TextLogUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class HookAMProxy implements InvocationHandler {

    private static final String TAG = "HookAMProxy";

    private Object mBase;

    public HookAMProxy(Object base){
        this.mBase = base;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
        if ("startActivity".equals(method.getName())) {
            TextLogUtil.textLog("wangcong hook "+method.getName());

            Intent raw;
            int index = 0;

            for (int i=0; i<args.length; i++) {
                if (args[i] instanceof Intent) {
                    index = i;
                    break;
                }
            }

            raw = (Intent) args[index];

            Intent newIntent = new Intent();

            //替身Activity的包名，也就是我们自己的包名
            String stubPackage = raw.getComponent().getPackageName();

            //这里把启动的Activity临时替换为占位Activity
            ComponentName componentName = new ComponentName(stubPackage,TargetActivity.class.getName());
            newIntent.setComponent(componentName);

            //把我们原始要启动的HookActivity先存起来
            newIntent.putExtra(HookHelper.EXTRA_TARGET_INTENT,raw);

            //替换掉Intent,达到欺骗AMS的目的
            args[index] = newIntent;

            TextLogUtil.textLog("wangcong hook success");

        }
        return method.invoke(mBase,args);
    }
}
