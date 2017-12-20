package com.bizarrecoding.example.bakemania.adapters;

import android.content.BroadcastReceiver;
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
import com.google.android.exoplayer2.ExoPlayer;
import com.orm.SugarContext;
import com.orm.SugarRecord;

import java.util.List;

import static com.bizarrecoding.example.bakemania.BakeListWidget.INGREDIENT_UPDATE;


public class RemoteListViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private long rid;
    private List<Ingredient> ingredients;

    public RemoteListViewsFactory(Context ctx) {
        SugarContext.init(ctx);
        this.context = ctx;
        List<Recipe> selectedList = Recipe.find(Recipe.class,"remember=?", "1");
        try {
            this.rid = selectedList.get(0).getRid();
            this.ingredients = Ingredient.find(Ingredient.class, "rid=?", new String[]{String.valueOf(rid)}, null, "taken", null);
            Log.d("FACTORY SERVICE","found "+ingredients.size()+" ingredients");
        }catch (Exception e){

        }
    }

    @Override
    public void onCreate() {}

    @Override
    public void onDataSetChanged() {
        List<Recipe> recipes= Recipe.find(Recipe.class,"remember=?", "1");
        if(recipes.size()>0) {
            this.rid = recipes.get(0).getRid();
            this.ingredients = Ingredient.find(Ingredient.class, "rid=?", String.valueOf(rid));
            getCount();
        }
    }

    @Override
    public void onDestroy() {}
    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION || position >= ingredients.size()) {
            return null;
        }
        RemoteViews ingredientView = new RemoteViews(context.getPackageName(), R.layout.ingredient_item);
        Ingredient i = ingredients.get(position);
        if (i.getTaken() == 0) {
            ingredientView.setImageViewResource(R.id.appwidget_taken, R.drawable.checkbox_off_hd);
        } else {
            ingredientView.setImageViewResource(R.id.appwidget_taken, R.drawable.checkbox_on_hd);
        }
        ingredientView.setTextViewText(R.id.appwidget_ingredient_item, i.getName());
        ingredientView.setTextViewText(R.id.appwidget_units, i.getQuantity() + i.getMeasure().toLowerCase());

        Intent intent = new Intent(context, BakeListWidget.class);
        intent.setAction(BakeListWidget.INGREDIENT_TAKEN);
        intent.putExtra("ingredient", i.getId());
        ingredientView.setOnClickFillInIntent(R.id.appwidget_taken, intent);
        return ingredientView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        long id = 0L;
        try {
            id = ingredients.get(position).getId();
        }catch (Exception e){

        }
        return id;

    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
