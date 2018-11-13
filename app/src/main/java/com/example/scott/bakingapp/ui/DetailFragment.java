package com.example.scott.bakingapp.ui;


import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scott.bakingapp.R;
import com.example.scott.bakingapp.adapter.DetailAdapter;
import com.example.scott.bakingapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class DetailFragment extends android.support.v4.app.Fragment implements DetailAdapter.StepItemClickListener {

    @BindView(R.id.step_recycler_view)
    RecyclerView mRecyclerView;

    private RecyclerView.LayoutManager mLayoutManager;
    private Parcelable mListState;

    private static Recipe mRecipeInstance;
    Context mContext;

    private static final String RECIPE = "recipe";
    private static final String LIST_STATE = "list_state";


    Unbinder unbinder;

    private DetailAdapter mAdapter;

    private StepClickListener stepClickListener;

    public DetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail_fragment, container, false);

        mContext = getContext();
        unbinder = ButterKnife.bind(this, rootView);

        mRecyclerView.setHasFixedSize(true);
        mAdapter = new DetailAdapter(mContext, this);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mLayoutManager = mRecyclerView.getLayoutManager();

        try {
            mAdapter.setStepData(mRecipeInstance.getSteps());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        mListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE, mListState);
        outState.putParcelable(RECIPE, mRecipeInstance);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            stepClickListener = (StepClickListener) context;
        } catch (ClassCastException cce) {
            throw new ClassCastException(context.toString() + " should implement interface StepClickListener");
        }
    }

    public static void setRecipe(Recipe recipe) {
        mRecipeInstance = recipe;
    }

    @Override
    public void StepItemClick(int position) {
        stepClickListener.onStepClicked(position);
    }

    public interface StepClickListener {
        void onStepClicked(int position);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
    }}

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}




