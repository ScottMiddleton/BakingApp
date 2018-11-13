package com.example.scott.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import com.example.scott.bakingapp.model.Recipe;
import com.example.scott.bakingapp.ui.StepDetailFragment;
import static com.example.scott.bakingapp.DetailActivity.RECIPE_EXTRA;
import static com.example.scott.bakingapp.DetailActivity.STEP_INDEX_EXTRA;
import static com.example.scott.bakingapp.ui.StepDetailFragment.setIndex;

public class StepDetailActivity extends AppCompatActivity implements StepDetailFragment.StepActionListener{

    private Recipe mRecipeInstance;
    private int mIndex;

    private static final String STEP_INDEX_OUTSTATE = "step_instance_outstate";
    private static final String RECIPE_INSTANCE_OUTSTATE = "recipe_instance_outstate";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.step_detail);

        Intent intentThatStartedThisActivity = getIntent();

        //Retrieve the recipe details passed from the MainActivity to populate the detail page
        try {
            if (intentThatStartedThisActivity != null) {
                Log.e("XXXXx", "intent is not null");
                    mIndex = intentThatStartedThisActivity.getIntExtra(STEP_INDEX_EXTRA, 0);
                    mRecipeInstance = intentThatStartedThisActivity.getParcelableExtra(RECIPE_EXTRA);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (savedInstanceState != null) {
            mRecipeInstance = savedInstanceState.getParcelable(RECIPE_INSTANCE_OUTSTATE);
            mIndex = savedInstanceState.getInt(STEP_INDEX_OUTSTATE);
        } else {
        updateStepDetailFragment(mIndex, mRecipeInstance);}


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
            getWindow().getDecorView().setSystemUiVisibility(
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {}


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(mRecipeInstance.getName());
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setCustomView(R.layout.abs_layout);
    }

    private void updateStepDetailFragment(int index, Recipe recipe) {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        StepDetailFragment.setRecipe(recipe);
        setIndex(index);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.step_detail_container, stepDetailFragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STEP_INDEX_OUTSTATE, mIndex);
        outState.putParcelable(RECIPE_INSTANCE_OUTSTATE, mRecipeInstance);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNextButtonClicked() {
        mIndex++;
        updateStepDetailFragment(mIndex, mRecipeInstance);
    }

    @Override
    public void onPreviousButtonClicked() {
        mIndex--;
        updateStepDetailFragment(mIndex, mRecipeInstance);
    }
}
