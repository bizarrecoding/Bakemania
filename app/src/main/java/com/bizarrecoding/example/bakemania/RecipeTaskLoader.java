package com.bizarrecoding.example.bakemania;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.bizarrecoding.example.bakemania.objects.Recipe;
import com.bizarrecoding.example.bakemania.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class RecipeTaskLoader extends AsyncTaskLoader<List<Recipe>> {

    private List<Recipe> results;

    public RecipeTaskLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(results!=null){
            deliverResult(results);
        }else{
            results = new ArrayList<>();
            forceLoad();
        }
    }

    @Override
    public List<Recipe> loadInBackground() {
        try{
            URL listing = NetworkUtils.getURL();
            String response = NetworkUtils.getResponseFromHttpUrl(listing);
            JSONArray json = new JSONArray(response);
            for (int i = 0; i < json.length(); i++){
                JSONObject recipejs = json.getJSONObject(i);
                Recipe recipe = new Recipe(recipejs);
                recipe.setId((long) i);
                String[] values = new String[]{ String.valueOf(i) };
                boolean exist = 0 < Recipe.count(Recipe.class,"id=?",values);
                if (!exist) {
                    recipe.save();
                }else {
                    recipe.update();
                }
                results.add(recipe);
            }
        }catch (Exception e){
            Log.e("Error",e.getMessage());
            e.printStackTrace();
        }
        return results;
    }

    @Override
    public void deliverResult(List<Recipe> data) {
        results = data;
        super.deliverResult(data);
    }
}
