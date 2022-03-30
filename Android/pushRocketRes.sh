RES_PATH=/sdcard/Android/data/com.immomo.xengine.demo/files/rocket

adb shell rm -rf $RES_PATH
adb push $1  $RES_PATH