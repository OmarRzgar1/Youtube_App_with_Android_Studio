package com.developeralamin.youtuber.connection.callback;

import com.developeralamin.youtuber.connection.responses.ResponseSearchVideo;

public interface CallbackSearchVideo {

    void onComplete(ResponseSearchVideo data);

    void onFailed();

}
