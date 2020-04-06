package com.belfoapps.youtubesync.utils;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.belfoapps.youtubesync.presenters.MainPresenter;
import com.belfoapps.youtubesync.presenters.WatchPresenter;
import com.belfoapps.youtubesync.views.activities.MainActivity;
import com.belfoapps.youtubesync.views.activities.WatchActivity;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;

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

        if (type.equals(Config.URL)){
            Intent intent = new Intent(mView, WatchActivity.class);
            intent.putExtra("url", value);
            mView.startActivity(intent);
        }else {
            mPresenter.getRequest(type, Integer.parseInt(value));
        }
    }

    @Override
    public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {

    }
}
