package com.immomo.xengine.rtc.model;

import com.qiniu.droid.rtc.QNConnectionDisconnectedInfo;
import com.qiniu.droid.rtc.QNConnectionState;

public class ConnectStateChangedEvent {
    public ConnectStateChangedEvent(QNConnectionState state, QNConnectionDisconnectedInfo info) {
        connectionState = state.toString();
        if (info != null) {
            disconnectedReason = info.getReason().toString();
            disconnectedErrorCode = info.getErrorCode();
            disconnectedErrorMessage = info.getErrorMessage();
        }
    }

    public String eventName = "onConnectionStateChanged";
    public String connectionState;
    public String disconnectedReason;
    public int disconnectedErrorCode;
    public String disconnectedErrorMessage;
}
