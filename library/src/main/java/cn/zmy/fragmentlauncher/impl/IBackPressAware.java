package cn.zmy.fragmentlauncher.impl;

/**
 * Created by zmy on 2018/11/26.
 */

public interface IBackPressAware
{
    /**
     * 当返回按键按下时触发
     * @return 返回false可以拦截此事件
     * */
    boolean onBackPress();
}
