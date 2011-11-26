Port of the Love2D api to Android using LuaJ and OpenGl
=======================================================

About
-----

can start existing .love files from sd card (/mnt/sdcard/love/mygame.love) or downloads folder
* we should be able to add a file browser for loading eventually.
* The project could be used to make a ready to use launcher with a nice menu, or to make a standalone app without using sdcard by adding the files as resources (resource load might not be implemented yet as of 2011-11-06)
* We plan to make android specific functionality like additional input available by an api extension called love.phone, e.g. multi-touch, gravi and accelerometer sensors etc.

build environment used is eclipse with android sdk

* adb logcat - to see Log.i("TAG","lala") in the terminal
** it is necessary to set ANDROID_HOME in the eclipse environment
** sdcard directory gets synced into the app directory on the phone's sdcard

* for android-aware games :
** set t.android_native_screen = true in conf.lua to keep the native screen resolution, otherwise it'll rescale to the pc resolution
** to write generic code that detects android, check for love.phone being set, this will be set to an empty table for now, but might get android specific functions later

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

*in linux : 
** adjust your ~/.bashrc to set ANDROID_HOME before launching eclipse
** export ANDROID_HOME=/home/ghoul/cavern/eclipse-workspace/android-sdk-linux
** when launching eclipse, launch it from bash 

if you want to copy everything to the sd card again, delete ./tools/push_dir.DEVICE.db  (a database file where it stores md5-sum)

NOTE : this guide looks a bit like what we did : http://linuxconfig.org/get-started-with-android-application-development-using-linux-and-android-sdk

### BUILD publish/release a binary/apk

remove all Log.i() and Log.e() calls and the import line (central function in LoveVM.java)

* eclipse : open manifest editor : scroll down in manifest tab : 
** Use the Export Wizard to export and sign an AP

* for android 2.1 : change project.properties : 
** target=android-8
** to
** target=android-7

### lua.conf settings 

* t.android_allow_screensleep
** if this is not set, then love.phone.setKeepScreenOn(true) is called at startup

* t.android_native_screen
** if this is not set, then love.graphics.setMode(w,h) is called at startup with t.screen.width/height or default pc resolution

### android/phone specific api 


* since this section migth be outdated, see also https://github.com/hagish/love-android/blob/master/src/net/schattenkind/androidLove/luan/LuanPhone.java


* img = love.phone.newResourceImage(int iResID)
** loads an image from a resource id


* source = love.phone.newResourceAudioSource(int iResID,string type)
** loads a sound/music/audio source from a resource id


* String love.phone.getPackageName()
** Return the name of this application's package.


* String love.phone.getResourceName(int iResID)
** Return the full name for a given resource identifier.
** iResID = love.phone.getResourceID(String name, String defType, String defPackage)
** @name The name of the desired resource.
** @defType Optional default resource type to find, if "type/" is not included in the name. Can be null to require an explicit type.
** @defPackage Optional default package to find, if "package:" is not included in the name. Can be null to require an explicit package.


* class LoveSensor
** constructed via {sensor,..} = love.phone.getSensorList(iSensorType)  or getDefaultSensor(iSensorType)
** int 	getLoveSensorID()	uniqueid for love.phone.sensorevent
** float 	getMaximumRange()	maximum range of the sensor in the sensor's unit. 
** int 	getMinDelay()		the minimum delay allowed between two events in microsecond or zero if this sensor only returns a value when the data it's measuring changes. 
** String 	getName()			name string of the sensor. 
** float 	getPower()			the power in mA used by this sensor while in use 
** float 	getResolution()		resolution of the sensor in the sensor's unit. 
** int 	getType()			generic type of this sensor. 
** String 	getVendor()			vendor string of this sensor. 
** int 	getVersion() 		version of the sensor's module. 
** int		getLoveSensorID()	unique id for love.phone.sensorevent


* {sensor,..} = love.phone.getSensorList(iSensorType)
** see also love.phone.SENSOR_TYPE


* sensor = love.phone.getDefaultSensor(iSensorType)
** see also love.phone.SENSOR_TYPE


* love.phone.SENSOR_TYPE = {[name]=value,...}
** see also http://developer.android.com/reference/android/hardware/Sensor.html
** see also http://developer.android.com/reference/android/hardware/SensorEvent.html#values
** TYPE_ACCELEROMETER,TYPE_ALL,TYPE_AMBIENT_TEMPERATURE,TYPE_GRAVITY,TYPE_GYROSCOPE,TYPE_LIGHT,
** TYPE_LINEAR_ACCELERATION,TYPE_MAGNETIC_FIELD,TYPE_ORIENTATION,TYPE_PRESSURE,TYPE_PROXIMITY,
** TYPE_RELATIVE_HUMIDITY,TYPE_ROTATION_VECTOR,


* love.phone.enableTouchEvents()
** must be called once for love.phone.touch callback to be enabled


* love.phone.touch(action,{id1,x1,y1,id2,x2,y2},...)
** USER DEFINED CALLBACK
** for action see also love.phone.MOTION_EVENT_ACTION_TYPE
** you can define this callback similar to love.mousepressed to get detailed touch info
** only if love.phone.enableTouchEvents() has been called
** see http://developer.android.com/reference/android/view/MotionEvent.html for details about multitouch handling


* boolean love.phone.registerSensorListener(sensor,rate)
** Registers a SensorEventListener for the given sensor.
** will call user defined love.phone.sensorevent()


* love.phone.sensorevent(sensorid,{f1,f2,....,accuracy=?,timestamp=?})
** USER DEFINED CALLBACK
** Called when sensor values have changed.
** you can define this callback similar to love.mousepressed to get detailed touch info
** see also http://developer.android.com/reference/android/hardware/SensorEvent.html
		

* love.phone.MOTION_EVENT_ACTION_TYPE = {[name]=value,...} 
** e.g. love.phone.touch(..) event
** see also http://developer.android.com/reference/android/view/MotionEvent.html
** ACTION_CANCEL,ACTION_DOWN,ACTION_HOVER_ENTER,ACTION_HOVER_EXIT,ACTION_HOVER_MOVE,
** ACTION_MASK,ACTION_MOVE,ACTION_OUTSIDE,ACTION_POINTER_1_DOWN,ACTION_POINTER_1_UP,
** ACTION_POINTER_2_DOWN,ACTION_POINTER_2_UP,ACTION_POINTER_3_DOWN,ACTION_POINTER_3_UP,
** ACTION_POINTER_DOWN,ACTION_POINTER_ID_MASK,ACTION_POINTER_ID_SHIFT,ACTION_POINTER_INDEX_MASK,
** ACTION_POINTER_INDEX_SHIFT,ACTION_POINTER_UP,ACTION_SCROLL,ACTION_UP,

* love.phone.performHapticFeedback(feedbackConstant), see love.phone.FEEDBACK_CONSTANT
* love.phone.setHapticFeedbackEnabled(bEnabled)

* love.phone.setKeepScreenOn(bool bKeepScreenOn)

* love.phone.setRequestedOrientation(requestedOrientation), see love.phone.SCREEN_ORIENTATION

* love.phone.setBlockMainKey_Back(bBlocked)
* love.phone.setBlockMainKey_Menu(bBlocked)
* love.phone.setBlockMainKey_Search(bBlocked)
