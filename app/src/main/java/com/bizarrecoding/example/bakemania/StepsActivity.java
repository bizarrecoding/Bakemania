package com.bizarrecoding.example.bakemania;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bizarrecoding.example.bakemania.fragments.StepDetailsFragment;
import com.bizarrecoding.example.bakemania.fragments.StepListFragment;
import com.bizarrecoding.example.bakemania.fragments.StepListFragment.StepClickListener;
import com.bizarrecoding.example.bakemania.objects.Recipe;
import com.bizarrecoding.example.bakemania.objects.Step;
import com.orm.SugarContext;

public class StepsActivity extends AppCompatActivity implements StepClickListener{

    private boolean is2pane;
    private FragmentManager fm;
    private Recipe recipe;
    private boolean showSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        SugarContext.init(this);
        long selectedRecipe = getIntent().getLongExtra("Recipe",0);
        recipe = Recipe.findById(Recipe.class,selectedRecipe);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle(recipe.getName());
            actionBar.setIcon(R.mipmap.ic_launcher);
            actionBar.setLogo(R.mipmap.ic_launcher);
        }
        is2pane = findViewById(R.id.details) != null;

        if(savedInstanceState==null){
            StepListFragment list = StepListFragment.newInstance(recipe.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipeList, list)
                    .commit();
        }else if(savedInstanceState.containsKey("selected")){
            showSelected = savedInstanceState.getBoolean("selected");
        }
        if (showSelected){
            findViewById(R.id.selection).setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("selected", showSelected);
    }

    @Override
    public void onStepClickListener(Step step) {
        if(is2pane){
            // TODO: add fragment
            //Log.d("STEP","sid: "+step.getSid()+"\nrid: "+step.getRid());
            StepDetailsFragment recipeDeteails = StepDetailsFragment.newInstance(step.getId());
            fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.details,recipeDeteails)
                    .commit();
            findViewById(R.id.selection).setVisibility(View.GONE);
            showSelected =true;
        }else{
            // TODO: start by intent
            Intent in = new Intent(this, StepDetailsActivity.class);
            in.putExtra("CurrentStep",step.getId());
            in.putExtra("Steps",recipe.getId());
            startActivity(in);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.remember){
            for (Recipe r : Recipe.listAll(Recipe.class)){
                r.setRemember(recipe.getRid()==r.getRid()? 1 : 0);
                r.update();
                //Log.d("Remember "+r.getRemember(),"widget update for "+r.getRid());
                if(recipe.getRid()==r.getRid()){
                    Context context = getApplicationContext();
                    Intent in = new Intent(context,BakeListWidget.class);
                    in.setAction(BakeListWidget.REMEMBER_UPDATE);
                    sendBroadcast(in);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
