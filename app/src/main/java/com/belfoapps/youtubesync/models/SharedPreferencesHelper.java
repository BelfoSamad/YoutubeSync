package com.belfoapps.youtubesync.models;

import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SharedPreferencesHelper {
    public static final String NAME = "Name";
    private static final String DARK_MODE = "Dark Mode";
    private static final String FIRST = "First";

    /************************************* Declarations *******************************************/
    private SharedPreferences sharedPref;
    private Gson gson;

    /************************************* Constructor ********************************************/
    public SharedPreferencesHelper(SharedPreferences sharedPref, Gson gson) {
        this.sharedPref = sharedPref;
        this.gson = gson;
    }

    /************************************* Methods ***********************************************/

    private boolean isEmpty(String mode) {
        return sharedPref.getString(mode, null) == null;
    }

    //Collections
    /*
    public void saveRecipes(ArrayList<Recipe> recipes) {
        String jsonToString = gson.toJson(recipes);
        SharedPreferences.Editor editor;
        editor = sharedPref.edit();
        editor.putString(SAVED, jsonToString).apply();
    }

    public ArrayList<Recipe> getSavedRecipes() {
        if (isEmpty(SAVED)) return null;
        else {
            String jsonRecipes = sharedPref.getString(SAVED, null);
            Recipe[] recipeItem = gson.fromJson(jsonRecipes, Recipe[].class);
            return new ArrayList<>(Arrays.asList(recipeItem));
        }
    }
     */

    /************************************* Extra Methods ******************************************/
    //Dark Mode
    public void setDarkModeEnable(boolean isEnabled) {
        SharedPreferences.Editor editor;
        editor = sharedPref.edit();
        editor.putBoolean(DARK_MODE, isEnabled).apply();
    }

    public boolean isDarkModeEnabled() {
        return sharedPref.getBoolean(DARK_MODE, false);
    }

    public void saveName(String name) {
        SharedPreferences.Editor editor;
        editor = sharedPref.edit();
        editor.putString(NAME, name).apply();
    }

    //Permissions
    public void firstTimeAskingPermission(String permission) {
        SharedPreferences.Editor editor;
        editor = sharedPref.edit();
        editor.putBoolean(permission, false).apply();
    }

    public boolean isFirstTimeAskingPermission(String permission) {
        return sharedPref != null && sharedPref.getBoolean(permission, true);
    }

    public boolean isFirstTime() {
        return sharedPref.getBoolean(FIRST, true);
    }

    public void setFirstTime(boolean first) {
        SharedPreferences.Editor editor;
        editor = sharedPref.edit();
        editor.putBoolean(FIRST, first).apply();
    }
}
