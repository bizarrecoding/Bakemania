package com.bizarrecoding.example.bakemania.adapters;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bizarrecoding.example.bakemania.BakeListWidget;
import com.bizarrecoding.example.bakemania.R;
import com.bizarrecoding.example.bakemania.objects.Ingredient;
import com.bizarrecoding.example.bakemania.objects.Recipe;
import com.orm.SugarContext;
import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Herik on 22/10/2017.
 */

public class RemoteIngredientViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context context;
    private long rid;
    private List<Ingredient> ingredients;

    public RemoteIngredientViewsFactory(Context ctx, long rid) {
        SugarContext.init(ctx);
        this.context = ctx;
        this.rid = rid;
        this.ingredients = Ingredient.find(Ingredient.class,"rid=?",new String[]{String.valueOf(rid)},null,"taken",null);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        this.ingredients = Ingredient.find(Ingredient.class,"rid=?",new String[]{String.valueOf(rid)});
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION || position>=ingredients.size()) {
            return null;
        }
        RemoteViews ingredientView = new RemoteViews(context.getPackageName(), R.layout.ingredient_item);
        Ingredient i = ingredients.get(position);
        if(i.getTaken()==0) {
            ingredientView.setImageViewResource(R.id.appwidget_taken, R.drawable.checkbox_off_hd );
        }else {
            ingredientView.setImageViewResource(R.id.appwidget_taken, R.drawable.checkbox_on_hd );
        }

        Intent intent = new Intent(context, BakeListWidget.class);
        intent.setAction(BakeListWidget.INGREDIENT_TAKEN);
        intent.putExtra("ingredient", i.getId());
        ingredientView.setOnClickFillInIntent(R.id.appwidget_taken,intent);
        ingredientView.setTextViewText(R.id.appwidget_ingredient_item,i.getName());
        ingredientView.setTextViewText(R.id.appwidget_units,i.getQuantity()+i.getMeasure().toLowerCase());
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
        return ingredients.get(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
