package com.developeralamin.youtuber.connection.responses;

import com.developeralamin.youtuber.model.SearchItemModel;

import java.io.Serializable;
import java.util.List;

public class ResponseSearchVideo implements Serializable {
    public String nextPageToken;
    public List<SearchItemModel> items;
}
