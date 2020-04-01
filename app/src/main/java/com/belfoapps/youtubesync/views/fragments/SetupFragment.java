package com.belfoapps.youtubesync.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.belfoapps.youtubesync.R;
import com.belfoapps.youtubesync.contracts.SetupContract;
import com.belfoapps.youtubesync.di.components.DaggerMVPComponent;
import com.belfoapps.youtubesync.di.components.MVPComponent;
import com.belfoapps.youtubesync.di.modules.ApplicationModule;
import com.belfoapps.youtubesync.di.modules.MVPModule;
import com.belfoapps.youtubesync.presenters.SetupPresenter;
import com.belfoapps.youtubesync.utils.Config;
import com.belfoapps.youtubesync.views.activities.MainActivity;
import com.belfoapps.youtubesync.views.ui.custom.FragmentLifeCycle;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetupFragment extends Fragment implements SetupContract.View, FragmentLifeCycle {
    private static final String TAG = "SetupFragment";
    /**************************************** Declarations ****************************************/
    private MVPComponent mvpComponent;
    private MainActivity mView;
    @Inject
    SetupPresenter mPresenter;

    /***************************************** Constructor ****************************************/
    public SetupFragment() {
        // Required empty public constructor
    }

    public SetupFragment(MainActivity mView) {
        this.mView = mView;
    }
    /**************************************** View Declarations ***********************************/
    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.discover)
    public void discover() {
        mView.nextStep(Config.DISCOVER_STEP);
    }

    @OnClick(R.id.advertise)
    public void advertise() {
        mView.nextStep(Config.ADVERTISE_STEP);
    }

    /**************************************** Essential Methods ***********************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup, container, false);

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

    }

    @Override
    public void onStopFragment() {

    }

    /**************************************** Methods *********************************************/
}
