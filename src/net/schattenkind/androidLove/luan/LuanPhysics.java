package net.schattenkind.androidLove.luan;

import net.schattenkind.androidLove.LoveVM;

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
	
	
	public LuaTable InitLib () {
		LuaTable t = LuaValue.tableOf();
		LuaValue _G = vm.get_G();
		
		_G.set(sMetaName_World			,LuanWorld			.CreateMetaTable(this));
		_G.set(sMetaName_Body			,LuanBody			.CreateMetaTable(this));
		
		_G.set(sMetaName_Shape			,LuanShape			.CreateMetaTable(this));
		_G.set(sMetaName_CircleShape	,LuanCircleShape	.CreateMetaTable(this));
		_G.set(sMetaName_PolygonShape	,LuanPolygonShape	.CreateMetaTable(this));
		
		_G.set(sMetaName_Contact		,LuanContact		.CreateMetaTable(this));
		_G.set(sMetaName_Joint			,LuanJoint			.CreateMetaTable(this));
	
		t.set("newWorld",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.userdataOf(new LuanWorld(LuanPhysics.this),vm.get_G().get(sMetaName_World));	} }); 		
		t.set("newBody",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.userdataOf(new LuanBody(LuanPhysics.this),vm.get_G().get(sMetaName_Body));	} });
		
		t.set("newRectangleShape",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.userdataOf(new LuanShape(LuanPhysics.this),vm.get_G().get(sMetaName_Shape));	} });	
		t.set("newCircleShape",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.userdataOf(new LuanCircleShape(LuanPhysics.this),vm.get_G().get(sMetaName_CircleShape));	} }); 		
		t.set("newPolygonShape",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.userdataOf(new LuanPolygonShape(LuanPhysics.this),vm.get_G().get(sMetaName_PolygonShape));	} });	
		
		t.set("newDistanceJoint",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.userdataOf(new LuanJoint(LuanPhysics.this),vm.get_G().get(sMetaName_Joint));	} });		 	
		t.set("newGearJoint",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.userdataOf(new LuanJoint(LuanPhysics.this),vm.get_G().get(sMetaName_Joint));	} });	 		
		t.set("newMouseJoint",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.userdataOf(new LuanJoint(LuanPhysics.this),vm.get_G().get(sMetaName_Joint));	} });			 	
		t.set("newPrismaticJoint",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.userdataOf(new LuanJoint(LuanPhysics.this),vm.get_G().get(sMetaName_Joint));	} });		 	
		t.set("newPulleyJoint",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.userdataOf(new LuanJoint(LuanPhysics.this),vm.get_G().get(sMetaName_Joint));	} });	 	 	
		t.set("newRevoluteJoint",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.userdataOf(new LuanJoint(LuanPhysics.this),vm.get_G().get(sMetaName_Joint));	} });		 	
		
		return t;
	}
	
	
	// ***** ***** ***** ***** ***** LuanWorld
	public static class LuanWorld {
		public LuanPhysics phys;
		public LuanWorld (LuanPhysics phys) { this.phys = phys; }
		public static LuaTable CreateMetaTable (final LuanPhysics phys) {
			LuaTable mt = LuaValue.tableOf();
			LuaTable t = LuaValue.tableOf();
			mt.set("__index",t);
			return mt;
		}
	}
	
	// ***** ***** ***** ***** ***** LuanBody
	public static class LuanBody {
		public LuanPhysics phys;
		public LuanBody (LuanPhysics phys) { this.phys = phys; }
		public static LuaTable CreateMetaTable (final LuanPhysics phys) {
			LuaTable mt = LuaValue.tableOf();
			LuaTable t = LuaValue.tableOf();
			mt.set("__index",t);
			return mt;
		}
	}
	// ***** ***** ***** ***** ***** LuanShape
	public static class LuanShape {
		public LuanPhysics phys;
		public LuanShape (LuanPhysics phys) { this.phys = phys; }
		public static LuaTable CreateMetaTable (final LuanPhysics phys) {
			LuaTable mt = LuaValue.tableOf();
			LuaTable t = LuaValue.tableOf();
			mt.set("__index",t);
			return mt;
		}
	}
	// ***** ***** ***** ***** ***** LuanCircleShape
	public static class LuanCircleShape {
		public LuanPhysics phys;
		public LuanCircleShape (LuanPhysics phys) { this.phys = phys; }
		public static LuaTable CreateMetaTable (final LuanPhysics phys) {
			LuaTable mt = LuaValue.tableOf();
			LuaTable t = LuaValue.tableOf();
			mt.set("__index",t);
			return mt;
		}
	}
	// ***** ***** ***** ***** ***** LuanPolygonShape
	public static class LuanPolygonShape {
		public LuanPhysics phys;
		public LuanPolygonShape (LuanPhysics phys) { this.phys = phys; }
		public static LuaTable CreateMetaTable (final LuanPhysics phys) {
			LuaTable mt = LuaValue.tableOf();
			LuaTable t = LuaValue.tableOf();
			mt.set("__index",t);
			return mt;
		}
	}
	// ***** ***** ***** ***** ***** LuanContact
	public static class LuanContact {
		public LuanPhysics phys;
		public LuanContact (LuanPhysics phys) { this.phys = phys; }
		public static LuaTable CreateMetaTable (final LuanPhysics phys) {
			LuaTable mt = LuaValue.tableOf();
			LuaTable t = LuaValue.tableOf();
			mt.set("__index",t);
			return mt;
		}
	}
	// ***** ***** ***** ***** ***** LuanJoint
	public static class LuanJoint {
		public LuanPhysics phys;
		public LuanJoint (LuanPhysics phys) { this.phys = phys; }
		public static LuaTable CreateMetaTable (final LuanPhysics phys) {
			LuaTable mt = LuaValue.tableOf();
			LuaTable t = LuaValue.tableOf();
			mt.set("__index",t);
			return mt;
		}
	}
}
