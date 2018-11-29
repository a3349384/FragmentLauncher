package cn.zmy.fragmentlauncher;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by zmy on 2018/9/13.
 */

public interface IFragmentLaunchHandler
{
    void handleLaunch(Context context, String fragmentClass, Bundle arguments, String targetActivityCls);

    void handleLaunchForResult(Activity activity, int requestCode, String fragmentClass, Bundle arguments, String targetActivityCls);

    void handleLaunchForResult(Fragment fragment, int requestCode, String fragmentClass, Bundle arguments, String targetActivityCls);

    void handleLaunchForResult(android.app.Fragment fragment, int requestCode, String fragmentClass, Bundle arguments, String targetActivityCls);
}
