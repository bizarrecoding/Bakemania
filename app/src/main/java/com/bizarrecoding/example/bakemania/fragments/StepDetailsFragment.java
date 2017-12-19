package com.bizarrecoding.example.bakemania.fragments;

import android.net.Uri;
import android.os.Bundle;
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
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
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
    private long position = 0;
    private boolean ready = true;

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
        if(savedInstanceState != null){
            if(savedInstanceState.containsKey("position")) {
                position = savedInstanceState.getLong("position");
            }
            if(savedInstanceState.containsKey("ready")){
                ready = savedInstanceState.getBoolean("ready");
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("position",position);
        outState.putBoolean("ready",ready);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_details, container, false);
        ButterKnife.bind(this, view);
        if (step.getSid() > 0) {
            title.setText("Step #" + step.getSid());
        }else{
            title.setText("Introduction");
        }
        description.setText(step.getDescription());
        if(step.getVideoURL().length()==0){
            mPlayerView.setVisibility(View.GONE);
            video.getLayoutParams().width = 0;
        }else {
            initializePlayer();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mExoPlayer!=null){
            Log.d("STATE","pos: "+position+"\nready: "+ready);
            mExoPlayer.seekTo(position);
            mExoPlayer.setPlayWhenReady(ready);
        }else {
            initializePlayer();
            Log.d("STATE","reinit ExoPlayer");
        }

    }

    @Override
    public void onDestroy() {
        if(mExoPlayer!=null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mExoPlayer!=null) {
            position = mExoPlayer.getCurrentPosition();

            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void initializePlayer(){
        if(mPlayerView != null){
            TrackSelector ts = new DefaultTrackSelector();
            LoadControl lControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this.getActivity(),ts,lControl);
            mPlayerView.setPlayer(mExoPlayer);

            Uri mediaUri = Uri.parse(step.getVideoURL());
            //Log.d("mediaUri",mediaUri.toString());
            // HttpDataSource mSource
            String userAgent = Util.getUserAgent(getContext(),getString(R.string.app_name));


            final DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                    Util.getUserAgent(getContext(), userAgent));
            Cache cache = new SimpleCache(getActivity().getCacheDir(), new LeastRecentlyUsedCacheEvictor(1024 * 1024 * 10));
            CacheDataSourceFactory cacheDataSourceFactory = new CacheDataSourceFactory(cache, dataSourceFactory, CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);


            MediaSource mSource = new ExtractorMediaSource(
                    mediaUri,
                    //new DefaultDataSourceFactory(getActivity(),userAgent),
                    cacheDataSourceFactory,
                    new DefaultExtractorsFactory(),
                    null,
                    null);
            mExoPlayer.prepare(mSource);
            mExoPlayer.seekTo(position);
            mExoPlayer.setPlayWhenReady(ready);
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
