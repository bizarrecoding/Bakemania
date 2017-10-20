package com.bizarrecoding.example.bakemania;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.anton46.stepsview.StepsView;
import com.bizarrecoding.example.bakemania.adapters.StepPagerAdapter;
import com.bizarrecoding.example.bakemania.objects.Step;

import java.util.ArrayList;

public class StepDetailsActivity extends AppCompatActivity {

    private FragmentManager fm;
    private ArrayList<Step> stepList;
    private Step step;
    private int stepIndex;
    private int currentPage = 0;
    private ViewPager stepPager;
    private StepPagerAdapter stepPagerAdapter;
    private StepsView stepNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        stepList = new ArrayList<>();
        stepList = getIntent().getParcelableArrayListExtra("Steps");
        stepIndex =  getIntent().getIntExtra("CurrentStep",0);
        step = stepList.get(stepIndex);

        stepPager = (ViewPager)findViewById(R.id.stepPager);
        stepPagerAdapter = new StepPagerAdapter(getSupportFragmentManager(),stepList);
        stepPager.setAdapter(stepPagerAdapter);
        stepPager.setCurrentItem(stepIndex);
        currentPage = stepIndex;
        stepPager.setOffscreenPageLimit(1);
        stepPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(currentPage!=position) {
                    Log.d("VIEWPAGER", "position: " + position+"\nCurrentPage: "+currentPage);
                    stepNavView.setCompletedPosition(position);
                    stepNavView.drawView();
                    currentPage=position;

                }
            }
            @Override
            public void onPageSelected(int position) {}
            @Override
            public void onPageScrollStateChanged(int state) {}
        });


        stepNavView = (StepsView)findViewById(R.id.stepsView);
        String[] labels = new String[stepList.size()];
        for(int i=0;i<stepList.size();i++){
            labels[i]="";
        }
        stepNavView.setLabels(labels)
                .setBarColorIndicator(getResources().getColor(R.color.cardview_dark_background))
                .setProgressColorIndicator(getResources().getColor(R.color.colorPrimary))
                .setCompletedPosition(stepIndex);
        stepNavView.drawView();
    }
}
