package com.belfoapps.youtubesync.presenters;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.belfoapps.youtubesync.contracts.AdvertiseContract;
import com.belfoapps.youtubesync.pojo.Device;
import com.belfoapps.youtubesync.utils.Config;
import com.belfoapps.youtubesync.utils.ReceiveBytesPayloadListener;
import com.belfoapps.youtubesync.views.fragments.AdvertiseFragment;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.Strategy;

import java.util.ArrayList;

public class AdvertisePresenter implements AdvertiseContract.Presenter {
    private static final String TAG = "AdvertisePresenter";
    /***************************************** Declarations ***************************************/
    private AdvertiseFragment mView;
    private ConnectionLifecycleCallback connectionLifecycleCallback;
    private ReceiveBytesPayloadListener payLoadCallback;
    private ArrayList<Device> devices;

    /***************************************** Constructor ****************************************/
    public AdvertisePresenter(ReceiveBytesPayloadListener payLoadCallback) {
        this.payLoadCallback = payLoadCallback;
    }

    /***************************************** Essential Methods **********************************/
    @Override
    public void attach(AdvertiseContract.View view) {
        mView = (AdvertiseFragment) view;
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
    public void setupConnectionCallback() {
        connectionLifecycleCallback =
                new ConnectionLifecycleCallback() {

                    @Override
                    public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                        Nearby.getConnectionsClient(mView.getContext()).acceptConnection(endpointId, payLoadCallback);
                    }

                    @Override
                    public void onConnectionResult(String endpointId, ConnectionResolution result) {
                        switch (result.getStatus().getStatusCode()) {
                            case ConnectionsStatusCodes.STATUS_OK: {
                                //receiver = endpointId;
                                if (devices != null)
                                    devices = new ArrayList<>();

                                devices.add(new Device(endpointId));
                            }
                            break;
                            case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                                Log.d(TAG, "onConnectionResult: Connection Rejected");
                                break;
                            case ConnectionsStatusCodes.STATUS_ERROR:
                                Log.d(TAG, "onConnectionResult: Connection Error");
                                break;
                            default:
                                // Unknown status code
                        }
                    }

                    @Override
                    public void onDisconnected(String endpointId) {
                        Log.d(TAG, "onDisconnected: Disconnected from EndPoint");
                    }
                };
    }

    @Override
    public void startAdvertising() {
        AdvertisingOptions advertisingOptions =
                new AdvertisingOptions.Builder().setStrategy(Strategy.P2P_STAR).build();

        Nearby.getConnectionsClient(mView.getContext())
                .startAdvertising(
                        Build.MANUFACTURER + "-" + Build.MODEL, Config.SERVICE_ID, connectionLifecycleCallback, advertisingOptions)
                .addOnSuccessListener(
                        (Void unused) -> {
                            Toast.makeText(mView.getContext(), "Advertising Succeed", Toast.LENGTH_SHORT).show();
                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            Toast.makeText(mView.getContext(), "Advertising Failed", Toast.LENGTH_SHORT).show();
                        });
    }

    @Override
    public void stopAdvertising() {

    }
}
