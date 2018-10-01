1. 引入依赖

```
implementation 'cn.zmy:fragmentlauncher-library:1.0.0'
annatationProcessor 'cn.zmy:fragmentlauncher-compiler:1.0.3'
```

2. 开始使用

在Fragment上标注`@Launch`注解。

```
@Launch(name = "startToTest")
public class TestFragment extends Fragment
{
    ...
}
```

如果这个Fragment需要携带参数启动，可以标注`@Arg`或者`@ArrayListArg`注解。

```
@Launch(name = "startToTest")
@Arg(name = "parcelableArrayArg", type = Parcelable[].class)
@ArrayListArg(name = "intArrayListArg", type = Integer.class)
public class TestFragment extends Fragment
{
    ...
}
```

其中，`@Arg`注解主要用于int、long、double等8种Java基本类型以及相关数组类型和Parcelable类型等。

`@ArrayListArg`主要用于ArrayList类型，如ArrayList<Integer>.

在Build工程之后，会自动生成`cn.zmy.fragmentlauncher.Launcher`类。类中自动生成了一个方法：

```
public static void startToTest(Context context, Parcelable[] parcelableArrayArg, ArrayList<Integer> intArrayListArg)
{
    ...
}
```

外部在调用了startToTest方法后，FragmentLauncher会将相关参数打包为一个Bundle，并传递给`cn.zmy.fragmentlauncher.IFragmentLaunchHandler`进行处理。

所以你还需要实现一个`cn.zmy.fragmentlauncher.IFragmentLaunchHandler`来接收Bundle。

```
public interface IFragmentLaunchHandler
{
    void handle(Context context, String fragmentClass, Bundle arguments);
}

// 推荐放置于Application#onCreate
FragmentLauncher.init(new FragmentLaunchHandler());
```

你需要实现这个接口，其中：

- fragmentClass为即将启动的Fragment的完整类名。

- arguments为启动这个Fragment需要的参数。

`FragmentLauncher`为每一个标注了`@Launch`的Fragment生成了一个XXFragmentArguments类用于解析参数。比如上面的TestFragment，
生成的参数解析类就是TestFragmentArguments。

你需要在适当的时候调用其init方法：

```
TestFragmentArguments.instance.init(Bundle bundle);
```

获取参数：

```
Parcelable[] parcelables = TestFragmentArguments.instance.parcelableArrayArg();
ArrayList<Integer> intArrayList = TestFragmentArguments.instance.intArrayListArg();
```

3. 最后

目前，FragmentLauncher支持除`SparseArray<? extends Parcelable>`类型之外的所有其他类型的参数。

FragmentLauncher实际上并为真正意义上帮助你启动Fragment，而是通过`@Launcher`、`@Arg`、`@ArrayListArg`等注解帮助你将相关参数打包为Bundle，然后传递给指定的Handler接口。

你需要自己实现Handler接口来完成最终的启动。
