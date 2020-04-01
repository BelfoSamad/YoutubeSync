package com.belfoapps.youtubesync.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.belfoapps.youtubesync.R;
import com.belfoapps.youtubesync.di.components.DaggerMVPComponent;
import com.belfoapps.youtubesync.di.components.MVPComponent;
import com.belfoapps.youtubesync.di.modules.ApplicationModule;
import com.belfoapps.youtubesync.di.modules.MVPModule;
import com.belfoapps.youtubesync.presenters.MainPresenter;
import com.belfoapps.youtubesync.views.ui.custom.FragmentLifeCycle;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WatchFragment extends Fragment implements FragmentLifeCycle {
    private static final String TAG = "WatchFragment";
    /**************************************** Declarations ****************************************/
    private MainPresenter mPresenter;
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
    public void send(){
        mPresenter.sendMessage();
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

    }

    @Override
    public void onStopFragment() {

    }

    /**************************************** Methods *********************************************/
}
