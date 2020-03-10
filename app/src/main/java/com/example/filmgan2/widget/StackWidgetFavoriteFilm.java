package com.example.filmgan2.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.filmgan2.R;

/**
 * Implementation of App Widget functionality.
 */
public class StackWidgetFavoriteFilm extends AppWidgetProvider {

    public static final String ACTION_UPDATE = "com.example.filmgan2.ACTION_UPDATE";
    public static final String TOAST_ACTION = "com.example.filmgan2.widget.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.example.filmgan2.widget.EXTRA_ITEM";
    private static final String TAG = StackWidgetFavoriteFilm.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stack_widget_favorite_film);
        views.setRemoteAdapter(R.id.stack_view, intent);
        views.setEmptyView(R.id.stack_view, R.id.empty_view);
//         Instruct the widget manager to update the widget SEND Broadcast
        Intent toastIntent = new Intent(context, StackWidgetFavoriteFilm.class);
        toastIntent.setAction(StackWidgetFavoriteFilm.TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view,toastPendingIntent);

        //Log.d(TAG,"onReceive WIdget updateAppWidget");

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        //Log.d(TAG,"onReceive WIdget");
        if (intent.getAction() != null) {
            if (intent.getAction().equals(TOAST_ACTION)){
                int viewIndex = intent.getIntExtra(EXTRA_ITEM,0);
                Toast.makeText(context,"Touch View " + viewIndex,Toast.LENGTH_SHORT).show();
            }

            if (intent.getAction().equals(ACTION_UPDATE)){
                AppWidgetManager gm = AppWidgetManager.getInstance(context);
                int[] ids = gm.getAppWidgetIds(new ComponentName(context,StackWidgetFavoriteFilm.class));
                Log.d(TAG,String.valueOf(ids));
                gm.notifyAppWidgetViewDataChanged(ids,R.id.stack_view);
            }
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        //Log.d(TAG,"onUpdate WIdget");
        for (int appWidgetId : appWidgetIds) {
            //Log.d(TAG,"onUpdate ID : " + appWidgetId);
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