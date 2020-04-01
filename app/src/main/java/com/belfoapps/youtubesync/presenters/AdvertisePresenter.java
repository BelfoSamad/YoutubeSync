package com.belfoapps.youtubesync.presenters;

import com.belfoapps.youtubesync.contracts.AdvertiseContract;
import com.belfoapps.youtubesync.views.fragments.AdvertiseFragment;

public class AdvertisePresenter implements AdvertiseContract.Presenter {
    private static final String TAG = "AdvertisePresenter";
    /***************************************** Declarations ***************************************/
    private AdvertiseFragment mView;
    /***************************************** Constructor ****************************************/
    /***************************************** Essential Methods **********************************/
    @Override
    public void attach(AdvertiseContract.View view) {
        mView = (AdvertiseFragment) view;
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
