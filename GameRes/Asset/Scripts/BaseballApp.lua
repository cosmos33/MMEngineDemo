
require("Asset.Scripts.native.init")
require("Asset.Scripts.native.QNRTCHandler")

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

    QNRTCHandler:init(function(msg)
        ONLINE_LOG(msg)
        local event = XEJSON.decode(msg)

        local switch = {
            ["onConnectionStateChanged"] = function()
                if event.connectionState == "CONNECTED" then
                    QNRTCHandler:publishAudio()
                end
            end;

            ["onUserPublished"] = function()
                ONLINE_LOG("onUserPublished - " .. "remoteUserID : " .. event.remoteUserID .. " audioTrackIDList : " .. event.audioTrackIDList)
                QNRTCHandler:subscribeAudio(event.audioTrackIDList)
            end;

            ["onUserUnpublished"] = function()
                ONLINE_LOG("onUserPublished - " .. "remoteUserID : " .. event.remoteUserID .. " audioTrackIDList : " .. event.audioTrackIDList)
            end;

            ["onSubscribed"] = function()
                ONLINE_LOG("onSubscribed - " .. "remoteUserID : " .. event.remoteUserID .. " audioTrackIDList : " .. event.audioTrackIDList)
                QNRTCHandler:setRemoteVolume(event.audioTrackIDList[0], 2.0)
            end;

            ["onUserJoined"] = function()
                local remoteUserID = event.remoteUserID
                local userData = event.userData
                ONLINE_LOG("onUserJoined - " .. "remoteUserID : " .. event.remoteUserID .. " userData" .. event.userData)
            end;

            ["onUserLeft"] = function()
                local remoteUserID = event.remoteUserID
                ONLINE_LOG("onUserLeft - " .. "remoteUserID : " .. event.remoteUserID)
            end
        }

        local fun = switch[event.eventName]
        if(fun) then
            fun()
        else
            ONLINE_LOG("33333")
        end
    end
    );

    QNRTCHandler:join();

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
end

xe.AppDeleggate = App
