package com.example.scott.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.scott.bakingapp.model.Step;
import com.example.scott.bakingapp.R;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter {

    private TextView mStepTextView;
    private transient List<Step> mStepDataArray;
    private Context mContext;

    private final StepItemClickListener mStepItemClickListener;

    public DetailAdapter(Context context, StepItemClickListener stepItemClickListener) {
        this.mContext = context;
        this.mStepItemClickListener = stepItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.step_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new DetailAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Step stepInstance = mStepDataArray.get(position);
        String shortDescription = stepInstance.getShortDescription();
        String stepNumberText = getStepNumberText(stepInstance.getId());
        String stepTitle = stepNumberText + " " + shortDescription;
        mStepTextView.setText(stepTitle);
    }

    @Override
    public int getItemCount() {
        if (mStepDataArray == null) {
            return 0;
        } else {
            return mStepDataArray.size();
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

    public void setStepData(List<Step> stepData) {
        mStepDataArray = stepData;
        notifyDataSetChanged();
    }

    private String getStepNumberText(int id) {
        String stepNumberText = null;

        switch (id) {
            case 0:
                stepNumberText = "";
                break;
            case 1:
                stepNumberText = mContext.getResources().getString(R.string.step_1);
                break;
            case 2:
                stepNumberText = mContext.getResources().getString(R.string.step_2);
                break;
            case 3:
                stepNumberText = mContext.getResources().getString(R.string.step_3);
                break;
            case 4:
                stepNumberText = mContext.getResources().getString(R.string.step_4);
                break;
            case 5:
                stepNumberText = mContext.getResources().getString(R.string.step_5);
                break;
            case 6:
                stepNumberText = mContext.getResources().getString(R.string.step_6);
                break;
            case 7:
                stepNumberText = mContext.getResources().getString(R.string.step_7);
                break;
            case 8:
                stepNumberText = mContext.getResources().getString(R.string.step_8);
                break;
            case 9:
                stepNumberText = mContext.getResources().getString(R.string.step_9);
                break;
            case 10:
                stepNumberText = mContext.getResources().getString(R.string.step_10);
                break;
            case 11:
                stepNumberText = mContext.getResources().getString(R.string.step_11);
                break;
            case 12:
                stepNumberText = mContext.getResources().getString(R.string.step_12);
                break;
            case 13:
                stepNumberText = mContext.getResources().getString(R.string.step_13);
                break;
            case 14:
                stepNumberText = mContext.getResources().getString(R.string.step_14);
                break;
            case 15:
                stepNumberText = mContext.getResources().getString(R.string.step_15);
                break;
        }
        return stepNumberText;
    }

    public class DetailAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        DetailAdapterViewHolder(View view) {
            super(view);
            mStepTextView = view.findViewById(R.id.step_text_view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mStepItemClickListener.StepItemClick(getAdapterPosition());
        }
    }

    public interface StepItemClickListener {
        void StepItemClick(int position);
    }


}



