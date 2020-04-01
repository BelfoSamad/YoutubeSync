package com.belfoapps.youtubesync.di.modules;

import android.app.Activity;
import android.content.Context;

import com.belfoapps.youtubesync.di.annotations.ActivityContext;
import com.belfoapps.youtubesync.models.SharedPreferencesHelper;
import com.belfoapps.youtubesync.presenters.AdvertisePresenter;
import com.belfoapps.youtubesync.presenters.DiscoverPresenter;
import com.belfoapps.youtubesync.presenters.MainPresenter;
import com.belfoapps.youtubesync.presenters.SetupPresenter;
import com.belfoapps.youtubesync.presenters.WatchPresenter;
import com.belfoapps.youtubesync.utils.ReceiveBytesPayloadListener;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

//import com.belfoapps.recipepro.utils.GDPR;

@Module
public class MVPModule {

    private Activity mActivity;

    //Constructor
    public MVPModule(Activity mActivity) {
        this.mActivity = mActivity;
    }

    //Context
    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @Singleton
    MainPresenter providesMainPresenter(SharedPreferencesHelper mSharedPrefs) {
        return new MainPresenter(mSharedPrefs);
    }

    @Provides
    @Singleton
    SetupPresenter providesSetupPresenter() {
        return new SetupPresenter();
    }

    @Provides
    @Singleton
    DiscoverPresenter providesDiscoverPresenter(ReceiveBytesPayloadListener payloadCallback) {
        return new DiscoverPresenter(payloadCallback);
    }

    @Provides
    @Singleton
    WatchPresenter providesWatchPresenter() {
        return new WatchPresenter();
    }

    @Provides
    @Singleton
    AdvertisePresenter providesAdvertisePresenter(ReceiveBytesPayloadListener payloadCallback) {
        return new AdvertisePresenter(payloadCallback);
    }
}
