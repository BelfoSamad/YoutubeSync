package com.belfoapps.youtubesync.views.activities;

import android.os.Bundle;
import android.util.Log;

import com.belfoapps.youtubesync.R;
import com.belfoapps.youtubesync.contracts.WatchContract;
import com.belfoapps.youtubesync.di.components.DaggerMVPComponent;
import com.belfoapps.youtubesync.di.components.MVPComponent;
import com.belfoapps.youtubesync.di.modules.ApplicationModule;
import com.belfoapps.youtubesync.di.modules.MVPModule;
import com.belfoapps.youtubesync.presenters.MainPresenter;
import com.belfoapps.youtubesync.presenters.WatchPresenter;
import com.belfoapps.youtubesync.utils.Config;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WatchActivity extends YouTubeBaseActivity implements WatchContract.View {
    private static final String TAG = "WatchActivity";
    /**************************************** Declarations ****************************************/
    private MVPComponent mvpComponent;
    @Inject
    WatchPresenter mPresenter;
    @Inject
    MainPresenter mainPresenter;
    private YouTubePlayer ytbPlayer;
    private ArrayList<String> discoverers;

    /**************************************** View Declarations ***********************************/
    @BindView(R.id.player)
    YouTubePlayerView player;
    /**************************************** Click Listeners *************************************/

    /**************************************** Essential Methods ***********************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);

        //Initialize Dagger For Application
        mvpComponent = getComponent();
        //Inject the Component Here
        mvpComponent.inject(this);

        //Set ButterKnife
        ButterKnife.bind(this);

        //Attach View To Presenter
        mPresenter.attach(this);
        Log.d(TAG, "onCreate: Attaching the WatchActivity");
        mainPresenter.attachWatchView(this);

        //Init Youtube Player
        player.initialize(Config.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG, "onInitializationSuccess");
                ytbPlayer = youTubePlayer;
                initYoutube("nLnfHreq7CE");
                //initYoutube(getIntent().getStringExtra("url"));
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "onInitializationFailure");
            }
        });

        discoverers = getIntent().getStringArrayListExtra("discoverers");
        if (discoverers != null)
            mPresenter.setDiscoverers(discoverers);
    }

    @Override
    public MVPComponent getComponent() {
        if (mvpComponent == null) {
            mvpComponent = DaggerMVPComponent
                    .builder()
                    .applicationModule(new ApplicationModule(getApplication()))
                    .mVPModule(new MVPModule(this))
                    .build();
        }
        return mvpComponent;
    }

    /**************************************** Methods *********************************************/
    @Override
    public void initYoutube(String url) {

        ytbPlayer.loadVideo(url);

        ytbPlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onPlaying() {
                if (discoverers != null)
                    mPresenter.sendRequest(Config.START, ytbPlayer.getCurrentTimeMillis());
            }

            @Override
            public void onPaused() {
                if (discoverers != null)
                    mPresenter.sendRequest(Config.PAUSE, ytbPlayer.getCurrentTimeMillis());
            }

            @Override
            public void onStopped() {

            }

            @Override
            public void onBuffering(boolean b) {
                if (discoverers != null)
                    if (b) mPresenter.sendRequest(Config.BUFFER, ytbPlayer.getCurrentTimeMillis());
            }

            @Override
            public void onSeekTo(int i) {
                if (discoverers != null)
                    mPresenter.sendRequest(Config.SEEK, i);
            }
        });
    }

    @Override
    public void startYoutubeVideo(int timestamp_millis) {
        ytbPlayer.seekToMillis(timestamp_millis);
        ytbPlayer.play();
    }

    @Override
    public void pauseYoutubeVideo(int timestamp_millis) {
        ytbPlayer.seekToMillis(timestamp_millis);
        ytbPlayer.pause();
    }

    @Override
    public void seekToYoutubeVideo(int seekTo_millis) {
        ytbPlayer.seekToMillis(seekTo_millis);
    }
}
