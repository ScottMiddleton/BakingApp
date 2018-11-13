package com.example.scott.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.example.scott.bakingapp.model.Ingredient;
import com.example.scott.bakingapp.model.Recipe;
import com.example.scott.bakingapp.service.UpdateWidgetIngredientsService;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class IngredientsActivity extends AppCompatActivity {

    TextView mIngredientsTv;
    public static String ingredientsString;
    List<Ingredient> mIngredientData;
    Recipe mRecipeInstance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        Intent intentThatStartedThisActivity = getIntent();

        //Retrieve the recipe details passed from the MainActivity to populate the detail page
        try {
            if (intentThatStartedThisActivity != null) {
                if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                    mRecipeInstance = intentThatStartedThisActivity.getParcelableExtra(Intent.EXTRA_TEXT);
                    mIngredientData = mRecipeInstance.getIngredients();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mIngredientsTv = findViewById(R.id.ingredients_tv);

        ingredientsString = getIngredients(mIngredientData);
        mIngredientsTv.setText(ingredientsString);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(mRecipeInstance.getName());
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setCustomView(R.layout.abs_layout);
    }

    private String getIngredients(List<Ingredient> ingredientsList) {

        StringBuilder sb = new StringBuilder();
        sb.append(mRecipeInstance.getName() + "\n\n");

        for (int i = 0; i < ingredientsList.size(); i++) {

            Ingredient currentIngredient = ingredientsList.get(i);
            String quantity = formatDecimals(currentIngredient.getQuantity());
            String measure = currentIngredient.getMeasure();
            String ingredientLowerCase = currentIngredient.getIngredient();
            String ingredient = ingredientLowerCase.substring(0, 1).toUpperCase() + ingredientLowerCase.substring(1);
            int ingredientNumber = i + 1;
            String numberString = Integer.toString(ingredientNumber);

            sb.append(numberString + ". " + ingredient + ": " + quantity + " " + measure + "\n");
        }

        String ingredientsTitle = getResources().getString(R.string.ingredients);
        SpannableString ss1 = new SpannableString(ingredientsTitle);
        ss1.setSpan(new RelativeSizeSpan(2f), 0, 5, 0); // set size

        return sb.toString();
    }

    private String formatDecimals(Double quantity) {
        // This is to show symbol . instead of ,
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
// Define the maximum number of decimals (number of symbols #)
        DecimalFormat df = new DecimalFormat(getApplicationContext().getResources().getString(R.string.decimal_format), otherSymbols);

        return df.format(quantity).toString();
    }

    public static String getIngredientsString(){
        return ingredientsString;
    }

    public void addIngredientsToWidget(View v){
        UpdateWidgetIngredientsService.startActionUpdateIngredients(this);
    }
}

