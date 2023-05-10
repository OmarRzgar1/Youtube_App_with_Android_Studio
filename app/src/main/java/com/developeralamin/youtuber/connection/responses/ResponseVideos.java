package com.developeralamin.youtuber.connection.responses;

import com.developeralamin.youtuber.model.Video;

import java.io.Serializable;
import java.util.List;

public class ResponseVideos implements Serializable {
    public String nextPageToken;
    public List<Video> items;
}
