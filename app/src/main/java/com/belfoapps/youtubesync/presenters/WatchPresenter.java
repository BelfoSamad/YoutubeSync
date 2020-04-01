package com.belfoapps.youtubesync.presenters;

import com.belfoapps.youtubesync.contracts.WatchContract;
import com.belfoapps.youtubesync.views.fragments.WatchFragment;

public class WatchPresenter implements WatchContract.Presenter {
    private static final String TAG = "WatchPresenter";
    /***************************************** Declarations ***************************************/
    private WatchFragment mView;
    /***************************************** Constructor ****************************************/
    /***************************************** Essential Methods **********************************/
    @Override
    public void attach(com.belfoapps.youtubesync.contracts.WatchContract.View view) {
        mView = (WatchFragment) view;
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
