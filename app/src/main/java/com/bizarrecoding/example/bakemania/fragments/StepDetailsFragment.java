package com.bizarrecoding.example.bakemania.fragments;

import android.content.Context;
import android.media.session.MediaSession;
import android.media.session.MediaSession.Callback;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bizarrecoding.example.bakemania.R;
import com.bizarrecoding.example.bakemania.objects.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailsFragment extends Fragment implements ExoPlayer.EventListener{

    @BindView(R.id.title) TextView title;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.video) FrameLayout video;
    @BindView(R.id.player) SimpleExoPlayerView mPlayerView;

    private Step step;
    private SimpleExoPlayer mExoPlayer;
    private long stepId;

    public StepDetailsFragment() {}

    public static StepDetailsFragment newInstance(Long stepId) {
        StepDetailsFragment fragment = new StepDetailsFragment();
        Bundle args = new Bundle();
        args.putLong("Step",stepId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            stepId = getArguments().getLong("Step");
            step = Step.findById(Step.class,stepId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_details, container, false);
        ButterKnife.bind(this, view);
        if (step.getId() > 0) {
            title.setText("Step #" + stepId);
        }else{
            title.setText("Introduction");
        }
        description.setText(step.getDescription());
        if(step.getVideoURL().length()==0){
            mPlayerView.setVisibility(View.GONE);
        }else {
            initializePlayer();
        }
        return view;
    }

    @Override
    public void onDestroy() {
        if(mExoPlayer!=null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
        super.onDestroy();
    }

    private void initializePlayer(){
        if(mPlayerView != null){
            TrackSelector ts = new DefaultTrackSelector();
            LoadControl lControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this.getActivity(),ts,lControl);
            mPlayerView.setPlayer(mExoPlayer);

            Uri mediaUri = Uri.parse(step.getVideoURL());
            Log.d("mediaUri",mediaUri.toString());
            // HttpDataSource mSource
            String userAgent = Util.getUserAgent(getContext(),getString(R.string.app_name));
            MediaSource mSource = new ExtractorMediaSource(
                    mediaUri,
                    new DefaultDataSourceFactory(getActivity(),userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null);
            mExoPlayer.prepare(mSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()){
            if (!isVisibleToUser){   // If we are becoming invisible, then...
                //pause or stop video
            }
            if (isVisibleToUser){
                //play your video
            }
        }
    }
}
