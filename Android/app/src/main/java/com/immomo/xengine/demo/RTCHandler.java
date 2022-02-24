package com.immomo.xengine.demo;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.immomo.xengine.rtc.model.ConnectStateChangedEvent;
import com.immomo.xengine.rtc.model.RemoteVolume;
import com.immomo.xengine.rtc.model.SubscribedEvent;
import com.immomo.xengine.rtc.model.UserJoinedEvent;
import com.immomo.xengine.rtc.model.UserLeftEvent;
import com.immomo.xengine.rtc.model.UserPublishedEvent;
import com.immomo.xengine.rtc.model.UserUnpublishedEvent;
import com.momo.xeengine.script.ScriptBridge;
import com.qiniu.droid.rtc.QNClientEventListener;
import com.qiniu.droid.rtc.QNConnectionDisconnectedInfo;
import com.qiniu.droid.rtc.QNConnectionState;
import com.qiniu.droid.rtc.QNCustomMessage;
import com.qiniu.droid.rtc.QNMediaRelayState;
import com.qiniu.droid.rtc.QNMicrophoneAudioTrack;
import com.qiniu.droid.rtc.QNRTC;
import com.qiniu.droid.rtc.QNRTCClient;
import com.qiniu.droid.rtc.QNRemoteAudioTrack;
import com.qiniu.droid.rtc.QNRemoteTrack;
import com.qiniu.droid.rtc.QNRemoteVideoTrack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class RTCHandler {
    private static final String TAG = "RTCHandler : hk -- ";

    private Activity activity;
    private QNRTCClient client;
    private QNMicrophoneAudioTrack microphoneAudioTrack;
    private List<QNRemoteAudioTrack> remoteAudioTracks = new ArrayList<>();

    public RTCHandler(Activity activity) {
        this.activity = activity;
    }

    public String init(String message, ScriptBridge.Callback callback) {
        QNRTC.init(activity, null);
        client = QNRTC.createClient(new QNClientEventListener() {
            @Override
            public void onConnectionStateChanged(QNConnectionState qnConnectionState, QNConnectionDisconnectedInfo qnConnectionDisconnectedInfo) {
                Gson gson = new Gson();
                ConnectStateChangedEvent connectStateChangedEvent = new ConnectStateChangedEvent(qnConnectionState, qnConnectionDisconnectedInfo);
                String jsonString = gson.toJson(connectStateChangedEvent);
                Log.i(TAG, "onConnectionStateChanged : " + jsonString);
                callback.call(jsonString);
            }

            @Override
            public void onUserJoined(String remoteUserID, String userData) {
                Gson gson = new Gson();
                UserJoinedEvent userJoinedEvent = new UserJoinedEvent(remoteUserID, userData);
                String jsonString = gson.toJson(userJoinedEvent);
                Log.i(TAG, "onUserJoined : " + jsonString);
                callback.call(jsonString);
            }

            @Override
            public void onUserReconnecting(String s) {

            }

            @Override
            public void onUserReconnected(String s) {

            }

            @Override
            public void onUserLeft(String remoteUserID) {
                Gson gson = new Gson();
                UserLeftEvent userLeftEvent = new UserLeftEvent(remoteUserID);
                String jsonString = gson.toJson(userLeftEvent);
                Log.i(TAG, "onUserJoined : " + jsonString);
                callback.call(jsonString);
            }

            @Override
            public void onUserPublished(String remoteUserID, List<QNRemoteTrack> list) {
                addRemoteAudioTracks(list);

                Gson gson = new Gson();
                UserPublishedEvent userPublishedEvent = new UserPublishedEvent(remoteUserID, list);
                String jsonString = gson.toJson(userPublishedEvent);
                Log.i(TAG, "onUserPublished : " + jsonString);
                callback.call(jsonString);
            }

            @Override
            public void onUserUnpublished(String remoteUserID, List<QNRemoteTrack> list) {
                remoteRemoteAudioTracks(list);

                Gson gson = new Gson();
                UserUnpublishedEvent userUnpublishedEvent = new UserUnpublishedEvent(remoteUserID, list);
                String jsonString = gson.toJson(userUnpublishedEvent);
                Log.i(TAG, "onUserUnpublished : " + jsonString);
                callback.call(jsonString);
            }

            @Override
            public void onSubscribed(String remoteUserID, List<QNRemoteAudioTrack> list, List<QNRemoteVideoTrack> list1) {
                Gson gson = new Gson();
                SubscribedEvent subscribedEvent = new SubscribedEvent(remoteUserID, list);
                String jsonString = gson.toJson(subscribedEvent);
                Log.i(TAG, "onSubscribed : " + jsonString);
                callback.call(jsonString);
            }

            @Override
            public void onMessageReceived(QNCustomMessage qnCustomMessage) {

            }

            @Override
            public void onMediaRelayStateChanged(String s, QNMediaRelayState qnMediaRelayState) {

            }
        });
        return null;
    }

    public void join(String token) {
        Log.i(TAG, "join : " + token);
        client.join(token);
    }

    public void publishAudio(String message) {
        Log.i(TAG, "publishAudio : " + message);
        if (microphoneAudioTrack == null) {
            microphoneAudioTrack = QNRTC.createMicrophoneAudioTrack();
        }
        client.publish(microphoneAudioTrack);
    }

    public void unpublishAudio(String message) {
        Log.i(TAG, "unpublishAudio : " + message);
        if (microphoneAudioTrack == null) {
            Log.e(TAG, "error : no microphoneAudioTrack to unpublished !");
            return;
        }
        client.unpublish(microphoneAudioTrack);
    }

    public void subscribeAudio(String trackIDs) {
        Log.i(TAG, "subscribeAudio : " + trackIDs);
        Gson gson = new Gson();
        String[] trackIDArray = gson.fromJson(trackIDs, String[].class);
        List<QNRemoteAudioTrack> tracks = findTracksByIDs(trackIDArray);
        client.subscribe((QNRemoteTrack) tracks);
    }

    public void unsubscribeAudio(String trackIDs) {
        Log.i(TAG, "unsubscribeAudio : " + trackIDs);
        Gson gson = new Gson();
        String[] trackIDArray = gson.fromJson(trackIDs, String[].class);
        List<QNRemoteAudioTrack> tracks = findTracksByIDs(trackIDArray);
        client.unsubscribe((QNRemoteTrack) tracks);
    }

    public void leave(String msg) {
        if (client == null) {
            return;
        }
        client.leave();
    }

    public void setRemoteVolume(String message) {
        Log.i(TAG, "setRemoteVolume : " + message);
        Gson gson = new Gson();
        RemoteVolume remoteVolume = gson.fromJson(message, RemoteVolume.class);
        QNRemoteAudioTrack track = findTrackByID(remoteVolume.trackID);
        if (track != null) {
            track.setVolume(remoteVolume.volume);
        }
    }

    public void setLocalVolume(String volume) {
        microphoneAudioTrack.setVolume(Double.parseDouble(volume));
    }

    public void setLocalMuted(String isMutedStr) {
        boolean isMuted = "true".equals(isMutedStr);
        microphoneAudioTrack.setMuted(isMuted);
    }

    private void addRemoteAudioTracks(List<QNRemoteTrack> list) {
        for (QNRemoteTrack track : list) {
            if (track.isAudio()) {
                remoteAudioTracks.add((QNRemoteAudioTrack) track);
            }
        }
    }

    private void remoteRemoteAudioTracks(List<QNRemoteTrack> list) {
        Iterator<QNRemoteAudioTrack> it = remoteAudioTracks.iterator();
        while (it.hasNext()) {
            QNRemoteAudioTrack remoteAudioTrack = it.next();
            for (QNRemoteTrack track : list) {
                if (remoteAudioTrack.getTrackID().equals(track.getTrackID())) {
                    it.remove();
                }
            }
        }
    }

    private List<QNRemoteAudioTrack> findTracksByIDs(String[] trackIDs) {
        List<QNRemoteAudioTrack> remoteAudioTracks = null;
        for (String trackID : trackIDs) {
            QNRemoteAudioTrack track = findTrackByID(trackID);
            if (track != null) {
                remoteAudioTracks.add(track);
            }
        }
        return remoteAudioTracks;
    }

    private QNRemoteAudioTrack findTrackByID(String trackID) {
        for (QNRemoteAudioTrack track : this.remoteAudioTracks) {
            if (track.getTrackID().equals(trackID)) {
                return track;
            }
        }
        return null;
    }
}
