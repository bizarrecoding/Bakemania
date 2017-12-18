package com.bizarrecoding.example.bakemania;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
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

    public static final String INGREDIENT_TAKEN = "INGREDIENT_TAKEN";
    public static final String INGREDIENT_UPDATE = "INGREDIENT_UPDATE";
    public static final String REMEMBER_UPDATE = "REMEMBER_UPDATE";
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        SugarContext.init(context);
        List<Recipe> rpList = SugarRecord.find(Recipe.class,"remember=?", "1");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bake_list_widget);
        if(rpList.size()>0) {
            Recipe rp = rpList.get(0);
            CharSequence widgetText = rp.getName();
            views.setTextViewText(R.id.appwidget_text, widgetText);
            views.setTextViewText(R.id.appwidget_servings, String.valueOf(rp.getServings()));
            views.setViewVisibility(R.id.appwidget_ingredients, View.VISIBLE);
            views.setViewVisibility(R.id.none_selected, View.GONE);
            Intent intent = new Intent(context, RemoteIngredientViewsService.class);
            intent.putExtra("rid", rp.getRid());
            views.setRemoteAdapter(R.id.appwidget_ingredients, intent);


            Intent appintent = new Intent(context, BakeListWidget.class);

            PendingIntent pintent = PendingIntent.getBroadcast(context, 0, appintent, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setPendingIntentTemplate(R.id.appwidget_ingredients, pintent);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }else{
            views.setViewVisibility(R.id.none_selected, View.VISIBLE);
        }
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        AppWidgetManager appWidgetManager = AppWidgetManager
                .getInstance(context);
        ComponentName thisAppWidget = new ComponentName(
                context.getPackageName(), getClass().getName());

        int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);

        switch (intent.getAction()) {
            case INGREDIENT_TAKEN:
                long i = intent.getLongExtra("ingredient", 0);
                Ingredient ingredient = Ingredient.findById(Ingredient.class, i);
                int value = ingredient.getTaken() == 1 ? 0 : 1;
                Ingredient.executeQuery("UPDATE INGREDIENT SET TAKEN = "+value+" WHERE ID = ?", String.valueOf(i));
                appWidgetManager.notifyAppWidgetViewDataChanged(ids,R.id.appwidget_ingredients);
                onUpdate(context, appWidgetManager, ids);
                break;
            case INGREDIENT_UPDATE:
                appWidgetManager.notifyAppWidgetViewDataChanged(ids,R.id.appwidget_ingredients);
                onUpdate(context, appWidgetManager, ids);
                break;
            case REMEMBER_UPDATE:
                onUpdate(context, appWidgetManager, ids);
                break;
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

