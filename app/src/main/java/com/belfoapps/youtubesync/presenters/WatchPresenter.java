package com.belfoapps.youtubesync.presenters;

import android.util.Log;

import com.belfoapps.youtubesync.contracts.WatchContract;
import com.belfoapps.youtubesync.utils.Config;
import com.belfoapps.youtubesync.views.activities.WatchActivity;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.Payload;

import java.util.ArrayList;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class WatchPresenter implements WatchContract.Presenter {
    private static final String TAG = "WatchPresenter";
    /***************************************** Declarations ***************************************/
    private WatchActivity mView;
    private ArrayList<String> discoverers;

    /***************************************** Constructor ****************************************/
    public WatchPresenter() {
    }

    /***************************************** Essential Methods **********************************/
    @Override
    public void attach(WatchContract.View view) {
        mView = (WatchActivity) view;

    }

    @Override
    public void dettach() {
        mView = null;
    }

    @Override
    public boolean isAttached() {
        return !(mView == null);
    }

    /***************************************** Methods ********************************************/
    @Override
    public void sendRequest(String type, int time) {
        String msg = type + ":" + time;
        Log.d(TAG, "sendRequest: " + msg);
        for (String discoverer :
                discoverers) {
            Payload bytesPayload = Payload.fromBytes(msg.getBytes());
            Nearby.getConnectionsClient(mView).sendPayload(discoverer, bytesPayload);
        }
    }

    @Override
    public void getRequest(String type, int time) {
        switch (type) {
            case Config.START:
                mView.startYoutubeVideo(time);
                break;
            case Config.PAUSE:
                mView.pauseYoutubeVideo(time);
                break;
            case Config.SEEK:
                mView.seekToYoutubeVideo(time);
                break;
        }
    }

    @Override
    public void setDiscoverers(ArrayList<String> discoverers) {
        this.discoverers = discoverers;
    }
}
