package com.immomo.xengine.rtc.model;

import com.qiniu.droid.rtc.QNRemoteAudioTrack;

import java.util.ArrayList;
import java.util.List;

public class SubscribedEvent {
    public String eventName = "onSubscribed";
    public String remoteUserID;
    public List audioTrackIDList = new ArrayList();

    public SubscribedEvent(String remoteUserID, List<QNRemoteAudioTrack> list) {
        this.remoteUserID = remoteUserID;
        for (QNRemoteAudioTrack track : list) {
            audioTrackIDList.add(track.getTrackID());
        }
    }
}
