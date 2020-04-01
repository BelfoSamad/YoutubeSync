package com.belfoapps.youtubesync.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.belfoapps.youtubesync.R;
import com.belfoapps.youtubesync.contracts.AdvertiseContract;
import com.belfoapps.youtubesync.di.components.DaggerMVPComponent;
import com.belfoapps.youtubesync.di.components.MVPComponent;
import com.belfoapps.youtubesync.di.modules.ApplicationModule;
import com.belfoapps.youtubesync.di.modules.MVPModule;
import com.belfoapps.youtubesync.presenters.AdvertisePresenter;
import com.belfoapps.youtubesync.utils.ReceiveBytesPayloadListener;
import com.belfoapps.youtubesync.views.activities.MainActivity;
import com.belfoapps.youtubesync.views.ui.custom.FragmentLifeCycle;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.Strategy;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdvertiseFragment extends Fragment implements AdvertiseContract.View, FragmentLifeCycle {
    private static final String TAG = "AdvertiseFragment";
    private static final String SERVICE_ID = "YoutubeSync";
    /**************************************** Declarations ****************************************/
    private MVPComponent mvpComponent;
    private MainActivity mView;
    @Inject
    AdvertisePresenter mPresenter;
    String receiver;

    /***************************************** Constructor ****************************************/
    public AdvertiseFragment() {
        // Required empty public constructor
    }

    public AdvertiseFragment(MainActivity mView) {
        this.mView = mView;
    }

    /**************************************** View Declarations ***********************************/
    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.send)
    public void sendMessage(){
        Payload bytesPayload = Payload.fromBytes(new byte[] {0xa, 0xb, 0xc, 0xd});
        Nearby.getConnectionsClient(mView).sendPayload(receiver, bytesPayload);
    }
    /**************************************** Essential Methods ***********************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_advertise, container, false);

        //Initialize Dagger For Application
        mvpComponent = getComponent();
        //Inject the Component Here
        mvpComponent.inject(this);

        //Set ButterKnife
        ButterKnife.bind(this, view);

        //Attach View To Presenter
        mPresenter.attach(this);

        return view;
    }


    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                ReceiveBytesPayloadListener payloadCallback = new ReceiveBytesPayloadListener();

                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                    Log.d(TAG, "onConnectionInitiated");
                    Nearby.getConnectionsClient(mView).acceptConnection(endpointId, payloadCallback);
                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    switch (result.getStatus().getStatusCode()) {
                        case ConnectionsStatusCodes.STATUS_OK: {
                            Log.d(TAG, "onConnectionResult: Connection OK!");
                            receiver = endpointId;
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

    @Override
    public MVPComponent getComponent() {
        if (mvpComponent == null) {
            mvpComponent = DaggerMVPComponent
                    .builder()
                    .applicationModule(new ApplicationModule(Objects.requireNonNull(getActivity()).getApplication()))
                    .mVPModule(new MVPModule(getActivity()))
                    .build();
        }
        return mvpComponent;
    }

    @Override
    public void onStartFragment() {
        Log.d(TAG, "onStartFragment");

        AdvertisingOptions advertisingOptions =
                new AdvertisingOptions.Builder().setStrategy(Strategy.P2P_STAR).build();

        Nearby.getConnectionsClient(mView)
                .startAdvertising(
                        "Advertiser", SERVICE_ID, connectionLifecycleCallback, advertisingOptions)
                .addOnSuccessListener(
                        (Void unused) -> {
                            Toast.makeText(mView, "Advertising Succeed", Toast.LENGTH_SHORT).show();
                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            Toast.makeText(mView, "Advertising Failed", Toast.LENGTH_SHORT).show();
                        });
    }

    @Override
    public void onStopFragment() {

    }

    /**************************************** Methods *********************************************/
}
