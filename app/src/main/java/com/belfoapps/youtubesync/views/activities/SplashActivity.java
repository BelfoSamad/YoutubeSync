package com.belfoapps.youtubesync.views.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.belfoapps.youtubesync.R;
import com.belfoapps.youtubesync.contracts.SplashContract;
import com.belfoapps.youtubesync.di.components.DaggerMVPComponent;
import com.belfoapps.youtubesync.di.components.MVPComponent;
import com.belfoapps.youtubesync.di.modules.ApplicationModule;
import com.belfoapps.youtubesync.di.modules.MVPModule;
import com.belfoapps.youtubesync.presenters.SplashPresenter;
import com.belfoapps.youtubesync.views.fragments.NameFragment;
import com.belfoapps.youtubesync.views.fragments.PermissionFragment;
import com.belfoapps.youtubesync.views.fragments.StartupFragment;
import com.belfoapps.youtubesync.views.ui.adapters.MainPagerAdapter;
import com.belfoapps.youtubesync.views.ui.custom.FragmentLifeCycle;
import com.belfoapps.youtubesync.views.ui.custom.UnScrollableViewPager;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {
    private static final String TAG = "SplashActivity";
    /**************************************** Declarations ****************************************/
    private MVPComponent mvpComponent;
    @Inject
    SplashPresenter mPresenter;
    private MainPagerAdapter mAdapter;

    /**************************************** View Declarations ***********************************/
    @BindView(R.id.viewpager)
    UnScrollableViewPager mViewPager;

    /**************************************** Essential Methods ***********************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Initialize Dagger For Application
        mvpComponent = getComponent();
        //Inject the Component Here
        mvpComponent.inject(this);

        //Set ButterKnife
        ButterKnife.bind(this);

        //Attach View To Presenter
        mPresenter.attach(this);

        //init ViewPager
        if (mPresenter.isFirstTime())
            initViewPager();
        else startActivity(new Intent(SplashActivity.this, MainActivity.class));
    }

    @Override
    public MVPComponent getComponent() {
        if (mvpComponent == null) {
            mvpComponent = DaggerMVPComponent
                    .builder()
                    .applicationModule(new ApplicationModule(getApplication()))
                    .mVPModule(new MVPModule(this))
                    .build();
        }
        return mvpComponent;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPresenter.setFirstTime(false);
                goToMain();
            }
            else
                Toast.makeText(this, "This permission must be required to continue", Toast.LENGTH_SHORT).show();
    }

    /**************************************** Methods *********************************************/
    @Override
    public void initViewPager() {
        Log.d(TAG, "initViewPager");
        ArrayList<Fragment> fragments = new ArrayList<>();

        StartupFragment startFragment = new StartupFragment(this, mPresenter);
        NameFragment nameFragment = new NameFragment(this, mPresenter);
        PermissionFragment permissionFragment = new PermissionFragment(this, mPresenter);

        //Add Fragment
        fragments.add(startFragment);
        fragments.add(nameFragment);
        fragments.add(permissionFragment);

        mAdapter = new MainPagerAdapter(getSupportFragmentManager(), fragments, SplashActivity.this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setPagingEnabled(true);
    }

    @Override
    public void nextStep(int position) {
        Log.d(TAG, "nextStep");
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void prevStep(int position) {
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void goToMain() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
    }
}
