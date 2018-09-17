package cn.zmy.fragmentlauncher;

import android.app.Application;

/**
 * Created by zmy on 2018/9/17.
 */

public class TheApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        FragmentLauncher.init(new FragmentLaunchHandler());
    }
}
