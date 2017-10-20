package com.bizarrecoding.example.bakemania;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.bizarrecoding.example.bakemania.adapters.RecipeAdapter;
import com.bizarrecoding.example.bakemania.objects.Recipe;
import com.orm.SugarContext;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Recipe>>{

    private static final int RECIPE_LOADER = 111;

    private int mColumnCount = 2;
    private RecyclerView recipeList;
    private RecipeAdapter rAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recipeList = (RecyclerView) findViewById(R.id.recipeList);
        recipeList.setLayoutManager(new GridLayoutManager(this,mColumnCount));
        rAdapter = new RecipeAdapter(Collections.EMPTY_LIST);
        recipeList.setAdapter(rAdapter);
        loadRecipes();
        SugarContext.init(this);
        RecipeProvider rp = new RecipeProvider("ABC","1");
        rp.save();
        RecipeProvider rp2 = new RecipeProvider("ABC","2");
        rp2.save();
        RecipeProvider rp3 = new RecipeProvider("DEF","1");
        rp3.save();
    }

    public void loadRecipes(){
        LoaderManager lm = getSupportLoaderManager();
        Loader loader = lm.getLoader(RECIPE_LOADER);
        if(loader != null){
            lm.initLoader(RECIPE_LOADER,new Bundle(),this);
        } else {
            lm.restartLoader(RECIPE_LOADER,new Bundle(),this);
        }
    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        Log.d("LOADER","init loader "+id);
        return new RecipeTaskLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
        Log.d("LOADER","loader finished: "+data.size());
        rAdapter.setRecipes(data);
        Log.d("DATASIZE",""+rAdapter.getItemCount());
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader){}
}
