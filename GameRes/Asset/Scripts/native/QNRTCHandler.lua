QNRTCHandler = QNRTCHandler or {}

-- 官方文档
-- https://developer.qiniu.com/rtc/8802/pd-overview

-- 初始化 RTC
function QNRTCHandler:init( rtcEvent) 
    local rtcSetting = {}
    rtcSetting["sampleRate"]    = 16000
    rtcSetting["channelCount"]  = 1
    rtcSetting["bitrate"]       = 24
    
    local setting = xjson.encode(rtcSetting)
    xe.ScriptBridge:callAsync("RTCHandler", "init", setting, rtcEvent)
end

-- 加入房间
function QNRTCHandler:join(token)
    xe.ScriptBridge:call("RTCHandler", "join", token)
end

-- 离开房间
function QNRTCHandler:leave()
    xe.ScriptBridge:call("RTCHandler", "leave", "")
end

-- 发布本地麦克风音频 Track
function QNRTCHandler:publishAudio()
    xe.ScriptBridge:call("RTCHandler", "publishAudio", "")
end

-- 取消发布本地麦克风音频 Track
function QNRTCHandler:unpublishAudio()
    xe.ScriptBridge:call("RTCHandler", "unpublishAudio", "")
end

-- 订阅远端音频 Track
function QNRTCHandler:subscribeAudio(audioTrackIDList)
    xe.ScriptBridge:call("RTCHandler", "subscribeAudio", xjson.encode(audioTrackIDList))
end

-- 取消订阅远端音频 Track
function QNRTCHandler:unsubscribeAudio(audioTrackIDList)
    xe.ScriptBridge:call("RTCHandler", "unsubscribeAudio", xjson.encode(audioTrackIDList))
end

-- 设置远端 Track 的音量, volume : [0.0-10.0], 默认为 1.0
function QNRTCHandler:setRemoteVolume(trackID, volume)
    local param = {}
    param["trackID"] = trackID;
    param["volume"] = volume;
    local message = xjson.encode(param)
    xe.ScriptBridge:call("RTCHandler", "setRemoteVolume", message)
end

-- 设置本地麦克风采集的音量, volume : [0.0-10.0], 默认为 1.0
function QNRTCHandler:setLocalVolume(volume)
    xe.ScriptBridge:call("RTCHandler", "setLocalVolume", volume)
end

-- 设置本地麦克风是否静音
function QNRTCHandler:setLocalMuted(isMuted) 
    xe.ScriptBridge:call("RTCHandler", "setLocalMuted", isMuted)
end

-- 通过远端音频 TrackID 得到与之对应的 UserID
function QNRTCHandler:getUserIDByTrack(remoteAudioTrackID)
    return xe.ScriptBridge:call("RTCHandler", "getUserID", remoteAudioTrackID)
end

-- 通过远端 UserID 得到与之对应的音频 TrackID
function QNRTCHandler:getTrackIDByUser(remoteUserID)
    return xe.ScriptBridge:call("RTCHandler", "getTrackID", remoteUserID)
end

function QNRTCHandler:setAutoSubscribe(autoSubscribe)
    xe.ScriptBridge:call("RTCHandler", "setAutoSubscribe", xjson.encode(autoSubscribe))
end

-- 销毁 QNRTC
function QNRTCHandler:deinit()
    xe.ScriptBridge:call("RTCHandler", "deinit", "")
end