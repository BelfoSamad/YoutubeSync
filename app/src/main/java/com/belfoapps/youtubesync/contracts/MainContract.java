package com.belfoapps.youtubesync.contracts;

import com.belfoapps.youtubesync.base.BasePresenter;
import com.belfoapps.youtubesync.base.BaseView;
import com.belfoapps.youtubesync.pojo.Device;

import java.util.ArrayList;

public interface MainContract {

    interface Presenter extends BasePresenter<View> {

        void setMode(String mode);

        void setWatchers(int count);

        void setYoutubeVideoUrl(String url);

        void startDiscovering();

        void startAdvertising();

        void stopDiscovering();

        void stopAdvertising();

        void requestConnection(int position);

        void acceptConnection(int position);

        void disconnectAll();

        void sendYoutubeUrl();

        void sendWatchersCount();

        String getYoutubeVideoUrl();

        String getMode();

        int getWatchers();

        void sendRequest(String type, int time);

        void getRequest(String type, int time);

        ArrayList<Device> getDevicesCopy(ArrayList<Device> devices);
    }

    interface View extends BaseView {

        void initViewPager();

        void updateAdvertiseRecyclerView(ArrayList<Device> connections);

        void updateDiscoveryRecyclerView(ArrayList<Device> connections);

        void nextStep(int position);

        void prevStep(int position);

        void startYoutubeVideo(int time);

        void pauseYoutubeVideo(int time);

        void seekToYoutubeVideo(int time);

        void syncYoutubeVideo(int time);
    }

}
