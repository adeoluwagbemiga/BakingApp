package com.adeoluwa.android.bakingapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by Merlyne on 6/22/2017.
 */

public class BakingProvider extends ContentProvider {
    public static final int CODE_RECIPES = 100;
    public static final int CODE_RECIPES_WITH_ID = 101;
    public static final int CODE_INGREDIENTS = 200;
    public static final int CODE_INGREDIENTS_WITH_ID = 201;
    public static final int CODE_STEPS = 300;
    public static final int CODE_STEPS_WITH_ID = 301;

    public static final String ACTION_DATA_UPDATED =
            "com.adeoluwa.android.bakingapp.ACTION_DATA_UPDATED";

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BakingContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, BakingContract.PATH_RECIPE, CODE_RECIPES);
        matcher.addURI(authority, BakingContract.PATH_RECIPE + "/#", CODE_RECIPES_WITH_ID);

        matcher.addURI(authority, BakingContract.PATH_INGREDIENT, CODE_INGREDIENTS);
        matcher.addURI(authority, BakingContract.PATH_INGREDIENT + "/#", CODE_INGREDIENTS_WITH_ID);

        matcher.addURI(authority, BakingContract.PATH_STEP, CODE_STEPS);
        matcher.addURI(authority, BakingContract.PATH_STEP + "/#", CODE_STEPS_WITH_ID);

        return matcher;
    }
    private BakingDbHelper mBakingHelper;
    @Override
    public boolean onCreate() {
        mBakingHelper = new BakingDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor mCursor;
        switch (sUriMatcher.match(uri)) {

            case CODE_RECIPES_WITH_ID: {
                String idString = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{idString};

                mCursor = mBakingHelper.getReadableDatabase().query(
                        BakingContract.RecipeEntry.TABLE_NAME,
                        projection,
                        BakingContract.RecipeEntry.COLUMN_RECIPE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_RECIPES: {
                mCursor = mBakingHelper.getReadableDatabase().query(
                        BakingContract.RecipeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }
            case CODE_INGREDIENTS_WITH_ID: {
                String idString = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{idString};

                mCursor = mBakingHelper.getReadableDatabase().query(
                        BakingContract.IngredientEntry.TABLE_NAME,
                        projection,
                        BakingContract.IngredientEntry.COLUMN_RECIPE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_INGREDIENTS: {
                mCursor = mBakingHelper.getReadableDatabase().query(
                        BakingContract.IngredientEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }
            case CODE_STEPS_WITH_ID: {
                String idString = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{idString};

                mCursor = mBakingHelper.getReadableDatabase().query(
                        BakingContract.StepEntry.TABLE_NAME,
                        projection,
                        BakingContract.StepEntry.COLUMN_RECIPE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_STEPS: {
                mCursor = mBakingHelper.getReadableDatabase().query(
                        BakingContract.StepEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        mCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return mCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (buildUriMatcher().match(uri)) {
            case CODE_RECIPES:
                return "vnd.android.cursor.dir/vnd.com.adeoluwa.android.contentprovider.bakingapp";
            case CODE_RECIPES_WITH_ID:
                return "vnd.android.cursor.item/vnd.com.adeoluwa.android.contentprovider.bakingapp";
            case CODE_INGREDIENTS:
                return "vnd.android.cursor.dir/vnd.com.adeoluwa.android.contentprovider.bakingapp";
            case CODE_INGREDIENTS_WITH_ID:
                return "vnd.android.cursor.item/vnd.com.adeoluwa.android.contentprovider.bakingapp";
            case CODE_STEPS:
                return "vnd.android.cursor.dir/vnd.com.adeoluwa.android.contentprovider.bakingapp";
            case CODE_STEPS_WITH_ID:
                return "vnd.android.cursor.item/vnd.com.adeoluwa.android.contentprovider.bakingapp";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (buildUriMatcher().match(uri)) {
            case CODE_RECIPES:
                //do nothing
                long id = mBakingHelper.getWritableDatabase().insert(BakingContract.RecipeEntry.TABLE_NAME, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                //updateWidgets();
                return Uri.parse(BakingContract.RecipeEntry.CONTENT_URI + "/" + id);
                //break;
            case CODE_INGREDIENTS:
                //do nothing
                long ing_id = mBakingHelper.getWritableDatabase().insert(BakingContract.IngredientEntry.TABLE_NAME, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                //updateWidgets();
                return Uri.parse(BakingContract.IngredientEntry.CONTENT_URI + "/" + ing_id);
                //break;
            case CODE_STEPS:
                //do nothing
                long ste_id = mBakingHelper.getWritableDatabase().insert(BakingContract.StepEntry.TABLE_NAME, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                //updateWidgets();
                return Uri.parse(BakingContract.StepEntry.CONTENT_URI + "/" + ste_id);
                //break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }


        //return null;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mBakingHelper.getWritableDatabase();
        int rowsInserted = 0;
        switch (sUriMatcher.match(uri)) {

            case CODE_RECIPES:
                db.beginTransaction();

                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(BakingContract.RecipeEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;
            case CODE_INGREDIENTS:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(BakingContract.IngredientEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;
            case CODE_STEPS:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(BakingContract.StepEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
        //return super.bulkInsert(uri, values);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted;
        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case CODE_RECIPES:
                numRowsDeleted = mBakingHelper.getWritableDatabase().delete(
                        BakingContract.RecipeEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_RECIPES_WITH_ID:
                numRowsDeleted = mBakingHelper.getWritableDatabase().delete(
                        BakingContract.RecipeEntry.TABLE_NAME,
                        BakingContract.RecipeEntry.COLUMN_RECIPE_ID + " = ?",
                        selectionArgs);
            case CODE_INGREDIENTS:
                numRowsDeleted = mBakingHelper.getWritableDatabase().delete(
                        BakingContract.IngredientEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_INGREDIENTS_WITH_ID:
                numRowsDeleted = mBakingHelper.getWritableDatabase().delete(
                        BakingContract.IngredientEntry.TABLE_NAME,
                        BakingContract.IngredientEntry.COLUMN_RECIPE_ID + " = ?",
                        selectionArgs);
                break;
            case CODE_STEPS:
                numRowsDeleted = mBakingHelper.getWritableDatabase().delete(
                        BakingContract.RecipeEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_STEPS_WITH_ID:
                numRowsDeleted = mBakingHelper.getWritableDatabase().delete(
                        BakingContract.StepEntry.TABLE_NAME,
                        BakingContract.StepEntry.COLUMN_RECIPE_ID + " = ?",
                        selectionArgs);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            //updateWidgets();
        }

        return numRowsDeleted;
        //return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int updateCount;
        String id = uri.getPathSegments().get(1);
        switch (buildUriMatcher().match(uri)) {
            case CODE_RECIPES_WITH_ID:

                selection = BakingContract.RecipeEntry.COLUMN_RECIPE_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                updateCount = mBakingHelper.getWritableDatabase().update(BakingContract.RecipeEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CODE_INGREDIENTS_WITH_ID:
                //String id = uri.getPathSegments().get(1);
                selection = BakingContract.IngredientEntry.COLUMN_RECIPE_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                updateCount = mBakingHelper.getWritableDatabase().update(BakingContract.IngredientEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CODE_STEPS_WITH_ID:
                //String id = uri.getPathSegments().get(1);
                selection = BakingContract.StepEntry.COLUMN_RECIPE_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                updateCount = mBakingHelper.getWritableDatabase().update(BakingContract.StepEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        //updateWidgets();
        return updateCount;
        //return 0;
    }
    /*private void updateWidgets() {
        Context context = getContext();
        // Setting the package ensures that only components in our app will receive the broadcast
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                .setPackage(context.getPackageName());
        context.sendBroadcast(dataUpdatedIntent);
    }*/
}
