package com.developeralamin.youtuber.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPref {

    private Context ctx;
    private SharedPreferences custom_prefence;
    private SharedPreferences default_prefence;

    public SharedPref(Context context) {
        this.ctx = context;
        custom_prefence = context.getSharedPreferences("MAIN_PREF", Context.MODE_PRIVATE);
        default_prefence = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private String str(int string_id) {
        return ctx.getString(string_id);
    }

    /**
     * Preference for Fcm register
     */
    public void setFcmRegId(String fcmRegId) {
        custom_prefence.edit().putString("FCM_PREF_KEY", fcmRegId).apply();
    }

    public String getFcmRegId() {
        return custom_prefence.getString("FCM_PREF_KEY", "");
    }

    /**
     * To save dialog permission state
     */
    public void setNeverAskAgain(String key, boolean value) {
        custom_prefence.edit().putBoolean(key, value).apply();
    }

    public void setSubscibeNotif(boolean value) {
        custom_prefence.edit().putBoolean("SUBSCRIBE_NOTIF", value).apply();
    }

    public boolean isSubscibeNotif() {
        return custom_prefence.getBoolean("SUBSCRIBE_NOTIF", false);
    }

}
