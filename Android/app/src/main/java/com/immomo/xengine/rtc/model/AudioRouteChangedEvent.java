package com.immomo.xengine.rtc.model;

import com.qiniu.droid.rtc.model.QNAudioDevice;

public class AudioRouteChangedEvent {
    public String eventName = "onAudioRouteChanged";
    public String audioDevice;

    public AudioRouteChangedEvent(QNAudioDevice device) {
        this.audioDevice = device.name();
    }
}
