package cn.zmy.fragmentlauncher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by zmy on 2018/9/17.
 */

public class FragmentLaunchHandler implements IFragmentLaunchHandler
{
    @Override
    public void handle(Context context, String fragmentClass, Bundle arguments)
    {
        Intent intent = new Intent(context, LaunchFragmentActivity.class);
        intent.putExtra("fragmentClass", fragmentClass);
        intent.putExtra("arg", arguments);
        context.startActivity(intent);
    }
}
