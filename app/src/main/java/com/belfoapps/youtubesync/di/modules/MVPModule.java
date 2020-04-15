package com.belfoapps.youtubesync.di.modules;

import android.app.Activity;
import android.content.Context;

import com.belfoapps.youtubesync.di.annotations.ActivityContext;
import com.belfoapps.youtubesync.models.SharedPreferencesHelper;
import com.belfoapps.youtubesync.presenters.MainPresenter;
import com.belfoapps.youtubesync.presenters.SplashPresenter;
import com.belfoapps.youtubesync.utils.ReceiveBytesPayloadListener;

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
    MainPresenter providesMainPresenter(@ActivityContext Context context, ReceiveBytesPayloadListener payloadCallback,
                                        SharedPreferencesHelper mSharedPrefs) {
        return new MainPresenter(context, payloadCallback, mSharedPrefs);
    }

    @Provides
    @Singleton
    SplashPresenter providesSplashPresenter(@ActivityContext Context context, SharedPreferencesHelper mSharedPrefs) {
        return new SplashPresenter(context, mSharedPrefs);
    }
}
