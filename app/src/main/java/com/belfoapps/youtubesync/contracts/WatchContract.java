package com.belfoapps.youtubesync.contracts;

import com.belfoapps.youtubesync.base.BasePresenter;
import com.belfoapps.youtubesync.base.BaseView;

import java.util.ArrayList;

public interface WatchContract {

    interface Presenter extends BasePresenter<View> {

        void sendRequest(String type, int time);

        void getRequest(String type, int time);

        void setDiscoverers(ArrayList<String> discoverers);
    }

    interface View extends BaseView {

        void initYoutube(String url);

        void startYoutubeVideo(int timestamp);

        void pauseYoutubeVideo(int timestamp);

        void seekToYoutubeVideo(int seekTo);

    }

}
