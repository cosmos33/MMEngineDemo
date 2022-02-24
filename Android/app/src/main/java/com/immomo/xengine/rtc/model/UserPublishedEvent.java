package com.immomo.xengine.rtc.model;

import com.qiniu.droid.rtc.QNRemoteTrack;

import java.util.ArrayList;
import java.util.List;

public class UserPublishedEvent {
    public UserPublishedEvent(String remoteUserID, List<QNRemoteTrack> list) {
        this.remoteUserID = remoteUserID;
        for (QNRemoteTrack remoteTrack : list) {
            if (remoteTrack.isAudio()) {
                audioTrackIDList.add(remoteTrack.getTrackID());
            }
        }
    }
    public String eventName = "onUserPublished";
    public String remoteUserID;
    public List audioTrackIDList = new ArrayList();
}
