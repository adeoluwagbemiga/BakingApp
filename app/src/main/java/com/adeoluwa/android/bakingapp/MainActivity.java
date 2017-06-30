package com.adeoluwa.android.bakingapp;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.adeoluwa.android.bakingapp.adapters.RecipesAdapter;
import com.adeoluwa.android.bakingapp.models.Recipe;
import com.adeoluwa.android.bakingapp.utils.RecipeAsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.ListItemClickListener,
        LoaderManager.LoaderCallbacks<List<Recipe>> {
    private List<Recipe> mRecipeList;
    private RecipesAdapter mRecipesAdapter;
    private LinearLayoutManager layoutManager;
    @BindView(R.id.rv_recipes) RecyclerView mRecipesRecyclerView;
    //@BindView(R.id.pb_progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout mSwipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mRecipeList = new ArrayList<>();
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(MainActivity.this, "Quite Refreshing", Toast.LENGTH_SHORT).show();
                getSupportLoaderManager().restartLoader(0, null, MainActivity.this);
            }
        });
        //mSwipeContainer.setColorSchemeColors;
        layoutManager = new LinearLayoutManager(this);
        mRecipesRecyclerView.setLayoutManager(layoutManager);
        mRecipesRecyclerView.setHasFixedSize(true);
        if(savedInstanceState != null && savedInstanceState.containsKey("recipes")){
            mRecipeList = savedInstanceState.getParcelableArrayList("recipes");
        }
        if(getSupportLoaderManager().getLoader(0)!=null){
            //Log.e("MSG", "I AM HERE 7");
            getSupportLoaderManager().initLoader(0,null,this);
        }
        //Log.e("MSG", "I AM HERE 6");
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("recipes", (ArrayList<? extends Parcelable>) mRecipeList);
    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        //Log.e("MSG", "I AM HERE 5");
        return new RecipeAsyncTaskLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
        mRecipeList = data;
        mRecipesAdapter = new RecipesAdapter(mRecipeList, this);
        mRecipesRecyclerView.setAdapter(mRecipesAdapter);
        mRecipesAdapter.notifyDataSetChanged();
        //Log.e("MSG", "I AM HERE 8");
        //mProgressBar.setVisibility(View.INVISIBLE);
        mSwipeContainer.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, RecipeStepListActivity.class);
        Recipe recipe = mRecipeList.get(position);
        intent.putExtra("recipe", recipe);
        startActivity(intent);
        //Toast.makeText(this, "Recipe " + String.valueOf(position) + " Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(getSupportLoaderManager().getLoader(0)!=null){
            getSupportLoaderManager().initLoader(0,null,this);
        }
    }
}
