package com.belfoapps.youtubesync.views.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.belfoapps.youtubesync.R;
import com.belfoapps.youtubesync.contracts.MainContract;
import com.belfoapps.youtubesync.di.components.DaggerMVPComponent;
import com.belfoapps.youtubesync.di.components.MVPComponent;
import com.belfoapps.youtubesync.di.modules.ApplicationModule;
import com.belfoapps.youtubesync.di.modules.MVPModule;
import com.belfoapps.youtubesync.presenters.MainPresenter;
import com.belfoapps.youtubesync.views.fragments.AdvertiseFragment;
import com.belfoapps.youtubesync.views.fragments.DiscoverFragment;
import com.belfoapps.youtubesync.views.fragments.SetupFragment;
import com.belfoapps.youtubesync.views.fragments.WatchFragment;
import com.belfoapps.youtubesync.views.ui.adapters.MainPagerAdapter;
import com.belfoapps.youtubesync.views.ui.custom.UnScrollableViewPager;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    private static final String TAG = "MainActivity";
    /**************************************** Declarations ****************************************/
    private MVPComponent mvpComponent;
    @Inject
    MainPresenter mPresenter;
    private MainPagerAdapter mAdapter;
    /**************************************** View Declarations ***********************************/
    @BindView(R.id.viewpager)
    UnScrollableViewPager mViewPager;
    /**************************************** Click Listeners *************************************/

    /**************************************** Essential Methods ***********************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Dagger For Application
        mvpComponent = getComponent();
        //Inject the Component Here
        mvpComponent.inject(this);

        //Set ButterKnife
        ButterKnife.bind(this);

        //Attach View To Presenter
        mPresenter.attach(this);

        //init ViewPager
        initViewPager();
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

    /**************************************** Methods *********************************************/
    @Override
    public void initViewPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();

        SetupFragment setupFragment = new SetupFragment();
        DiscoverFragment discoverFragment = new DiscoverFragment();
        AdvertiseFragment advertiseFragment = new AdvertiseFragment();
        WatchFragment watchFragment = new WatchFragment();

        fragments.add(setupFragment);
        fragments.add(discoverFragment);
        fragments.add(advertiseFragment);
        fragments.add(watchFragment);

        mAdapter = new MainPagerAdapter(getSupportFragmentManager(), fragments, MainActivity.this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setPagingEnabled(true);
    }

    @Override
    public void nextStep(int position) {
        mViewPager.setCurrentItem(position);
    }
}
