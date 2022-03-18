package com.immomo.xengine.rtc.model;

import com.qiniu.droid.rtc.QNRemoteAudioTrack;

public class RemoteAudioUser {
    private String remoteUserID;
    private QNRemoteAudioTrack remoteAudioTrack;

    public RemoteAudioUser(String remoteUserID, QNRemoteAudioTrack remoteAudioTrack) {
        this.remoteUserID = remoteUserID;
        this.remoteAudioTrack = remoteAudioTrack;
    }

    public String getUserID() {
        return remoteUserID;
    }

    public QNRemoteAudioTrack getAudioTrack() {
        return remoteAudioTrack;
    }
}
