package cn.zmy.fragmentlauncher;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by zmy on 2018/9/13.
 */

public interface IFragmentLaunchHandler
{
    void handle(Context context, String fragmentClass, Bundle arguments);
}
