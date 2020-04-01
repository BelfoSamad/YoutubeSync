package com.belfoapps.youtubesync.utils;

import android.content.SharedPreferences;
import android.os.Build;

public class Config {
    //Steps
    public static final int SETUP_STEP = 0;
    public static final int ADVERTISE_STEP = 1;
    public static final int DISCOVER_STEP = 2;
    public static final int WATCH_STEP = 3;
    public static final String SERVICE_ID = "YoutubeSync";
    public static final String DEVICE_NAME = Build.MANUFACTURER + "-" + Build.MODEL;

    //Essentials
    private static final String GDPR = "Gdpr";
    private static final String BANNER = "Banner";
    private static final String INTERSTITIAL = "Interstitial";
    private static final String PUBLISHER = "Publisher";
    private static final String BANNER_ID = "Banner ID";
    private static final String INTERSTITIAL_ID = "Interstitial ID";

    //Developer Infos
    private static final String NAME = "Name";
    private static final String EMAIL = "Email";

    private SharedPreferences preferences;

    public Config(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    /************************************** Basics ************************************************/
    public void setAdBannerId(String id) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putString(BANNER_ID, id).apply();
    }

    public void setAdInterstitialId(String id) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putString(INTERSTITIAL_ID, id).apply();
    }

    public String getAdBannerId() {
        return preferences.getString(BANNER_ID, "test");
    }

    public String getAdInterstitialId() {
        return preferences.getString(INTERSTITIAL_ID, "test");
    }

    /************************************ Developer Info ******************************************/

    public void setDeveloperName(String name) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putString(NAME, name).apply();
    }

    public void setDeveloperEmail(String email) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putString(EMAIL, email).apply();
    }

    public String getDeveloperName() {
        return preferences.getString(NAME, "Developer");
    }

    public String getDeveloperEmail() {
        return preferences.getString(EMAIL, "developer@gmail.com");
    }

    /************************************** Configs ***********************************************/
    public void setBannerEnabled(boolean enabled) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putBoolean(BANNER, enabled).apply();
    }

    public void setInterstitialEnabled(boolean enabled) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putBoolean(INTERSTITIAL, enabled).apply();
    }

    public void setGDPREnabled(boolean enabled) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putBoolean(GDPR, enabled).apply();
    }

    public void setPublisherId(String id) {
        SharedPreferences.Editor editor;
        editor = preferences.edit();
        editor.putString(PUBLISHER, id).apply();
    }

    public boolean isBannerEnabled() {
        return preferences.getBoolean(BANNER, true);
    }

    public boolean isInterstitialEnabled() {
        return preferences.getBoolean(INTERSTITIAL, true);
    }

    public boolean isGDPREnabled() {
        return preferences.getBoolean(GDPR, true);
    }

    public String getPublisherId() {
        //TODO: Remove After Testings
        return preferences.getString(PUBLISHER, "pub-4679171106713552");
    }
}
