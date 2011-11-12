Port of the Love2D api to Android using LuaJ and OpenGl
=======================================================

About
-----

can start existing .love files from sd card (currently need to be unpacked manually, but we want to add .love direct loading eventually)
currently the path to the .love folder on the sd card is hardcoded in LoveAndroid.java
we should be able to add a file browser for loading eventually.
The project could be used to make a ready to use launcher with a nice menu, or to make a standalone app without using sdcard by adding the files as resources (resource load might not be implemented yet as of 2011-11-06)
We plan to make android specific functionality like additional input available by an api extension called love.android, e.g. multi-touch, gravi and accelerometer sensors etc.

build environment used is eclipse with android sdk

adb logcat - to see Log.i("TAG","lala") in the terminal
it is necessary to set ANDROID_HOME in the eclipse environment
sdcard directory gets synced into the app directory on the phone's sdcard

for android-aware games :
set t.android_native_screen = true in conf.lua to keep the native screen resolution, otherwise it'll rescale to the pc resolution
to write generic code that detects android, check for love.android being set, this will be set to an empty table for now, but might get android specific functions later

website : http://ghoulsblade.schattenkind.net/wiki/index.php/Love2d-android

### MISSING FUNCTIONALITY
* lots of api calls still need to be implemented
* ttf/truetype fonts won't be available anytime soon, help appreciated
* love.physics / box2d will probably not be available anytime soon, help appreciated
* love.threads has dummy stubs, so calling doesn't trigger errors, but the thread-code will never be executed, probably won't be implemented anytime soon, help appreciated

### WARNING
* 2011-11-04 luaj 2.0.2 : it seems i found a bug, testing Stealth2D(git) i get "invalid key to 'next'" when removing entries from table during pairs-iteration, sample snippet in core.lua for testing =(
	bugtracker entry already exists : http://sourceforge.net/tracker/?func=detail&aid=3430986&group_id=197627&atid=962226

### Things not supported by luaj
* newproxy
* module

### SETUP of sdk : 
(if you want to use eclipse to compile, e.g. for standalone without sdcard access or to help out in this project) 

in linux : 
adjust your ~/.bashrc to set ANDROID_HOME before launching eclipse
export ANDROID_HOME=/home/ghoul/cavern/eclipse-workspace/android-sdk-linux
when launching eclipse, launch it from bash 

if you want to copy everything to the sd card again, delete ./tools/push_dir.db  (a database file where it stores md5-sum)

NOTE : this guide looks a bit like what we did : http://linuxconfig.org/get-started-with-android-application-development-using-linux-and-android-sdk

### BUILD publish/release a binary/apk

remove all Log.i() and Log.e() calls and the import line (central function in LoveVM.java)

eclipse : open manifest editor : scroll down in manifest tab : 
Use the Export Wizard to export and sign an AP			<---- USED

for android 2.1 : change project.properties : 
target=android-8
to
target=android-7
