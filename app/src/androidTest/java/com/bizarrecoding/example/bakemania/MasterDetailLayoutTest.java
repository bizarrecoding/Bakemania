package com.bizarrecoding.example.bakemania;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.DisplayMetrics;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.PositionAssertions.isRightOf;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MasterDetailLayoutTest {

    @Rule
    public ActivityTestRule<StepsActivity> mStepTestRule = new ActivityTestRule<>(StepsActivity.class);

    @Test
    public void masterDetailLayoutTest(){
        StepsActivity sActivity = mStepTestRule.getActivity();
        sActivity.getIntent().putExtra("Recipe",1);
        DisplayMetrics metrics = mStepTestRule.getActivity().getResources().getDisplayMetrics();

        float smallest = metrics.widthPixels > metrics.heightPixels? metrics.heightPixels : metrics.widthPixels;

        //550 for tablets on landscape with digital buttons

        if(metrics.densityDpi>=550){
            assertEquals(true,smallest/metrics.density>=550);
            onView(withId(R.id.details)).check(isRightOf(withId(R.id.recipeList)));
        }else{
            assertEquals(true,smallest/metrics.density<550);
            onView(withId(R.id.details)).check(doesNotExist());
        }
    }
}
