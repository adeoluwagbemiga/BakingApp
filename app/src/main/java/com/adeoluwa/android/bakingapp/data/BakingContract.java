package com.adeoluwa.android.bakingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Merlyne on 6/22/2017.
 */

public class BakingContract {
    public static final String CONTENT_AUTHORITY = "com.adeoluwa.android.bakingapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_RECIPE = "recipes";
    public static final String PATH_INGREDIENT = "ingredients";
    public static final String PATH_STEP = "steps";


    public static final class RecipeEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_RECIPE)
                .build();

        public static final String TABLE_NAME = "recipes";


        public static final String COLUMN_RECIPE_ID = "id";
        public static final String COLUMN_RECIPE_NAME = "name";
        public static final String COLUMN_RECIPE_SERVINGS = "servings";
        public static final String COLUMN_IMAGE_URL = "image";
    }
    public static final class IngredientEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_INGREDIENT)
                .build();

        public static final String TABLE_NAME = "ingredients";


        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_RECIPE_NAME = "recipe_name";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_MEASURE = "measure";
        public static final String COLUMN_INGREDIENT = "ingredient";
    }
    public static final class StepEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_STEP)
                .build();

        public static final String TABLE_NAME = "steps";


        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_RECIPE_NAME = "recipe_name";
        public static final String COLUMN_STEP_ID = "step_id";
        public static final String COLUMN_VIDEO_URL = "video_url";
        public static final String COLUMN_THUMBNAIL_URL = "thumbnail_url";
        public static final String COLUMN_SHORT_DESCRIPTION = "short_description";
        public static final String COLUMN_DESCRIPTION = "description";
    }
}
