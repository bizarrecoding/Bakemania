package com.bizarrecoding.example.bakemania.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.bizarrecoding.example.bakemania.R;
import com.bizarrecoding.example.bakemania.fragments.StepDetailsFragment;
import com.bizarrecoding.example.bakemania.objects.Step;

import java.util.ArrayList;

public class StepPagerAdapter extends FragmentStatePagerAdapter{

    private final ArrayList<Step> steps;
    private FragmentManager fm;


    public StepPagerAdapter(FragmentManager fm, ArrayList<Step> steps) {
        super(fm);
        this.steps = steps;
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment sdf = fm.findFragmentByTag(getTag(position));
        if(sdf == null) {
            Log.d("PAGER","fragment "+getTag(position)+" not found");
            sdf = StepDetailsFragment.newInstance(steps.get(position).getId());
        }
        return sdf;
    }

    private static String getTag(long id) {
        return "android:switcher:" + R.id.stepPager + ":" + id;
    }

    @Override
    public int getCount() {
        return steps.size();
    }

}
