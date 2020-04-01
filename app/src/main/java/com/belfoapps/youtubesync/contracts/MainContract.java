package com.belfoapps.youtubesync.contracts;

import com.belfoapps.youtubesync.base.BasePresenter;
import com.belfoapps.youtubesync.base.BaseView;
import com.belfoapps.youtubesync.pojo.Device;

import java.util.ArrayList;

public interface MainContract {

    interface Presenter extends BasePresenter<View> {

        void startDiscovering();

        void startAdvertising();

        void stopDiscovering();

        void stopAdvertising();

        void requestConnection(int position);

        void acceptConnection(int position);

        void sendMessage();

    }

    interface View extends BaseView {

        void initViewPager();

        void updateAdvertiseRecyclerView(ArrayList<Device> connections);

        void updateDiscoveryRecyclerView(ArrayList<Device> connections);

        void nextStep(int position);

    }

}
