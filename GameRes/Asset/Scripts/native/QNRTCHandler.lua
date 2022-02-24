QNRTCHandler = QNRTCHandler or {}

ROOM_TOKEN = "QxZugR8TAhI38AiJ_cptTl3RbzLyca3t-AAiH-Hh:zUbLZLgXvkTrZCyDGYnuIS-fE3Q=:eyJhcHBJZCI6ImQ4bGs3bDRlZCIsImV4cGlyZUF0IjoxNjQ1NzY5NTAxLCJwZXJtaXNzaW9uIjoidXNlciIsInJvb21OYW1lIjoic2RrMTExIiwidXNlcklkIjoibHVhIn0="

-- 初始化 RTC
function QNRTCHandler:init(rtcEvent) 
    local ret = {}
    ret[0] = "hello"
    ret[1] = "volue"

    local str = xjson.encode(ret)
    xe.ScriptBridge:callAsync("RTCHandler", "init", str, rtcEvent)
end

-- 加入房间
function QNRTCHandler:join(token)
    xe.ScriptBridge:call("RTCHandler", "join", ROOM_TOKEN)
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

-- 设置本地麦克风采集的音量, volume : 
function QNRTCHandler:setLocalAudioVolume(volume)
    xe.ScriptBridge:call("RTCHandler", "setLocalVolume", volume)
end


function QNRTCHandler:setLocalMuted(isMuted) 
    xe.ScriptBridge:call("RTCHandler", "setLocalMuted", isMuted)
end

-- 离开房间
function QNRTCHandler:leave()
    xe.ScriptBridge:call("RTCHandler", "leave", "")
end