package cn.zmy.fragmentlauncher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Size;
import android.util.SizeF;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    public static final int REQUEST_CODE = 8888;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            Toast.makeText(this, "Recv result ok", Toast.LENGTH_SHORT).show();
        }
    }

    public void launchTestFragment(View view)
    {
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);

        ArrayList<String> strings = new ArrayList<>();
        strings.add("hello");
        strings.add("world");

        ArrayList<CharSequence> charSequences = new ArrayList<>();
        charSequences.add("fragment");
        charSequences.add("launcher");

        ArrayList<Parcelable> parcelables = new ArrayList<>();
        parcelables.add(new ParcelableObject());
        parcelables.add(new ParcelableObject());

        Launcher.startToTest(this, true, (byte) 1, 'a', (short) 2, 3, 4L, 5F, 6, "7",
                "8", new SerializableObject(), new ParcelableObject(), new Size(1,2), new SizeF(3, 4),
                new boolean[]{true, false}, new byte[]{(byte) 1, (byte) 2}, new short[]{(short) 1, (short) 2}, new char[]{'a', 'b'}, new int[]{1, 2}, new long[]{1, 2},
                new float[]{1, 2}, new double[]{1, 2}, new String[]{"hello", "world"}, new CharSequence[]{"fragment", "launcher"}, new Parcelable[]{new ParcelableObject(), new ParcelableObject()},
                integers, strings, charSequences, parcelables);
    }

    public void launchTestFragmentForResult(View view)
    {
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);

        ArrayList<String> strings = new ArrayList<>();
        strings.add("hello");
        strings.add("world");

        ArrayList<CharSequence> charSequences = new ArrayList<>();
        charSequences.add("fragment");
        charSequences.add("launcher");

        ArrayList<Parcelable> parcelables = new ArrayList<>();
        parcelables.add(new ParcelableObject());
        parcelables.add(new ParcelableObject());

//        Launcher.startToTestForResult(this, REQUEST_CODE, true, (byte) 1, 'a', (short) 2, 3, 4L, 5F, 6, "7",
//                "8", new SerializableObject(), new ParcelableObject(), new Size(1,2), new SizeF(3, 4),
//                new boolean[]{true, false}, new byte[]{(byte) 1, (byte) 2}, new short[]{(short) 1, (short) 2}, new char[]{'a', 'b'}, new int[]{1, 2}, new long[]{1, 2},
//                new float[]{1, 2}, new double[]{1, 2}, new String[]{"hello", "world"}, new CharSequence[]{"fragment", "launcher"}, new Parcelable[]{new ParcelableObject(), new ParcelableObject()},
//                integers, strings, charSequences, parcelables);
    }
}
