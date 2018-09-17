package cn.zmy.fragmentlauncher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by zmy on 2018/9/17.
 */

public class LaunchFragmentActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        String fragmentClassName = getIntent().getStringExtra("fragmentClass");
        Bundle arg = getIntent().getBundleExtra("arg");

        try
        {
            Fragment fragment = (Fragment) Class.forName(fragmentClassName).newInstance();
            fragment.setArguments(arg);

            FrameLayout frameLayout = new FrameLayout(this);
            frameLayout.setId(View.generateViewId());
            getSupportFragmentManager().beginTransaction()
                    .replace(frameLayout.getId(), fragment)
                    .commit();
            setContentView(frameLayout);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
