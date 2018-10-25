package cn.zmy.fragmentlauncher;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

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
        mHandler.handleLaunch(context, fragmentClass, arguments);
    }

    static void postHandle(Object activityOrFragment, int requestCode, String fragmentClass, Bundle arguments)
    {
        if (activityOrFragment instanceof Activity)
        {
            mHandler.handleLaunchForResult((Activity) activityOrFragment, requestCode, fragmentClass, arguments);

        }
        else if (activityOrFragment instanceof Fragment)
        {
            mHandler.handleLaunchForResult((Fragment) activityOrFragment, requestCode, fragmentClass, arguments);
        }
        else if (activityOrFragment instanceof android.app.Fragment)
        {
            mHandler.handleLaunchForResult((android.app.Fragment) activityOrFragment, requestCode, fragmentClass, arguments);
        }
        else
        {
            throw new IllegalArgumentException("the argument fragment is invalid.");
        }
    }
}
