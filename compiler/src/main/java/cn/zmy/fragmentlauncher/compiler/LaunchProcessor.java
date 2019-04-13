package cn.zmy.fragmentlauncher.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
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
import javax.tools.Diagnostic;

import cn.zmy.fragmentlauncher.Arg;
import cn.zmy.fragmentlauncher.Args;
import cn.zmy.fragmentlauncher.ArrayListArg;
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

    private String mBasePackage = "cn.zmy.fragmentlauncher";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv)
    {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElements = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        Map<String, String> options = processingEnv.getOptions();
        if (options != null && options.containsKey("basePackage"))
        {
            mBasePackage = options.get("basePackage");
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment)
    {
        Set<? extends Element> launchElements = roundEnvironment.getElementsAnnotatedWith(Launch.class);
        List<GenerateModel> generateModels = new ArrayList<>();
        for (Element element : launchElements)
        {
            if (element.getKind() != ElementKind.CLASS)
            {
                continue;
            }

            Launch launch = element.getAnnotation(Launch.class);
            GenerateModel generateModel = new GenerateModel();
            generateModel.setFragmentElement((TypeElement) element);
            generateModel.setMethodName(launch.name());
            generateModel.setForResult(launch.forResult());
            generateModel.getArgs().addAll(getArgs(element));
            generateModel.setTargetActivityClassName(getTargetActivityClassName(element));
            generateModels.add(generateModel);
        }
        if (generateModels.size() > 0)
        {
            generateLauncher(generateModels);
            generateArguments(generateModels);
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes()
    {
        Set<String> types = new HashSet<>();
        types.add(Launch.class.getCanonicalName());
        types.add(Args.class.getCanonicalName());
        types.add(ArrayListArgs.class.getCanonicalName());
        types.add(Arg.class.getCanonicalName());
        types.add(ArrayListArg.class.getCanonicalName());
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

    private String getTargetActivityClassName(Element element)
    {
        List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
        AnnotationMirror launchAnnotationMirror = null;
        for (AnnotationMirror annotationMirror : annotationMirrors)
        {
            String annotationName = annotationMirror.getAnnotationType().toString();
            if (annotationName.contentEquals(Launch.class.getCanonicalName()))
            {
                launchAnnotationMirror = annotationMirror;
                break;
            }
        }
        if (launchAnnotationMirror == null)
        {
            mMessager.printMessage(Diagnostic.Kind.ERROR, "AAA:launchAnnotationMirror == null");
            return null;
        }
        TypeMirror typeMirror = null;
        Map<? extends ExecutableElement,? extends AnnotationValue> map = launchAnnotationMirror.getElementValues();
        for (Map.Entry<? extends ExecutableElement,? extends AnnotationValue> entry : map.entrySet())
        {
            switch (entry.getKey().getSimpleName().toString())
            {
                case "target":
                {
                    typeMirror = (TypeMirror) entry.getValue().getValue();
                    break;
                }
            }
        }
        if(typeMirror == null)
        {
            return null;
        }
        return typeMirror.toString();
    }

    private void generateLauncher(List<GenerateModel> generateModels)
    {
        String launcherClassName = "Launcher";
        TypeSpec.Builder launcherBuilder = TypeSpec.classBuilder(launcherClassName)
                                                   .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        for (GenerateModel generateModel : generateModels)
        {
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(generateModel.getMethodName())
                                                                .returns(TypeName.VOID)
                                                                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
            if (generateModel.isForResult())
            {
                methodBuilder.addParameter(ClassName.OBJECT, "fragment");
                methodBuilder.addParameter(ClassName.INT, "requestCode");
            }
            else
            {
                methodBuilder.addParameter(TypeNames.Context, "context");
            }
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
                        default:
                        {
                            //视为Parcelable的子类
                            argTypeName = ParameterizedTypeName.get(TypeNames.ArrayList,
                                    TypeName.get(argModel.getTypeMirror()));
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
                        default:
                        {
                            callMethodName = "fillBundleParcelableArrayList";
                            break;
                        }
                    }
                }
                methodBuilder.addStatement("$T.$L($L, $S, $L)", TypeNames.BundleHelper, callMethodName, bundleName, argModel.getName(), argModel.getName());
            }
            String fragmentClassName = generateModel.getFragmentElement().getQualifiedName().toString();
            String targetActivityClassName = generateModel.getTargetActivityClassName();
            if (generateModel.isForResult())
            {
                methodBuilder.addStatement("$T.postHandle(fragment, requestCode, $S, bundle, $S)", TypeNames.FragmentLauncher, fragmentClassName, targetActivityClassName);
            }
            else
            {
                methodBuilder.addStatement("$T.postHandle(context, $S, bundle, $S)", TypeNames.FragmentLauncher, fragmentClassName, targetActivityClassName);
            }
            launcherBuilder.addMethod(methodBuilder.build());
        }
        JavaFile javaFile = JavaFile.builder(mBasePackage, launcherBuilder.build()).build();
        try
        {
            javaFile.writeTo(mFiler);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void generateArguments(List<GenerateModel> generateModels)
    {
        Set<String> classNameSet = new HashSet<>();
        for (GenerateModel generateModel : generateModels)
        {
            String packageName = ElementUtil.getTypeElementPackage(generateModel.getFragmentElement());
            String className = generateModel.getFragmentElement().getSimpleName().toString() + "Arguments";
            //由于@Launch和LaunchForResult可能同时注解在同一个Fragment上面，这里需要去重
            if (classNameSet.contains(className))
            {
                continue;
            }
            classNameSet.add(className);
            TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

            //instance
            ClassName argumentsClassName = ClassName.get(packageName, className);
            FieldSpec.Builder instanceFieldBuilder =
                    FieldSpec.builder(argumentsClassName, "instance", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);
            instanceFieldBuilder.initializer("new $T()", argumentsClassName);
            classBuilder.addField(instanceFieldBuilder.build());

            //mBundle
            FieldSpec bundleField = FieldSpec.builder(TypeNames.Bundle, "mBundle", Modifier.PRIVATE).build();
            classBuilder.addField(bundleField);

            //private constructor
            classBuilder.addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build());

            //initBundle
            ParameterSpec bundleParameter = ParameterSpec.builder(TypeNames.Bundle, "bundle").build();
            MethodSpec.Builder initMethodBuilder = MethodSpec.methodBuilder("init")
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(bundleParameter)
                    .addStatement("$N = $N", bundleField, bundleParameter);
            classBuilder.addMethod(initMethodBuilder.build());

            for (ArgModel argModel : generateModel.getArgs())
            {
                TypeName returnType;
                if (argModel.isArrayList())
                {
                    returnType = ParameterizedTypeName.get(TypeNames.ArrayList, TypeName.get(argModel.getTypeMirror()));
                }
                else
                {
                    returnType = TypeName.get(argModel.getTypeMirror());
                }
                MethodSpec.Builder argMethodBuilder = MethodSpec.methodBuilder(argModel.getName())
                        .addModifiers(Modifier.PUBLIC)
                        .returns(returnType)
                        .addStatement("return ($T)$N.get($S)", returnType, bundleField, argModel.getName());
                classBuilder.addMethod(argMethodBuilder.build());
            }

            JavaFile javaFile = JavaFile.builder(packageName, classBuilder.build()).build();
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
}
