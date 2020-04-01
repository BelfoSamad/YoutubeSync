package com.belfoapps.youtubesync.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.belfoapps.youtubesync.R;
import com.belfoapps.youtubesync.contracts.WatchContract;
import com.belfoapps.youtubesync.di.components.DaggerMVPComponent;
import com.belfoapps.youtubesync.di.components.MVPComponent;
import com.belfoapps.youtubesync.di.modules.ApplicationModule;
import com.belfoapps.youtubesync.di.modules.MVPModule;
import com.belfoapps.youtubesync.presenters.WatchPresenter;
import com.belfoapps.youtubesync.views.ui.custom.FragmentLifeCycle;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.views.YouTubePlayerSeekBar;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WatchFragment extends Fragment implements WatchContract.View, FragmentLifeCycle {
    private static final String TAG = "WatchFragment";
    /**************************************** Declarations ****************************************/
    private MVPComponent mvpComponent;
    @Inject
    WatchPresenter mPresenter;

    /***************************************** Constructor ****************************************/
    public WatchFragment() {
        // Required empty public constructor
    }

    /**************************************** View Declarations ***********************************/
    @BindView(R.id.player)
    YouTubePlayerView player;
    /**************************************** Click Listeners *************************************/

    /**************************************** Essential Methods ***********************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_watch, container, false);

        //Initialize Dagger For Application
        mvpComponent = getComponent();
        //Inject the Component Here
        mvpComponent.inject(this);

        //Set ButterKnife
        ButterKnife.bind(this, view);

        //Attach View To Presenter
        mPresenter.attach(this);

        player.addYouTubePlayerListener(new YouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo("IVP34jKIrTQ", 0);
            }

            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState playerState) {
                Log.d(TAG, "onStateChange: " + playerState);
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

        return view;
    }

    @Override
    public MVPComponent getComponent() {
        if (mvpComponent == null) {
            mvpComponent = DaggerMVPComponent
                    .builder()
                    .applicationModule(new ApplicationModule(Objects.requireNonNull(getActivity()).getApplication()))
                    .mVPModule(new MVPModule(getActivity()))
                    .build();
        }
        return mvpComponent;
    }

    @Override
    public void onStartFragment() {

    }

    @Override
    public void onStopFragment() {

    }

    /**************************************** Methods *********************************************/
}
