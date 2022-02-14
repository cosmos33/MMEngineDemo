RES_PATH=/sdcard/Android/data/com.immomo.xengine.demo/files/demo

adb shell rm -rf $RES_PATH
adb push ../GameRes $RES_PATH