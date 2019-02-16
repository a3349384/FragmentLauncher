package cn.zmy.fragmentlauncher.impl;

/**
 * Created by zmy on 2018/12/26.
 * Fragment实现此接口可以收到OnNewIntent事件
 */

public interface INewIntentAware
{
    void onNewIntent();
}
