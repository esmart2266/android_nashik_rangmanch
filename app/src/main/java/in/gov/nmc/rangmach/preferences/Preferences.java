package in.gov.nmc.rangmach.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Set;

import in.gov.nmc.rangmach.model.BankDetails;
import in.gov.nmc.rangmach.model.UserData;

public class Preferences {
    private static final String LOGGED_IN_USER = "LOGGED_IN_USER";
    private static final String BANK_DETAILS = "BANK_DETAILS";
    private static final String TOKEN = "TOKEN";
    private static final String LAT = "LAT";
    private static final String LNG = "LNG";
    private static final String GEO_ADDRESS = "GEO_ADDRESS";

    private Context context;
    private Gson gson;

    public Preferences(Context context) {
        this.context = context;
        this.gson = new Gson();
    }

    protected SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private String getString(String key, String def) {
        SharedPreferences prefs = getSharedPreferences();
        return prefs.getString(key, def);
    }

    public void setString(String key, String val) {
        SharedPreferences prefs = getSharedPreferences();
        Editor e = prefs.edit();
        e.putString(key, val);
        e.apply();
    }

    private boolean getBoolean(String key, boolean def) {
        SharedPreferences prefs = getSharedPreferences();
        return prefs.getBoolean(key, def);
    }

    private void setBoolean(String key, boolean val) {
        SharedPreferences prefs = getSharedPreferences();
        Editor e = prefs.edit();
        e.putBoolean(key, val);
        e.apply();
    }

    // ============== User Management ==============

    public boolean isLoggedInUser() {
        String json = getString(LOGGED_IN_USER, null);
        return json != null && !TextUtils.isEmpty(json);
    }

    public void logOutUser() {
        SharedPreferences prefs = getSharedPreferences();
        Editor e = prefs.edit();
        e.remove(LOGGED_IN_USER);
        e.remove(BANK_DETAILS);
        e.apply();
    }

    public UserData getLoggedInUser() {
        String json = getString(LOGGED_IN_USER, null);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            return gson.fromJson(json, UserData.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setLoggedInUser(UserData user) {
        if (user != null) {
            setString(LOGGED_IN_USER, gson.toJson(user));
        }
    }

    // ============== Bank Details Management ==============

    public BankDetails getBankDetails() {
        String json = getString(BANK_DETAILS, null);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            return gson.fromJson(json, BankDetails.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setBankDetails(BankDetails bankDetails) {
        if (bankDetails != null) {
            setString(BANK_DETAILS, gson.toJson(bankDetails));
        }
    }

    public void clearBankDetails() {
        SharedPreferences prefs = getSharedPreferences();
        Editor e = prefs.edit();
        e.remove(BANK_DETAILS);
        e.apply();
    }

    public boolean hasBankDetails() {
        String json = getString(BANK_DETAILS, null);
        return json != null && !TextUtils.isEmpty(json);
    }

    // ============== Token Management ==============

    public String getToken() {
        return getString(TOKEN, null);
    }

    public void setToken(String token) {
        setString(TOKEN, token);
    }

    // ============== Location Management ==============

    public String getLat() {
        return getString(LAT, "0.0");
    }

    public void setLat(String lat) {
        setString(LAT, lat);
    }

    public String getLng() {
        return getString(LNG, "0.0");
    }

    public void setLng(String lng) {
        setString(LNG, lng);
    }

    public String getGeoAddress() {
        return getString(GEO_ADDRESS, "");
    }

    public void setGeoAddress(String address) {
        setString(GEO_ADDRESS, address);
    }

    // ============== Utility Methods ==============

    public void clearAll() {
        SharedPreferences prefs = getSharedPreferences();
        Editor e = prefs.edit();
        e.clear();
        e.apply();
    }
}