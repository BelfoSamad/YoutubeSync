package com.belfoapps.youtubesync.presenters;

import com.belfoapps.youtubesync.contracts.DiscoverContract;
import com.belfoapps.youtubesync.views.fragments.DiscoverFragment;

public class DiscoverPresenter implements DiscoverContract.Presenter {
    private static final String TAG = "DiscoverPresenter";
    /***************************************** Declarations ***************************************/
    private DiscoverFragment mView;
    /***************************************** Constructor ****************************************/
    /***************************************** Essential Methods **********************************/
    @Override
    public void attach(DiscoverContract.View view) {
        mView = (DiscoverFragment) view;
    }

    @Override
    public void dettach() {
        mView = null;
    }

    @Override
    public boolean isAttached() {
        return !(mView == null);
    }

    /***************************************** Methods ********************************************/
}
