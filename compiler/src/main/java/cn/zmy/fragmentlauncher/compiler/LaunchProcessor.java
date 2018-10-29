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
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import cn.zmy.fragmentlauncher.Arg;
import cn.zmy.fragmentlauncher.Args;
import cn.zmy.fragmentlauncher.ArrayListArg;
import cn.zmy.fragmentlauncher.ArrayListArgs;
import cn.zmy.fragmentlauncher.Launch;
import cn.zmy.fragmentlauncher.LaunchForResult;

/**
 * Created by zmy on 2018/9/13.
 */

public class LaunchProcessor extends AbstractProcessor
{
    public static final String TAG = "LaunchProcessor";

    private Filer mFiler;
    private Elements mElements;
    private Messager mMessager;
    private IElementArgsParser mElementArgsParser;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv)
    {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElements = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        mElementArgsParser = new ElementArgsParser(true);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment)
    {
        Set<? extends Element> launchElements = roundEnvironment.getElementsAnnotatedWith(Launch.class);
        Set<? extends Element> launchForResultElements = roundEnvironment.getElementsAnnotatedWith(LaunchForResult.class);
        List<GenerateModel> generateModels = new ArrayList<>();
        for (Element element : launchElements)
        {
            if (element.getKind() != ElementKind.CLASS)
            {
                continue;
            }
            GenerateModel generateModel = new GenerateModel();
            generateModel.setFragmentElement((TypeElement) element);
            generateModel.setMethodName(element.getAnnotation(Launch.class).name());
            generateModel.getArgs().addAll(mElementArgsParser.parse(element));
            generateModels.add(generateModel);
        }
        for (Element element : launchForResultElements)
        {
            if (element.getKind() != ElementKind.CLASS)
            {
                continue;
            }
            GenerateModel generateModel = new GenerateModel();
            generateModel.setFragmentElement((TypeElement) element);
            generateModel.setMethodName(element.getAnnotation(LaunchForResult.class).name());
            generateModel.getArgs().addAll(mElementArgsParser.parse(element));
            generateModel.setForResult(true);
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
        types.add(LaunchForResult.class.getCanonicalName());
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

    private void generateLauncher(List<GenerateModel> generateModels)
    {
        String packageName = "cn.zmy.fragmentlauncher";
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
            String fragmentClassName = generateModel.getFragmentElement().getQualifiedName().toString();
            if (generateModel.isForResult())
            {
                methodBuilder.addStatement("$T.postHandle(fragment, requestCode, $S, bundle)", TypeNames.FragmentLauncher, fragmentClassName);
            }
            else
            {
                methodBuilder.addStatement("$T.postHandle(context, $S, bundle)", TypeNames.FragmentLauncher, fragmentClassName);
            }
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
