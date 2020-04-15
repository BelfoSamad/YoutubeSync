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
import com.belfoapps.youtubesync.presenters.SplashPresenter;
import com.belfoapps.youtubesync.utils.Config;
import com.belfoapps.youtubesync.views.activities.SplashActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NameFragment extends Fragment {
    private static final String TAG = "NameFragment";
    /**************************************** Declarations ****************************************/
    private SplashActivity mView;
    private SplashPresenter mPresenter;

    /***************************************** Constructor ****************************************/
    public NameFragment() {
        // Required empty public constructor
    }

    public NameFragment(SplashActivity mView, SplashPresenter mPresenter) {
        this.mView = mView;
        this.mPresenter = mPresenter;
    }

    /**************************************** View Declarations ***********************************/
    @BindView(R.id.get_name)
    EditText get_name;
    @BindView(R.id.next)
    Button next;

    /**************************************** Click Listeners *************************************/
    @OnClick(R.id.next)
    public void goNext() {
        mPresenter.setName(get_name.getText().toString());
        mView.nextStep(Config.PERMISSION_STEP);
    }

    /**************************************** Essential Methods ***********************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_name, container, false);

        //Set ButterKnife
        ButterKnife.bind(this, view);

        get_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0)
                    next.setEnabled(true);
                else next.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }
}
