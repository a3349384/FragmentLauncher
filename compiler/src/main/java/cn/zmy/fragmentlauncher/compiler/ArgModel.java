package cn.zmy.fragmentlauncher.compiler;

import javax.lang.model.type.TypeMirror;

/**
 * Created by zmy on 2018/9/17.
 */

public class ArgModel
{
    private String name;
    private TypeMirror typeMirror;
    private boolean isArrayList;

    public ArgModel(String name, TypeMirror typeMirror, boolean isArrayList)
    {
        this.name = name;
        this.typeMirror = typeMirror;
        this.isArrayList = isArrayList;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public TypeMirror getTypeMirror()
    {
        return typeMirror;
    }

    public void setTypeMirror(TypeMirror typeMirror)
    {
        this.typeMirror = typeMirror;
    }

    public boolean isArrayList()
    {
        return isArrayList;
    }

    public void setArrayList(boolean arrayList)
    {
        isArrayList = arrayList;
    }
}
