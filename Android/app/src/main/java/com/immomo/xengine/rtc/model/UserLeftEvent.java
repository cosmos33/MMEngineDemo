package com.immomo.xengine.rtc.model;

public class UserLeftEvent {
    public String eventName = "onUserJoined";
    public String remoteUserID;

    public UserLeftEvent(String remoteUserID) {
        this.remoteUserID = remoteUserID;
    }
}
