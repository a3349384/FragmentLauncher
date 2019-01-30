# 引入依赖

```
implementation 'cn.zmy:fragmentlauncher-library:1.4.1'
annatationProcessor 'cn.zmy:fragmentlauncher-compiler:1.4.1'
```

# 如何启动Fragment

# 初始化`IFragmentLaunchHandler`

```
public class FragmentLaunchHandler implements IFragmentLaunchHandler
{
    @Override
    public void handle(Context context, String fragmentClass, Bundle arguments)
    {
        //fragmentClass为即将启动的Fragment的完整类名。
        //arguments为启动这个Fragment需要的参数。
        //在这里需要你自己实现如何将Fragment放入Activity中
    }
}

// 推荐放置于Application#onCreate
FragmentLauncher.init(new FragmentLaunchHandler());
```

# 添加注解

在Fragment上标注`@Launch`注解。

```
@Launch(name = "startToTest")
public class TestFragment extends Fragment
{
    ...
}
```

Build之后，会生成一个Launcher类：

```
package cn.zmy.fragmentlauncher;

import android.content.Context;
import android.os.Bundle;

public final class Launcher {
  public static void startToTest(Context context) {
    Bundle bundle = new Bundle();
    FragmentLauncher.postHandle(context, "cn.zmy.fragmentlauncher.TestFragment", bundle, null);
  }
}
```

Launcher是根据`@Launch(name = "startToTest")`生成的，方法的名称就是`name`参数的值。

如果此Fragment需要参数启动，可以通过标注`@Arg`或者`@ArrayListArg`注解指定。

```
@Launch(name = "startToTest")
@Arg(name = "parcelableArrayArg", type = Parcelable[].class)
@ArrayListArg(name = "intArrayListArg", type = Integer.class)
public class TestFragment extends Fragment
{
    ...
}
```

`@Arg`注解主要用于int、long、double等8种Java基本类型以及相关数组类型和Parcelable类型等。

`@ArrayListArg`主要用于ArrayList类型，如ArrayList<Integer>.

Build之后，生成的方法如下：

```
public static void startToTest(Context context, Parcelable[] parcelableArrayArg,
    ArrayList<Integer> intArrayListArg) {
  Bundle bundle = new Bundle();
  BundleHelper.fillBundle(bundle, "parcelableArrayArg", parcelableArrayArg);
  BundleHelper.fillBundleIntArrayList(bundle, "intArrayListArg", intArrayListArg);
  FragmentLauncher.postHandle(context, "cn.zmy.fragmentlauncher.DemoFragment", bundle, null);
}
```

现在，如果需要启动TestFragment，只需要调用Launcher#startToTest方法。

## 如何在Fragment中获取参数

每一个标注了`@Arg`或者`@ArrayListArg`注解的Fragment，都会自动生成一个辅助类用于解析传递给Fragment的参数。辅助类的名称为Fragment的名称+Arguments。
比如上面的TestFragment，生成的辅助类就是TestFragmentArguments。

使用参数类之前，需要先初始化：

```
//Fragment#onCreate
@Override
public void onCreate(Bundle savedInstanceState)
{
    TestFragmentArguments.instance.init(getArguments());
}
```

获取参数：

```
Parcelable[] parcelables = TestFragmentArguments.instance.parcelableArrayArg();
ArrayList<Integer> intArrayList = TestFragmentArguments.instance.intArrayListArg();
```

# startForResult支持

如果某个Fragment需要以startForResult方式启动，可以通过`@Launch`注解的参数`forResult`指定：

```
@Launch(name = "startToTest", forResult = true)
public class TestFragment extends Fragment
{
    ...
}
```

Build之后会生成如下方法：

```
public static void startToTestForResult(Object fragmentOrActivity, int requestCode) {
  Bundle bundle = new Bundle();
  FragmentLauncher.postHandle(fragment, requestCode, "cn.zmy.fragmentlauncher.TestResultFragment", bundle, null);
}
```

`fragmentOrActivity`参数可以传递Activity的实例，也可以传递Fragment(android.app.Fragment、android.support.v4.app.Fragment均支持)的实例。

传递Activity实例，则在Activity中接收结果。

传递Fragment实例，则在Fragment中接收结果。

# 最后

目前，FragmentLauncher支持除`SparseArray<? extends Parcelable>`类型之外的所有其他类型的参数。
