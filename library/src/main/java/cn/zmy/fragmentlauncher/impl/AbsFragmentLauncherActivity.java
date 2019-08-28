package cn.zmy.fragmentlauncher.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Set;

/**
 * Created by zmy on 2018/9/13.
 * 默认的FragmentLauncher Activity实现。
 * <p>
 *     本类不关心Activity的布局如何，子类只需要给定一个View容器，然后调用{@link AbsFragmentLauncherActivity#fillFragmentToActivity(int)}即可。
 * </p>
 * <p>
 *     本类对onNewIntent提供了支持。Fragment如果关心onNewIntent,只需要实现{@link INewIntentAware}即可。
 * </p>
 * <p>
 *     本类对BackPressed事件提供了支持。Fragment如果关心BackPressed事件,只需要实现{@link IBackPressAware}即可。
 * </p>
 */

public abstract class AbsFragmentLauncherActivity extends AppCompatActivity
{
    //Fragment分为标准和V4包的，所以这里用Object表示
    protected Object mFragment;

    protected SupportFragmentLifecycleCallbacks mSupportFragmentLifecycleCallbacks;
    protected Set<IBackPressAware> mBackPressAwareList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mBackPressAwareList = new ArraySet<>();
        mSupportFragmentLifecycleCallbacks = new SupportFragmentLifecycleCallbacks();;
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(mSupportFragmentLifecycleCallbacks, true);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mSupportFragmentLifecycleCallbacks != null)
        {
            getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(mSupportFragmentLifecycleCallbacks);
        }
        if (mBackPressAwareList != null)
        {
            mBackPressAwareList.clear();
        }
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
        fillFragmentArgs();
        if (mFragment instanceof INewIntentAware)
        {
            ((INewIntentAware) mFragment).onNewIntent();
        }
    }

    @Override
    public void onBackPressed()
    {
        for (IBackPressAware backPressAware : mBackPressAwareList)
        {
            if (!backPressAware.onBackPress())
            {
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public void onAttachFragment(android.app.Fragment fragment)
    {
        super.onAttachFragment(fragment);
        doOnFragmentAttach(fragment);
    }

    /**
     * 将Fragment放置于Activity中。
     * <p>
     *     本方法会先创建Fragment，给Fragment赋予参数，最后将Fragment放置于参数指定的View容器中。
     * </p>
     * */
    protected void fillFragmentToActivity(int fragmentContainerId)
    {
        mFragment = createFragment();
        fillFragmentArgs();
        if (mFragment instanceof Fragment)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(fragmentContainerId, (Fragment) mFragment)
                    .commit();
        }
        else if (mFragment instanceof android.app.Fragment)
        {
            getFragmentManager().beginTransaction()
                                .replace(fragmentContainerId, (android.app.Fragment) mFragment)
                                .commit();
        }
        doOnFragmentFilled(mFragment);
    }

    /**
     * 从Intent参数中解析出Fragment，返回这个Fragment的实例化结果。
     * 如果Intent参数中不存在Fragment，则使用{@link AbsFragmentLauncherActivity#getDefaultFragmentClass()}返回的Fragment。
     * */
    private Object createFragment()
    {
        try
        {
            Intent intent = getIntent();
            if (intent == null)
            {
                return null;
            }
            Class<?> fragmentClass = (Class<?>) intent.getSerializableExtra(FragmentLauncherConstant.KEY_FRAGMENT_CLASS);
            if (fragmentClass == null)
            {
                fragmentClass = getDefaultFragmentClass();
            }
            if (!(Fragment.class.isAssignableFrom(fragmentClass) || android.app.Fragment.class.isAssignableFrom(fragmentClass)))
            {
                return null;
            }
            return fragmentClass.newInstance();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 从Intent中获取参数，并将参数设置给Fragment
     * */
    private void fillFragmentArgs()
    {
        Bundle args = getIntent().getBundleExtra(FragmentLauncherConstant.KEY_ARGS);
        if (mFragment instanceof Fragment)
        {
            ((Fragment) mFragment).setArguments(args);
        }
        else if (mFragment instanceof android.app.Fragment)
        {
            ((android.app.Fragment) mFragment).setArguments(args);
        }
    }

    /**
     * 返回一个默认的Fragment。
     * <p>
     *     当从Intent中未能得到Fragment时，就会使用默认的Fragment。比如应用程序的启动Activity，可以使用。
     * </p>
     * */
    protected abstract Class<?> getDefaultFragmentClass();

    protected void doOnFragmentFilled(Object fragment)
    {

    }

    protected void doOnFragmentAttach(Object fragment)
    {
        if (fragment instanceof IBackPressAware)
        {
            mBackPressAwareList.add((IBackPressAware) fragment);
        }
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    protected void doOnFragmentDetached(Object fragment)
    {
        if (mBackPressAwareList != null && mBackPressAwareList.contains(fragment))
        {
            mBackPressAwareList.remove(fragment);
        }
    }

    private class SupportFragmentLifecycleCallbacks extends FragmentManager.FragmentLifecycleCallbacks
    {
        @Override
        public void onFragmentAttached(FragmentManager fm, Fragment f, Context context)
        {
            super.onFragmentAttached(fm, f, context);
            doOnFragmentAttach(f);
        }

        @Override
        public void onFragmentDetached(FragmentManager fm, Fragment f)
        {
            super.onFragmentDetached(fm, f);
            doOnFragmentDetached(f);
        }
    }
}
