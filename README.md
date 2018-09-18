1. 引入依赖

```
implementation 'cn.zmy:fragmentlauncher-library:1.0.0'
annatationProcessor 'cn.zmy:fragmentlauncher-compiler:1.0.0'
```

2. 开始使用

使用前需要先进行初始化。

```
// 推荐放置于Application#onCreate
FragmentLauncher.init(new FragmentLaunchHandler());
```

初始化需要一个`cn.zmy.fragmentlauncher.IFragmentLaunchHandler`类型的实例。

```
public interface IFragmentLaunchHandler
{
    void handle(Context context, String fragmentClass, Bundle arguments);
}
```

你需要实现这个接口，其中：

fragmentClass为即将启动的Fragment的完整类名。

arguments为启动这个Fragment需要携带的参数。

当需要启动Fragment时，系统会调用这个handle方法。

在Fragment上标注注解。

```
@Launch(name = "startToTest")
public class TestFragment extends Fragment
{
    ...
}
```

build之后，会自动生成一个Launcher类（完整包名：`cn.zmy.fragmentlauncher.Launcher`）.

你可以使用Launcher#startToTest启动Fragment。

如果需要为Fragment携带参数，可以使用`@Arg`和`@ArrayListArg`注解。

```
@Launch(name = "startToTest")
@Arg(name = "parcelableArrayArg", type = Parcelable[].class)
@ArrayListArg(name = "intArrayListArg", type = Integer.class)
public class TestFragment extends Fragment
{
    ...
}
```

上述代码表明启动这个Fragment，需要一个`Parcelable[]`类型的参数和一个`ArrayList<Integer>`的参数。

目前，FragmentLauncher支持除`SparseArray<? extends Parcelable>`类型之外的所有其他类型的参数。
