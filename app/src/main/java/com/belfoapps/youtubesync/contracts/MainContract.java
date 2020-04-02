package com.belfoapps.youtubesync.contracts;

import com.belfoapps.youtubesync.base.BasePresenter;
import com.belfoapps.youtubesync.base.BaseView;
import com.belfoapps.youtubesync.pojo.Device;

import java.util.ArrayList;

public interface MainContract {

    interface Presenter extends BasePresenter<View> {

        void setMode(String mode);

        void setYoutubeVideoUrl(String url);

        void startDiscovering();

        void startAdvertising();

        void stopDiscovering();

        void stopAdvertising();

        void requestConnection(int position);

        void acceptConnection(int position);

        void sendYoutubeUrl();

        void sendRequest(String type, float time);

        String getYoutubeVideoUrl();

        String getMode();

    }

    interface View extends BaseView {

        void initViewPager();

        void updateAdvertiseRecyclerView(ArrayList<Device> connections);

        void updateDiscoveryRecyclerView(ArrayList<Device> connections);

        void nextStep(int position);

        void initYoutube(String url);

        void startYoutubeVideo(float timestamp);

        void pauseYoutubeVideo(float timestamp);

        void seekToYoutubeVideo(float seekTo);

    }

}
