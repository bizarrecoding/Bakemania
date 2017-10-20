package com.bizarrecoding.example.bakemania;

import android.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.bizarrecoding.example.bakemania.fragments.StepDetailsFragment;
import com.bizarrecoding.example.bakemania.fragments.StepListFragment;
import com.bizarrecoding.example.bakemania.fragments.StepListFragment.StepClickListener;
import com.bizarrecoding.example.bakemania.objects.Recipe;
import com.bizarrecoding.example.bakemania.objects.Step;

import java.util.ArrayList;

public class StepsActivity extends AppCompatActivity implements StepClickListener{

    private boolean is2pane;
    private FragmentManager fm;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        recipe = getIntent().getParcelableExtra("Recipe");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle(recipe.getName());
        }
        is2pane = findViewById(R.id.details) != null;
        StepListFragment list = StepListFragment.newInstance(recipe);
        getSupportFragmentManager().beginTransaction()
            .add(R.id.recipeList, list)
            .commit();
    }

    @Override
    public void onStepClickListener(Step step) {
        if(is2pane){
            // TODO: add fragment
            StepDetailsFragment recipeDeteails = StepDetailsFragment.newInstance(step);
            fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .add(R.id.details,recipeDeteails)
                    .commit();
        }else{
            // TODO: start by intent
            Intent in = new Intent(this, StepDetailsActivity.class);
            in.putExtra("CurrentStep",step.getId()-1);
            ArrayList<Step> steps = (ArrayList<Step>) recipe.getSteps();
            in.putParcelableArrayListExtra("Steps",steps);
            startActivity(in);
        }
    }
}
