package com.belfoapps.youtubesync.presenters;

import com.belfoapps.youtubesync.contracts.SetupContract;
import com.belfoapps.youtubesync.views.fragments.SetupFragment;

public class SetupPresenter implements SetupContract.Presenter {
    private static final String TAG = "SetupPresenter";
    /***************************************** Declarations ***************************************/
    private SetupFragment mView;
    /***************************************** Constructor ****************************************/
    /***************************************** Essential Methods **********************************/
    @Override
    public void attach(SetupContract.View view) {
        mView = (SetupFragment) view;
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
