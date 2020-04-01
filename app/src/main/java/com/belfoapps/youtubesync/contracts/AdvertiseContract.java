package com.belfoapps.youtubesync.contracts;

import com.belfoapps.youtubesync.base.BasePresenter;
import com.belfoapps.youtubesync.base.BaseView;
import com.belfoapps.youtubesync.pojo.Device;

import java.util.ArrayList;

public interface AdvertiseContract {

    interface Presenter extends BasePresenter<View> {

        void setupConnectionCallback();

        void startAdvertising();

        void stopAdvertising();

    }

    interface View extends BaseView {

        void initRecyclerView();

        void updateRecyclerView(ArrayList<Device> devices);

    }

}
