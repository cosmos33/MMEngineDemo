package com.immomo.xengine.rtc.model;

public class UserJoinedEvent {
    public String eventName = "onUserJoined";
    public String remoteUserID;
    public String userData;

    public UserJoinedEvent(String remoteUserID, String userData) {
        this.remoteUserID = remoteUserID;
        this.userData = userData;
    }
}
