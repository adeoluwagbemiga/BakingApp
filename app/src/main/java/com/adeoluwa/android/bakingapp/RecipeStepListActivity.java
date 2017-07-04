package com.adeoluwa.android.bakingapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

import com.adeoluwa.android.bakingapp.adapters.IngredientsAdapter;
import com.adeoluwa.android.bakingapp.adapters.StepsAdapter;
import com.adeoluwa.android.bakingapp.data.BakingContract;
import com.adeoluwa.android.bakingapp.models.Ingredient;
import com.adeoluwa.android.bakingapp.models.Recipe;
import com.adeoluwa.android.bakingapp.models.Step;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

/**
 * An activity representing a list of RecipeSteps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeStepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeStepListActivity extends AppCompatActivity implements StepsAdapter.ListItemClickListener{

    private Recipe mRecipe;
    private List<Ingredient> mIngredients = new ArrayList<Ingredient>();
    private List<Step> mSteps = new ArrayList<Step>();

    private static int mServings = 0;
    private static int mRecipeId;
    private static String mName;

     @BindView(R.id.txt_recipe_servings) TextView mServingsTextView;
     @BindView(R.id.rv_ingredients) RecyclerView mIngredientsRecyclerView;
     @BindView(R.id.recipestep_list) RecyclerView mStepsRecyclerView;
    //@BindView(R.id.recipestep_list) NestedScrollView mNestedScrollViewContainer;

    private LinearLayoutManager ingredientsLayoutManager;
    private LinearLayoutManager stepsLayoutManager;
    private IngredientsAdapter mIngredientsAdapter;
    private StepsAdapter mStepsAdapter;
    private static int mStepAdapterPosition = 0;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipestep_list);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setTitle(getTitle());

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("recipe")) {
            Bundle extras = intent.getExtras();
            if (extras != null && extras.containsKey("recipe")) {
                mRecipe = intent.getParcelableExtra("recipe");
                //Log.e("RECIPE NAME", mRecipe.getName());
                //Toast.makeText(this, mRecipe.getName(), Toast.LENGTH_SHORT).show();
                mIngredients = mRecipe.getIngredients();
                mSteps = mRecipe.getSteps();
                //Toast.makeText(this, mRecipe.getName(), Toast.LENGTH_SHORT).show();

                mName = mRecipe.getName();
                mRecipeId = mRecipe.getid();
                mServings = mRecipe.getServings();
                toolbar.setTitle(mName);
                saveIngredientsLocal();
            }
                /*loadCompleteRecipe();
                mName = mRecipe.getName();
                mRecipeId = mRecipe.getid();
                mServings = mRecipe.getServings();
                toolbar.setTitle(mName);
            }*/

        }else {
            if (savedInstanceState != null && savedInstanceState.containsKey("recipe")) {
                mRecipe = savedInstanceState.getParcelable("recipe");
                if(savedInstanceState.containsKey("ingredients")) {
                    mIngredients = savedInstanceState.getParcelableArrayList("ingredients");
                }else{
                    mIngredients = mRecipe.getIngredients();
                }
                if(savedInstanceState.containsKey("ingredients")) {
                    mSteps = savedInstanceState.getParcelableArrayList("steps");
                }else{
                    mSteps = mRecipe.getSteps();
                }
                //mSteps = mRecipe.getSteps();
                //Toast.makeText(this, mRecipe.getName(), Toast.LENGTH_SHORT).show();

                //mName = mRecipe.getName();
                //mRecipeId = mRecipe.getid();
                //mServings = mRecipe.getServings();
                //toolbar.setTitle(mName);
            }else if(getIntent().hasExtra("steps")){
                Intent detailIntent = getIntent();
                mSteps = detailIntent.getParcelableArrayListExtra("steps");
                mStepAdapterPosition = detailIntent.getIntExtra("step_position", 0);
                loadIngredients(1);
            }
        }
        /*mIngredients = mRecipe.getIngredients();
        mSteps = mRecipe.getSteps();
        //Toast.makeText(this, mRecipe.getName(), Toast.LENGTH_SHORT).show();

        mName = mRecipe.getName();
        mRecipeId = mRecipe.getid();
        mServings = mRecipe.getServings();*/
        toolbar.setTitle(mName);
        //View recyclerView = findViewById(R.id.recipestep_list);
        //assert recyclerView != null;
        //setupRecyclerView((RecyclerView) recyclerView);
        configureRecyclerViews();

        //if(mNestedScrollViewContainer != null){
        if (findViewById(R.id.recipestep_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("recipe", mRecipe);
        outState.putParcelableArrayList("ingredients", (ArrayList<? extends Parcelable>) mIngredients);
        outState.putParcelableArrayList("steps", (ArrayList<? extends Parcelable>) mSteps);
        outState.putInt("step_position", mStepAdapterPosition);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void saveIngredientsLocal(){
        getContentResolver().delete(BakingContract.IngredientEntry.CONTENT_URI, null, null);
        getIngredient((ArrayList<Ingredient>) mRecipe.getIngredients());
    }
    private void getIngredient(ArrayList<Ingredient> ingredients) {

        for (Ingredient ingredient : ingredients) {
            ContentValues values = new ContentValues();
            values.put(BakingContract.IngredientEntry.COLUMN_RECIPE_ID, mRecipe.getid());
            values.put(BakingContract.IngredientEntry.COLUMN_RECIPE_NAME, mRecipe.getName());
            values.put(BakingContract.IngredientEntry.COLUMN_INGREDIENT, ingredient.getIngredient());
            values.put(BakingContract.IngredientEntry.COLUMN_MEASURE, ingredient.getMeasure());
            values.put(BakingContract.IngredientEntry.COLUMN_QUANTITY, ingredient.getQuantity());
            getContentResolver().insert(BakingContract.IngredientEntry.CONTENT_URI, values);
            //Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
        }

    }
    private void configureRecyclerViews(){
        ingredientsLayoutManager = new LinearLayoutManager(this);
        mIngredientsRecyclerView.setLayoutManager(ingredientsLayoutManager);
        mIngredientsRecyclerView.setHasFixedSize(true);

        stepsLayoutManager = new LinearLayoutManager(this);
        mStepsRecyclerView.setLayoutManager(stepsLayoutManager);
        mStepsRecyclerView.setHasFixedSize(true);

        mServingsTextView.setText(mServings + " Servings");
        mIngredientsAdapter = new IngredientsAdapter(mIngredients);
        mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);
        mIngredientsAdapter.notifyDataSetChanged();

        mStepsAdapter = new StepsAdapter(this, mSteps);
        mStepsRecyclerView.setAdapter(mStepsAdapter);
        mStepsAdapter.notifyDataSetChanged();
        mStepsRecyclerView.scrollToPosition(mStepAdapterPosition);
    }
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        //recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }

    @Override
    public void onItemClick(int position) {
        if(mTwoPane){
            Bundle arguments = new Bundle();
            arguments.putString(RecipeStepDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(RecipeStepDetailFragment.ARG_ITEM_ID));
            arguments.putParcelable("step", mSteps.get(position));
            arguments.putInt("step_position", position);
            arguments.putInt("steps_size", mSteps.size());
            arguments.putParcelableArrayList("steps", (ArrayList<? extends Parcelable>) mSteps);
            RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipestep_detail_container, fragment)
                    .commit();
        }else {
            Intent intent = new Intent(this, RecipeStepDetailActivity.class);
            intent.putExtra("recipe_name", mName);
            intent.putExtra("step_position", position);
            intent.putExtra("steps_size", mSteps.size());
            intent.putExtra("step", mSteps.get(position));
            intent.putParcelableArrayListExtra("steps", (ArrayList<? extends Parcelable>) mSteps);
            startActivity(intent);
        }
        //Toast.makeText(this, "Step " + String.valueOf(position) + " Clicked", Toast.LENGTH_SHORT).show();
    }
    private void loadCompleteRecipe(){
        //ContentResolver contentResolver = getContentResolver();
        //String recipeToFetch = BakingContract.RecipeEntry.COLUMN_RECIPE_ID + " = " + recipeId;
        Cursor recipe_cursor = getContentResolver().query(BakingContract.RecipeEntry.CONTENT_URI, null, null  , null, null);


        //Cursor recipe_cursor = contentResolver.query()
        if (recipe_cursor != null && recipe_cursor.getCount() > 0) {

            //recipe_cursor.moveToFirst();
            //while (!recipe_cursor.isAfterLast()) {
            //}
            if (recipe_cursor.moveToFirst()) {
                int _id = recipe_cursor.getInt(recipe_cursor.getColumnIndex(BakingContract.RecipeEntry.COLUMN_RECIPE_ID));
                String name = recipe_cursor.getString(recipe_cursor.getColumnIndex(BakingContract.RecipeEntry.COLUMN_RECIPE_NAME));
                int servings = recipe_cursor.getInt(recipe_cursor.getColumnIndex(BakingContract.RecipeEntry.COLUMN_RECIPE_SERVINGS));
                String image_url = recipe_cursor.getString(recipe_cursor.getColumnIndex(BakingContract.RecipeEntry.COLUMN_IMAGE_URL));
                loadIngredients(_id); loadSteps(_id);
                mRecipe = new Recipe(_id, name, servings, image_url, mIngredients, mSteps);
            }
            recipe_cursor.close();
        }

    }
    private void loadIngredients(int recipeId){
        String ingredientToFetch = BakingContract.IngredientEntry.COLUMN_RECIPE_ID + " = " + recipeId;
        Cursor ingredient_cursor = getContentResolver().query(BakingContract.IngredientEntry.CONTENT_URI, null, null, null, null);
        if (ingredient_cursor != null && ingredient_cursor.getCount() > 0) {

            ingredient_cursor.moveToFirst();
            while (!ingredient_cursor.isAfterLast()) {
                int recipe_id = ingredient_cursor.getInt(ingredient_cursor.getColumnIndex(BakingContract.IngredientEntry.COLUMN_RECIPE_ID));
                String recipe_name = ingredient_cursor.getString(ingredient_cursor.getColumnIndex(BakingContract.IngredientEntry.COLUMN_RECIPE_NAME));
                double quantity = ingredient_cursor.getDouble(ingredient_cursor.getColumnIndex(BakingContract.IngredientEntry.COLUMN_QUANTITY));
                String measure = ingredient_cursor.getString(ingredient_cursor.getColumnIndex(BakingContract.IngredientEntry.COLUMN_MEASURE));
                String ingredient = ingredient_cursor.getString(ingredient_cursor.getColumnIndex(BakingContract.IngredientEntry.COLUMN_INGREDIENT));
                mIngredients.add(new Ingredient(quantity, measure, ingredient));
                ingredient_cursor.moveToNext();
            }

            ingredient_cursor.close();
        }
    }
    private void loadSteps(int recipeId){
        String stepToFetch = BakingContract.StepEntry.COLUMN_RECIPE_ID + " = " + recipeId;
        Cursor step_cursor = getContentResolver().query(BakingContract.StepEntry.CONTENT_URI, null, stepToFetch, null, null);
        if (step_cursor != null && step_cursor.getCount() > 0) {

            step_cursor.moveToFirst();
            while (!step_cursor.isAfterLast()) {
                int recipe_id = step_cursor.getInt(step_cursor.getColumnIndex(BakingContract.StepEntry.COLUMN_RECIPE_ID));
                String recipe_name = step_cursor.getString(step_cursor.getColumnIndex(BakingContract.StepEntry.COLUMN_RECIPE_NAME));
                int step_id = step_cursor.getInt(step_cursor.getColumnIndex(BakingContract.StepEntry.COLUMN_STEP_ID));
                String shortDesc = step_cursor.getString(step_cursor.getColumnIndex(BakingContract.StepEntry.COLUMN_SHORT_DESCRIPTION));
                String description = step_cursor.getString(step_cursor.getColumnIndex(BakingContract.StepEntry.COLUMN_DESCRIPTION));
                String videoUrl = step_cursor.getString(step_cursor.getColumnIndex(BakingContract.StepEntry.COLUMN_VIDEO_URL));
                String thumbnailUrl = step_cursor.getString(step_cursor.getColumnIndex(BakingContract.StepEntry.COLUMN_THUMBNAIL_URL));
                mSteps.add(new Step(step_id, shortDesc, description, videoUrl, thumbnailUrl));
                step_cursor.moveToNext();
            }
            step_cursor.close();
        }
    }
}
