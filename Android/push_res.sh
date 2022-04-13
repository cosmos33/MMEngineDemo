RES_PATH=/sdcard/Android/data/com.immomo.xengine.demo/files/

adb shell rm -rf $RES_PATH
adb push ../GameRes $RES_PATH/demo
adb push ../avatarDemo $RES_PATH/avatarDemo