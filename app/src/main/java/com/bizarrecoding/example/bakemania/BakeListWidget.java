package com.bizarrecoding.example.bakemania;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import com.bizarrecoding.example.bakemania.objects.Ingredient;
import com.bizarrecoding.example.bakemania.objects.Recipe;
import com.orm.SugarContext;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class BakeListWidget extends AppWidgetProvider {

    public static final String INGREDIENT_TAKEN = "INGREDIENT_TAKEN";
    public static final String INGREDIENT_UPDATE = "INGREDIENT_UPDATE";
    public static final String REMEMBER_UPDATE = "REMEMBER_UPDATE";
    public static final String RECIPE_BACK = "RECIPE_BACK";
    public static final String RECIPE_SELECTED = "RECIPE_SELECTED";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        SugarContext.init(context);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bake_list_widget);
        List<Recipe> recipeList = Recipe.listAll(Recipe.class);
        List<Recipe> selectedList = Recipe.find(Recipe.class,"remember=?", "1");

        if(recipeList.size()>0) {
            if(selectedList.size()>0) {
                Recipe rp = selectedList.get(0);
                views.setTextViewText(R.id.appwidget_title, rp.getName());
                views.setTextViewText(R.id.appwidget_servings, String.valueOf(rp.getServings()));
                views.setOnClickPendingIntent(R.id.appwidget_back,getPendingIntent(context,RECIPE_BACK,rp.getId()));

                views.setViewVisibility(R.id.appwidget_back, View.VISIBLE);
                views.setViewVisibility(R.id.appwidget_text, View.VISIBLE);
                views.setViewVisibility(R.id.appwidget_servings, View.VISIBLE);
                views.setViewVisibility(R.id.appwidget_list, View.VISIBLE);
                views.setViewVisibility(R.id.appwidget_recipeList, View.GONE);
                views.setViewVisibility(R.id.none_selected, View.GONE);

                Intent intent = new Intent(context, RemoteListViewsService.class);
                intent.putExtra("rid", rp.getId());
                views.setRemoteAdapter(R.id.appwidget_list, intent);

                Intent appintent = new Intent(context, BakeListWidget.class);
                PendingIntent pintent = PendingIntent.getBroadcast(context, 0, appintent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setPendingIntentTemplate(R.id.appwidget_list, pintent);
            }else{
                views.setTextViewText(R.id.appwidget_title, context.getString(R.string.app_name));
                views.setViewVisibility(R.id.appwidget_back, View.GONE);
                views.setViewVisibility(R.id.appwidget_text,View.GONE);
                views.setViewVisibility(R.id.appwidget_servings,View.GONE);
                views.setViewVisibility(R.id.none_selected, View.GONE);
                views.setViewVisibility(R.id.appwidget_list,View.GONE);
                views.setViewVisibility(R.id.appwidget_recipeList,View.VISIBLE);

                Intent rIntent = new Intent(context, RemoteRecipeViewsService.class);
                views.setRemoteAdapter(R.id.appwidget_recipeList, rIntent);

                Intent appintent = new Intent(context, BakeListWidget.class);
                PendingIntent pintent = PendingIntent.getBroadcast(context, 0, appintent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setPendingIntentTemplate(R.id.appwidget_recipeList, pintent);
            }
        }else{
            Log.e("ERROR","no recipes found");
            views.setViewVisibility(R.id.appwidget_back, View.GONE);
            views.setTextViewText(R.id.appwidget_title, context.getString(R.string.app_name));
            views.setViewVisibility(R.id.appwidget_list, View.GONE);
            views.setViewVisibility(R.id.appwidget_text,View.GONE);
            views.setViewVisibility(R.id.appwidget_servings,View.GONE);
            views.setViewVisibility(R.id.none_selected, View.VISIBLE);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static PendingIntent getPendingIntent(Context ctx, String action,long id){
        Intent appintent = new Intent(ctx, BakeListWidget.class);
        appintent.setAction(action);
        appintent.putExtra("rid",id);
        return PendingIntent.getBroadcast(ctx, 0, appintent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);
        SugarContext.init(context);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());

        int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);

        switch (intent.getAction()) {
            case INGREDIENT_TAKEN:
                long i = intent.getLongExtra("ingredient", 0);
                Ingredient ingredient = Ingredient.findById(Ingredient.class, i);
                int value = ingredient.getTaken() == 1 ? 0 : 1;
                Ingredient.executeQuery("UPDATE INGREDIENT SET TAKEN = " + value + " WHERE ID = ?", String.valueOf(i));
                appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.appwidget_list);
                onUpdate(context, appWidgetManager, ids);
                break;
            case INGREDIENT_UPDATE:
                appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.appwidget_list);
                onUpdate(context, appWidgetManager, ids);
                break;
            case REMEMBER_UPDATE:
                appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.appwidget_list);
                onUpdate(context, appWidgetManager, ids);
                break;
            case RECIPE_BACK:
                Recipe.executeQuery("UPDATE RECIPE SET REMEMBER = 0 WHERE REMEMBER = 1");
                appWidgetManager.notifyAppWidgetViewDataChanged(ids,R.id.appwidget_list);
                onUpdate(context, appWidgetManager, ids);
                break;
            case RECIPE_SELECTED:
                long ru = intent.getLongExtra("recipe",-1);
                if(ru!=-1) {
                    Recipe.executeQuery("UPDATE RECIPE SET REMEMBER = 1 WHERE ID = ?", String.valueOf(ru));
                }else {
                    Log.e("ERROR SELECTED","recipe id cant be "+ru);
                }
                appWidgetManager.notifyAppWidgetViewDataChanged(ids,R.id.appwidget_list);
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

