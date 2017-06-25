package com.adeoluwa.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.adeoluwa.android.bakingapp.data.BakingContract;
import com.adeoluwa.android.bakingapp.data.BakingProvider;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        //views.setTextViewText(R.id.appwidget_text, widgetText);
        Intent intent = new Intent(context, IngredientsWidgetRemoteViewsService.class);
        views.setRemoteAdapter(R.id.ingredient_widget_listview, intent);
        Intent newIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, newIntent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
        // Instruct the widget manager to update the widget



        PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(newIntent)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.ingredient_widget_listview, clickPendingIntentTemplate);
        //views.setEmptyView(R.id.lv_ingredients, R.id.widget_empty);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            /*RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
            Intent intent = new Intent(context, IngredientsWidgetRemoteViewsService.class);
            views.setRemoteAdapter(R.id.ingredient_widget_listview, intent);
            appWidgetManager.updateAppWidget(appWidgetId, views);*/
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        /*if (BakingProvider.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredient_widget_listview);
        }*/
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

