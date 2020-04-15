package com.belfoapps.youtubesync.views.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.belfoapps.youtubesync.R;
import com.belfoapps.youtubesync.presenters.SplashPresenter;
import com.belfoapps.youtubesync.utils.Config;
import com.belfoapps.youtubesync.views.activities.SplashActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartupFragment extends Fragment{
    private static final String TAG = "StartupFragment";
    /**************************************** Declarations ****************************************/
    private SplashActivity mView;
    private SplashPresenter mPresenter;

    /***************************************** Constructor ****************************************/
    public StartupFragment() {
        // Required empty public constructor
    }

    public StartupFragment(SplashActivity mView, SplashPresenter mPresenter) {
        this.mView = mView;
        this.mPresenter = mPresenter;
    }

    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.start)
    public void start(){
        mView.nextStep(Config.NAME_STEP);
    }

    /**************************************** Essential Methods ***********************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_startup, container, false);

        //Set ButterKnife
        ButterKnife.bind(this, view);

        return view;
    }
}
