// phone/android/iphone specific extensions
// set love.phone, so android-aware games can detect it, might be used for additional util functions later

package net.schattenkind.androidLove.luan.module;

import java.util.List;

import net.schattenkind.androidLove.LoveAndroid;
import net.schattenkind.androidLove.LoveVM;
import net.schattenkind.androidLove.luan.LuanBase;
import net.schattenkind.androidLove.luan.LuanObjBase;
import net.schattenkind.androidLove.luan.obj.LuanObjImage;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.MotionEvent;

public class LuanPhone extends LuanBase {
	private boolean mbEnableTouchEvents = false;
	protected static final String TAG = "LovePhone";
	
	static final String sMetaName_LuanSensor = "__MetaLuanSensor";

	private int miNextLoveSensorID = 1;
	
	// ***** ***** ***** ***** ***** constructor

	public LuanPhone(LoveVM vm) {
		super(vm);
	}
	
	// ***** ***** ***** ***** ***** utils 
	
	public void Log (String s) { LoveVM.LoveLog(TAG, s); }

	public int generateNewLoveSensorID () { return miNextLoveSensorID++; }
	
	// ***** ***** ***** ***** ***** init 
	
	public LuaTable InitLib() {
		LuaTable t = LuaValue.tableOf();
		
		LuaValue _G = vm.get_G();
		_G.set(sMetaName_LuanSensor,LuanObjSensor.CreateMetaTable(this));
		
		// TODO: add phone/android/iphone specific api additions here, e.g. multi-touch event stuff, accelerometer, gravity sensor, maybe intent/running apps/network/start browser etc ?
		
		/// img = love.phone.newResourceImage(int iResID)
		/// loads an image from a resource id
		t.set("newResourceImage", new VarArgFunction() {
			@Override public Varargs invoke(Varargs args) {
				int iResID = args.checkint(1);
				try {
					return LuaValue.userdataOf(new LuanObjImage(vm.getLuanGraphics(),iResID),vm.get_G().get(LuanGraphics.sMetaName_LuanImage));
				} catch (Exception e) {
					vm.handleError(e);
				}
				return LuaValue.NONE;
			}
		});
		
		
		/// source = love.phone.newResourceAudioSource(int iResID,string type)
		/// loads a sound/music/audio source from a resource id
		t.set("newResourceAudioSource", new VarArgFunction() {
			@Override public Varargs invoke(Varargs args) {
				int iResID = args.checkint(1);
				try {
					String sType = IsArgSet(args,2) ? args.checkjstring(2) : "static";
					return LuaValue.userdataOf(new LuanAudio.LuanObjSource(vm.getLuanAudio(),iResID,sType),vm.get_G().get(LuanAudio.sMetaName_LuanSource));
				} catch (Exception e) {
					vm.handleError(e);
				}
				return LuaValue.NONE;
			}
		});
		
		// TODO: newResourceFontTTF !!!
		/// source = love.phone.newResourceFontTTF(int iResID,int iSize)
		/// loads a ttf from ressource id
		
		/// String 	love.phone.getPackageName()
		/// Return the name of this application's package.
		t.set("getPackageName", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(vm.getActivity().getPackageName()); } });
		
		
		/// String 	love.phone.getResourceName(int iResID)
		/// Return the full name for a given resource identifier.
		t.set("getResourceName", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { 
			try {
				return LuaValue.valueOf(vm.getResources().getResourceName(args.checkint(1)));
			} catch (Exception e) {
				vm.handleError(e);
			}
			return LuaValue.NONE;
		} });
		
		
		/// iResID = love.phone.getResourceID(String name, String defType, String defPackage)
		/// @name 	The name of the desired resource.
		/// @defType 	Optional default resource type to find, if "type/" is not included in the name. Can be null to require an explicit type.
		/// @defPackage 	Optional default package to find, if "package:" is not included in the name. Can be null to require an explicit package.
		t.set("getResourceID", new VarArgFunction() {
			@Override public Varargs invoke(Varargs args) {
				return LuaValue.valueOf(vm.getResources().getIdentifier(
					args.checkjstring(1),
					IsArgSet(args,2) ? args.checkjstring(2) : null,
					IsArgSet(args,3) ? args.checkjstring(3) : null
					));
			}
		});
		// int 	getIdentifier(String name, String defType, String defPackage)
		
		
		/// {sensor,..} = love.phone.getSensorList(iSensorType)
		/// see also love.phone.SENSOR_TYPE
		t.set("getSensorList", new VarArgFunction() {
			@Override public Varargs invoke(Varargs args) {
				List<Sensor> myList = vm.getSensorManager().getSensorList(args.checkint(1));
				LuaTable t = new LuaTable(myList.size(),0);
				for (int i=0;i<myList.size();++i) t.set(i+1,LuaValue.userdataOf(new LuanObjSensor(LuanPhone.this,myList.get(i)),vm.get_G().get(sMetaName_LuanSensor))); 
				return t;
			}
		});
		
		
		/// sensor = love.phone.getDefaultSensor(iSensorType)
		/// see also love.phone.SENSOR_TYPE
		t.set("getDefaultSensor", new VarArgFunction() {
			@Override public Varargs invoke(Varargs args) {
				Sensor s = vm.getSensorManager().getDefaultSensor(args.checkint(1));
				return LuaValue.userdataOf(new LuanObjSensor(LuanPhone.this,s),vm.get_G().get(sMetaName_LuanSensor));
			}
		});
		
		
		/// love.phone.enableTouchEvents()
		/// enable love.phone.touch(...) callback
		t.set("enableTouchEvents", new VarArgFunction() {
			@Override public Varargs invoke(Varargs args) { mbEnableTouchEvents = true; return LuaValue.NONE; }
		});
		
		
		
		/// boolean love.phone.registerSensorListener(sensor,rate)
		/// Registers a SensorEventListener for the given sensor.
		t.set("registerSensorListener", new VarArgFunction() {
			@Override public Varargs invoke(Varargs args) { 
				LuanObjSensor s = (LuanObjSensor)args.checkuserdata(1,LuanObjSensor.class);
				int rate = args.checkint(2);
				return LuaValue.valueOf(vm.getSensorManager().registerListener(s,s.getSensor(),rate));
			}
		});
		
		/// love.phone.setBlockMainKey_Back(bBlocked)
		t.set("setBlockMainKey_Back", new VarArgFunction() {
			@Override public Varargs invoke(Varargs args) { ((LoveAndroid)vm.getActivity()).setBlockMainKey_Back(args.checkboolean(1)); return LuaValue.NONE; }
		});
		
		/// love.phone.setBlockMainKey_Menu(bBlocked)
		t.set("setBlockMainKey_Menu", new VarArgFunction() {
			@Override public Varargs invoke(Varargs args) { ((LoveAndroid)vm.getActivity()).setBlockMainKey_Menu(args.checkboolean(1)); return LuaValue.NONE; }
		});
		
		/// love.phone.setBlockMainKey_Search(bBlocked)
		t.set("setBlockMainKey_Search", new VarArgFunction() {
			@Override public Varargs invoke(Varargs args) { ((LoveAndroid)vm.getActivity()).setBlockMainKey_Search(args.checkboolean(1)); return LuaValue.NONE; }
		});
		
		/// love.phone.setHapticFeedbackEnabled(bEnabled)
		t.set("setHapticFeedbackEnabled", new VarArgFunction() {
			@Override public Varargs invoke(Varargs args) { vm.getView().setHapticFeedbackEnabled(args.checkboolean(1)); return LuaValue.NONE; }
		});
		
		/// love.phone.performHapticFeedback(feedbackConstant)
		/// see also FEEDBACK_CONSTANT
		t.set("performHapticFeedback", new VarArgFunction() {
			@Override public Varargs invoke(Varargs args) { vm.getView().performHapticFeedback(args.checkint(1)); return LuaValue.NONE; }
		});
		
		
		/// love.phone.setRequestedOrientation(requestedOrientation)
		/// see also SCREEN_ORIENTATION
		t.set("setRequestedOrientation", new VarArgFunction() {
			@Override public Varargs invoke(Varargs args) { vm.getActivity().setRequestedOrientation(args.checkint(1)); return LuaValue.NONE; }
		});
		
		/// love.phone.SCREEN_ORIENTATION = {[name]=value,...}
		/// see also http://developer.android.com/reference/android/content/pm/ActivityInfo.html
		{
			LuaTable c = new LuaTable();
			t.set("SCREEN_ORIENTATION",c);
			c.set("SCREEN_ORIENTATION_BEHIND",				android.content.pm.ActivityInfo.SCREEN_ORIENTATION_BEHIND		); ///< 	Constant corresponding to behind in the screenOrientation attribute.
			//~ c.set("SCREEN_ORIENTATION_FULL_SENSOR",			android.content.pm.ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR	); ///< 	Constant corresponding to fullSensor in the screenOrientation attribute.
			c.set("SCREEN_ORIENTATION_LANDSCAPE",			android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE	); ///< 	Constant corresponding to landscape in the screenOrientation attribute.
			c.set("SCREEN_ORIENTATION_NOSENSOR",			android.content.pm.ActivityInfo.SCREEN_ORIENTATION_NOSENSOR		); ///< 	Constant corresponding to nosensor in the screenOrientation attribute.
			c.set("SCREEN_ORIENTATION_PORTRAIT",			android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT		); ///< 	Constant corresponding to portrait in the screenOrientation attribute.
			//~ c.set("SCREEN_ORIENTATION_REVERSE_LANDSCAPE",	android.content.pm.ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE	); ///< 	Constant corresponding to reverseLandscape in the screenOrientation attribute.
			//~ c.set("SCREEN_ORIENTATION_REVERSE_PORTRAIT",	android.content.pm.ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT	); ///< 	Constant corresponding to reversePortrait in the screenOrientation attribute.
			c.set("SCREEN_ORIENTATION_SENSOR",				android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR	); ///< 	Constant corresponding to sensor in the screenOrientation attribute.
			//~ c.set("SCREEN_ORIENTATION_SENSOR_LANDSCAPE",	android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE	); ///< 	Constant corresponding to sensorLandscape in the screenOrientation attribute.
			//~ c.set("SCREEN_ORIENTATION_SENSOR_PORTRAIT",		android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT	); ///< 	Constant corresponding to sensorPortrait in the screenOrientation attribute.
			c.set("SCREEN_ORIENTATION_UNSPECIFIED",			android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED	); ///< 	Constant corresponding to unspecified in the screenOrientation attribute.
			c.set("SCREEN_ORIENTATION_USER",				android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER	); ///< 	Constant corresponding to user in the screenOrientation attribute.
		}
		
		/// love.phone.FEEDBACK_CONSTANT = {[name]=value,...}
		/// see also http://developer.android.com/reference/android/view/HapticFeedbackConstants.html
		{
			LuaTable c = new LuaTable();
			t.set("FEEDBACK_CONSTANT",c);
			c.set("FLAG_IGNORE_GLOBAL_SETTING",	android.view.HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING 	); ///< Flag for View.performHapticFeedback(int, int): Ignore the global setting for whether to perform haptic feedback, do it always.
			c.set("FLAG_IGNORE_VIEW_SETTING",	android.view.HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING 		); ///< Flag for View.performHapticFeedback(int, int): Ignore the setting in the view for whether to perform haptic feedback, do it always.
			//~ c.set("KEYBOARD_TAP",				android.view.HapticFeedbackConstants.KEYBOARD_TAP 					); ///< The user has pressed a soft keyboard key.	// FIXME: cannot be resolved or is not a field
			c.set("LONG_PRESS",					android.view.HapticFeedbackConstants.LONG_PRESS 					); ///< The user has performed a long press on an object that is resulting in an action being performed.
			c.set("VIRTUAL_KEY",				android.view.HapticFeedbackConstants.VIRTUAL_KEY 					); ///< The user has pressed on a virtual on-screen key.
		}
		
		/// love.phone.SENSOR_TYPE = {[name]=value,...}
		/// see also http://developer.android.com/reference/android/hardware/Sensor.html
		/// see also http://developer.android.com/reference/android/hardware/SensorEvent.html#values
		{
			LuaTable c = new LuaTable();
			t.set("SENSOR_TYPE",c);
			c.set(	"TYPE_ACCELEROMETER",			Sensor.TYPE_ACCELEROMETER		);
			c.set(	"TYPE_ALL",						Sensor.TYPE_ALL					);
			//~ c.set(	"TYPE_AMBIENT_TEMPERATURE",		Sensor.TYPE_AMBIENT_TEMPERATURE	); // FIXME: cannot be resolved or is not a field
			//~ c.set(	"TYPE_GRAVITY",					Sensor.TYPE_GRAVITY				); // FIXME: cannot be resolved or is not a field
			c.set(	"TYPE_GYROSCOPE",				Sensor.TYPE_GYROSCOPE			);
			c.set(	"TYPE_LIGHT",					Sensor.TYPE_LIGHT				);
			//~ c.set(	"TYPE_LINEAR_ACCELERATION",		Sensor.TYPE_LINEAR_ACCELERATION	); // FIXME: cannot be resolved or is not a field
			c.set(	"TYPE_MAGNETIC_FIELD",			Sensor.TYPE_MAGNETIC_FIELD		); // compass
			c.set(	"TYPE_ORIENTATION",				Sensor.TYPE_ORIENTATION			);
			c.set(	"TYPE_PRESSURE",				Sensor.TYPE_PRESSURE			);
			c.set(	"TYPE_PROXIMITY",				Sensor.TYPE_PROXIMITY			);
			//~ c.set(	"TYPE_RELATIVE_HUMIDITY",		Sensor.TYPE_RELATIVE_HUMIDITY	); // FIXME: cannot be resolved or is not a field
			//~ c.set(	"TYPE_ROTATION_VECTOR",			Sensor.TYPE_ROTATION_VECTOR		); // FIXME: cannot be resolved or is not a field
		}
		
		/// love.phone.MOTION_EVENT_ACTION_TYPE = {[name]=value,...} 
		/// e.g. love.phone.touch(..) event
		/// see also http://developer.android.com/reference/android/view/MotionEvent.html
		{
			LuaTable c = new LuaTable();
			t.set("MOTION_EVENT_ACTION_TYPE",c);
			c.set("ACTION_CANCEL"				, MotionEvent.ACTION_CANCEL				 );						
			c.set("ACTION_DOWN"					, MotionEvent.ACTION_DOWN				 );
			c.set("ACTION_MASK"					, MotionEvent.ACTION_MASK				 );
			c.set("ACTION_MOVE"					, MotionEvent.ACTION_MOVE				 );
			c.set("ACTION_OUTSIDE"				, MotionEvent.ACTION_OUTSIDE			 );	
			c.set("ACTION_POINTER_1_DOWN"		, MotionEvent.ACTION_POINTER_1_DOWN		 );			
			c.set("ACTION_POINTER_1_UP"			, MotionEvent.ACTION_POINTER_1_UP		 );		
			c.set("ACTION_POINTER_2_DOWN"		, MotionEvent.ACTION_POINTER_2_DOWN		 );			
			c.set("ACTION_POINTER_2_UP"			, MotionEvent.ACTION_POINTER_2_UP		 );		
			c.set("ACTION_POINTER_3_DOWN"		, MotionEvent.ACTION_POINTER_3_DOWN		 );			
			c.set("ACTION_POINTER_3_UP"			, MotionEvent.ACTION_POINTER_3_UP		 );		
			c.set("ACTION_POINTER_DOWN"			, MotionEvent.ACTION_POINTER_DOWN		 );		
			c.set("ACTION_POINTER_ID_MASK"		, MotionEvent.ACTION_POINTER_ID_MASK	 );			
			c.set("ACTION_POINTER_ID_SHIFT"		, MotionEvent.ACTION_POINTER_ID_SHIFT	 );				
			c.set("ACTION_POINTER_UP"			, MotionEvent.ACTION_POINTER_UP			 );		
			c.set("ACTION_UP"					, MotionEvent.ACTION_UP					 );						
		}
		
		return t;
	}
	
	
	/// calls love.phone.touch(action,{id1,x1,y1,id2,x2,y2,...})
	/// for action see also love.phone.MOTION_EVENT_ACTION_TYPE
	/// you can define this callback similar to love.mousepressed to get detailed touch info
	/// only if love.phone.enableTouchEvents() has been called
	/// see http://developer.android.com/reference/android/view/MotionEvent.html for details about multitouch handling
	public void onTouch (MotionEvent event) {
		if (!mbEnableTouchEvents) return;
		if (!vm.isInitDone()) return;
			
		try {
			LuaTable t = new LuaTable();
			for (int i=0;i<event.getPointerCount();++i) {
				t.set(1+i*3+0,LuaValue.valueOf(event.getPointerId(i)));
				t.set(1+i*3+1,LuaValue.valueOf(event.getX(i)));
				t.set(1+i*3+2,LuaValue.valueOf(event.getY(i)));
			}
			vm.get_G().get("love").get("phone").get("touch").call(LuaValue.valueOf(event.getAction()),t);
		} catch (LuaError e) {
			vm.handleLuaError(e);
		}
	}
	
	/// calls love.phone.main_key_event (sEventName) with sEventName being one of back,menu,search,home,leavehint
	public void fireLuaMainKeyEvent (String sEventName) {
		if (!vm.isInitDone()) return;
		try {
			vm.get_G().get("love").get("phone").get("main_key_event").call(LuaValue.valueOf(sEventName));
		} catch (LuaError e) {
			vm.handleLuaError(e);
		}
	}
		
	public void notifyMainKey_Back		() { fireLuaMainKeyEvent("back"); }
	public void notifyMainKey_Menu		() { fireLuaMainKeyEvent("menu"); }
	public void notifyMainKey_Search	() { fireLuaMainKeyEvent("search"); }
	public void notifyUserLeaveHint		() { fireLuaMainKeyEvent("home"); fireLuaMainKeyEvent("leavehint"); } // assumed home key, migth be others too tho
	
	// ***** ***** ***** ***** *****  LuanSensor
	
	//~ constructed via love.phone.getSensorList(iSensorType)  or getDefaultSensor(iSensorType)
	public static class LuanObjSensor extends LuanObjBase implements SensorEventListener {
		protected static final String TAG = "LoveSensor";
		
		private LuanPhone	phone;
		private Sensor 		mSensor;
		private int 		miLoveSensorID;
		
		// ***** ***** ***** ***** ***** constructor
		
		public LuanObjSensor (LuanPhone phone,Sensor mSensor) { 
			this.phone = phone;
			this.mSensor = mSensor;
			this.miLoveSensorID = phone.generateNewLoveSensorID();
		}
	
		// ***** ***** ***** ***** ***** api
		
		public Sensor getSensor() { return mSensor; }
		
		public int getLoveSensorID () { return miLoveSensorID; }
		
		// ***** ***** ***** ***** ***** interface SensorEventListener
		
		/// Called when the accuracy of a sensor has changed.
		public void 	onAccuracyChanged (Sensor sensor, int accuracy) {}
		
		/// Called when sensor values have changed.
		/// calls love.phone.sensorevent(sensorid,{f1,f2,....,accuracy=?,timestamp=?})
		/// see also http://developer.android.com/reference/android/hardware/SensorEvent.html
		public void 	onSensorChanged (SensorEvent event) {
			try {
				LuaTable t = new LuaTable(event.values.length,2);
				for (int i=0;i<event.values.length;++i) t.set(i+1,LuaValue.valueOf(event.values[i]));
				t.set("accuracy",LuaValue.valueOf(event.accuracy));
				t.set("timestamp",LuaValue.valueOf(event.timestamp));
				phone.vm.get_G().get("love").get("phone").get("sensorevent").call(LuaValue.valueOf(getLoveSensorID()),t);
			} catch (LuaError e) {
				phone.vm.handleLuaError(e);
			}
		}
		
		// ***** ***** ***** ***** ***** methods
		
		public static LuanObjSensor self (Varargs args) { return (LuanObjSensor)args.checkuserdata(1,LuanObjSensor.class); }
		
		public static LuaTable CreateMetaTable (final LuanPhone phone) {
			LuaTable mt = LuaValue.tableOf();
			LuaTable t = LuaValue.tableOf();
			mt.set("__index",t);
			
			/// int	getLoveSensorID ()
			/// unique id for love.phone.sensorevent
			t.set("getLoveSensorID",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(self(args).getLoveSensorID()); } });	
			
			/// float 	getMaximumRange()	maximum range of the sensor in the sensor's unit. 
			t.set("getMaximumRange",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(self(args).getSensor().getMaximumRange()); } });	
			
			/// int 	getMinDelay()		the minimum delay allowed between two events in microsecond or zero if this sensor only returns a value when the data it's measuring changes. 
			//~ TODO: not in android 2.1:   t.set("getMinDelay",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(self(args).getSensor().getMinDelay()); } });	
			
			/// String 	getName()			name string of the sensor. 
			t.set("getName",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(self(args).getSensor().getName()); } });	
			
			/// float 	getPower()			the power in mA used by this sensor while in use 
			t.set("getPower",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(self(args).getSensor().getPower()); } });	
			
			/// float 	getResolution()		resolution of the sensor in the sensor's unit. 
			t.set("getResolution",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(self(args).getSensor().getResolution()); } });
			
			/// int 	getType()			generic type of this sensor. 
			t.set("getType",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(self(args).getSensor().getType()); } });	
			
			/// String 	getVendor()			vendor string of this sensor. 
			t.set("getVendor",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(self(args).getSensor().getVendor()); } });	
			
			/// int 	getVersion() 		version of the sensor's module. 	
			t.set("getVersion",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(self(args).getSensor().getVersion()); } });	

			return mt;
		}
	}
	
}


