package com.immomo.xengine.demo;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.immomo.xengine.rtc.model.AudioRouteChangedEvent;
import com.immomo.xengine.rtc.model.ConnectStateChangedEvent;
import com.immomo.xengine.rtc.model.RemoteAudioUser;
import com.immomo.xengine.rtc.model.RemoteVolume;
import com.immomo.xengine.rtc.model.SubscribedEvent;
import com.immomo.xengine.rtc.model.UserJoinedEvent;
import com.immomo.xengine.rtc.model.UserLeftEvent;
import com.immomo.xengine.rtc.model.UserPublishedEvent;
import com.immomo.xengine.rtc.model.UserReconnectedEvent;
import com.immomo.xengine.rtc.model.UserReconnectingEvent;
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
import com.qiniu.droid.rtc.QNRTCEventListener;
import com.qiniu.droid.rtc.QNRemoteAudioTrack;
import com.qiniu.droid.rtc.QNRemoteTrack;
import com.qiniu.droid.rtc.QNRemoteVideoTrack;
import com.qiniu.droid.rtc.model.QNAudioDevice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class RTCHandler {
    private static final String TAG = "RTCHandler : hk --  LOG";

    private Activity activity;
    private QNRTCClient client;
    private QNMicrophoneAudioTrack microphoneAudioTrack;
    private List<RemoteAudioUser> remoteAudioUsers = new ArrayList<>();

    public RTCHandler(Activity activity) {
        this.activity = activity;
    }

    public String init(String message, ScriptBridge.Callback callback) {
        Log.i(TAG, "init : setting : " + message);

        QNRTC.init(activity, new QNRTCEventListener() {
            @Override
            public void onAudioRouteChanged(QNAudioDevice qnAudioDevice) {
                Gson gson = new Gson();
                AudioRouteChangedEvent audioRouteChangedEvent = new AudioRouteChangedEvent(qnAudioDevice);
                String jsonString = gson.toJson(audioRouteChangedEvent);
                Log.i(TAG, "onAudioRouteChanged : " + jsonString);
                callback.call(jsonString);
            }
        });

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
            public void onUserReconnecting(String remoteUserID) {
                Gson gson = new Gson();
                UserReconnectingEvent userReconnectingEvent = new UserReconnectingEvent(remoteUserID);
                String jsonString = gson.toJson(userReconnectingEvent);
                Log.i(TAG, "onUserReconnecting : " + jsonString);
                callback.call(jsonString);
            }

            @Override
            public void onUserReconnected(String remoteUserID) {
                Gson gson = new Gson();
                UserReconnectedEvent userReconnectedEvent = new UserReconnectedEvent(remoteUserID);
                String jsonString = gson.toJson(userReconnectedEvent);
                Log.i(TAG, "onUserReconnected : " + jsonString);
                callback.call(jsonString);
            }

            @Override
            public void onUserLeft(String remoteUserID) {
                Gson gson = new Gson();
                UserLeftEvent userLeftEvent = new UserLeftEvent(remoteUserID);
                String jsonString = gson.toJson(userLeftEvent);
                Log.i(TAG, "onUserLeft : " + jsonString);
                callback.call(jsonString);
            }

            @Override
            public void onUserPublished(String remoteUserID, List<QNRemoteTrack> list) {
                String remoteAudioTrackID = addRemoteAudioTracks(remoteUserID, list);
                if (remoteAudioTrackID == null) {
                    Log.e(TAG, "there are no remote audio tracks by user published! ");
                    return;
                }

                Gson gson = new Gson();
                UserPublishedEvent userPublishedEvent = new UserPublishedEvent(remoteUserID, remoteAudioTrackID);
                String jsonString = gson.toJson(userPublishedEvent);
                Log.i(TAG, "onUserPublished : " + jsonString);
                callback.call(jsonString);
            }

            @Override
            public void onUserUnpublished(String remoteUserID, List<QNRemoteTrack> list) {
                String remoteAudioTrackID = remoteRemoteAudioTracks(list);
                if (remoteAudioTrackID == null) {
                    Log.e(TAG, "there are no remote audio tracks to be found published, so we can't remove it when unpublished.");
                    return;
                }

                Gson gson = new Gson();
                UserUnpublishedEvent userUnpublishedEvent = new UserUnpublishedEvent(remoteUserID, remoteAudioTrackID);
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
        Log.i(TAG, "publishAudio - MicrophoneAudioTrack !");
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

    public void deinit() {
        microphoneAudioTrack = null;
        client = null;
        remoteAudioUsers.clear();
        QNRTC.deinit();
    }

    public void subscribeAudio(String trackIDs) {
        Log.i(TAG, "subscribeAudio : " + trackIDs);
        Gson gson = new Gson();
        String[] trackIDArray = gson.fromJson(trackIDs, String[].class);
        Log.i(TAG, "subscribeAudio : find track11 : " + trackIDs.toString());

        List<QNRemoteTrack> tracks = findTracksByIDs(trackIDArray);
        Log.i(TAG, "subscribeAudio : find track22 : " + tracks.get(0).getTrackID());
        client.subscribe(tracks);
    }

    public void unsubscribeAudio(String trackIDs) {
        Log.i(TAG, "unsubscribeAudio : " + trackIDs);
        Gson gson = new Gson();
        String[] trackIDArray = gson.fromJson(trackIDs, String[].class);
        List<QNRemoteTrack> tracks = findTracksByIDs(trackIDArray);
        client.unsubscribe(tracks);
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

    public String getUserID(String remoteAudioTrackID) {
        for (RemoteAudioUser user : remoteAudioUsers) {
            if (user.getAudioTrack().getTrackID().equals(remoteAudioTrackID)) {
                return user.getUserID();
            }
        }
        return null;
    }

    public String getTrackID(String remoteUserID) {
        for (RemoteAudioUser user : remoteAudioUsers) {
            if (user.getUserID().equals(remoteUserID)) {
                return user.getAudioTrack().getTrackID();
            }
        }
        return null;
    }

    private String addRemoteAudioTracks(String remoteUserID, List<QNRemoteTrack> list) {
        for (QNRemoteTrack track : list) {
            if (track.isAudio()) {
                remoteAudioUsers.add(new RemoteAudioUser(remoteUserID, (QNRemoteAudioTrack) track));
                return track.getTrackID();
            }
        }
        return null;
    }

    private String remoteRemoteAudioTracks(List<QNRemoteTrack> list) {
        Iterator<RemoteAudioUser> it = remoteAudioUsers.iterator();
        while (it.hasNext()) {
            RemoteAudioUser remoteAudioUser = it.next();
            for (QNRemoteTrack track : list) {
                if (remoteAudioUser.getAudioTrack().getTrackID().equals(track.getTrackID())) {
                    it.remove();
                    return track.getTrackID();
                }
            }
        }
        return null;
    }

    private List<QNRemoteTrack> findTracksByIDs(String[] trackIDs) {
        List<QNRemoteTrack> remoteAudioTracks = null;
        for (String trackID : trackIDs) {
            QNRemoteTrack track = findTrackByID(trackID);
            if (track != null) {
                if (remoteAudioTracks == null) {
                    remoteAudioTracks = new ArrayList<>();
                }
                remoteAudioTracks.add(track);
            }
        }
        return remoteAudioTracks;
    }

    private QNRemoteAudioTrack findTrackByID(String trackID) {
        for (RemoteAudioUser user : this.remoteAudioUsers) {
            if (user.getAudioTrack().getTrackID().equals(trackID)) {
                return user.getAudioTrack();
            }
        }
        return null;
    }
}
