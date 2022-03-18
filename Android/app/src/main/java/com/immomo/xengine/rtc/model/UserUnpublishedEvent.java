package com.immomo.xengine.rtc.model;

public class UserUnpublishedEvent {
    public String eventName = "onUserUnpublished";
    public String remoteUserID;
    public String audioTrackID;

    public UserUnpublishedEvent(String remoteUserID, String audioTrackID) {
        this.remoteUserID = remoteUserID;
        this.audioTrackID = audioTrackID;
    }
}
