package com.adeoluwa.android.bakingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.adeoluwa.android.bakingapp.models.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailActivity extends AppCompatActivity {
    private Step mStep;
    private String mVideoURL;
    private String mThumbnailURL;
    private SimpleExoPlayer mExoPlayer;

    @BindView(R.id.txt_step_description) TextView mStepDescriptionTextView;
    @BindView(R.id.exo_player) SimpleExoPlayerView mExoPlayerView;
    private boolean autoPlay = false;
    private int currentWindow;
    private long playbackPosition;

    public static final String AUTOPLAY = "autoplay";
    public static final String CURRENT_WINDOW_INDEX = "current_window_index";
    public static final String PLAYBACK_POSITION = "playback_position";

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_item_detail);
        ButterKnife.bind(this);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null && extras.containsKey("recipename")) {
            mStep = intent.getParcelableExtra("step");
            mStepDescriptionTextView.setText(mStep.getDescription());
            Toast.makeText(this, mStep.getVideoURL(), Toast.LENGTH_LONG).show();
        }
        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION, 0);
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW_INDEX, 0);
            autoPlay = savedInstanceState.getBoolean(AUTOPLAY, false);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mExoPlayer == null) {
            outState.putLong(PLAYBACK_POSITION, playbackPosition);
            outState.putInt(CURRENT_WINDOW_INDEX, currentWindow);
            outState.putBoolean(AUTOPLAY, autoPlay);
        }
    }

    void initExoPlayer(){
        DefaultLoadControl defaultLoadControl = new DefaultLoadControl(new DefaultAllocator(true, 30000));
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(),
                defaultLoadControl);
                //new DefaultLoadControl());
        mExoPlayerView.setPlayer(mExoPlayer);
        mExoPlayer.setPlayWhenReady(autoPlay);

        // resume playback position
        mExoPlayer.seekTo(currentWindow, playbackPosition);

        Uri uri = Uri.parse(mStep.getVideoURL());
        MediaSource mediaSource = buildMediaSource(uri);

        // now we are ready to start playing our media files
        mExoPlayer.prepare(mediaSource, true, false);
        mExoPlayer.setPlayWhenReady(true);
    }
    private MediaSource buildMediaSource(Uri uri) {
        DefaultExtractorsFactory extractorSourceFactory = new DefaultExtractorsFactory();
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("ua");

        ExtractorMediaSource audioSource = new ExtractorMediaSource(uri, dataSourceFactory, extractorSourceFactory, null, null);

        // this return a single mediaSource object. i.e. no next, previous buttons to play next/prev media file
        return new ExtractorMediaSource(uri, dataSourceFactory, extractorSourceFactory, null, null);

        /*
         * Uncomment the line below to play multiple meidiaSources in sequence aka playlist (and totally without buffering!)
         * NOTE: you have to comment the return statement just above this comment
         */

        /*
          ExtractorMediaSource videoSource1 = new ExtractorMediaSource(Uri.parse(VIDEO_1), dataSourceFactory, extractorSourceFactory, null, null);
          ExtractorMediaSource videoSource2 = new ExtractorMediaSource(Uri.parse(VIDEO_2), dataSourceFactory, extractorSourceFactory, null, null);
          // returns a mediaSource collection
          return new ConcatenatingMediaSource(videoSource1, audioSource, videoSource2);
         */

    }
    private void releasePlayer() {
        if (mExoPlayer != null) {
            // save the player state before releasing its resources
            playbackPosition = mExoPlayer.getCurrentPosition();
            currentWindow = mExoPlayer.getCurrentWindowIndex();
            autoPlay = mExoPlayer.getPlayWhenReady();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        mExoPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initExoPlayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            initExoPlayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }
}
