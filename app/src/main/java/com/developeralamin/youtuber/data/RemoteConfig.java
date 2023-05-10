package com.developeralamin.youtuber.data;

import com.developeralamin.youtuber.BuildConfig;
import com.developeralamin.youtuber.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import androidx.annotation.NonNull;

public class RemoteConfig {

    // firebase remote config key property
    private static final String APP_VERSION = "app_version";
    private static final String FORCE_UPDATE = "force_update";
    private static final String CHANNEL_ID = "channel_id";
    private static final String PLAYLIST_ID = "playlist_id";

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    private static RemoteConfig instance = null;

    public static RemoteConfig getInstance(){
        return instance != null ? instance : new RemoteConfig();
    }

    public RemoteConfig() {
        instance = this;
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(BuildConfig.DEBUG ? 5 : 60*30)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
    }

    public void fetchData(final OnFetchComplete onFetchComplete) {
        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if(onFetchComplete != null) onFetchComplete.onComplete();
            }
        });
    }

    public Long getAppVersion() {
        return mFirebaseRemoteConfig.getLong(APP_VERSION);
    }

    public boolean getForceUpdate() {
        return mFirebaseRemoteConfig.getBoolean(FORCE_UPDATE);
    }

    public String getChannelID() {
        return mFirebaseRemoteConfig.getString(CHANNEL_ID);
    }

    public String getPlaylistID() {
        return mFirebaseRemoteConfig.getString(PLAYLIST_ID);
    }

    public interface OnFetchComplete {
        void onComplete();
    }
}
