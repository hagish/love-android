adb logcat - to see Log.i("TAG","lala") in the terminal
it is necessary to set ANDROID_HOME in the eclipse environment
sdcard directory gets synced into the app directory on the phone's sdcard

for android-aware games :
set t.android_native_screen = true in conf.lua to keep the native screen resolution, otherwise it'll rescale to the pc resolution
to write generic code that detects android, check for love.android being set, this will be set to an empty table for now, but might get android specific functions later

WARNING:
2011-11-04 luaj 2.0.2 : it seems i found a bug, testing Stealth2D(git) i get "invalid key to 'next'" when removing entries from table during pairs-iteration, sample snippet in core.lua for testing =(
