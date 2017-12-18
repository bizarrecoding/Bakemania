package com.bizarrecoding.example.bakemania.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bizarrecoding.example.bakemania.fragments.StepDetailsFragment;
import com.bizarrecoding.example.bakemania.objects.Step;

import java.util.ArrayList;

public class StepPagerAdapter extends FragmentStatePagerAdapter{

    private final ArrayList<Step> steps;

    public StepPagerAdapter(FragmentManager fm, ArrayList<Step> steps) {
        super(fm);
        this.steps = steps;
    }

    @Override
    public Fragment getItem(int position) {
        return StepDetailsFragment.newInstance(steps.get(position).getId());
    }

    @Override
    public int getCount() {
        return steps.size();
    }
}
