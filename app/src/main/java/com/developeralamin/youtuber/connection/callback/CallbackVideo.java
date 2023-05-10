package com.developeralamin.youtuber.connection.callback;

import com.developeralamin.youtuber.connection.responses.ResponseVideos;

public interface CallbackVideo {

    void onComplete(ResponseVideos data);

    void onFailed();
}
