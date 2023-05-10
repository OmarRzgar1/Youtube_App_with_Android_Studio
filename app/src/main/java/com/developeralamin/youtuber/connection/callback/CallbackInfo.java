package com.developeralamin.youtuber.connection.callback;

import com.developeralamin.youtuber.connection.responses.ResponseInfo;

public interface CallbackInfo {

    void onComplete(ResponseInfo data);

    void onFailed();
}
