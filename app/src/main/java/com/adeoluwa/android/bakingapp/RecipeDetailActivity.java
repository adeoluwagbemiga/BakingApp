package com.adeoluwa.android.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.adeoluwa.android.bakingapp.adapters.IngredientsAdapter;
import com.adeoluwa.android.bakingapp.adapters.StepsAdapter;
import com.adeoluwa.android.bakingapp.models.Ingredient;
import com.adeoluwa.android.bakingapp.models.Recipe;
import com.adeoluwa.android.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements StepsAdapter.ListItemClickListener {

    private Recipe mRecipe;
    private List<Ingredient> mIngredients = new ArrayList<Ingredient>();
    private List<Step> mSteps = new ArrayList<Step>();

    private static int mServings;
    private static int mRecipeId;
    private static String mName;
    @BindView(R.id.txt_recipe_servings) TextView mServingsTextView;
    @BindView(R.id.rv_ingredients) RecyclerView mIngredientsRecyclerView;
    @BindView(R.id.rv_steps) RecyclerView mStepsRecyclerView;
    private LinearLayoutManager ingredientsLayoutManager;
    private LinearLayoutManager stepsLayoutManager;
    private IngredientsAdapter mIngredientsAdapter;
    private StepsAdapter mStepsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null && extras.containsKey("recipe")){
            mRecipe = intent.getParcelableExtra("recipe");
            Log.e("RECIPE NAME", mRecipe.getName());
            //Toast.makeText(this, mRecipe.getName(), Toast.LENGTH_SHORT).show();
            mIngredients = mRecipe.getIngredients();
            mSteps = mRecipe.getSteps();
            Toast.makeText(this, mRecipe.getName(), Toast.LENGTH_SHORT).show();

            mName = mRecipe.getName();
            mRecipeId = mRecipe.getid();
            mServings = mRecipe.getServings();
        };

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
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, StepDetailActivity.class);
        intent.putExtra("recipename", mName);
        intent.putExtra("step", mSteps.get(position));
        startActivity(intent);
        Toast.makeText(this, "Step " + String.valueOf(position) + " Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}
