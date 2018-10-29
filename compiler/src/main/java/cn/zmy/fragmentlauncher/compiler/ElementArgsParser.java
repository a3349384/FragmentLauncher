package cn.zmy.fragmentlauncher.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

import cn.zmy.fragmentlauncher.Arg;
import cn.zmy.fragmentlauncher.Args;
import cn.zmy.fragmentlauncher.ArrayListArg;
import cn.zmy.fragmentlauncher.ArrayListArgs;

public class ElementArgsParser implements IElementArgsParser
{
    private boolean mReverse;

    public ElementArgsParser(boolean reverse)
    {
        this.mReverse = reverse;
    }

    @Override
    public List<ArgModel> parse(Element element)
    {
        List<ArgModel> args = new ArrayList<>();
        List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
        if (mReverse)
        {
            Collections.reverse(annotationMirrors);
        }
        for (AnnotationMirror annotationMirror : annotationMirrors)
        {
            String annotationName = annotationMirror.getAnnotationType().toString();
            boolean isArgs = annotationName.contentEquals(Args.class.getCanonicalName());
            boolean isArrayListArgs = annotationName.contentEquals(ArrayListArgs.class.getCanonicalName());
            if (isArgs || isArrayListArgs)
            {
                Map<? extends ExecutableElement,? extends AnnotationValue> map = annotationMirror.getElementValues();
                for (Map.Entry<? extends ExecutableElement,? extends AnnotationValue> entry : map.entrySet())
                {
                    if (entry.getKey().getSimpleName().contentEquals("value"))
                    {
                        List<AnnotationMirror> subAnnotationMirrors = (List<AnnotationMirror>) entry.getValue().getValue();
                        if (mReverse)
                        {
                            Collections.reverse(subAnnotationMirrors);
                        }
                        args.addAll(parseArgs(subAnnotationMirrors, isArrayListArgs));
                        break;
                    }
                }
                continue;
            }
            boolean isArg = annotationName.contentEquals(Arg.class.getCanonicalName());
            boolean isArrayListArg = annotationName.contentEquals(ArrayListArg.class.getCanonicalName());
            if (isArg || isArrayListArg)
            {
                ArgModel argModel = parseArg(annotationMirror, isArrayListArg);
                if (argModel != null)
                {
                    args.add(argModel);
                }
            }
        }
        return args;
    }

    private List<ArgModel> parseArgs(List<AnnotationMirror> mirrors, boolean isArrayList)
    {
        List<ArgModel> args = new ArrayList<>();
        if (mirrors == null)
        {
            return args;
        }
        for (AnnotationMirror argMirror : mirrors)
        {
            ArgModel argModel = parseArg(argMirror, isArrayList);
            if (argModel != null)
            {
                args.add(argModel);
            }
        }
        return args;
    }

    private ArgModel parseArg(AnnotationMirror argMirror, boolean isArrayList)
    {
        String name = null;
        TypeMirror typeMirror = null;
        Map<? extends ExecutableElement,? extends AnnotationValue> map = argMirror.getElementValues();
        for (Map.Entry<? extends ExecutableElement,? extends AnnotationValue> entry : map.entrySet())
        {
            switch (entry.getKey().getSimpleName().toString())
            {
                case "name":
                {
                    name = entry.getValue().getValue().toString();
                    break;
                }
                case "type":
                {
                    typeMirror = (TypeMirror) entry.getValue().getValue();
                    break;
                }
            }
        }
        if (name != null && typeMirror != null)
        {
            return new ArgModel(name, typeMirror, isArrayList);
        }
        return null;
    }
}
