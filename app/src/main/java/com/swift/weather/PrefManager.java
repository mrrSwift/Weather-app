package com.swift.weather;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private static final String CITY_NAME = "CityName";
    private static final String CONTRY_NAME = "contryName";
    private static final String COD_COD = "COD";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "data";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public void putCityName(String name) {
        editor.putString(CITY_NAME, String.valueOf(name));
        editor.commit();
    }
    public String getCityName() { return pref.getString(CITY_NAME, ""); }

    public void putcontry(String name) {
        editor.putString(CONTRY_NAME, String.valueOf(name));
        editor.commit();
    }
    public String getcontry() { return pref.getString(CONTRY_NAME, "IR"); }


    public void delete() {
        editor.remove(CITY_NAME);
        editor.commit();
    }
    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void putCod(Boolean cod) {
        editor.putBoolean(COD_COD, cod);
        editor.commit();
    }
    public boolean getCod() { return pref.getBoolean(COD_COD, false); }

    public void putLat(String lat) {
        editor.putString("lat", lat);
        editor.commit();
    }
    public String getLat() { return pref.getString("lat", ""); }

    public void putLon(String lon) {
        editor.putString("lon", lon);
        editor.commit();
    }
    public String getLon() { return pref.getString("lon", ""); }
}
