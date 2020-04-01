package com.belfoapps.youtubesync.contracts;

import com.belfoapps.youtubesync.base.BasePresenter;
import com.belfoapps.youtubesync.base.BaseView;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;

public interface DiscoverContract {

    interface Presenter extends BasePresenter<View> {

        void setupConnectionCallback();

        void setupDiscoveryCallback();

        void startDiscovery();

        void stopDiscovery();

    }

    interface View extends BaseView {


    }

}
