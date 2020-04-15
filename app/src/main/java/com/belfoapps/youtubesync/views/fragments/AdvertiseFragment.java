package com.belfoapps.youtubesync.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.belfoapps.youtubesync.R;
import com.belfoapps.youtubesync.pojo.Device;
import com.belfoapps.youtubesync.presenters.MainPresenter;
import com.belfoapps.youtubesync.utils.Config;
import com.belfoapps.youtubesync.views.activities.MainActivity;
import com.belfoapps.youtubesync.views.ui.adapters.DevicesAdapter;
import com.belfoapps.youtubesync.views.ui.custom.FragmentLifeCycle;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdvertiseFragment extends Fragment implements FragmentLifeCycle {
    private static final String TAG = "AdvertiseFragment";
    private static final int COL_NUM = 1;
    /**************************************** Declarations ****************************************/
    private MainActivity mView;
    private MainPresenter mPresenter;
    private DevicesAdapter mAdapter;

    /***************************************** Constructor ****************************************/
    public AdvertiseFragment() {
        // Required empty public constructor
    }

    public AdvertiseFragment(MainActivity mView, MainPresenter mPresenter) {
        this.mView = mView;
        this.mPresenter = mPresenter;
    }

    /**************************************** View Declarations ***********************************/
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ripple_background)
    RippleBackground mRipple;

    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.watch)
    void goToWatch() {
        mPresenter.sendYoutubeUrl();
        mPresenter.sendWatchersCount();
        mView.nextStep(Config.WATCH_STEP);
    }

    @OnClick(R.id.back)
    public void back() {
        mView.onBackPressed();
    }

    /**************************************** Essential Methods ***********************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_advertise, container, false);

        //Set ButterKnife
        ButterKnife.bind(this, view);

        //Init Recycler View
        initRecyclerView();

        return view;
    }

    @Override
    public void onStartFragment() {
        mPresenter.startAdvertising();
        mRipple.startRippleAnimation();
    }

    @Override
    public void onStopFragment() {
        mRipple.stopRippleAnimation();
    }

    /**************************************** Methods *********************************************/
    public void initRecyclerView() {
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(COL_NUM, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new DevicesAdapter(true, mPresenter, new ArrayList<Device>());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void updateRecyclerView(ArrayList<Device> devices) {
        if (mAdapter != null) {
            //Deleting the List of the Categories
            mAdapter.clearAll();

            // Adding The New List of Categories
            mAdapter.addAll(mPresenter.getDevicesCopy(devices));
        }
    }
}
