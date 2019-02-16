package cn.zmy.fragmentlauncher;

import android.app.Activity;
import android.app.Application;

import cn.zmy.fragmentlauncher.impl.AbsFragmentLaunchHandler;

/**
 * Created by zmy on 2018/9/17.
 */

public class TheApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        FragmentLauncher.init(new AbsFragmentLaunchHandler()
        {
            @Override
            protected Class<? extends Activity> getDefaultActivityClass()
            {
                return FragmentLauncherActivity.class;
            }
        });
    }
}
