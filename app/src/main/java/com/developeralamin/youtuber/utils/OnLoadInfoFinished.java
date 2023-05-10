package com.developeralamin.youtuber.utils;

import com.developeralamin.youtuber.room.table.EntityInfo;

public interface OnLoadInfoFinished {

    void onComplete(EntityInfo data);

    void onFailed();

}
