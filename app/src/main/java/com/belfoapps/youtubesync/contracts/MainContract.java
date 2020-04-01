package com.belfoapps.youtubesync.contracts;

import com.belfoapps.youtubesync.base.BasePresenter;
import com.belfoapps.youtubesync.base.BaseView;

public interface MainContract {

    interface Presenter extends BasePresenter<View> {


    }

    interface View extends BaseView {

        void initViewPager();

        void nextStep(int position);

    }

}
