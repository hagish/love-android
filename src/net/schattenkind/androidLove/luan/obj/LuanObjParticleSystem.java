package net.schattenkind.androidLove.luan.obj;

import net.schattenkind.androidLove.luan.module.LuanGraphics;
import net.schattenkind.androidLove.luan.module.LuanRenderer.LuanObjDrawable;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public class LuanObjParticleSystem extends LuanObjDrawable {
	protected static final String TAG = "LoveParticleSystem";
	
	private LuanGraphics	g;
		
	@Override public void RenderSelf (float x,float y,float r,float sx,float sy,float ox,float oy) { g.vm.NotImplemented("LoveParticleSystem:draw"); } // TODO
		
	public static LuanObjParticleSystem self (Varargs args) { return (LuanObjParticleSystem)args.checkuserdata(1,LuanObjParticleSystem.class); }
	
	public static LuaTable CreateMetaTable (final LuanGraphics g) {
		LuaTable mt = LuaValue.tableOf();
		LuaTable t = LuaValue.tableOf();
		mt.set("__index",t);
		
		t.set("count"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"count"					); return LuaValue.ZERO; } }); // TODO: not yet implemented
		t.set("getDirection"				,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"getDirection"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getOffsetX"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"getOffsetX"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getOffsetY"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"getOffsetY"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getSpread"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"getSpread"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getX"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"getX"						); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getY"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"getY"						); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("isActive"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"isActive"					); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("isEmpty"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"isEmpty"					); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("isFull"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"isFull"					); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("pause"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"pause"					); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("reset"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"reset"					); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setBufferSize"				,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"setBufferSize"			); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setColor"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"setColor"					); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setDirection"				,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"setDirection"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setEmissionRate"				,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"setEmissionRate"			); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setGravity"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"setGravity"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setLifetime"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"setLifetime"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setOffset"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"setOffset"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setParticleLife"				,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"setParticleLife"			); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setPosition"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"setPosition"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setRadialAcceleration"		,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"setRadialAcceleration"	); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setRotation"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"setRotation"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setSize"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"setSize"					); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setSizeVariation"			,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"setSizeVariation"			); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setSpeed"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"setSpeed"					); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setSpin"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"setSpin"					); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setSpinVariation"			,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"setSpinVariation"			); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setSpread"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"setSpread"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setSprite"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"setSprite"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setTangentialAcceleration"	,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"setTangentialAcceleration"); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("start"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"start"					); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("stop"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"stop"						); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("update"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("love.graphics."+"update"					); return LuaValue.NONE; } }); // TODO: not yet implemented
		
		/// b = Object:typeOf( name )
		t.set("typeOf", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { 
			String s = args.checkjstring(2); 
			return LuaValue.valueOf(s.equals("Object") || s.equals("Drawable") || s.equals("ParticleSystem")); 
		} });
		
		
		return mt;
	}
	
	public LuanObjParticleSystem (LuanGraphics g,LuanObjImage img,int iBufferSize) { this.g = g; g.vm.NotImplemented("love.graphics.newParticleSystem"); }
}
