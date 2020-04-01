package com.belfoapps.youtubesync.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.belfoapps.youtubesync.R;
import com.belfoapps.youtubesync.contracts.DiscoverContract;
import com.belfoapps.youtubesync.di.components.DaggerMVPComponent;
import com.belfoapps.youtubesync.di.components.MVPComponent;
import com.belfoapps.youtubesync.di.modules.ApplicationModule;
import com.belfoapps.youtubesync.di.modules.MVPModule;
import com.belfoapps.youtubesync.presenters.DiscoverPresenter;
import com.belfoapps.youtubesync.views.activities.MainActivity;
import com.belfoapps.youtubesync.views.ui.custom.FragmentLifeCycle;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class DiscoverFragment extends Fragment implements DiscoverContract.View, FragmentLifeCycle {
    private static final String TAG = "DiscoverFragment";
    private static final String SERVICE_ID = "YoutubeSync";
    /**************************************** Declarations ****************************************/
    private MVPComponent mvpComponent;
    private MainActivity mView;
    @Inject
    DiscoverPresenter mPresenter;

    /***************************************** Constructor ****************************************/
    public DiscoverFragment() {
        // Required empty public constructor
    }

    public DiscoverFragment(MainActivity mView) {
        this.mView = mView;
    }

    /**************************************** View Declarations ***********************************/
    /**************************************** Click Listeners *************************************/

    /**************************************** Essential Methods ***********************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        //Initialize Dagger For Application
        mvpComponent = getComponent();
        //Inject the Component Here
        mvpComponent.inject(this);

        //Set ButterKnife
        ButterKnife.bind(this, view);

        //Attach View To Presenter
        mPresenter.attach(this);

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
        mPresenter.setupConnectionCallback();
        mPresenter.setupDiscoveryCallback();
        mPresenter.startDiscovery();
    }

    @Override
    public void onStopFragment() {

    }

    /**************************************** Methods *********************************************/
}
