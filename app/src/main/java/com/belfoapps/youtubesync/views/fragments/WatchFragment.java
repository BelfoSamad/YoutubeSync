package com.belfoapps.youtubesync.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.belfoapps.youtubesync.R;
import com.belfoapps.youtubesync.presenters.MainPresenter;
import com.belfoapps.youtubesync.utils.Config;
import com.belfoapps.youtubesync.views.ui.custom.FragmentLifeCycle;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WatchFragment extends Fragment implements FragmentLifeCycle, YouTubePlayer.OnInitializedListener {
    private static final String TAG = "WatchFragment";
    /**************************************** Declarations ****************************************/
    private MainPresenter mPresenter;
    private float current = 0f;
    private YouTubePlayer ytbPlayer;

    /***************************************** Constructor ****************************************/
    public WatchFragment() {
        // Required empty public constructor
    }

    public WatchFragment(MainPresenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    /**************************************** View Declarations ***********************************/
    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.send)
    public void send() {

    }

    /**************************************** Essential Methods ***********************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.fragment_watch, container, false);

        //Set ButterKnife
        ButterKnife.bind(this, view);


        return view;
    }

    @Override
    public void onStartFragment() {
        //Init Youtube Player
        Log.d(TAG, "onStartFragment");
        initYoutubeVideo(mPresenter.getYoutubeVideoUrl());
    }

    @Override
    public void onStopFragment() {

    }

    /**************************************** Methods *********************************************/
    public void initYoutubeVideo(String url) {
        ytbPlayer.loadVideo("U_aQfBKz8ps");
        //ytbPlayer.play();
    }

    public void startVideo(float timestamp) {
    }

    public void pauseVideo(float timestamp) {
    }

    public void seekTo(float seekTo) {
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
