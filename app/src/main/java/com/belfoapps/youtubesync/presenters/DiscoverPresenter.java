package com.belfoapps.youtubesync.presenters;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.belfoapps.youtubesync.contracts.DiscoverContract;
import com.belfoapps.youtubesync.utils.Config;
import com.belfoapps.youtubesync.utils.ReceiveBytesPayloadListener;
import com.belfoapps.youtubesync.views.fragments.DiscoverFragment;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Strategy;

public class DiscoverPresenter implements DiscoverContract.Presenter {
    private static final String TAG = "DiscoverPresenter";
    /***************************************** Declarations ***************************************/
    private DiscoverFragment mView;
    private ConnectionLifecycleCallback connectionLifecycleCallback;
    private EndpointDiscoveryCallback endpointDiscoveryCallback;
    private ReceiveBytesPayloadListener payLoadCallback;
    /***************************************** Constructor ****************************************/
    public DiscoverPresenter(ReceiveBytesPayloadListener payLoadCallback) {
        this.payLoadCallback = payLoadCallback;
    }

    /***************************************** Essential Methods **********************************/
    @Override
    public void attach(DiscoverContract.View view) {
        mView = (DiscoverFragment) view;
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
        connectionLifecycleCallback = new ConnectionLifecycleCallback() {

            @Override
            public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                Log.d(TAG, "onConnectionInitiated");
                Nearby.getConnectionsClient(mView.getContext()).acceptConnection(endpointId, payLoadCallback);
            }

            @Override
            public void onConnectionResult(String endpointId, ConnectionResolution result) {
                switch (result.getStatus().getStatusCode()) {
                    case ConnectionsStatusCodes.STATUS_OK: {
                        Log.d(TAG, "onConnectionResult: Connection OK!");
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
    public void setupDiscoveryCallback() {
        endpointDiscoveryCallback =
                new EndpointDiscoveryCallback() {

                    @Override
                    public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
                        Nearby.getConnectionsClient(mView.getContext())
                                .requestConnection(Build.MANUFACTURER + "-" + Build.MODEL, endpointId, connectionLifecycleCallback)
                                .addOnSuccessListener(
                                        (Void unused) -> {
                                            Log.d(TAG, "onEndpointFound: Requested Connection successfully");
                                        })
                                .addOnFailureListener(
                                        (Exception e) -> {
                                            Log.d(TAG, "onEndpointFound: Couldnt request connection");
                                        });
                    }

                    @Override
                    public void onEndpointLost(String endpointId) {
                        Log.d(TAG, "onEndpointLost");
                    }
                };
    }

    @Override
    public void startDiscovery() {
        DiscoveryOptions discoveryOptions =
                new DiscoveryOptions.Builder().setStrategy(Strategy.P2P_STAR).build();

        Nearby.getConnectionsClient(mView.getContext())
                .startDiscovery(Config.SERVICE_ID, endpointDiscoveryCallback, discoveryOptions)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(mView.getContext(), "Discovery Succeeded", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(mView.getContext(), "Discovery Failed", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void stopDiscovery() {
    }
}
