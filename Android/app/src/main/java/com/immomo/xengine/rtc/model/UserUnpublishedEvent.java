package com.immomo.xengine.rtc.model;

import com.qiniu.droid.rtc.QNRemoteTrack;

import java.util.ArrayList;
import java.util.List;

public class UserUnpublishedEvent {
    public String eventName = "onUserUnpublished";
    public String remoteUserID;
    public List audioTrackIDList = new ArrayList();

    public UserUnpublishedEvent(String remoteUserID, List<QNRemoteTrack> list) {
        this.remoteUserID = remoteUserID;
        for (QNRemoteTrack remoteTrack : list) {
            if (remoteTrack.isAudio()) {
                audioTrackIDList.add(remoteTrack.getTrackID());
            }
        }
    }
}
