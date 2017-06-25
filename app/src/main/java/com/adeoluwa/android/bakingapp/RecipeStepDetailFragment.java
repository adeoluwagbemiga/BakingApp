package com.adeoluwa.android.bakingapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.adeoluwa.android.bakingapp.dummy.DummyContent;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a single RecipeStep detail screen.
 * This fragment is either contained in a {@link RecipeStepListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeStepDetailActivity}
 * on handsets.
 */
public class RecipeStepDetailFragment extends Fragment implements View.OnClickListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    //private DummyContent.DummyItem mItem;
    private Step mStep;
    private String mVideoURL;
    private String mThumbnailURL;
    private SimpleExoPlayer mExoPlayer;

    @BindView(R.id.txt_step_description) TextView mStepDescriptionTextView;
    @BindView(R.id.exo_player) SimpleExoPlayerView mExoPlayerView;
    @BindView(R.id.btn_next) Button mNextButton;
    @BindView(R.id.btn_previous) Button mPreviousButton;
    //@BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout  appBarLayout;

    private int mStepPosition;
    private int mStepIndex;
    ArrayList<Step> mSteps = new ArrayList<Step>();


    private boolean autoPlay = false;
    private int currentWindow;
    private long playbackPosition;

    public static final String AUTOPLAY = "autoplay";
    public static final String CURRENT_WINDOW_INDEX = "current_window_index";
    public static final String PLAYBACK_POSITION = "playback_position";

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            mStep = getArguments().getParcelable("step");
            mSteps = getArguments().getParcelableArrayList("steps");
            mStepPosition = getArguments().getInt("step_position");
            mStepIndex = mStepPosition - 1;
            Activity activity = this.getActivity();

            //CollapsingToolbarLayout
                    appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mStep.getShortdescription());
            }
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mExoPlayer != null) {
            outState.putLong(PLAYBACK_POSITION, playbackPosition);
            outState.putInt(CURRENT_WINDOW_INDEX, currentWindow);
            outState.putBoolean(AUTOPLAY, autoPlay);
        }
        outState.putParcelableArrayList("steps", mSteps);
        outState.putParcelable("step", mStep);
        outState.putInt("step_position", mStepPosition);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipestep_detail, container, false);
        ButterKnife.bind(this, rootView);
        // Show the dummy content as text in a TextView.
        if (mStep != null) {
            //((TextView) rootView.findViewById(R.id.recipestep_detail)).setText(mItem.details);
            mStepDescriptionTextView.setText(mStep.getDescription());
        }

        mPreviousButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        return rootView;
    }
    void initExoPlayer(){
        DefaultLoadControl defaultLoadControl = new DefaultLoadControl(new DefaultAllocator(true, 30000));
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this.getActivity()),
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
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initExoPlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            initExoPlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (mStepIndex < mSteps.size() - 1) {
                    int index = ++mStepIndex;
                    mStep = mSteps.get(index);
                    mStepDescriptionTextView.setText(mStep.getDescription());
                    appBarLayout.setTitle(mStep.getShortdescription());
                    //playStepVideo(index);
                    initExoPlayer();
                } else {
                    mNextButton.setVisibility(View.INVISIBLE);
                    //Toast.makeText(getActivity(), R.string.end_of_steps, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_previous:
                if (mStepIndex > 0) {
                    int index = --mStepIndex;
                    mStep = mSteps.get(index);
                    mStepDescriptionTextView.setText(mStep.getDescription());
                    appBarLayout.setTitle(mStep.getShortdescription());
                    //playStepVideo(index);
                    initExoPlayer();
                } else {
                    mPreviousButton.setVisibility(View.INVISIBLE);
                    //Toast.makeText(getActivity(), R.string.start_of_steps, Toast.LENGTH_LONG).show();
                }
                break;
        }

    }
}
