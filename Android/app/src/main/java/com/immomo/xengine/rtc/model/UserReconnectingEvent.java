package com.immomo.xengine.rtc.model;

public class UserReconnectingEvent {
    public String eventName = "onUserReconnecting";
    public String remoteUserID;

    public UserReconnectingEvent(String remoteUserID) {
        this.remoteUserID = remoteUserID;
    }
}
