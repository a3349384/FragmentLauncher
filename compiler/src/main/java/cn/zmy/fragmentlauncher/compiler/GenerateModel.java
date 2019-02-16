package cn.zmy.fragmentlauncher.compiler;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;

/**
 * Created by zmy on 2018/9/14.
 */

public class GenerateModel
{
    private TypeElement fragmentElement;
    private String methodName;
    private List<ArgModel> args;
    private boolean isForResult;
    private String targetActivityClassName;

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

    public boolean isForResult()
    {
        return isForResult;
    }

    public void setForResult(boolean forResult)
    {
        isForResult = forResult;
    }

    public String getTargetActivityClassName()
    {
        return targetActivityClassName;
    }

    public void setTargetActivityClassName(String targetActivityClassName)
    {
        this.targetActivityClassName = targetActivityClassName;
    }
}
