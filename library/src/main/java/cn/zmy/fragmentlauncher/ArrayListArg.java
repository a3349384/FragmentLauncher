package cn.zmy.fragmentlauncher;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zmy on 2018/9/17.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Repeatable(ArrayListArgs.class)
public @interface ArrayListArg
{
    String name();

    Class<?> type();
}
