package com.sam_chordas.android.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;

/**
 * Created by cse on 15-07-2016.
 */
public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context,AppWidgetManager appWidgetManager,int[] appwidgetIds){
        for(int widgetIds:appwidgetIds){
            RemoteViews remoteViews=initViews(context,appWidgetManager,widgetIds);
            appWidgetManager.updateAppWidget(widgetIds,remoteViews);
        }
        super.onUpdate(context,appWidgetManager,appwidgetIds);
    }

    public RemoteViews initViews(Context context,AppWidgetManager appWidgetManager,int widgetId){
        RemoteViews remoteViews=new RemoteViews(context.getPackageName(), R.layout.widget_layout_info);
        Intent intent=new Intent(context,WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        remoteViews.setRemoteAdapter(widgetId, R.id.list_view, intent);

        return remoteViews;
    }

    @Override
    public void onReceive(Context context,Intent intent){
        super.onReceive(context,intent);
    }
}
