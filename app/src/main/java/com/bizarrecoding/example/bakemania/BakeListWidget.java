package com.bizarrecoding.example.bakemania;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import com.bizarrecoding.example.bakemania.objects.Ingredient;
import com.bizarrecoding.example.bakemania.objects.Recipe;
import com.orm.SugarContext;
import com.orm.SugarRecord;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class BakeListWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SugarContext.init(context);
        List<Recipe> rpList = SugarRecord.find(Recipe.class,"rid=?",new String[]{"1"});
        Recipe rp = rpList.get(0);

        List<Ingredient> ingredients = Ingredient.find(Ingredient.class,"rid=?",new String[]{"1"});
        CharSequence widgetText = rp.getName()+" ("+rp.getId()+")";
        String list = "";

        for (Ingredient i : ingredients){
            list+=i.getName()+"   "+i.getQuantity()+" "+i.getMeasure()+"\n";
        }
        Log.d("WIDGET",list+"\nsize: "+ingredients.size());
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bake_list_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        views.setTextViewText(R.id.appwidget_ingredients, list);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

