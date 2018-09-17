package cn.zmy.fragmentlauncher;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Size;
import android.util.SizeF;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zmy on 2018/9/14.
 */

public class BundleHelper
{
    public static void fillBundle(Bundle bundle, String name, boolean value)
    {
        bundle.putBoolean(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, byte value)
    {
        bundle.putByte(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, char value)
    {
        bundle.putChar(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, short value)
    {
        bundle.putShort(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, int value)
    {
        bundle.putInt(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, long value)
    {
        bundle.putLong(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, float value)
    {
        bundle.putFloat(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, double value)
    {
        bundle.putDouble(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, String value)
    {
        bundle.putString(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, CharSequence value)
    {
        bundle.putCharSequence(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, Serializable value)
    {
        bundle.putSerializable(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, Parcelable value)
    {
        bundle.putParcelable(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, Size value)
    {
        bundle.putSize(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, SizeF value)
    {
        bundle.putSizeF(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, Bundle value)
    {
        bundle.putBundle(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, IBinder value)
    {
        bundle.putBinder(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, boolean[] value)
    {
        bundle.putBooleanArray(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, byte[] value)
    {
        bundle.putByteArray(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, short[] value)
    {
        bundle.putShortArray(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, char[] value)
    {
        bundle.putCharArray(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, int[] value)
    {
        bundle.putIntArray(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, long[] value)
    {
        bundle.putLongArray(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, float[] value)
    {
        bundle.putFloatArray(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, double[] value)
    {
        bundle.putDoubleArray(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, String[] value)
    {
        bundle.putStringArray(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, CharSequence[] value)
    {
        bundle.putCharSequenceArray(name, value);
    }

    public static void fillBundle(Bundle bundle, String name, Parcelable[] value)
    {
        bundle.putParcelableArray(name, value);
    }

    public static void fillBundleIntArrayList(Bundle bundle, String name, ArrayList<Integer> value)
    {
        bundle.putIntegerArrayList(name, value);
    }

    public static void fillBundleStringArrayList(Bundle bundle, String name, ArrayList<String> value)
    {
        bundle.putStringArrayList(name, value);
    }

    public static void fillBundleCharSequenceArrayList(Bundle bundle, String name, ArrayList<CharSequence> value)
    {
        bundle.putCharSequenceArrayList(name, value);
    }

    public static void fillBundleParcelableArrayList(Bundle bundle, String name, ArrayList<Parcelable> value)
    {
        bundle.putParcelableArrayList(name, value);
    }
}
