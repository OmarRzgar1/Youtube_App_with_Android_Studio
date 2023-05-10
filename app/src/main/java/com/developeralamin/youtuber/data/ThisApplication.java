package com.developeralamin.youtuber.data;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;

import com.developeralamin.youtuber.R;
import com.developeralamin.youtuber.connection.Request;
import com.developeralamin.youtuber.connection.callback.CallbackInfo;
import com.developeralamin.youtuber.connection.responses.ResponseInfo;
import com.developeralamin.youtuber.room.AppDatabase;
import com.developeralamin.youtuber.room.DAO;
import com.developeralamin.youtuber.room.table.EntityInfo;
import com.developeralamin.youtuber.utils.NetworkCheck;
import com.developeralamin.youtuber.utils.OnLoadInfoFinished;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

public class ThisApplication extends Application {

    private Request request;
    private DAO database;
    private OnLoadInfoFinished onLoadInfoFinished;
    private int retry_index = 0;
    private boolean on_request_info = false;
    private boolean ever_update = false;
    private SharedPref shared_pref;
    private int fcm_count = 0;
    private final int FCM_MAX_COUNT = 10;

    private static ThisApplication mInstance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        shared_pref = new SharedPref(this);

        // Init firebase.
        FirebaseApp.initializeApp(this);

        // configure admob
        MobileAds.initialize(this, getString(R.string.ADS_APP_ID));

        request = new Request();
        database = AppDatabase.getDb(this).getDAO();

        // request channel info data
        retryLoadChannelInfo();
        obtainFirebaseToken();
        subscribeTopicNotif();

        Log.d("FCM_REG_ID", shared_pref.getFcmRegId());
    }

    public static synchronized ThisApplication getInstance() {
        return mInstance;
    }

    public void retryLoadChannelInfo() {
        EntityInfo info = database.getInfo();
        if (info == null) {
            retry_index = 0;
            loadChannelInfo();
        }
    }

    private void loadChannelInfo() {
        on_request_info = true;
        if (retry_index >= Constant.MAX_RETRY_INFO) {
            on_request_info = false;
            if (onLoadInfoFinished != null) onLoadInfoFinished.onFailed();
            return;
        }
        retry_index++;
        request.getInfo(new CallbackInfo() {
            @Override
            public void onComplete(ResponseInfo data) {
                on_request_info = false;
                if (data != null && data.items != null && data.items.size() > 0) {
                    setEverUpdate(true);
                    database.insertInfo(EntityInfo.getEntity(data.items.get(0)));
                    EntityInfo info = database.getInfo();
                    if (onLoadInfoFinished != null) onLoadInfoFinished.onComplete(info);
                } else {
                    delayLoadChannelInfo();
                }
            }

            @Override
            public void onFailed() {
                on_request_info = false;
                delayLoadChannelInfo();
            }
        });
    }

    private void delayLoadChannelInfo() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadChannelInfo();
            }
        }, 500);
    }

    public void setOnLoadInfoFinished(OnLoadInfoFinished onLoadInfoFinished) {
        this.onLoadInfoFinished = onLoadInfoFinished;
    }

    public boolean isOnRequestInfo() {
        return on_request_info;
    }

    private void obtainFirebaseToken() {
        if (NetworkCheck.isConnect(this)) {
            fcm_count++;

            Task<InstanceIdResult> resultTask = FirebaseInstanceId.getInstance().getInstanceId();
            resultTask.addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String regId = instanceIdResult.getToken();
                    shared_pref.setFcmRegId(regId);
                    if (!TextUtils.isEmpty(regId)) {
                        Log.d("FCM_REG_ID", regId);
                    }
                }
            });

            resultTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (fcm_count > FCM_MAX_COUNT) return;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            obtainFirebaseToken();
                        }
                    }, 500);
                }
            });
        }
    }


    private void subscribeTopicNotif() {
        if (shared_pref.isSubscibeNotif()) return;
        FirebaseMessaging.getInstance().subscribeToTopic(Constant.NOTIF_TOPIC).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                shared_pref.setSubscibeNotif(task.isSuccessful());
            }
        });
    }

    public boolean isEverUpdate() {
        return ever_update;
    }

    public void setEverUpdate(boolean ever_update) {
        this.ever_update = ever_update;
    }
}
