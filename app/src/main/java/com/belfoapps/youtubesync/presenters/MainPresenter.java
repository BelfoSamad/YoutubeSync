package com.belfoapps.youtubesync.presenters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.belfoapps.youtubesync.contracts.MainContract;
import com.belfoapps.youtubesync.di.annotations.ActivityContext;
import com.belfoapps.youtubesync.models.SharedPreferencesHelper;
import com.belfoapps.youtubesync.pojo.Device;
import com.belfoapps.youtubesync.utils.Config;
import com.belfoapps.youtubesync.utils.ReceiveBytesPayloadListener;
import com.belfoapps.youtubesync.views.activities.MainActivity;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.Strategy;

import java.util.ArrayList;

public class MainPresenter implements MainContract.Presenter {
    private static final String TAG = "MainPresenter";
    /***************************************** Declarations ***************************************/
    private MainActivity mView;
    private SharedPreferencesHelper mSharedPrefs;
    private ReceiveBytesPayloadListener payLoadCallback;
    private ConnectionLifecycleCallback advertiserConnectionCallback;
    private ConnectionLifecycleCallback discovererConnectionCallback;
    private EndpointDiscoveryCallback discoveryEndpointCallback;
    private ArrayList<Device> advertisers;
    private ArrayList<Device> discoverers;
    private Context context;

    /***************************************** Constructor ****************************************/
    public MainPresenter(@ActivityContext Context context, ReceiveBytesPayloadListener payLoadCallback, SharedPreferencesHelper mSharedPrefs) {

        this.mSharedPrefs = mSharedPrefs;
        this.context = context;
        this.payLoadCallback = payLoadCallback;

        advertiserConnectionCallback = new ConnectionLifecycleCallback() {
            @Override
            public void onConnectionInitiated(@NonNull String endpointId, @NonNull ConnectionInfo connectionInfo) {
                if (discoverers == null)
                    discoverers = new ArrayList<>();

                Log.d(TAG, "onConnectionInitiated: " + endpointId);

                Log.d(TAG, "onConnectionInitiated: ");
                discoverers.add(new Device(connectionInfo.getEndpointName(), endpointId));
                Log.d(TAG, "onConnectionInitiated: " + discoverers.size());

                mView.updateAdvertiseRecyclerView(discoverers);
            }

            @Override
            public void onConnectionResult(@NonNull String endpointId, @NonNull ConnectionResolution result) {
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
            public void onDisconnected(@NonNull String endpointId) {
                Log.d(TAG, "onDisconnected: Disconnected from EndPoint");
            }
        };

        discovererConnectionCallback = new ConnectionLifecycleCallback() {
            @Override
            public void onConnectionInitiated(@NonNull String endpointId, @NonNull ConnectionInfo connectionInfo) {

            }

            @Override
            public void onConnectionResult(@NonNull String endpointId, @NonNull ConnectionResolution result) {
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
            public void onDisconnected(@NonNull String endpointId) {
                Log.d(TAG, "onDisconnected: Disconnected from EndPoint");
            }
        };

        discoveryEndpointCallback = new EndpointDiscoveryCallback() {
            @Override
            public void onEndpointFound(@NonNull String endpointId, @NonNull DiscoveredEndpointInfo info) {
                if (advertisers == null)
                    advertisers = new ArrayList<>();

                Toast.makeText(context, endpointId, Toast.LENGTH_SHORT).show();

                advertisers.add(new Device(info.getEndpointName(), endpointId));

                mView.updateDiscoveryRecyclerView(advertisers);
            }

            @Override
            public void onEndpointLost(@NonNull String endpointId) {
                Log.d(TAG, "onEndpointLost");
            }
        };
    }

    /***************************************** Essential Methods **********************************/
    @Override
    public void attach(MainContract.View view) {
        mView = (MainActivity) view;
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
    public void startDiscovering() {
        DiscoveryOptions discoveryOptions =
                new DiscoveryOptions.Builder().setStrategy(Strategy.P2P_STAR).build();

        Nearby.getConnectionsClient(context)
                .startDiscovery(Config.SERVICE_ID, discoveryEndpointCallback, discoveryOptions)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Discovery Succeeded", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Discovery Failed", Toast.LENGTH_SHORT).show();
                });

    }

    @Override
    public void startAdvertising() {
        AdvertisingOptions advertisingOptions =
                new AdvertisingOptions.Builder().setStrategy(Strategy.P2P_STAR).build();

        Nearby.getConnectionsClient(context)
                .startAdvertising(
                        Build.MANUFACTURER + "-" + Build.MODEL, Config.SERVICE_ID, advertiserConnectionCallback, advertisingOptions)
                .addOnSuccessListener(
                        (Void unused) -> {
                            Toast.makeText(context, "Advertising Succeed", Toast.LENGTH_SHORT).show();
                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            Toast.makeText(context, "Advertising Failed", Toast.LENGTH_SHORT).show();
                        });
    }

    @Override
    public void stopDiscovering() {
        Nearby.getConnectionsClient(context).stopDiscovery();
    }

    @Override
    public void stopAdvertising() {
        Nearby.getConnectionsClient(context).stopAdvertising();
    }

    @Override
    public void requestConnection(int position) {
        Nearby.getConnectionsClient(context)
                .requestConnection(Config.DEVICE_NAME, advertisers.get(position).getEndPoint(), discovererConnectionCallback)
                .addOnSuccessListener(
                        (Void unused) -> {
                            Log.d(TAG, "onEndpointFound: Requested Connection successfully");
                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            Log.d(TAG, "onEndpointFound: Couldn't request connection");
                        });
    }

    @Override
    public void acceptConnection(int position) {
        Log.d(TAG, "acceptConnection: " + discoverers.get(position).getEndPoint());
        Nearby.getConnectionsClient(context).acceptConnection(discoverers.get(position).getEndPoint(), payLoadCallback);
    }

    @Override
    public void sendMessage() {
        Log.d(TAG, "sendMessage: Sending a Message");
        for (Device discoverer :
                discoverers) {
            Payload bytesPayload = Payload.fromBytes(new byte[]{0xa, 0xb, 0xc, 0xd});
            Nearby.getConnectionsClient(context).sendPayload(discoverer.getEndPoint(), bytesPayload);
        }
    }

}
