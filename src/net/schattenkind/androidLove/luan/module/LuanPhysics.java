package net.schattenkind.androidLove.luan.module;

import net.schattenkind.androidLove.LoveVM;
import net.schattenkind.androidLove.luan.LuanBase;
import net.schattenkind.androidLove.luan.LuanObjBase;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;




public class LuanPhysics extends LuanBase {
	protected static final String TAG = "LovePhysics";
	
	public LuanPhysics (LoveVM vm) { super(vm); }
	public static final String sMetaName_Body			= "__MetaBody";
	public static final String sMetaName_CircleShape	= "__MetaCircleShape";
	public static final String sMetaName_Contact		= "__MetaContact";
	public static final String sMetaName_Joint			= "__MetaJoint";
	public static final String sMetaName_PolygonShape	= "__MetaPolygonShape";
	public static final String sMetaName_Shape			= "__MetaShape";
	public static final String sMetaName_World			= "__MetaWorld";
	
	public void Log (String s) { LoveVM.LoveLog(TAG, s); }
	
	public static Varargs RetVector2 (float x,float y) { return LuaValue.varargsOf(LuaValue.valueOf(x),LuaValue.valueOf(y)); }
	
	public LuaTable InitLib () {
		LuaTable t = LuaValue.tableOf();
		LuaValue _G = vm.get_G();
		
		_G.set(sMetaName_World			,LuanObjWorld			.CreateMetaTable(this));
		_G.set(sMetaName_Body			,LuanObjBody			.CreateMetaTable(this));
		
		_G.set(sMetaName_Shape			,LuanObjShape			.CreateMetaTable(this));
		_G.set(sMetaName_CircleShape	,LuanObjCircleShape	.CreateMetaTable(this));
		_G.set(sMetaName_PolygonShape	,LuanObjPolygonShape	.CreateMetaTable(this));
		
		_G.set(sMetaName_Contact		,LuanObjContact		.CreateMetaTable(this));
		_G.set(sMetaName_Joint			,LuanObjJoint			.CreateMetaTable(this));
	
		t.set("newWorld",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.userdataOf(new LuanObjWorld(LuanPhysics.this),vm.get_G().get(sMetaName_World));	} }); 		
		t.set("newBody",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.userdataOf(new LuanObjBody(LuanPhysics.this),vm.get_G().get(sMetaName_Body));	} });
		
		t.set("newRectangleShape",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.userdataOf(new LuanObjPolygonShape(LuanPhysics.this),vm.get_G().get(sMetaName_PolygonShape));	} });	
		t.set("newCircleShape",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.userdataOf(new LuanObjCircleShape(LuanPhysics.this),vm.get_G().get(sMetaName_CircleShape));	} }); 		
		t.set("newPolygonShape",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.userdataOf(new LuanObjPolygonShape(LuanPhysics.this),vm.get_G().get(sMetaName_PolygonShape));	} });	
		
		t.set("newDistanceJoint",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.userdataOf(new LuanObjJoint(LuanPhysics.this),vm.get_G().get(sMetaName_Joint));	} });		 	
		t.set("newGearJoint",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.userdataOf(new LuanObjJoint(LuanPhysics.this),vm.get_G().get(sMetaName_Joint));	} });	 		
		t.set("newMouseJoint",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.userdataOf(new LuanObjJoint(LuanPhysics.this),vm.get_G().get(sMetaName_Joint));	} });			 	
		t.set("newPrismaticJoint",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.userdataOf(new LuanObjJoint(LuanPhysics.this),vm.get_G().get(sMetaName_Joint));	} });		 	
		t.set("newPulleyJoint",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.userdataOf(new LuanObjJoint(LuanPhysics.this),vm.get_G().get(sMetaName_Joint));	} });	 	 	
		t.set("newRevoluteJoint",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.userdataOf(new LuanObjJoint(LuanPhysics.this),vm.get_G().get(sMetaName_Joint));	} });		 	
		
		return t;
	}
	
	
	// ***** ***** ***** ***** ***** LuanWorld
	public static class LuanObjWorld extends LuanObjBase {
		public LuanPhysics phys;
		public LuanObjWorld (LuanPhysics phys) { super(phys.vm); this.phys = phys; phys.vm.NotImplemented("LuanWorld:constructor"); }
		public static LuaTable CreateMetaTable (final LuanPhysics phys) {
			LuaTable mt = LuaValue.tableOf();
			LuaTable t = LuaValue.tableOf();
			mt.set("__index",t);
			
			t.set("type"			,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("World:"+"type"			);	return LuaValue.NONE; } });	
			t.set("typeOf"			,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("World:"+"typeOf"		);	return LuaValue.NONE; } });	
			t.set("getBodyCount"	,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("World:"+"getBodyCount"	);	return LuaValue.NONE; } });	
			t.set("getCallbacks"	,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("World:"+"getCallbacks"	);	return LuaValue.NONE; } });	
			t.set("getGravity"		,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("World:"+"getGravity"	);	return LuaValue.NONE; } });	
			t.set("getJointCount"	,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("World:"+"getJointCount"	);	return LuaValue.NONE; } });	
			t.set("getMeter"		,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("World:"+"getMeter"		);	return LuaValue.NONE; } });	
			t.set("isAllowSleep"	,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("World:"+"isAllowSleep"	);	return LuaValue.NONE; } });	
			t.set("setAllowSleep"	,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("World:"+"setAllowSleep"	);	return LuaValue.NONE; } });	
			t.set("setCallbacks"	,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("World:"+"setCallbacks"	);	return LuaValue.NONE; } });	
			t.set("setGravity"		,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("World:"+"setGravity"	);	return LuaValue.NONE; } });	
			t.set("setMeter"		,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("World:"+"setMeter"		);	return LuaValue.NONE; } });	
			t.set("update"			,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("World:"+"update"		);	return LuaValue.NONE; } });	

			return mt;
		}
	}
	
	// ***** ***** ***** ***** ***** LuanBody
	
	/// Bodies are objects with velocity and position. 
	public static class LuanObjBody extends LuanObjBase {
		public LuanPhysics phys;
		public LuanObjBody (LuanPhysics phys) { super(phys.vm); this.phys = phys; phys.vm.NotImplemented("LuanBody:constructor"); }
		public static LuaTable CreateMetaTable (final LuanPhysics phys) {
			LuaTable mt = LuaValue.tableOf();
			LuaTable t = LuaValue.tableOf();
			mt.set("__index",t);
			
			t.set("applyForce"							,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"applyForce"							);	return LuaValue.NONE; } });	
			t.set("applyImpulse"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"applyImpulse"						);	return LuaValue.NONE; } });	
			t.set("applyTorque"							,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"applyTorque"						);	return LuaValue.NONE; } });	
			t.set("destroy"								,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"destroy"							);	return LuaValue.NONE; } });	
			t.set("getAllowSleeping"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"getAllowSleeping"					);	return LuaValue.NONE; } });	
			t.set("getAngle"							,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"getAngle"							);	return LuaValue.ZERO; } });	
			t.set("getAngularDamping"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"getAngularDamping"					);	return LuaValue.ZERO; } });	
			t.set("getAngularVelocity"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"getAngularVelocity"					);	return LuaValue.ZERO; } });	
			t.set("getInertia"							,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"getInertia"							);	return LuaValue.ZERO; } });	
			t.set("getLinearDamping"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"getLinearDamping"					);	return LuaValue.ZERO; } });	
			t.set("getLinearVelocity"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"getLinearVelocity"					);	return LuanPhysics.RetVector2(0f,0f); } });	
			t.set("getLinearVelocityFromLocalPoint"		,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"getLinearVelocityFromLocalPoint"	);	return LuanPhysics.RetVector2(0f,0f); } });	
			t.set("getLinearVelocityFromWorldPoint"		,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"getLinearVelocityFromWorldPoint"	);	return LuanPhysics.RetVector2(0f,0f); } });	
			t.set("getLocalCenter"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"getLocalCenter"						);	return LuanPhysics.RetVector2(0f,0f); } });	
			t.set("getLocalPoint"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"getLocalPoint"						);	return LuanPhysics.RetVector2(0f,0f); } });	
			t.set("getLocalVector"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"getLocalVector"						);	return LuanPhysics.RetVector2(0f,0f); } });	
			t.set("getMass"								,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"getMass"							);	return LuaValue.ZERO; } });	
			t.set("getPosition"							,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"getPosition"						);	return LuanPhysics.RetVector2(0f,0f); } });	
			t.set("getWorldCenter"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"getWorldCenter"						);	return LuanPhysics.RetVector2(0f,0f); } });	
			t.set("getWorldPoint"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"getWorldPoint"						);	return LuanPhysics.RetVector2(0f,0f); } });	
			t.set("getWorldVector"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"getWorldVector"						);	return LuanPhysics.RetVector2(0f,0f); } });	
			t.set("getX"								,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"getX"								);	return LuaValue.ZERO; } });	
			t.set("getY"								,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"getY"								);	return LuaValue.ZERO; } });	
			t.set("isBullet"							,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"isBullet"							);	return LuaValue.NONE; } });	
			t.set("isDynamic"							,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"isDynamic"							);	return LuaValue.NONE; } });	
			t.set("isFrozen"							,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"isFrozen"							);	return LuaValue.NONE; } });	
			t.set("isSleeping"							,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"isSleeping"							);	return LuaValue.NONE; } });	
			t.set("isStatic"							,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"isStatic"							);	return LuaValue.NONE; } });	
			t.set("putToSleep"							,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"putToSleep"							);	return LuaValue.NONE; } });	
			t.set("setAllowSleeping"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"setAllowSleeping"					);	return LuaValue.NONE; } });	
			t.set("setAngle"							,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"setAngle"							);	return LuaValue.NONE; } });	
			t.set("setAngularDamping"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"setAngularDamping"					);	return LuaValue.NONE; } });	
			t.set("setAngularVelocity"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"setAngularVelocity"					);	return LuaValue.NONE; } });	
			t.set("setBullet"							,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"setBullet"							);	return LuaValue.NONE; } });	
			t.set("setFixedRotation"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"setFixedRotation"					);	return LuaValue.NONE; } });	
			t.set("setInertia"							,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"setInertia"							);	return LuaValue.NONE; } });	
			t.set("setLinearDamping"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"setLinearDamping"					);	return LuaValue.NONE; } });	
			t.set("setLinearVelocity"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"setLinearVelocity"					);	return LuaValue.NONE; } });	
			t.set("setMass"								,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"setMass"							);	return LuaValue.NONE; } });	
			t.set("setMassFromShapes"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"setMassFromShapes"					);	return LuaValue.NONE; } });	
			t.set("setPosition"							,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"setPosition"						);	return LuaValue.NONE; } });	
			t.set("setX"								,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"setX"								);	return LuaValue.NONE; } });	
			t.set("setY"								,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"setY"								);	return LuaValue.NONE; } });	
			t.set("wakeUp"								,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"wakeUp"								);	return LuaValue.NONE; } });	
			t.set("type"								,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"type"								);	return LuaValue.NONE; } });	
			t.set("typeOf"								,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Body:"+"typeOf"								);	return LuaValue.NONE; } });	

			return mt;
		}
	}
	// ***** ***** ***** ***** ***** LuanShape
	public static class LuanObjShape extends LuanObjBase {
		public LuanPhysics phys;
		public LuanObjShape (LuanPhysics phys) { super(phys.vm); this.phys = phys; phys.vm.NotImplemented("LuanShape:constructor"); }
		
		
		public static void AddMethodsToTable (final LuanPhysics phys,LuaTable t) {
			t.set("type"			,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Shape:"+"type"			);	return LuaValue.NONE; } });	
			t.set("typeOf"			,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Shape:"+"typeOf"			);	return LuaValue.NONE; } });	
			t.set("destroy"			,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Shape:"+"destroy"			);	return LuaValue.NONE; } });	
			t.set("getBody"			,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Shape:"+"getBody"			);	return LuaValue.NONE; } });	
			t.set("getBoundingBox"	,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Shape:"+"getBoundingBox"	);	return LuaValue.NONE; } });	
			t.set("getCategory"		,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Shape:"+"getCategory"		);	return LuaValue.NONE; } });	
			t.set("getCategoryBits"	,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Shape:"+"getCategoryBits"	);	return LuaValue.NONE; } });	
			t.set("getData"			,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Shape:"+"getData"			);	return LuaValue.NONE; } });	
			t.set("getDensity"		,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Shape:"+"getDensity"		);	return LuaValue.NONE; } });	
			t.set("getFilterData"	,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Shape:"+"getFilterData"	);	return LuaValue.NONE; } });	
			t.set("getFriction"		,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Shape:"+"getFriction"		);	return LuaValue.NONE; } });	
			t.set("getMask"			,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Shape:"+"getMask"			);	return LuaValue.NONE; } });	
			t.set("getRestitution"	,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Shape:"+"getRestitution"	);	return LuaValue.NONE; } });	
			t.set("getType"			,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Shape:"+"getType"			);	return LuaValue.NONE; } });	
			t.set("isSensor"		,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Shape:"+"isSensor"		);	return LuaValue.NONE; } });	
			t.set("setCategory"		,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Shape:"+"setCategory"		);	return LuaValue.NONE; } });	
			t.set("setData"			,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Shape:"+"setData"			);	return LuaValue.NONE; } });	
			t.set("setDensity"		,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Shape:"+"setDensity"		);	return LuaValue.NONE; } });	
			t.set("setFilterData"	,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Shape:"+"setFilterData"	);	return LuaValue.NONE; } });	
			t.set("setFriction"		,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Shape:"+"setFriction"		);	return LuaValue.NONE; } });	
			t.set("setMask"			,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Shape:"+"setMask"			);	return LuaValue.NONE; } });	
			t.set("setRestitution"	,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Shape:"+"setRestitution"	);	return LuaValue.NONE; } });	
			t.set("setSensor"		,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Shape:"+"setSensor"		);	return LuaValue.NONE; } });	
			t.set("testPoint"		,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Shape:"+"testPoint"		);	return LuaValue.NONE; } });	
			t.set("testSegment"		,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Shape:"+"testSegment"		);	return LuaValue.NONE; } });	
		}
		
		public static LuaTable CreateMetaTable (final LuanPhysics phys) {
			LuaTable mt = LuaValue.tableOf();
			LuaTable t = LuaValue.tableOf();
			mt.set("__index",t);
			AddMethodsToTable(phys,t);
			return mt;
		}
	}
	// ***** ***** ***** ***** ***** LuanCircleShape
	public static class LuanObjCircleShape extends LuanObjBase {
		public LuanPhysics phys;
		public LuanObjCircleShape (LuanPhysics phys) { super(phys.vm); this.phys = phys; phys.vm.NotImplemented("LuanCircleShape:constructor"); }
		public static LuaTable CreateMetaTable (final LuanPhysics phys) {
			LuaTable mt = LuaValue.tableOf();
			LuaTable t = LuaValue.tableOf();
			mt.set("__index",t);
			
			LuanObjShape.AddMethodsToTable(phys,t);
			
			t.set("getLocalCenter"		,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("CircleShape:"+"getLocalCenter"		);	return LuaValue.NONE; } });	
			t.set("getRadius"			,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("CircleShape:"+"getRadius"		);	return LuaValue.NONE; } });	
			t.set("getWorldCenter"		,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("CircleShape:"+"getWorldCenter"		);	return LuaValue.NONE; } });	
			
			return mt;
		}
	}
	// ***** ***** ***** ***** ***** LuanPolygonShape
	public static class LuanObjPolygonShape extends LuanObjBase {
		public LuanPhysics phys;
		public LuanObjPolygonShape (LuanPhysics phys) { super(phys.vm); this.phys = phys; phys.vm.NotImplemented("LuanPolygonShape:constructor"); }
		public static LuaTable CreateMetaTable (final LuanPhysics phys) {
			LuaTable mt = LuaValue.tableOf();
			LuaTable t = LuaValue.tableOf();
			mt.set("__index",t);
			
			LuanObjShape.AddMethodsToTable(phys,t);
			
			t.set("getPoints"		,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("PolygonShape:"+"getPoints"		);	return LuaValue.NONE; } });	
			
			return mt;
		}
	}
	
	// ***** ***** ***** ***** ***** LuanContact
	public static class LuanObjContact extends LuanObjBase {
		public LuanPhysics phys;
		public LuanObjContact (LuanPhysics phys) { super(phys.vm); this.phys = phys; phys.vm.NotImplemented("LuanContact:constructor"); }
		public static LuaTable CreateMetaTable (final LuanPhysics phys) {
			LuaTable mt = LuaValue.tableOf();
			LuaTable t = LuaValue.tableOf();
			mt.set("__index",t);
			
			t.set("getFriction"				,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Contact:"+"getFriction"		);	return LuaValue.NONE; } });	
			t.set("getNormal"				,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Contact:"+"getNormal"		);	return LuaValue.NONE; } });	
			t.set("getPosition"				,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Contact:"+"getPosition"		);	return LuanPhysics.RetVector2(0f,0f); } });	
			t.set("getRestitution"			,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Contact:"+"getRestitution"	);	return LuaValue.NONE; } });	
			t.set("getSeparation"			,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Contact:"+"getSeparation"	);	return LuaValue.NONE; } });	
			t.set("getVelocity"				,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Contact:"+"getVelocity"		);	return LuaValue.NONE; } });	
			t.set("type"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Contact:"+"type"			);	return LuaValue.NONE; } });	
			t.set("typeOf"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Contact:"+"typeOf"			);	return LuaValue.NONE; } });	

			return mt;
		}
	}
	
	// ***** ***** ***** ***** ***** LuanJoint
	public static class LuanObjJoint extends LuanObjBase {
		public LuanPhysics phys;
		public LuanObjJoint (LuanPhysics phys) { super(phys.vm); this.phys = phys; phys.vm.NotImplemented("LuanJoint:constructor"); }
		public static LuaTable CreateMetaTable (final LuanPhysics phys) {
			LuaTable mt = LuaValue.tableOf();
			LuaTable t = LuaValue.tableOf();
			mt.set("__index",t);
			
			t.set("destroy"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Joint:"+"destroy"					);	return LuaValue.NONE; } });	
			t.set("getAnchors"				,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Joint:"+"getAnchors"				);	return LuaValue.NONE; } });	
			t.set("getCollideConnected"		,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Joint:"+"getCollideConnected"		);	return LuaValue.NONE; } });	
			t.set("getReactionForce"		,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Joint:"+"getReactionForce"		);	return LuaValue.NONE; } });	
			t.set("getReactionTorque"		,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Joint:"+"getReactionTorque"		);	return LuaValue.NONE; } });	
			t.set("getType"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Joint:"+"getType"					);	return LuaValue.NONE; } });	
			t.set("setCollideConnected"		,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Joint:"+"setCollideConnected"		);	return LuaValue.NONE; } });	
			t.set("type"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Joint:"+"type"					);	return LuaValue.NONE; } });	
			t.set("typeOf"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { phys.vm.NotImplemented("Joint:"+"typeOf"					);	return LuaValue.NONE; } });	
						
			return mt;
		}
	}
	/*
	LuanJoint subtypes:
	DistanceJoint
	GearJoint
	MouseJoint
	PrismaticJoint
	PulleyJoint
	RevoluteJoint
	*/
}
