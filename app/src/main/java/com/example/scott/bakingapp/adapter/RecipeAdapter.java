package com.example.scott.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.scott.bakingapp.model.Recipe;
import com.example.scott.bakingapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter {

    private ImageView mRecipeImageView;
    private TextView mRecipeNameTextView;
    private List<Recipe> mRecipeDataArray;
    private final Context mContext;
    private final RecipeItemOnClickHandler mClickHandler;

    public RecipeAdapter(Context mContext, RecipeItemOnClickHandler mClickHandler) {
        this.mContext = mContext;
        this.mClickHandler = mClickHandler;
    }

    public class recipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public recipeAdapterViewHolder(View view) {
            super(view);
            mRecipeImageView = view.findViewById(R.id.recipe_image);
            mRecipeNameTextView = view.findViewById(R.id.recipe_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Recipe recipeInstance = mRecipeDataArray.get(adapterPosition);
            if (mRecipeDataArray != null) {
                mClickHandler.onClick(recipeInstance);
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new recipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Recipe recipeInstance = mRecipeDataArray.get(position);
        String imageUrl = recipeInstance.getImage();
        String recipeName = recipeInstance.getName();
        bind(recipeName, imageUrl, recipeInstance);
    }

    @Override
    public int getItemCount() {
        if (mRecipeDataArray == null) {
            return 0;
        } else {
            return mRecipeDataArray.size();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setRecipeData(List<Recipe> recipeData) {
        mRecipeDataArray = recipeData;
        notifyDataSetChanged();
    }

    public interface RecipeItemOnClickHandler {
        void onClick(Recipe recipe);
    }

    public void bind(String recipeName, String imageUrl, Recipe recipe) {

        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.with(mRecipeNameTextView.getContext()).load(recipe.getImage()).into(mRecipeImageView);
        } else {
            Picasso.with(mRecipeNameTextView.getContext()).load(getRecipeImage(recipeName)).into(mRecipeImageView);
        }
        mRecipeNameTextView.setText(recipeName);
    }

    private int getRecipeImage(String recipeName) {
        int recipeImage = R.drawable.cutlery_image;
        switch (recipeName) {
            case "Nutella Pie":
                recipeImage = R.drawable.nutella_pie;
                break;
            case "Brownies":
                recipeImage = R.drawable.brownies;
                break;
            case "Yellow Cake":
                recipeImage = R.drawable.yellow_cake;
                break;
            case "Cheesecake":
                recipeImage = R.drawable.cheesecake;
                break;
        }
        return recipeImage;
    }
}

