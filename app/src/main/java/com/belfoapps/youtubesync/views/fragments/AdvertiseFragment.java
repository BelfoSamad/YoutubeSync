package com.belfoapps.youtubesync.views.fragments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.belfoapps.youtubesync.R;
import com.belfoapps.youtubesync.contracts.AdvertiseContract;
import com.belfoapps.youtubesync.di.components.DaggerMVPComponent;
import com.belfoapps.youtubesync.di.components.MVPComponent;
import com.belfoapps.youtubesync.di.modules.ApplicationModule;
import com.belfoapps.youtubesync.di.modules.MVPModule;
import com.belfoapps.youtubesync.pojo.Device;
import com.belfoapps.youtubesync.presenters.AdvertisePresenter;
import com.belfoapps.youtubesync.utils.Config;
import com.belfoapps.youtubesync.utils.ReceiveBytesPayloadListener;
import com.belfoapps.youtubesync.views.activities.MainActivity;
import com.belfoapps.youtubesync.views.ui.adapters.DevicesAdapter;
import com.belfoapps.youtubesync.views.ui.custom.FragmentLifeCycle;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.Strategy;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdvertiseFragment extends Fragment implements AdvertiseContract.View, FragmentLifeCycle {
    private static final String TAG = "AdvertiseFragment";
    private static final String SERVICE_ID = "YoutubeSync";
    private static final int COL_NUM = 1;
    /**************************************** Declarations ****************************************/
    private MVPComponent mvpComponent;
    private MainActivity mView;
    @Inject
    AdvertisePresenter mPresenter;
    String receiver;
    private DevicesAdapter mAdapter;

    /***************************************** Constructor ****************************************/
    public AdvertiseFragment() {
        // Required empty public constructor
    }

    public AdvertiseFragment(MainActivity mView) {
        this.mView = mView;
    }

    /**************************************** View Declarations ***********************************/
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.watch)
    void sendMessage() {
        mView.nextStep(Config.WATCH_STEP);
    }

    /**************************************** Essential Methods ***********************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_advertise, container, false);

        //Initialize Dagger For Application
        mvpComponent = getComponent();
        //Inject the Component Here
        mvpComponent.inject(this);

        //Set ButterKnife
        ButterKnife.bind(this, view);

        //Attach View To Presenter
        mPresenter.attach(this);

        //Init Recycler View
        initRecyclerView();

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
        Log.d(TAG, "onStartFragment");
        mPresenter.setupConnectionCallback();
        mPresenter.startAdvertising();
    }

    @Override
    public void onStopFragment() {

    }

    /**************************************** Methods *********************************************/
    @Override
    public void initRecyclerView() {
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(COL_NUM, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new DevicesAdapter(new ArrayList<Device>());

        mRecyclerView.setLayoutManager(mLayoutManager);
        //mRecyclerView.addItemDecoration(new RecipesItemDecoration());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void updateRecyclerView(ArrayList<Device> devices) {
        if (mAdapter != null) {
            //Deleting the List of the Categories
            mAdapter.clearAll();

            // Adding The New List of Categories
            mAdapter.addAll(devices);
        }
    }
}
