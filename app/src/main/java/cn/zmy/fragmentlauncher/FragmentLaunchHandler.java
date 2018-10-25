package cn.zmy.fragmentlauncher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by zmy on 2018/9/17.
 */

public class FragmentLaunchHandler implements IFragmentLaunchHandler
{
    @Override
    public void handleLaunch(Context context, String fragmentClass, Bundle arguments)
    {
        Intent intent = new Intent(context, LaunchFragmentActivity.class);
        intent.putExtra("fragmentClass", fragmentClass);
        intent.putExtra("arg", arguments);
        context.startActivity(intent);
    }

    @Override
    public void handleLaunchForResult(Activity activity, int requestCode, String fragmentClass, Bundle arguments)
    {
        Intent intent = new Intent(activity, LaunchFragmentActivity.class);
        intent.putExtra("fragmentClass", fragmentClass);
        intent.putExtra("arg", arguments);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void handleLaunchForResult(Fragment fragment, int requestCode, String fragmentClass, Bundle arguments)
    {
        Intent intent = new Intent(fragment.getActivity(), LaunchFragmentActivity.class);
        intent.putExtra("fragmentClass", fragmentClass);
        intent.putExtra("arg", arguments);
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    public void handleLaunchForResult(android.app.Fragment fragment, int requestCode, String fragmentClass, Bundle arguments)
    {
        Intent intent = new Intent(fragment.getActivity(), LaunchFragmentActivity.class);
        intent.putExtra("fragmentClass", fragmentClass);
        intent.putExtra("arg", arguments);
        fragment.startActivityForResult(intent, requestCode);
    }
}
