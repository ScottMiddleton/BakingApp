package com.example.scott.bakingapp.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.scott.bakingapp.R;
import com.example.scott.bakingapp.model.Recipe;
import com.example.scott.bakingapp.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;

public class StepDetailFragment extends android.support.v4.app.Fragment {

    private static final String EXO_PLAYER_POSITION = "exo_player_position";
    private static final String EXO_PLAYER_READY = "exo_player_ready";

    @Nullable
    @BindView(R.id.thumbnail_iv)
    ImageView mThumbnailImageView;

    @Nullable
    @BindView(R.id.next_step_button)
    Button mNextStepButton;

    @Nullable
    @BindView(R.id.previous_step_button)
    Button mPreviousStepButton;

    @Nullable
    @BindView(R.id.step_title)
    TextView mStepTitleTv;

    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView mPlayerView;

    @Nullable
    @BindView(R.id.description_tv)
    TextView mDescriptionTv;

    private SimpleExoPlayer mExoPlayer;
    private Context mContext;
    private static Recipe mRecipeInstance;
    private int mId;
    private static int mIndex;
    private long position;
    private Uri mVideoUri;
    private StepActionListener stepActionListener;
    private boolean playWhenReady = true;

    Unbinder unbinder;

    public StepDetailFragment() {
        //Empty constructor required
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            position = savedInstanceState.getLong(EXO_PLAYER_POSITION);
            playWhenReady = savedInstanceState.getBoolean(EXO_PLAYER_READY);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            stepActionListener = (StepActionListener) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException(context.toString() + " should implements interface StepActionListener.");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail_fragment, container, false);

        mContext = getContext();
        unbinder = ButterKnife.bind(this, rootView);


        List<Step> stepList = mRecipeInstance.getSteps();
        Step mStepInstance = stepList.get(mIndex);
        mId = mStepInstance.getId();
        if (mStepTitleTv != null) {
            mStepTitleTv.setText(getStepTitle());
        }
        if (mDescriptionTv != null) {
            mDescriptionTv.setText(mStepInstance.getDescription());
        }
        mVideoUri = Uri.parse(mStepInstance.getVideoURL());
        if (mNextStepButton != null) {
            mNextStepButton.setText(getNextButtonText());
        }
        if (mPreviousStepButton != null) {
            mPreviousStepButton.setText(getPreviousButtonText());
        }

        if (TextUtils.isEmpty(mVideoUri.toString())) {
            mPlayerView.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(mStepInstance.getThumbnailURL())) {
                mThumbnailImageView.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(Uri.parse(mStepInstance.getThumbnailURL())).into(mThumbnailImageView);
            }
        } else {
            initializePlayer(mVideoUri);
        }
        return rootView;
    }

    private void initializePlayer(Uri mediaUri) {
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);
        mPlayerView.setPlayer(mExoPlayer);
        // Prepare the MediaSource.
        String userAgent = Util.getUserAgent(mContext, "bakingApp");
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                mContext, userAgent), new DefaultExtractorsFactory(), null, null);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(playWhenReady);

        mExoPlayer.seekTo(position);
    }

    private void releaseExoPlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    public String getNextButtonText() {
        List<Step> steps = mRecipeInstance.getSteps();
        int amountOfSteps = steps.size() - 1;
        String baseString = "Go to step ";
        int nextStepId = mId + 1;
        if (mId == amountOfSteps) {
            mNextStepButton.setVisibility(View.GONE);
        } else {
            mNextStepButton.setVisibility(View.VISIBLE);
        }
        return baseString + nextStepId;
    }

    public String getPreviousButtonText() {
        if (mId == 0) {
            mPreviousStepButton.setVisibility(View.GONE);
        } else if (mId == 1) {
            mPreviousStepButton.setVisibility(View.VISIBLE);
            return "Back to Introduction";
        } else if (mId > 1) {
            mPreviousStepButton.setVisibility(View.VISIBLE);
        }
        String baseString = "Back to step ";
        int nextStepId = mId - 1;
        return baseString + nextStepId;
    }

    public String getStepTitle() {
        if (mId > 0) {
            return "Step " + mId;
        } else {
            return "Introduction";
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        position = mExoPlayer != null ? mExoPlayer.getCurrentPosition() : 0;
        playWhenReady = mExoPlayer.getPlayWhenReady();
        releaseExoPlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mExoPlayer == null){
        initializePlayer(mVideoUri);}
    }

    public static void setIndex(int index) {
        mIndex = index;
    }

    public static void setRecipe(Recipe recipe) {
        mRecipeInstance = recipe;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(EXO_PLAYER_POSITION, position);
        outState.putBoolean(EXO_PLAYER_READY, playWhenReady);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }

    public interface StepActionListener {
        void onNextButtonClicked();

        void onPreviousButtonClicked();
    }

    @Optional
    @OnClick(R.id.previous_step_button)
    public void previousStep() {
        stepActionListener.onPreviousButtonClicked();
    }

    @Optional
    @OnClick(R.id.next_step_button)
    public void nextStep() {
        stepActionListener.onNextButtonClicked();
    }

}
