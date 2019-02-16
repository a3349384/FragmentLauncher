package cn.zmy.fragmentlauncher;

import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.zmy.fragmentlauncher.impl.AbsFragmentLauncherActivity;

/**
 * Created by zmy on 2018/9/17.
 */

public class FragmentLauncherActivity extends AbsFragmentLauncherActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_launcher);
        fillFragmentToActivity(R.id.fragmentContainer);
    }

    @Override
    protected Class<?> getDefaultFragmentClass()
    {
        return MainFragment.class;
    }
}
