package com.belfoapps.youtubesync.contracts;

import com.belfoapps.youtubesync.base.BasePresenter;
import com.belfoapps.youtubesync.base.BaseView;

public interface SplashContract {

    interface Presenter extends BasePresenter<View> {

        void setName(String name);

        void requestPermission(String writeExternalStorage, int i);

        void setFirstTime(boolean b);

        boolean isFirstTime();
    }

    interface View extends BaseView {

        void initViewPager();

        void nextStep(int position);

        void prevStep(int position);

        void goToMain();
    }

}
