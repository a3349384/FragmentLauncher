package cn.zmy.fragmentlauncher.compiler;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by zmy on 2018/9/14.
 */

public class GenerateModel
{
    private TypeElement fragmentElement;
    private String methodName;
    private List<ArgModel> args;

    public GenerateModel()
    {
        args = new ArrayList<>();
    }

    public TypeElement getFragmentElement()
    {
        return fragmentElement;
    }

    public void setFragmentElement(TypeElement fragmentElement)
    {
        this.fragmentElement = fragmentElement;
    }

    public String getMethodName()
    {
        return methodName;
    }

    public void setMethodName(String methodName)
    {
        this.methodName = methodName;
    }

    public List<ArgModel> getArgs()
    {
        return args;
    }
}
