package cn.zmy.fragmentlauncher;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Size;
import android.util.SizeF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zmy on 2018/9/13.
 */
@Launch(name = "startToTest")
@Arg(name = "booleanArg", type = boolean.class)
@Arg(name = "byteArg", type = byte.class)
@Arg(name = "charArg", type = char.class)
@Arg(name = "shortArg", type = short.class)
@Arg(name = "intArg", type = int.class)
@Arg(name = "longArg", type = long.class)
@Arg(name = "floatArg", type = float.class)
@Arg(name = "doubleArg", type = double.class)
@Arg(name = "stringArg", type = String.class)
@Arg(name = "charSequenceArg", type = CharSequence.class)
@Arg(name = "serializableArg", type = SerializableObject.class)
@Arg(name = "parcelableArg", type = ParcelableObject.class)
@Arg(name = "sizeArg", type = Size.class)
@Arg(name = "sizeFArg", type = SizeF.class)
@Arg(name = "booleanArrayArg", type = boolean[].class)
@Arg(name = "byteArrayArg", type = byte[].class)
@Arg(name = "shortArrayArg", type = short[].class)
@Arg(name = "charArrayArg", type = char[].class)
@Arg(name = "intArrayArg", type = int[].class)
@Arg(name = "longArrayArg", type = long[].class)
@Arg(name = "floatArrayArg", type = float[].class)
@Arg(name = "doubleArrayArg", type = double[].class)
@Arg(name = "stringArrayArg", type = String[].class)
@Arg(name = "charSequenceArrayArg", type = CharSequence[].class)
@Arg(name = "parcelableArrayArg", type = Parcelable[].class)
@ArrayListArg(name = "intArrayListArg", type = Integer.class)
@ArrayListArg(name = "stringArrayListArg", type = String.class)
@ArrayListArg(name = "charSequenceArrayListArg", type = CharSequence.class)
@ArrayListArg(name = "parcelableArrayListArg", type = Parcelable.class)
public class TestFragment extends Fragment
{
    private final String TAG = "TestFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        for (String key : bundle.keySet())
        {
            Log.d(TAG, bundle.get(key) + "");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        return view;
    }
}
