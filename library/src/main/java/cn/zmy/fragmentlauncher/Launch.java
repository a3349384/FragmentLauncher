package cn.zmy.fragmentlauncher;

import android.app.Activity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zmy on 2018/9/13.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Launch
{
    String name();

    boolean forResult() default false;

    Class<? extends Activity> target() default Activity.class;
}
