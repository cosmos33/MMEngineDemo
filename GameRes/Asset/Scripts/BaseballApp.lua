
require("Asset.Scripts.native.init")
require("Asset.Scripts.native.QNRTCHandler")

ROOM_TOKEN = "QxZugR8TAhI38AiJ_cptTl3RbzLyca3t-AAiH-Hh:zyoqWqariEh5-cHGT-cxkvakB7Y=:eyJhcHBJZCI6ImQ4bGs3bDRlZCIsImV4cGlyZUF0IjoxNjQ4NzE3NzA0LCJwZXJtaXNzaW9uIjoidXNlciIsInJvb21OYW1lIjoibW1zZGsiLCJ1c2VySWQiOiJsdWEifQ=="

local GameMainScene = require("Asset.Scripts.BaseballScene")
package.loaded["Asset.Scripts.BaseballGlobal"]  = nil

function ONLINE_LOG(log)
    print(tostring(os.time()) ..  "hk --  LOG:  " .. tostring(log))
end

local App = {}

function App.onStart()
    ONLINE_LOG("onStart")
    if App.mainScene then
        return
    end

    local scene = GameMainScene:new("com.wemomo.engine")
    App.mainScene = scene
    xe.Director:GetInstance():PushScene(scene)

    -- RTCSDK 初始化
    QNRTCHandler:init(function(msg)
        ONLINE_LOG(msg)
        local event = XEJSON.decode(msg)

        local switch = {
            -- 收到自己加入房间的结果通知
            ["onConnectionStateChanged"] = function()
                -- 加入房间成功
                if event.connectionState == "CONNECTED" then
                    -- 加入房间成功之后，就可以发布自己的音频数据
                    QNRTCHandler:publishAudio()
                end
            end;
            
            -- 收到远端用户加入房间通知
            ["onUserJoined"] = function()
                local remoteUserID = event.remoteUserID
                local userData = event.userData
                ONLINE_LOG("onUserJoined - " .. "remoteUserID : " .. remoteUserID .. " userData : " .. userData)
            end;

            -- 收到远端用户发布数据的回调
            ["onUserPublished"] = function()
                ONLINE_LOG("onUserPublished - " .. "remoteUserID : " .. event.remoteUserID .. " audioTrackID : " .. event.audioTrackID)
                local trackIDList = {event.audioTrackID}
                QNRTCHandler:subscribeAudio(trackIDList)
            end;

            -- 收到远端用户取消发布的回调
            ["onUserUnpublished"] = function()
                local remoteUserID = event.remoteUserID
                local audioTrackID = event.audioTrackID
                ONLINE_LOG("onUserUnpublished - " .. "remoteUserID : " .. remoteUserID .. " audioTrackID : " .. audioTrackID)
            end;

            -- 收到自己订阅远端用户成功的回调
            ["onSubscribed"] = function()
                local remoteUserID = event.remoteUserID
                local oneAudioTrackID = event.audioTrackIDList[1]
                ONLINE_LOG("onSubscribed - " .. "remoteUserID : " .. remoteUserID .. " oneAudioTrackID : " .. oneAudioTrackID)
                -- 获取订阅成功的 trackID 列表，就可以调整该远端 track 的音量
                QNRTCHandler:setRemoteVolume(oneAudioTrackID, 2.0)
            end;

            -- 收到远端用户离开房间的回调
            ["onUserLeft"] = function()
                local remoteUserID = event.remoteUserID
                ONLINE_LOG("onUserLeft - " .. "remoteUserID : " .. remoteUserID)
            end;

            -- 收到远端用户正在重连的回调
            ["onUserReconnecting"] = function()
                local remoteUserID = event.remoteUserID
                ONLINE_LOG("onUserReconnecting - " .. "remoteUserID : " .. remoteUserID)
            end;

            -- 收到远端用户重连成功的回调
            ["onUserReconnected"] = function()
                local remoteUserID = event.remoteUserID
                ONLINE_LOG("onUserReconnected - " .. "remoteUserID : " .. remoteUserID)
            end;

            -- 收到本地音频路由改变时会触的回调
            ["onAudioRouteChanged"] = function ()
                local audioDevice = event.audioDevice
                -- 当前播放音频的设备为 audioDevice
                ONLINE_LOG("onAudioRouteChanged - " .. "audioDevice : " .. audioDevice)
            end;
        }

        local fun = switch[event.eventName]
        if(fun) then
            fun()
        end
    end
    );

    --加入房间
    QNRTCHandler:join(ROOM_TOKEN);

    scene:SetGameOverCallBack(function(score)
        ONLINE_LOG("Romve Self.")
        local ret = {
            score = score
        }
        xe.ScriptBridge:call("GameHandler", "onGameOver", xjson.encode(ret))
    end)
end

function App.onResume()
    ONLINE_LOG("onResume")
end

function App.onPause()
    ONLINE_LOG("onPause")
end

function App.onEnd()
    ONLINE_LOG("onEnd")
    --引擎反初始化
    QNRTCHandler:deinit()
end

xe.AppDeleggate = App
