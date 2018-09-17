package cn.zmy.fragmentlauncher.compiler;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

import cn.zmy.fragmentlauncher.Args;
import cn.zmy.fragmentlauncher.ArrayListArgs;
import cn.zmy.fragmentlauncher.Launch;

/**
 * Created by zmy on 2018/9/13.
 */

public class LaunchProcessor extends AbstractProcessor
{
    public static final String TAG = "LaunchProcessor";

    private Filer mFiler;
    private Elements mElements;
    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv)
    {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElements = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment)
    {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Launch.class);
        List<GenerateModel> generateModels = new ArrayList<>();
        for (Element element : elements)
        {
            if (element.getKind() != ElementKind.CLASS)
            {
                continue;
            }
            GenerateModel generateModel = new GenerateModel();
            generateModel.setFragmentMirror(element.asType());
            generateModel.setMethodName(element.getAnnotation(Launch.class).name());
            generateModel.getArgs().addAll(getArgs(element));
            generateModels.add(generateModel);
        }
        generateCode(generateModels);
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes()
    {
        Set<String> types = new HashSet<>();
        types.add(Launch.class.getCanonicalName());
        types.add(Args.class.getCanonicalName());
        types.add(ArrayListArgs.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion()
    {
        return SourceVersion.latestSupported();
    }

    private List<ArgModel> getArgs(Element element)
    {
        List<ArgModel> args = new ArrayList<>();
        List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotationMirrors)
        {
            String annotaionName = annotationMirror.getAnnotationType().toString();
            boolean isArgs = annotaionName.contentEquals(Args.class.getCanonicalName());
            boolean isArrayListArgs = annotaionName.contentEquals(ArrayListArgs.class.getCanonicalName());
            if (isArgs || isArrayListArgs)
            {
                Map<? extends ExecutableElement,? extends AnnotationValue> map = annotationMirror.getElementValues();
                for (Map.Entry<? extends ExecutableElement,? extends AnnotationValue> entry : map.entrySet())
                {
                    if (entry.getKey().getSimpleName().contentEquals("value"))
                    {
                        List<AnnotationMirror> subAnnotationMirrors = (List<AnnotationMirror>) entry.getValue().getValue();
                        args.addAll(parseArgs(subAnnotationMirrors, isArrayListArgs));
                        break;
                    }
                }
            }
        }
        return args;
    }

    private List<ArgModel> parseArgs(List<AnnotationMirror> mirrors, boolean isArrayList)
    {
        List<ArgModel> args = new ArrayList<>();
        if (mirrors == null || mirrors.size() == 0)
        {
            return args;
        }
        for (AnnotationMirror argMirror : mirrors)
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
                args.add(new ArgModel(name, typeMirror, isArrayList));
            }
        }
        return args;
    }

    private void generateCode(List<GenerateModel> generateModels)
    {
        if (generateModels == null || generateModels.isEmpty())
        {
            return;
        }
        String packageName = "cn.zmy.fragmentlauncher";
        String launcherClassName = "Launcher";
        TypeSpec.Builder launcherBuilder = TypeSpec.classBuilder(launcherClassName)
                                                   .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        for (GenerateModel generateModel : generateModels)
        {
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(generateModel.getMethodName())
                                                                .returns(TypeName.VOID)
                                                                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
            methodBuilder.addParameter(TypeNames.Context, "context");
            for (ArgModel argModel : generateModel.getArgs())
            {
                TypeName argTypeName = TypeName.get(argModel.getTypeMirror());
                if (argModel.isArrayList())
                {
                    switch (argModel.getTypeMirror().toString())
                    {
                        case "java.lang.Integer":
                        {
                            argTypeName = TypeNames.ArrayListInt;
                            break;
                        }
                        case "java.lang.String":
                        {
                            argTypeName = TypeNames.ArrayListString;
                            break;
                        }
                        case "java.lang.CharSequence":
                        {
                            argTypeName = TypeNames.ArrayListCharSequence;
                            break;
                        }
                        case "android.os.Parcelable":
                        {
                            argTypeName = TypeNames.ArrayListParcelable;
                            break;
                        }
                    }
                }
                methodBuilder.addParameter(argTypeName, argModel.getName());
            }
            String bundleName = "bundle";
            methodBuilder.addStatement("$1T $2L = new $1T()", TypeNames.Bundle, bundleName);
            for (ArgModel argModel : generateModel.getArgs())
            {
                String callMethodName = "fillBundle";
                if (argModel.isArrayList())
                {
                    switch (argModel.getTypeMirror().toString())
                    {
                        case "java.lang.Integer":
                        {
                            callMethodName = "fillBundleIntArrayList";
                            break;
                        }
                        case "java.lang.String":
                        {
                            callMethodName = "fillBundleStringArrayList";
                            break;
                        }
                        case "java.lang.CharSequence":
                        {
                            callMethodName = "fillBundleCharSequenceArrayList";
                            break;
                        }
                        case "android.os.Parcelable":
                        {
                            callMethodName = "fillBundleParcelableArrayList";
                            break;
                        }
                    }
                }
                methodBuilder.addStatement("$T.$L($L, $S, $L)", TypeNames.BundleHelper, callMethodName, bundleName, argModel.getName(), argModel.getName());
            }
            String fragmentClassName = generateModel.getFragmentMirror().toString();
            methodBuilder.addStatement("$T.postHandle(context, $S, bundle)", TypeNames.FragmentLauncher, fragmentClassName);
            launcherBuilder.addMethod(methodBuilder.build());
        }
        JavaFile javaFile = JavaFile.builder(packageName, launcherBuilder.build()).build();
        try
        {
            javaFile.writeTo(mFiler);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
