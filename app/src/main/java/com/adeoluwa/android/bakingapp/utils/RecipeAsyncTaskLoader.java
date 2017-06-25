package com.adeoluwa.android.bakingapp.utils;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.adeoluwa.android.bakingapp.models.Ingredient;
import com.adeoluwa.android.bakingapp.models.Recipe;
import com.adeoluwa.android.bakingapp.models.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Merlyne on 6/20/2017.
 */

public class RecipeAsyncTaskLoader extends AsyncTaskLoader<List<Recipe>> {
    private List<Recipe> mRecipeList = new ArrayList<Recipe>();
    private Context mContext;
    public RecipeAsyncTaskLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        mRecipeList.clear();
        forceLoad();
    }

    @Override
    public List<Recipe> loadInBackground() {
        String result = "";
        try {
            URL url = NetworkUtils.buildUrl(mContext);
            result = NetworkUtils.getResponseFromHttpUrl(url);
            Log.e("MSG", "I AM HERE 2");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (result == null || result.isEmpty()) return mRecipeList;
            JSONArray resultsArray = new JSONArray(result);
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject recipe = resultsArray.getJSONObject(i);
                int id = recipe.getInt("id");
                String name = recipe.getString("name");
                int servings = recipe.getInt("servings");
                String image = recipe.getString("image");
                JSONArray ingredients = recipe.getJSONArray("ingredients");
                JSONArray steps = recipe.getJSONArray("steps");
                List<Ingredient> mIngredientsList = new ArrayList<Ingredient>();
                for(int ig = 0; ig < ingredients.length(); ig++){
                    JSONObject ingredientsJSONObject = ingredients.getJSONObject(ig);
                    double quantity = ingredientsJSONObject.getDouble("quantity");
                    String measure = ingredientsJSONObject.getString("measure");
                    String ingredient = ingredientsJSONObject.getString("ingredient");

                    mIngredientsList.add(new Ingredient(quantity, measure, ingredient));
                }

                List<Step> mStepsList = new ArrayList<Step>();
                for(int st = 0; st < steps.length(); st++){
                    JSONObject stepsJSONObject = steps.getJSONObject(st);
                    int stepid = stepsJSONObject.getInt("id");
                    String shortdescription = stepsJSONObject.getString("shortDescription");
                    String description = stepsJSONObject.getString("description");
                    String videourl = stepsJSONObject.getString("videoURL");
                    String thumbnailurl = stepsJSONObject.getString("thumbnailURL");
                    mStepsList.add(new Step(stepid, shortdescription, description, videourl, thumbnailurl));
                }
                mRecipeList.add(new Recipe(id, name, servings, image, mIngredientsList, mStepsList));
                Log.e("MSG", "I AM HERE 3");
            }
            return mRecipeList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mRecipeList;
    }

    @Override
    public void deliverResult(List<Recipe> data) {
        super.deliverResult(data);
        mRecipeList = data;
    }
}
