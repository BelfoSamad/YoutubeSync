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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DiscoverFragment extends Fragment implements FragmentLifeCycle {
    private static final String TAG = "DiscoverFragment";
    private static final int COL_NUM = 1;
    /**************************************** Declarations ****************************************/
    private MainActivity mView;
    private MainPresenter mPresenter;
    private DevicesAdapter mAdapter;

    /***************************************** Constructor ****************************************/
    public DiscoverFragment() {
        // Required empty public constructor
    }

    public DiscoverFragment(MainActivity mView, MainPresenter mPresenter) {
        this.mView = mView;
        this.mPresenter = mPresenter;
    }

    /**************************************** View Declarations ***********************************/
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.watch)
    void goToWatch() {
        mView.nextStep(Config.WATCH_STEP);
    }

    /**************************************** Essential Methods ***********************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        //Set ButterKnife
        ButterKnife.bind(this, view);

        //init Recycler View
        initRecyclerView();

        return view;
    }

    @Override
    public void onStartFragment() {
        mPresenter.startDiscovering();
    }

    @Override
    public void onStopFragment() {
        mPresenter.startDiscovering();
    }

    /**************************************** Methods *********************************************/
    public void initRecyclerView() {
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(COL_NUM, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new DevicesAdapter(false, mPresenter, new ArrayList<Device>());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void updateRecyclerView(ArrayList<Device> connections) {
        if (mAdapter != null) {
            //Deleting the List of the Categories
            mAdapter.clearAll();

            // Adding The New List of Categories
            mAdapter.addAll(connections);
        }
    }
}
