package cn.zmy.fragmentlauncher;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

/**
 * Created by zmy on 2018/9/14.
 */

public class FragmentLauncher
{
    static IFragmentLaunchHandler mHandler;

    public static void init(IFragmentLaunchHandler handler)
    {
        mHandler = handler;
    }

    public static void postHandle(Context context, String fragmentClass, Bundle arguments, String targetActivityCls)
    {
        mHandler.handleLaunch(context, fragmentClass, arguments, targetActivityCls);
    }

    public static void postHandle(Object activityOrFragment, int requestCode, String fragmentClass, Bundle arguments, String targetActivityCls)
    {
        if (activityOrFragment instanceof Activity)
        {
            mHandler.handleLaunchForResult((Activity) activityOrFragment, requestCode, fragmentClass, arguments, targetActivityCls);

        }
        else if (activityOrFragment instanceof Fragment)
        {
            mHandler.handleLaunchForResult((Fragment) activityOrFragment, requestCode, fragmentClass, arguments, targetActivityCls);
        }
        else if (activityOrFragment instanceof android.app.Fragment)
        {
            mHandler.handleLaunchForResult((android.app.Fragment) activityOrFragment, requestCode, fragmentClass, arguments, targetActivityCls);
        }
        else
        {
            throw new IllegalArgumentException("the argument fragment is invalid.");
        }
    }
}
