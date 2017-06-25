package com.adeoluwa.android.bakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.adeoluwa.android.bakingapp.data.BakingContract.*;
/**
 * Created by Merlyne on 6/22/2017.
 */

public class BakingDbHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "recipes.db";
    private static final int DATABASE_VERSION = 2;
    public BakingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_RECIPES_TABLE =

                "CREATE TABLE " + RecipeEntry.TABLE_NAME + " (" +
                        RecipeEntry.COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RecipeEntry.COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                        RecipeEntry.COLUMN_RECIPE_SERVINGS + " INTEGER NOT NULL, " +
                        RecipeEntry.COLUMN_IMAGE_URL + " TEXT, " +
                        " UNIQUE (" + RecipeEntry.COLUMN_RECIPE_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_INGREDIENTS_TABLE =

                "CREATE TABLE " + IngredientEntry.TABLE_NAME + " (" +
                        IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        IngredientEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                        IngredientEntry.COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                        IngredientEntry.COLUMN_QUANTITY + " REAL NOT NULL, " +
                        IngredientEntry.COLUMN_MEASURE + " TEXT NOT NULL, " +
                        IngredientEntry.COLUMN_INGREDIENT + " TEXT NOT NULL, " +
                        " UNIQUE (" + IngredientEntry._ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_STEPS_TABLE =

                "CREATE TABLE " + StepEntry.TABLE_NAME + " (" +
                        StepEntry.COLUMN_STEP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        StepEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                        StepEntry.COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                        StepEntry.COLUMN_SHORT_DESCRIPTION + " TEXT NOT NULL, " +
                        StepEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                        StepEntry.COLUMN_VIDEO_URL + " TEXT NOT NULL, " +
                        StepEntry.COLUMN_THUMBNAIL_URL + " TEXT NOT NULL, " +
                        " UNIQUE (" + StepEntry.COLUMN_STEP_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_RECIPES_TABLE);
        db.execSQL(SQL_CREATE_INGREDIENTS_TABLE);
        db.execSQL(SQL_CREATE_STEPS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RecipeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + IngredientEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + StepEntry.TABLE_NAME);
        onCreate(db);
    }
}
