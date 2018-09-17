package cn.zmy.fragmentlauncher.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;

import java.util.ArrayList;

/**
 * Created by zmy on 2018/9/14.
 */

public class TypeNames
{
    public static final ClassName FragmentLauncher = ClassName.get("cn.zmy.fragmentlauncher", "FragmentLauncher");
    public static final ClassName Bundle = ClassName.get("android.os", "Bundle");
    public static final ClassName BundleHelper = ClassName.get("cn.zmy.fragmentlauncher", "BundleHelper");
    public static final ClassName Parcelable = ClassName.get("android.os", "Parcelable");
    public static final ClassName ArrayList = ClassName.get("java.util", "ArrayList");
    public static final ClassName Context = ClassName.get("android.content", "Context");

    public static final ParameterizedTypeName ArrayListInt = ParameterizedTypeName.get(ArrayList.class, Integer.class);
    public static final ParameterizedTypeName ArrayListString = ParameterizedTypeName.get(ArrayList.class, String.class);
    public static final ParameterizedTypeName ArrayListCharSequence = ParameterizedTypeName.get(ArrayList.class, CharSequence.class);
    public static final ParameterizedTypeName ArrayListParcelable = ParameterizedTypeName.get(ArrayList, Parcelable);
}
