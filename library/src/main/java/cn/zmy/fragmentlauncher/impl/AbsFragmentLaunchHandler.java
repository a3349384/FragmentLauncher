package cn.zmy.fragmentlauncher.impl;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import cn.zmy.fragmentlauncher.IFragmentLaunchHandler;

/**
 * Created by zmy on 2018/9/18.
 * {@link IFragmentLaunchHandler}的默认实现。这是一个抽象类，子类需要实现{@link AbsFragmentLaunchHandler#getDefaultActivityClass()}来提供一个Activity用于承载Fragment。
 * <p>
 *     需要启动的Fragment以及参数会通过Intent传递。其key分别为:{@link FragmentLauncherConstant#KEY_FRAGMENT_CLASS}和{@link FragmentLauncherConstant#KEY_ARGS}
 * </p>
 */

public abstract class AbsFragmentLaunchHandler implements IFragmentLaunchHandler
{
    @Override
    public void handleLaunch(Context context, String fragmentClass, Bundle arguments, String targetActivityClassName)
    {
        context.startActivity(getIntent(context, fragmentClass, arguments, targetActivityClassName));
    }

    @Override
    public void handleLaunchForResult(Activity activity, int requestCode, String fragmentClass, Bundle arguments, String targetActivityClassName)
    {
        Intent intent = getIntent(activity, fragmentClass, arguments, targetActivityClassName);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void handleLaunchForResult(Fragment fragment, int requestCode, String fragmentClass, Bundle arguments, String targetActivityClassName)
    {
        Intent intent = getIntent(fragment.getActivity(), fragmentClass, arguments, targetActivityClassName);
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    public void handleLaunchForResult(android.app.Fragment fragment, int requestCode, String fragmentClass, Bundle arguments, String targetActivityClassName)
    {
        Intent intent = getIntent(fragment.getActivity(), fragmentClass, arguments, targetActivityClassName);
        fragment.startActivityForResult(intent, requestCode);
    }

    private Intent getIntent(Context context, String fragmentClass, Bundle arguments, String targetActivityClassName)
    {
        try
        {
            Class<?> fragmentCls = Class.forName(fragmentClass);
            Class<?> targetActivityCls;
            if (targetActivityClassName != null)
            {
                targetActivityCls = Class.forName(targetActivityClassName);
            }
            else
            {
                targetActivityCls = getDefaultActivityClass();
            }
            Intent intent = new Intent(context, targetActivityCls);
            if (context instanceof Application || context instanceof Service)
            {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            intent.putExtra(FragmentLauncherConstant.KEY_FRAGMENT_CLASS, fragmentCls);
            intent.putExtra(FragmentLauncherConstant.KEY_ARGS, arguments);

            return intent;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    protected abstract Class<? extends Activity> getDefaultActivityClass();
}
