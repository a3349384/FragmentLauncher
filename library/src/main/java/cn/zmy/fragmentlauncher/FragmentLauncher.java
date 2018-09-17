package cn.zmy.fragmentlauncher;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by zmy on 2018/9/14.
 */

public class FragmentLauncher
{
    private static IFragmentLaunchHandler mHandler;

    public static void init(IFragmentLaunchHandler handler)
    {
        mHandler = handler;
    }

    static void postHandle(Context context, String fragmentClass, Bundle arguments)
    {
        mHandler.handle(context, fragmentClass, arguments);
    }
}
