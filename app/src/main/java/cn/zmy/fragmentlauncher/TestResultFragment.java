package cn.zmy.fragmentlauncher;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zmy on 2019/1/18.
 */
@Launch(name = "startToTestForResult", forResult = true)
public class TestResultFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_test_result, container, false);
        view.findViewById(R.id.viewSetResult).setOnClickListener(this::onSetResultClick);
        return view;
    }

    public void onSetResultClick(View view)
    {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }
}
