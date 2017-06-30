package com.adeoluwa.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.adeoluwa.android.bakingapp.data.BakingContract;

/**
 * Created by Merlyne on 6/23/2017.
 */

public class IngredientsWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Cursor mCursor;
    private static final String[] projections = {
            BakingContract.IngredientEntry.COLUMN_RECIPE_ID,
            BakingContract.IngredientEntry.COLUMN_RECIPE_NAME,
            BakingContract.IngredientEntry.COLUMN_QUANTITY,
            BakingContract.IngredientEntry.COLUMN_MEASURE,
            BakingContract.IngredientEntry.COLUMN_INGREDIENT
    };
    public IngredientsWidgetRemoteViewsFactory(Context context, Intent intent){
        mContext = context;
    }
    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if(mCursor != null){
            mCursor.close();
        }
        final long identityToken = Binder.clearCallingIdentity();
        Uri uri = BakingContract.IngredientEntry.CONTENT_URI;
        mCursor = mContext.getContentResolver().query(uri, projections, null, null, null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if(mCursor != null){
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if(position == AdapterView.INVALID_POSITION || mCursor == null || !mCursor.moveToPosition(position)){
            return null;
        }
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_item);
        remoteViews.setTextViewText(R.id.txt_ingredient_description, " " + mCursor.getString(4));
        remoteViews.setTextViewText(R.id.txt_ingredient_measure, " " + mCursor.getString(3));
        remoteViews.setTextViewText(R.id.txt_ingredient_quantity, " " + String.valueOf(mCursor.getDouble(2)));
        //remoteViews.setTextViewText(R.id.appwidget_text, " " + mCursor.getString(1));

        //remoteViews.setOnClickFillInIntent(R.id.ingredient_item, new Intent());
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(mContext.getPackageName(), R.layout.ingredient_item);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return mCursor.moveToPosition(position) ? mCursor.getLong(0) : position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
