package com.belfoapps.youtubesync.utils;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;

public class ReceiveBytesPayloadListener extends PayloadCallback {
    private static final String TAG = "ReceiveBytesPayloadList";
    @Override
    public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
        byte[] receivedBytes = payload.asBytes();
        Log.d(TAG, "onPayloadReceived: Message Received");
    }

    @Override
    public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {

    }
}
