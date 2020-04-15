package com.belfoapps.youtubesync.views.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.belfoapps.youtubesync.R;
import com.belfoapps.youtubesync.contracts.MainContract;
import com.belfoapps.youtubesync.di.components.DaggerMVPComponent;
import com.belfoapps.youtubesync.di.components.MVPComponent;
import com.belfoapps.youtubesync.di.modules.ApplicationModule;
import com.belfoapps.youtubesync.di.modules.MVPModule;
import com.belfoapps.youtubesync.pojo.Device;
import com.belfoapps.youtubesync.presenters.MainPresenter;
import com.belfoapps.youtubesync.views.fragments.AdvertiseFragment;
import com.belfoapps.youtubesync.views.fragments.DiscoverFragment;
import com.belfoapps.youtubesync.views.fragments.SetupFragment;
import com.belfoapps.youtubesync.views.fragments.WatchFragment;
import com.belfoapps.youtubesync.views.ui.adapters.MainPagerAdapter;
import com.belfoapps.youtubesync.views.ui.custom.FragmentLifeCycle;
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
    private SetupFragment setupFragment;
    private DiscoverFragment discoverFragment;
    private AdvertiseFragment advertiseFragment;
    private WatchFragment watchFragment;

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
    public void onBackPressed() {
        //TODO: add double click to quit
        if (mViewPager.getCurrentItem() != 0) {

            //Go Back t setup
            mViewPager.setCurrentItem(0);

            //Disconnect from all
            mPresenter.disconnectAll();

            //Stop Advertising/Discovering
            mPresenter.stopAdvertising();
            mPresenter.stopDiscovering();
        }
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
        Log.d(TAG, "initViewPager");
        ArrayList<Fragment> fragments = new ArrayList<>();

        setupFragment = new SetupFragment(this, mPresenter);
        discoverFragment = new DiscoverFragment(this, mPresenter);
        advertiseFragment = new AdvertiseFragment(this, mPresenter);
        watchFragment = new WatchFragment(this, mPresenter);

        fragments.add(setupFragment);
        fragments.add(advertiseFragment);
        fragments.add(discoverFragment);
        fragments.add(watchFragment);

        mAdapter = new MainPagerAdapter(getSupportFragmentManager(), fragments, MainActivity.this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setPagingEnabled(true);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currentPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                FragmentLifeCycle fragmentToShow = (FragmentLifeCycle) mAdapter.getItem(position);
                fragmentToShow.onStartFragment();

                FragmentLifeCycle fragmentToHide = (FragmentLifeCycle) mAdapter.getItem(currentPosition);
                fragmentToHide.onStopFragment();

                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void updateAdvertiseRecyclerView(ArrayList<Device> connections) {
        advertiseFragment.updateRecyclerView(connections);
    }

    @Override
    public void updateDiscoveryRecyclerView(ArrayList<Device> connections) {
        discoverFragment.updateRecyclerView(connections);
    }

    @Override
    public void nextStep(int position) {
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void prevStep(int position) {
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void startYoutubeVideo(int time) {
        watchFragment.start(time);
    }

    @Override
    public void pauseYoutubeVideo(int time) {
        watchFragment.pause(time);
    }

    @Override
    public void seekToYoutubeVideo(int time) {
        watchFragment.seek(time);
    }

    @Override
    public void syncYoutubeVideo(int time) {
        watchFragment.sync(time);
    }
}
