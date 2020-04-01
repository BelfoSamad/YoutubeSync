package com.belfoapps.youtubesync.di.components;

import android.content.Context;

import com.belfoapps.youtubesync.base.BaseApplication;
import com.belfoapps.youtubesync.di.annotations.ApplicationContext;
import com.belfoapps.youtubesync.di.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(BaseApplication baseApplication);

    //Context
    @ApplicationContext
    Context getApplicationContext();
}
