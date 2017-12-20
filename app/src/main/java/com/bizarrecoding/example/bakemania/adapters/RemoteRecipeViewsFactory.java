package com.bizarrecoding.example.bakemania.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bizarrecoding.example.bakemania.BakeListWidget;
import com.bizarrecoding.example.bakemania.R;
import com.bizarrecoding.example.bakemania.objects.Ingredient;
import com.bizarrecoding.example.bakemania.objects.Recipe;
import com.orm.SugarContext;

import java.util.List;

/**
 * Created by Herik on 19/12/2017.
 */

public class RemoteRecipeViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context context;
    private List<Recipe> recipes;

    public RemoteRecipeViewsFactory(Context ctx) {
        SugarContext.init(ctx);
        this.context = ctx;
        this.recipes = Recipe.listAll(Recipe.class);
    }

    @Override
    public void onCreate() {}

    @Override
    public void onDataSetChanged() {
        this.recipes = Recipe.listAll(Recipe.class);
    }

    @Override
    public void onDestroy() {}
    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION || position>=recipes.size()) {
            Log.e("ERROR getViewAt","invalid position "+position);
            return null;
        }
        RemoteViews recipeView = new RemoteViews(context.getPackageName(), R.layout.recipe_item);
        Recipe r = recipes.get(position);
        recipeView.setTextViewText(R.id.appwidget_rtitle,r.getName());
        recipeView.setTextViewText(R.id.appwidget_content,"for "+r.getServings()+" servings");
        //r.toString();
        Intent intent = new Intent(context, BakeListWidget.class);
        intent.setAction(BakeListWidget.RECIPE_SELECTED);
        intent.putExtra("recipe", r.getId());
        recipeView.setOnClickFillInIntent(R.id.appwidget_recipe_layout,intent);
        return recipeView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return recipes.get(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
