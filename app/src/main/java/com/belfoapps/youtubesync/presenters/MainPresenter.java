package com.belfoapps.youtubesync.presenters;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
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
    private static final String TAG = "MainPresenterX";
    /***************************************** Declarations ***************************************/
    private MainActivity mView;
    private SharedPreferencesHelper mSharedPrefs;
    private String youtube_video_url;
    private String mode;
    private ReceiveBytesPayloadListener payLoadCallback;
    private ConnectionLifecycleCallback advertiserConnectionCallback;
    private ConnectionLifecycleCallback discovererConnectionCallback;
    private EndpointDiscoveryCallback discoveryEndpointCallback;
    private ArrayList<Device> advertisers;
    private ArrayList<Device> discoverers;
    private Context context;
    private int watchers;

    /***************************************** Constructor ****************************************/
    public MainPresenter(@ActivityContext Context context, ReceiveBytesPayloadListener payLoadCallback, SharedPreferencesHelper mSharedPrefs) {
        this.mSharedPrefs = mSharedPrefs;
        this.context = context;
        this.payLoadCallback = payLoadCallback;

        advertiserConnectionCallback = new ConnectionLifecycleCallback() {
            @Override
            public void onConnectionInitiated(@NonNull String endpointId, @NonNull ConnectionInfo connectionInfo) {
                Log.d(TAG, "onConnectionInitiated With: " + endpointId);
                if (discoverers == null)
                    discoverers = new ArrayList<>();

                discoverers.add(new Device(connectionInfo.getEndpointName(), endpointId));

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

                discoverers.remove(new Device("", endpointId));
                mView.updateDiscoveryRecyclerView(discoverers);
            }
        };

        discovererConnectionCallback = new ConnectionLifecycleCallback() {
            @Override
            public void onConnectionInitiated(@NonNull String endpointId, @NonNull ConnectionInfo connectionInfo) {
                Log.d(TAG, "onConnectionInitiated With: " + endpointId);
                Nearby.getConnectionsClient(context).acceptConnection(endpointId, payLoadCallback);
            }

            @Override
            public void onConnectionResult(@NonNull String endpointId, @NonNull ConnectionResolution result) {
                switch (result.getStatus().getStatusCode()) {
                    case ConnectionsStatusCodes.STATUS_OK: {
                        Log.d(TAG, "onConnectionResult: Connection Accepted");
                        Toast.makeText(context, "Connection Accepted", Toast.LENGTH_SHORT).show();
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

                advertisers.remove(new Device("", endpointId));
                mView.updateAdvertiseRecyclerView(advertisers);
            }
        };

        discoveryEndpointCallback = new EndpointDiscoveryCallback() {
            @Override
            public void onEndpointFound(@NonNull String endpointId, @NonNull DiscoveredEndpointInfo info) {
                Log.d(TAG, "onEndpointFound: Found " + endpointId);
                if (advertisers == null)
                    advertisers = new ArrayList<>();

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
        payLoadCallback.setupView(mView);
        payLoadCallback.setPresenter(this);
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
    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public void setWatchers(int count) {
        watchers = count;
    }

    @Override
    public void setYoutubeVideoUrl(String url) {
        youtube_video_url = url;
    }

    @Override
    public void startDiscovering() {

        //Enable Bluetooth, Wifi, Location
        setupServices();

        DiscoveryOptions discoveryOptions =
                new DiscoveryOptions.Builder().setStrategy(Strategy.P2P_STAR).build();

        Nearby.getConnectionsClient(context)
                .startDiscovery(Config.SERVICE_ID, discoveryEndpointCallback, discoveryOptions)
                .addOnSuccessListener(aVoid -> {
                    //Toast.makeText(context, "Discovery Succeeded", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    //Toast.makeText(context, "Discovery Failed", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void startAdvertising() {

        //Enable Bluetooth, Wifi, Location
        setupServices();

        AdvertisingOptions advertisingOptions =
                new AdvertisingOptions.Builder().setStrategy(Strategy.P2P_STAR).build();

        Nearby.getConnectionsClient(context)
                .startAdvertising(
                        Build.MANUFACTURER + "-" + Build.MODEL, Config.SERVICE_ID, advertiserConnectionCallback, advertisingOptions)
                .addOnSuccessListener(
                        (Void unused) -> {
                            //Toast.makeText(context, "Advertising Succeed", Toast.LENGTH_SHORT).show();
                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            //Toast.makeText(context, "Advertising Failed", Toast.LENGTH_SHORT).show();
                        });
    }

    private void setupServices() {
        //Enable Location
        if (!((LocationManager) context.getSystemService(Context.LOCATION_SERVICE))
                .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mView.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        //Enable Bluetooth
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled())
            mView.startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);

        //Enable Wifi
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (!wifi.isWifiEnabled())
            wifi.setWifiEnabled(true);
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
        Log.d(TAG, "acceptConnection: Accepted Connection With " + discoverers.get(position).getEndPoint());
        Nearby.getConnectionsClient(context).acceptConnection(discoverers.get(position).getEndPoint(), payLoadCallback);
    }

    @Override
    public void disconnectAll() {
        if (discoverers != null)
            for (Device device :
                    discoverers) {
                Nearby.getConnectionsClient(context).disconnectFromEndpoint(device.getEndPoint());
            }
        else if (advertisers != null)
            for (Device device :
                    advertisers) {
                Nearby.getConnectionsClient(context).disconnectFromEndpoint(device.getEndPoint());
            }
    }

    @Override
    public void sendYoutubeUrl() {
        String url = Config.URL + ":" + youtube_video_url;
        if (discoverers != null)
            for (Device discoverer :
                    discoverers) {
                Payload bytesPayload = Payload.fromBytes(url.getBytes());
                Nearby.getConnectionsClient(context).sendPayload(discoverer.getEndPoint(), bytesPayload);
            }
    }

    @Override
    public void sendWatchersCount() {
        if (discoverers != null) {
            String url = Config.WATCHERS + ":" + discoverers.size();
            setWatchers(discoverers.size());
            for (Device discoverer :
                    discoverers) {
                Payload bytesPayload = Payload.fromBytes(url.getBytes());
                Nearby.getConnectionsClient(context).sendPayload(discoverer.getEndPoint(), bytesPayload);
            }
        } else setWatchers(0);
    }

    @Override
    public String getYoutubeVideoUrl() {
        return youtube_video_url;
    }

    @Override
    public String getMode() {
        return mode;
    }

    @Override
    public int getWatchers() {
        return watchers;
    }

    @Override
    public void sendRequest(String type, int time) {
        String msg = type + ":" + time;
        if (discoverers != null)
            for (Device discoverer :
                    discoverers) {
                Log.d(TAG, "sendRequest: " + msg + " to " + discoverer.getEndPoint());
                Payload bytesPayload = Payload.fromBytes(msg.getBytes());
                Nearby.getConnectionsClient(mView).sendPayload(discoverer.getEndPoint(), bytesPayload);
            }
    }

    @Override
    public void getRequest(String type, int time) {
        switch (type) {
            case Config.START:
                mView.startYoutubeVideo(time);
                break;
            case Config.BUFFER:
            case Config.PAUSE:
                mView.pauseYoutubeVideo(time);
                break;
            case Config.SEEK:
                mView.seekToYoutubeVideo(time);
                break;
            case Config.SYNC:
                mView.syncYoutubeVideo(time);
                break;
        }
    }

    @Override
    public ArrayList<Device> getDevicesCopy(ArrayList<Device> devices) {
        ArrayList<Device> devices_copy = new ArrayList<>();
        for (Device device :
                devices) {
            devices_copy.add((Device) device.clone());
        }
        return devices_copy;
    }
}
