package com.immomo.xengine.rtc.model;

public class UserReconnectedEvent {
    public String eventName = "onUserReconnected";
    public String remoteUserID;

    public UserReconnectedEvent(String remoteUserID) {
        this.remoteUserID = remoteUserID;
    }
}