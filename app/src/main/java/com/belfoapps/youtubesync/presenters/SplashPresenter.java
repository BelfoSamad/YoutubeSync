package com.belfoapps.youtubesync.presenters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import com.belfoapps.youtubesync.contracts.SplashContract;
import com.belfoapps.youtubesync.di.annotations.ApplicationContext;
import com.belfoapps.youtubesync.models.SharedPreferencesHelper;
import com.belfoapps.youtubesync.views.activities.SplashActivity;

public class SplashPresenter implements SplashContract.Presenter {
    private static final String TAG = "SplashPresenter";
    /***************************************** Declarations ***************************************/
    private SplashActivity mView;
    private Context context;
    private SharedPreferencesHelper mSharedPrefs;

    /***************************************** Constructor ****************************************/
    public SplashPresenter(@ApplicationContext Context context, SharedPreferencesHelper mSharedPrefs) {
        this.context = context;
        this.mSharedPrefs = mSharedPrefs;
    }

    /***************************************** Essential Methods **********************************/
    @Override
    public void attach(SplashContract.View view) {
        mView = (SplashActivity) view;
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
    @Override
    public void setName(String name) {
        mSharedPrefs.saveName(name);
    }

    @Override
    public void requestPermission(String permission, int permissionRequest) {
        if (shouldAskPermission(mView, permission))
            if (mSharedPrefs.isFirstTimeAskingPermission(permission)) {
                mSharedPrefs.firstTimeAskingPermission(permission);
                ActivityCompat.requestPermissions(
                        mView,
                        new String[]{permission},
                        permissionRequest
                );
            } else mView.goToMain();
    }

    private boolean shouldAskPermission() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    private boolean shouldAskPermission(Context context, String permission) {
        if (shouldAskPermission()) {
            int permissionResult = ActivityCompat.checkSelfPermission(context, permission);
            return permissionResult != PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    @Override
    public void setFirstTime(boolean first) {
        mSharedPrefs.setFirstTime(first);
    }

    @Override
    public boolean isFirstTime() {
        return mSharedPrefs.isFirstTime();
    }
}
