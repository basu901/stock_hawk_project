package com.sam_chordas.android.stockhawk.widget;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.touch_helper.ItemTouchHelperViewHolder;

import java.util.ArrayList;

/**
 * Created by cse on 15-07-2016.
 */
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    ArrayList<DatabaseInfo> mCollections = new ArrayList<>();
    Cursor mCursor;

    Context mContext = null;

    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {

        query();

    }

    public void query() {
        mCursor = mContext.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                new String[]{QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                        QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                QuoteColumns.ISCURRENT + " = ?",
                new String[]{"1"},
                null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            while (!mCursor.isAfterLast()) {
                DatabaseInfo dbinfo = new DatabaseInfo();
                dbinfo.setSymbol(mCursor.getString(mCursor.getColumnIndex(QuoteColumns.SYMBOL)));
                dbinfo.setBid_price(mCursor.getString(mCursor.getColumnIndex(QuoteColumns.BIDPRICE)));
                dbinfo.setPercent_change(mCursor.getString(mCursor.getColumnIndex(QuoteColumns.PERCENT_CHANGE)));
                dbinfo.setChange(mCursor.getString(mCursor.getColumnIndex(QuoteColumns.CHANGE)));
                dbinfo.setIs_up(mCursor.getString(mCursor.getColumnIndex(QuoteColumns.ISUP)));

                mCollections.add(dbinfo);

                mCursor.moveToNext();
            }
        }

        mCursor.close();
    }

    @Override
    public void onDataSetChanged() {
        Thread thread = new Thread() {
            public void run() {
                query();
            }
        };
        try {
            thread.join();
        } catch (InterruptedException e) {

        }

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mCollections.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_details_layout);
        DatabaseInfo db_info = mCollections.get(position);


        remoteViews.setTextViewText(R.id.symbol_detail, db_info.getSymbol());
        remoteViews.setTextViewText(R.id.bid_price_detail, db_info.getBid_price());
        remoteViews.setTextViewText(R.id.change_detail, db_info.getChange());
        //remoteViews.setTextViewText(R.id.percent_change,db_info.getPercent_change());
        //remoteViews.setTextViewText(R.id.is_up,db_info.getIs_up());


        return remoteViews;


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
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}