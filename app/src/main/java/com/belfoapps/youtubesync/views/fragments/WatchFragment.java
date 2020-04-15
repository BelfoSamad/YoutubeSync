package com.belfoapps.youtubesync.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.belfoapps.youtubesync.R;
import com.belfoapps.youtubesync.presenters.MainPresenter;
import com.belfoapps.youtubesync.utils.Config;
import com.belfoapps.youtubesync.views.activities.MainActivity;
import com.belfoapps.youtubesync.views.ui.custom.FragmentLifeCycle;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WatchFragment extends Fragment implements FragmentLifeCycle, YouTubePlayer.OnInitializedListener {
    private static final String TAG = "WatchFragment";
    /**************************************** Declarations ****************************************/
    private MainPresenter mPresenter;
    private MainActivity mView;
    private YouTubePlayerSupportFragmentX mYoutubePlayerFragment;
    private YouTubePlayer ytbPlayer;

    /***************************************** Constructor ****************************************/
    public WatchFragment() {
        // Required empty public constructor
    }

    public WatchFragment(MainActivity mView, MainPresenter mPresenter) {
        this.mPresenter = mPresenter;
        this.mView = mView;
    }

    /**************************************** View Declarations ***********************************/
    @BindView(R.id.watchers)
    Button watchers;

    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.sync)
    public void sync() {
        pause(ytbPlayer.getCurrentTimeMillis());
        mPresenter.sendRequest(Config.SYNC, ytbPlayer.getCurrentTimeMillis());
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

        //Set Youtube Player
        mYoutubePlayerFragment = new YouTubePlayerSupportFragmentX();
        mYoutubePlayerFragment.initialize(Config.YOUTUBE_API_KEY, this);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_youtube_player, mYoutubePlayerFragment);
        fragmentTransaction.commit();

        //Set Watchers
        if (mPresenter.getMode().equals(Config.ADVERTISE))
            setWatchers(mPresenter.getWatchers());
    }

    @Override
    public void onStopFragment() {

    }

    /**************************************** Methods *********************************************/
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.cueVideo("whlvtU3Yi9M");
            ytbPlayer = youTubePlayer;

            youTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                @Override
                public void onPlaying() {
                    if (mPresenter.getMode().equals(Config.ADVERTISE))
                        mPresenter.sendRequest(Config.START, -1);
                }

                @Override
                public void onPaused() {
                    if (mPresenter.getMode().equals(Config.ADVERTISE))
                        mPresenter.sendRequest(Config.PAUSE, -1);
                }

                @Override
                public void onStopped() {

                }

                @Override
                public void onBuffering(boolean b) {
                    if (mPresenter.getMode().equals(Config.ADVERTISE))
                        mPresenter.sendRequest(Config.BUFFER, -1);
                }

                @Override
                public void onSeekTo(int i) {
                    if (mPresenter.getMode().equals(Config.ADVERTISE))
                        mPresenter.sendRequest(Config.SEEK, i);
                }
            });
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Log.d(TAG, "onInitializationFailure: Failed");
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this.getActivity(), 1).show();
        } else {
            Toast.makeText(this.getActivity(),
                    "YouTubePlayer.onInitializationFailure(): " + youTubeInitializationResult.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void setWatchers(int watchers) {
        Log.d(TAG, "setWatchers");
        this.watchers.setText("Watchers: " + watchers);
    }

    public void start(int time) {
        ytbPlayer.play();
    }

    public void pause(int time) {
        ytbPlayer.pause();
    }

    public void seek(int time) {
        ytbPlayer.seekToMillis(time);
        ytbPlayer.play();
    }

    public void sync(int time) {
        ytbPlayer.seekToMillis(time);
        ytbPlayer.pause();
    }
}
