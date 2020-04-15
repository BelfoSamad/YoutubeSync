package com.belfoapps.youtubesync.views.fragments;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.belfoapps.youtubesync.R;
import com.belfoapps.youtubesync.presenters.SplashPresenter;
import com.belfoapps.youtubesync.views.activities.SplashActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PermissionFragment extends Fragment {
    private static final String TAG = "PermissionFragment";
    /**************************************** Declarations ****************************************/
    private SplashActivity mView;
    private SplashPresenter mPresenter;

    /***************************************** Constructor ****************************************/
    public PermissionFragment() {
        // Required empty public constructor
    }

    public PermissionFragment(SplashActivity mView, SplashPresenter mPresenter) {
        this.mView = mView;
        this.mPresenter = mPresenter;
    }

    /**************************************** View Declarations ***********************************/
    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.permission)
    public void demandPermission() {
        //Request Storage Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            mPresenter.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, 1);
        else mPresenter.requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION, 1);
    }

    /**************************************** Essential Methods ***********************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_permission, container, false);

        ButterKnife.bind(this, view);

        return view;
    }
}
