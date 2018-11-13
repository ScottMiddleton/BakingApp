package com.example.scott.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.scott.bakingapp.model.Recipe;
import com.example.scott.bakingapp.ui.DetailFragment;
import com.example.scott.bakingapp.ui.StepDetailFragment;

import butterknife.BindView;

import static com.example.scott.bakingapp.ui.StepDetailFragment.setIndex;

public class DetailActivity extends AppCompatActivity implements DetailFragment.StepClickListener, StepDetailFragment.StepActionListener {

    @BindView(R.id.ingredients_tv)
    TextView mIngredientsTv;

    private Recipe mRecipeInstance;
    private int currentIndex = 0;

    private boolean mTwoPane;

    public static final String RECIPE_EXTRA = "recipe_extra";
    public static final String STEP_INDEX_EXTRA = "step_index_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("DetailActivity", "onCreate called");
        setContentView(R.layout.recipe_detail);
        Intent intentThatStartedThisActivity = getIntent();

        if (findViewById(R.id.step_detail_container) != null) mTwoPane = true;
        else mTwoPane = false;

        //Retrieve the recipe details passed from the MainActivity to populate the detail page
        try {
            if (intentThatStartedThisActivity != null) {
                if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                    Log.e("DetailActivity", "had intent and has extra");
                    mRecipeInstance = intentThatStartedThisActivity.getParcelableExtra(Intent.EXTRA_TEXT);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (savedInstanceState != null) {
            mRecipeInstance = savedInstanceState.getParcelable(RECIPE_EXTRA);
            currentIndex = savedInstanceState.getInt(STEP_INDEX_EXTRA);
        } else {
            updateDetailFragment(mRecipeInstance);
            if (mTwoPane) {
                updateStepDetailFragment(currentIndex, mRecipeInstance);
            }
        }


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(mRecipeInstance.getName());
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setCustomView(R.layout.abs_layout);
    }

    public void launchIngredientsActivity(View v) {
        Class destinationClass = IngredientsActivity.class;
        Intent intentToStartIngredientsActivity = new Intent(this, destinationClass);
        intentToStartIngredientsActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intentToStartIngredientsActivity.putExtra(Intent.EXTRA_TEXT, mRecipeInstance);
        startActivity(intentToStartIngredientsActivity);
    }

    private void updateDetailFragment(Recipe recipe) {
        DetailFragment detailFragment = new DetailFragment();
        DetailFragment.setRecipe(recipe);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.detail_container, detailFragment)
                .commit();
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
        outState.putInt(STEP_INDEX_EXTRA, currentIndex);
        outState.putParcelable(RECIPE_EXTRA, mRecipeInstance);
    }

    @Override
    public void onStepClicked(int position) {
        currentIndex = position;
        if (mTwoPane) {
            updateStepDetailFragment(currentIndex, mRecipeInstance);
        } else {
            setIndex(currentIndex);
            Class destinationClass = StepDetailActivity.class;
            Intent intentToStartVideoActivity = new Intent(this, destinationClass);
            intentToStartVideoActivity.putExtra(STEP_INDEX_EXTRA, currentIndex);
            intentToStartVideoActivity.putExtra(RECIPE_EXTRA, mRecipeInstance);
            startActivity(intentToStartVideoActivity);
        }
    }

    @Override
    public void onNextButtonClicked() {
        currentIndex++;
        updateStepDetailFragment(currentIndex, mRecipeInstance);
    }

    @Override
    public void onPreviousButtonClicked() {
        currentIndex--;
        updateStepDetailFragment(currentIndex, mRecipeInstance);
    }
}
