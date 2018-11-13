package com.example.scott.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.scott.bakingapp.model.Recipe;
import com.example.scott.bakingapp.adapter.RecipeAdapter;
import com.example.scott.bakingapp.utilities.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity implements RecipeAdapter.RecipeItemOnClickHandler {

    URL mUrl;

    {
        try {
            mUrl = new URL("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    RecipeAdapter mRecipeAdapter;
    fetchRecipeDataTask mFetchRecipeDataTask;
    RecyclerView recyclerViewGrid;
    RecyclerView.LayoutManager mLayoutManager;
    ProgressBar mLoadingIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        recyclerViewGrid = findViewById(R.id.recycler_view);
        mLoadingIndicator = findViewById(R.id.indeterminateBar);
        recyclerViewGrid.setLayoutManager(new GridLayoutManager(this, 1));
        mLayoutManager = recyclerViewGrid.getLayoutManager();

        mFetchRecipeDataTask = new fetchRecipeDataTask();

        mFetchRecipeDataTask.execute();

        mRecipeAdapter = new RecipeAdapter(this, this);
        recyclerViewGrid.setAdapter(mRecipeAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Recipe recipe) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, recipe);
        startActivity(intentToStartDetailActivity);
    }

    public class fetchRecipeDataTask extends AsyncTask<String, Void, List<Recipe>> {

        @Override
        protected List<Recipe> doInBackground(String... params) {
            try {
                String jsonWeatherResponse = NetworkUtils
                        .getResponseFromHttpUrl(mUrl);

                Gson gson = new Gson();

                TypeToken<List<Recipe>> token = new TypeToken<List<Recipe>>() {
                };

                List<Recipe> recipes = gson.fromJson(jsonWeatherResponse, token.getType());

                return recipes;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Recipe> recipeData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mRecipeAdapter.setRecipeData(recipeData);
        }
    }
}
