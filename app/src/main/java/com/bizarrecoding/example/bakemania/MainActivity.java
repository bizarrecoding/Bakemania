package com.bizarrecoding.example.bakemania;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bizarrecoding.example.bakemania.adapters.RecipeAdapter;
import com.bizarrecoding.example.bakemania.objects.Recipe;
import com.orm.SugarContext;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Recipe>>{

    private static final int RECIPE_LOADER = 111;

    private int mColumnCount = 2;
    @BindView(R.id.recipeList) RecyclerView recipeList;
    @BindView(R.id.errormain) TextView errorTV;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    private RecipeAdapter rAdapter;
    private SwipeRefreshLayout mySwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SugarContext.init(this);
        ButterKnife.bind(this);
        long recipeCount = Recipe.count(Recipe.class);
        showProgress(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        recipeList.setLayoutManager(new GridLayoutManager(this,mColumnCount));
        rAdapter = new RecipeAdapter(Collections.EMPTY_LIST);
        recipeList.setAdapter(rAdapter);
        if(isOnline()){
            loadRecipes();
        }else if(recipeCount>0) {
            List<Recipe> recipes = Recipe.listAll(Recipe.class);
            rAdapter.setRecipes(recipes);
            showProgress(false);
        }else{
            showError(R.string.no_recipes_found);
            //alert no internet
            AlertDialog.Builder adb = new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.connection_error)
                    .setMessage("");
            adb.create().show();

        }
        mySwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (isOnline()){
                            loadRecipes();
                        }else {
                            mySwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }
        );
    }


    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(MainActivity.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
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
        return new RecipeTaskLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
        List<Recipe> recipes = Recipe.listAll(Recipe.class);
        rAdapter.setRecipes(recipes);
        mySwipeRefreshLayout.setRefreshing(false);
        showProgress(false);
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader){}


    public void showProgress(boolean show){
        progressBar.setVisibility( show ? View.VISIBLE : View.INVISIBLE);
        recipeList.setVisibility( show ? View.INVISIBLE : View.VISIBLE);
        errorTV.setVisibility(View.INVISIBLE);
    }

    public void showError(int errorRes) {
        progressBar.setVisibility(View.INVISIBLE);
        recipeList.setVisibility(View.INVISIBLE);
        errorTV.setVisibility(View.VISIBLE);
        errorTV.setText(errorRes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.refresh:
                if (isOnline()){
                    mySwipeRefreshLayout.setRefreshing(true);
                    loadRecipes();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
