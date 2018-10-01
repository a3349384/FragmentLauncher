package cn.zmy.fragmentlauncher.compiler;

import javax.lang.model.element.TypeElement;

/**
 * Created by zmy on 2018/10/1.
 */

public class ElementUtil
{
    public static String getTypeElementPackage(TypeElement element)
    {
        String fullName = element.getQualifiedName().toString();
        String simpleName = element.getSimpleName().toString();
        int index = fullName.length() - simpleName.length() - 1;
        return fullName.substring(0, index);
    }
}
