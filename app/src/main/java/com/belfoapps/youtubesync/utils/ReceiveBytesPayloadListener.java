package com.belfoapps.youtubesync.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.belfoapps.youtubesync.presenters.MainPresenter;
import com.belfoapps.youtubesync.views.activities.MainActivity;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;

public class ReceiveBytesPayloadListener extends PayloadCallback {
    private static final String TAG = "ReceiveBytesPayloadList";
    private MainPresenter mPresenter;
    private MainActivity mView;

    public void setupView(MainActivity mView) {
        this.mView = mView;
    }

    public void setPresenter(MainPresenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
        byte[] receivedBytes = payload.asBytes();
        String msg = new String(receivedBytes);

        Log.d(TAG, "onPayloadReceived: " + msg);
        String type = msg.split(":")[0];
        String value = msg.split(":")[1];

        if (type.equals(Config.URL)) {
            mPresenter.setYoutubeVideoUrl(value);
        } else if (type.equals(Config.WATCHERS)) {
            mPresenter.setWatchers(Integer.parseInt(value));
            mView.nextStep(Config.WATCH_STEP);
        } else mPresenter.getRequest(type, Integer.parseInt(value));
    }

    @Override
    public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {

    }
}
