package com.belfoapps.youtubesync.contracts;

import com.belfoapps.youtubesync.base.BasePresenter;
import com.belfoapps.youtubesync.base.BaseView;
import com.belfoapps.youtubesync.pojo.Device;

import java.util.ArrayList;

public interface MainContract {

    interface Presenter extends BasePresenter<View> {

        void attachWatchView(WatchContract.View view);

        void setMode(String mode);

        void setYoutubeVideoUrl(String url);

        void startDiscovering();

        void startAdvertising();

        void stopDiscovering();

        void stopAdvertising();

        void requestConnection(int position);

        void acceptConnection(int position);

        void sendYoutubeUrl();

        String getYoutubeVideoUrl();

        void sendRequest(String type, int time);

        void getRequest(String type, int time);

        ArrayList<String> getDiscoverers();

    }

    interface View extends BaseView {

        void initViewPager();

        void updateAdvertiseRecyclerView(ArrayList<Device> connections);

        void updateDiscoveryRecyclerView(ArrayList<Device> connections);

        void nextStep(int position);

        void goToWatchActivity(String url, ArrayList<String> discoverers);

    }

}
