package com.example.scott.bakingapp.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.scott.bakingapp.IngredientsActivity;
import com.example.scott.bakingapp.widget.IngredientsWidgetProvider;

public class UpdateWidgetIngredientsService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */

    public UpdateWidgetIngredientsService() {
        super("UpdateWidgetIngredientsService");
    }

    public static void startActionUpdateIngredients(Context context) {
        Intent intent = new Intent(context, UpdateWidgetIngredientsService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String ingredients = IngredientsActivity.getIngredientsString();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());

        //Now update all widgets
        IngredientsWidgetProvider.updateWidgetText(this, appWidgetManager, ingredients);
    }
}