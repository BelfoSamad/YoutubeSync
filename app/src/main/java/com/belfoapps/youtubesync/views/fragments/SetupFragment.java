package com.belfoapps.youtubesync.views.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.belfoapps.youtubesync.R;
import com.belfoapps.youtubesync.presenters.MainPresenter;
import com.belfoapps.youtubesync.utils.Config;
import com.belfoapps.youtubesync.views.activities.MainActivity;
import com.belfoapps.youtubesync.views.ui.custom.FragmentLifeCycle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetupFragment extends Fragment implements FragmentLifeCycle {
    private static final String TAG = "SetupFragment";
    /**************************************** Declarations ****************************************/
    private MainActivity mView;
    private MainPresenter mPresenter;

    /***************************************** Constructor ****************************************/
    public SetupFragment() {
        // Required empty public constructor
    }

    public SetupFragment(MainActivity mView, MainPresenter mPresenter) {
        this.mView = mView;
        this.mPresenter = mPresenter;
    }

    /**************************************** View Declarations ***********************************/
    @BindView(R.id.youtub_url)
    EditText youtube_url;
    @BindView(R.id.advertise)
    Button advertise;

    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.discover)
    public void discover() {
        mPresenter.setMode(Config.DISCOVER);
        mView.nextStep(Config.DISCOVER_STEP);
    }

    @OnClick(R.id.advertise)
    public void advertise() {
        mPresenter.setMode(Config.ADVERTISE);
        mPresenter.setYoutubeVideoUrl(youtube_url.getText().toString());
        mView.nextStep(Config.ADVERTISE_STEP);
    }

    /**************************************** Essential Methods ***********************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup, container, false);

        //Set ButterKnife
        ButterKnife.bind(this, view);

        //Edit Text Listener
        youtube_url.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0)
                    advertise.setEnabled(true);
                else advertise.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    @Override
    public void onStartFragment() {

    }

    @Override
    public void onStopFragment() {

    }

    /**************************************** Methods *********************************************/
}
