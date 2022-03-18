package com.immomo.xengine.rtc.model;

public class UserPublishedEvent {
    public String eventName = "onUserPublished";
    public String remoteUserID;
    // 仅仅支持一路音频 Track
    public String audioTrackID;

    public UserPublishedEvent(String remoteUserID, String audioTrackID) {
        this.remoteUserID = remoteUserID;
        this.audioTrackID = audioTrackID;
    }
}
