package com.belfoapps.youtubesync.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.belfoapps.youtubesync.R;
import com.belfoapps.youtubesync.contracts.DiscoverContract;
import com.belfoapps.youtubesync.di.components.DaggerMVPComponent;
import com.belfoapps.youtubesync.di.components.MVPComponent;
import com.belfoapps.youtubesync.di.modules.ApplicationModule;
import com.belfoapps.youtubesync.di.modules.MVPModule;
import com.belfoapps.youtubesync.presenters.DiscoverPresenter;
import com.belfoapps.youtubesync.utils.ReceiveBytesPayloadListener;
import com.belfoapps.youtubesync.views.activities.MainActivity;
import com.belfoapps.youtubesync.views.ui.custom.FragmentLifeCycle;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Strategy;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class DiscoverFragment extends Fragment implements DiscoverContract.View, FragmentLifeCycle {
    private static final String TAG = "DiscoverFragment";
    private static final String SERVICE_ID = "YoutubeSync";
    /**************************************** Declarations ****************************************/
    private MVPComponent mvpComponent;
    private MainActivity mView;
    @Inject
    DiscoverPresenter mPresenter;

    /***************************************** Constructor ****************************************/
    public DiscoverFragment() {
        // Required empty public constructor
    }

    public DiscoverFragment(MainActivity mView) {
        this.mView = mView;
    }

    /**************************************** View Declarations ***********************************/
    /**************************************** Click Listeners *************************************/

    /**************************************** Essential Methods ***********************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_discover, container, false);

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

    private final EndpointDiscoveryCallback endpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {

                @Override
                public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
                    Log.d(TAG, "onEndpointFound " + info.getEndpointName());
                    Nearby.getConnectionsClient(mView)
                            .requestConnection("Discoverer", endpointId, connectionLifecycleCallback)
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

    @Override
    public void onStartFragment() {
        Log.d(TAG, "onStartFragment");

        DiscoveryOptions discoveryOptions =
                new DiscoveryOptions.Builder().setStrategy(Strategy.P2P_STAR).build();

        Nearby.getConnectionsClient(mView)
                .startDiscovery(SERVICE_ID, endpointDiscoveryCallback, discoveryOptions)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(mView, "Discovery Succeeded", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "onStartFragment: Error = " + e.getMessage());
                    Toast.makeText(mView, "Discovery Failed", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onStopFragment() {

    }

    /**************************************** Methods *********************************************/
}
