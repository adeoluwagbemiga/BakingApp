package com.adeoluwa.android.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.Button;

import com.adeoluwa.android.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * An activity representing a single RecipeStep detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeStepListActivity}.
 */
public class RecipeStepDetailActivity extends AppCompatActivity {
    private Step mStep;
    private int mStepPosition;
    private int mStepsSize;
    ArrayList<Step> mSteps = new ArrayList<Step>();

    @BindView(R.id.btn_next) Button mNextButton;
    @BindView(R.id.btn_previous) Button mPreviousButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipestep_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Bundle stepbundle = intent.getExtras();
            if(intent.hasExtra("step") && stepbundle.containsKey("step")){
                mStep = stepbundle.getParcelable("step");
                mStepPosition = stepbundle.getInt("step_position");
                mStepsSize = stepbundle.getInt("steps_size");
                mSteps = stepbundle.getParcelableArrayList("steps");
            }
            if(getApplication().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                actionBar.hide();
            }
            initializeFragment();
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

        }else{
            mStep = savedInstanceState.getParcelable("step");
            mStepPosition = savedInstanceState.getInt("step_position");
            mStepsSize = savedInstanceState.getInt("steps_size");
            mSteps = savedInstanceState.getParcelableArrayList("steps");
            if(getApplication().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                actionBar.hide();
            }
            initializeFragment();
        }
    }

    private void initializeFragment(){
        Bundle arguments = new Bundle();
        arguments.putString(RecipeStepDetailFragment.ARG_ITEM_ID,
                getIntent().getStringExtra(RecipeStepDetailFragment.ARG_ITEM_ID));
        arguments.putParcelable("step", mStep);
        arguments.putInt("step_position", mStepPosition);
        arguments.putInt("steps_size", mStepsSize);
        arguments.putParcelableArrayList("steps", mSteps);
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipestep_detail_container, fragment)
                .commit();
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
            Intent intent = new Intent(this, RecipeStepListActivity.class);
            intent.putExtra("steps", mSteps);
            intent.putExtra("step_position", mStepPosition);
            //NavUtils.navigateUpTo(this, new Intent(this, RecipeStepListActivity.class));
            NavUtils.navigateUpTo(this, intent);
            //NavUtils.navigateUpFromSameTask(this);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("step", mStep);
        outState.putInt("step_position", mStepPosition);
        outState.putInt("steps_size", mStepsSize);
        outState.putParcelableArrayList("steps", mSteps);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
