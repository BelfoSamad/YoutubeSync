package com.belfoapps.youtubesync.presenters;

import com.belfoapps.youtubesync.contracts.MainContract;
import com.belfoapps.youtubesync.models.SharedPreferencesHelper;
import com.belfoapps.youtubesync.views.activities.MainActivity;

public class MainPresenter implements MainContract.Presenter {
    private static final String TAG = "MainPresenter";
    /***************************************** Declarations ***************************************/
    private MainActivity mView;
    private SharedPreferencesHelper mSharedPrefs;

    /***************************************** Constructor ****************************************/
    public MainPresenter(SharedPreferencesHelper mSharedPrefs) {
        this.mSharedPrefs = mSharedPrefs;
    }

    /***************************************** Essential Methods **********************************/
    @Override
    public void attach(MainContract.View view) {
        mView = (MainActivity) view;
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
