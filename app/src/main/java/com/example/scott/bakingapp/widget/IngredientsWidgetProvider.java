package com.example.scott.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.scott.bakingapp.DetailActivity;
import com.example.scott.bakingapp.R;
import com.example.scott.bakingapp.service.UpdateWidgetIngredientsService;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, String widgetText) {

        Log.e("Widget Provider", "updateappWidget");

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_provider);
        views.setTextViewText(R.id.widget_ingredients_tv, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(new ComponentName(context.getPackageName(),IngredientsWidgetProvider.class.getName()), views);

        Intent intent = new Intent(context, DetailActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.widget_ingredients_tv, pendingIntent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Start the intent service update widget action, the service takes care of updating the widgets UI
        UpdateWidgetIngredientsService.startActionUpdateIngredients(context);
        }

    public static void updateWidgetText(Context context, AppWidgetManager appWidgetManager,
                                        String ingredients) {
        Log.e("Widget Provider", "updateWidgetText");
            updateAppWidget(context, appWidgetManager, ingredients);
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

