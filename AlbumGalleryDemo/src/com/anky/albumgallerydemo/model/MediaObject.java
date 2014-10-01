package com.anky.albumgallerydemo.model;

import android.net.Uri;

import com.anky.albumgallerydemo.utils.MediaType;

public class MediaObject implements Comparable<MediaObject> {

    private int id;
    private String path;
    private MediaType mediaType;
    private Long mediaTakenDateMillis;
    private String duration;

    public MediaObject(int id, String path, MediaType mediaType, long mediaTakenDateMillis) {
        this.id = id;
        this.path = path;
        this.mediaType = mediaType;
        this.mediaTakenDateMillis = mediaTakenDateMillis;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Long getMediaTakenDateMillis() {
        return mediaTakenDateMillis;
    }

    public void setMediaTakenDateMillis(long mediaTakenDateMillis) {
        this.mediaTakenDateMillis = mediaTakenDateMillis;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Uri getMediaUri() {
        return Uri.parse(path);
    }

    @Override
    public int compareTo(MediaObject another) {
        return another.mediaTakenDateMillis.compareTo(mediaTakenDateMillis);
    }
}
