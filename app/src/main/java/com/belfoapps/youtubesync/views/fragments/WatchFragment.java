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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WatchFragment extends Fragment implements FragmentLifeCycle {
    private static final String TAG = "WatchFragment";
    /**************************************** Declarations ****************************************/
    private MainPresenter mPresenter;
    private YouTubePlayer ytbPlayer;
    private float current = 0f;

    /***************************************** Constructor ****************************************/
    public WatchFragment() {
        // Required empty public constructor
    }

    public WatchFragment(MainPresenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    /**************************************** View Declarations ***********************************/
    @BindView(R.id.player)
    YouTubePlayerView player;

    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.send)
    public void send() {

    }

    /**************************************** Essential Methods ***********************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
        Log.d(TAG, "initYoutubeVideo");
        getLifecycle().addObserver(player);
        player.addYouTubePlayerListener(new YouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                Log.d(TAG, "onReady");
                youTubePlayer.loadVideo("U_aQfBKz8ps", 0);
                ytbPlayer = youTubePlayer;
            }

            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState playerState) {
                switch (playerState) {
                    case PAUSED:
                    case BUFFERING:
                        mPresenter.sendRequest(Config.PAUSE, current);
                        break;
                    case PLAYING:
                        mPresenter.sendRequest(Config.START, current);
                        break;
                }
            }

            @Override
            public void onPlaybackQualityChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackQuality playbackQuality) {

            }

            @Override
            public void onPlaybackRateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackRate playbackRate) {

            }

            @Override
            public void onError(YouTubePlayer youTubePlayer, PlayerConstants.PlayerError playerError) {

            }

            @Override
            public void onCurrentSecond(YouTubePlayer youTubePlayer, float v) {
                Log.d(TAG, "onCurrentSecond: " + v);
                current = v;
            }

            @Override
            public void onVideoDuration(YouTubePlayer youTubePlayer, float v) {

            }

            @Override
            public void onVideoLoadedFraction(YouTubePlayer youTubePlayer, float v) {

            }

            @Override
            public void onVideoId(YouTubePlayer youTubePlayer, String s) {

            }

            @Override
            public void onApiChange(YouTubePlayer youTubePlayer) {

            }
        });
    }

    public void startVideo(float timestamp) {
        ytbPlayer.seekTo(timestamp);
        ytbPlayer.play();
    }

    public void pauseVideo(float timestamp) {
        ytbPlayer.seekTo(timestamp);
        ytbPlayer.pause();
    }

    public void seekTo(float seekTo) {
        ytbPlayer.seekTo(seekTo);
        ytbPlayer.play();
    }
}
