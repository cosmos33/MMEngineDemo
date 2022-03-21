```
QNRTCHandler {
	void init(rtcSetting, rtcEvent) // 初始化引擎
	void join(token) // 加入房间
	void leave() // 离开房间
	void deinit() // 销毁 QNRTC

	void setAutoSubscribe(autoSubscribe) // 设置是否自动订阅（默认自动订阅）
	void publishAudio() // 发布本地麦克风音频 Track
	void unpublishAudio() // 取消发布本地麦克风音频 Track
	void subscribeAudio(audioTrackIDList) // 订阅远端音频 Track
	void unsubscribeAudio(audioTrackIDList) // 取消订阅远端音频 Track
	
	void setRemoteVolume(trackID, volume) // 设置远端 Track 的音量, volume : [0.0-10.0], 默认为 1.0
	void setLocalVolume(volume) // 设置本地麦克风采集的音量, volume : [0.0-10.0], 默认为 1.0
	void setLocalMuted(isMuted) // 设置本地麦克风是否静音
	
	String getUserIDByTrack(remoteAudioTrackID) // 通过远端音频 TrackID 得到与之对应的 UserID
	void getTrackIDByUser(userID) // 通过远端 UserID 得到与之对应的音频 TrackID
}
```

```
// rtcSetting 是在 RTC 初始化 ( init(rtcSetting, rtcEvent) ) 时候传入的配置
// 可以通过 rtcSetting 设置如下参数
local rtcSetting {}
	rtcSetting["audioQuality"] = "STANDARD"
	rtcSetting["communicationMode"] = "ON"
	
	// RTCSetting["audioQuality"] = "LOW" // 采样率 = 16000，声道数 = 1，码率 = 24kb（默认值）
	// RTCSetting["audioQuality"] = "STANDARD" // 采样率 = 48000，声道数 = 1，码率 = 64kb
	// RTCSetting["audioQuality"] = "STANDARD_STEREO" // 采样率 = 48000，声道数 = 2，码率 = 80kb
	// RTCSetting["audioQuality"] = "HIGH" // 采样率 = 48000，声道数 = 1，码率 = 96kb
	// RTCSetting["audioQuality"] = "HIGH_STEREO" // 采样率 = 48000，声道数 = 2，码率 = 128kb
		
	// RTCSetting["communicationMode"] = "ON" // 开启通话模式（默认为开启）
	// RTCSetting["communicationMode"] = "OFF" // 关闭通话模式
```

```
// rtcEvent 是在 RTC 初始化 ( init(rtcSetting, rtcEvent) ) 时候传入的参数
// rtcEvent 是 QNRTCSDK 向 Lua 层传递的回调事件，可以在相应的事件中加入自己的处理逻辑。

local rtcEvent = function(event)
	
	event.eventName = "onConnectionStateChanged" // 当房间状态改变时会触发此回调
	// event.connectionState : 链接状态
	// event.connectionState = "DISCONNECTED" : 链接断开，退出房间
	// event.connectionState = "CONNECTING" : 正在连接中
	// event.connectionState = "CONNECTED" : 链接成功，成功加入房间
	// event.connectionState = "RECONNECTING" : 正在重连中
	// event.connectionState = "RECONNECTED" : 重连成功
	// event.disconnectedReason : 链接失败原因，仅仅在 "DISCONNECTED" 状态下才有值
	// event.disconnectedReason = "LEAVE" : 自己退出房间，调用 leave 方法
	// event.disconnectedReason = "KICKED_OUT" : 被别人踢出房间
	// event.disconnectedReason = "ROOM_CLOSED" : 房间已关闭
	// event.disconnectedReason = "ROOM_FULL" : 房间人数已满
	// event.disconnectedReason = "ERROR" : 非预期错误
	// event.disconnectedErrorCode :  链接断开错误码，仅仅在 "DISCONNECTED" 状态下才有值
	// event.disconnectedErrorMessage : 链接断开的信息，仅仅在 "DISCONNECTED" 状态下才有值
	
	event.eventName = "onUserJoined" // 当远端用户加入房间时会触发此回调
	// event.remoteUserID : 加入房间的远端用户 ID
	// event.userData : 加入房间的远端用户带有的数据
	
	event.eventName = "onUserPublished" // 当远端 Track 发布时会触发此回调
	// event.remoteUserID : 发布音频 Track 的远端用户 ID
	// event.audioTrackID : 远端用户发布的音频 TrackID
	
	event.eventName = "onUserUnpublished" // 当远端用户取消发布音频 Track 时会触发此回调
	// event.remoteUserID : 取消发布音频 Track 的远端用户 ID
	// event.audioTrackID : 远端用户取消发布的音频 TrackID
	
	event.eventName = "onSubscribed" // 当订阅 Track 成功时会触发此回调
	// event.remoteUserID : 订阅的音频 Track 属于哪个远端用户
	// event.audioTrackIDList : 订阅成功的音频 Track
	
	event.eventName = "onUserLeft" // 当远端用户离开房间时会触发此回调
	// event.remoteUserID : 离开房间的远端用户的用户 ID
	
	event.eventName = "onUserReconnecting" // 当远端用户进入重连时会触发此回调
	// event.remoteUserID : 正在重连的远端用户的用户 ID
	
	event.eventName = "onUserReconnected" // 当远端用户重连成功时会触发此回调
	// event.remoteUserID : 重连成功的远端用户的用户 ID
	
	event.eventName = "onAudioRouteChanged" // 当本地音频路由改变时会触发此回调
	// event.audioDevice = SPEAKER_PHONE : 音频路播放为扬声器
	// event.audioDevice = EARPIECE : 音频路播放为听筒
	// event.audioDevice = WIRED_HEADSET : 音频路播放为有线耳机
	// event.audioDevice = BLUETOOTH : 音频路播放为蓝牙设备
	// event.audioDevice = NONE : 异常情况
	
end
}
```