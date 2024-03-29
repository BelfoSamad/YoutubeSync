package com.belfoapps.youtubesync.di.components;


import android.content.Context;

import com.belfoapps.youtubesync.di.annotations.ActivityContext;
import com.belfoapps.youtubesync.di.modules.ApplicationModule;
import com.belfoapps.youtubesync.di.modules.MVPModule;
import com.belfoapps.youtubesync.views.activities.MainActivity;
import com.belfoapps.youtubesync.views.activities.SplashActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, MVPModule.class})
public interface MVPComponent {

    //Inject in Activities
    void inject(MainActivity mainActivity);
    void inject(SplashActivity splashActivity);


    //Context
    @ActivityContext
    Context getActivityContext();
}
