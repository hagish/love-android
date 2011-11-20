package net.schattenkind.androidLove.luan.obj;

import java.nio.FloatBuffer;

import net.schattenkind.androidLove.luan.LuanObjBase;
import net.schattenkind.androidLove.luan.module.LuanGraphics;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;



/// A quadrilateral with texture coordinate information.
/// Quads can be used to select part of a texture to draw. In this way, one large Texture Atlas can be loaded, and then split up into sub-images. 
/// see also love.graphics.newQuad 	Creates a new Quad.
/// see also love.graphics.drawq(myimg, myquad, x, y)
/// see also http://love2d.org/wiki/love.graphics.newQuad
public class LuanObjQuad extends LuanObjBase {
	@SuppressWarnings("unused")
	private LuanGraphics	g;
	public float	x;
	public float	y;
	public float	w;
	public float	h;
	public float	sw;
	public float	sh;
	public boolean	bFlippedX = false;
	public boolean	bFlippedY = false;
	public FloatBuffer	vb_Tex;
	
	public static LuanObjQuad self (Varargs args) { return (LuanObjQuad)args.checkuserdata(1,LuanObjQuad.class); }
	
	/// called from love.graphics.newQuad ( x, y, width, height, sw, sh )
	/// see also http://love2d.org/wiki/love.graphics.newQuad
	/// e.g. top left 32x32 pixels of a 64x64 image : top_left = love.graphics.newQuad(0, 0, 32, 32, 64, 64)
	public LuanObjQuad (LuanGraphics g,float x,float y,float w,float h,float sw,float sh) {
		super(g.vm);
		this.g = g;
		this.sw = sw;
		this.sh = sh;
		vb_Tex = LuanGraphics.LuanCreateBuffer(4*2);
		//~ g.Log("LuanQuad request="+x+","+y+"  "+ w+","+h+"  "+ sw+","+sh);
		setViewport(x,y,w,h);
	}
	
	public void UpdateTexCoordBuffer () {
		float		u0 = x/sw;
		float		v0 = y/sh;
		float		u1 = (x+w)/sw;
		float		v1 = (y+h)/sh;
		float		a;
		if (bFlippedX) { a = u0; u0 = u1; u1 = a; }
		if (bFlippedY) { a = v0; v0 = v1; v1 = a; }
		//~ float[]		spriteTexFloats = { u0,v1, u1,v1, u0,v0, u1,v0 }; // cloud test ok ? my
		float[]		spriteTexFloats = { u0,v0, u1,v0, u0,v1, u1,v1 };  // 2011-11-04 direct coord system test
		//~ g.Log("LuanQuad texcoords="+u0+","+v1+"  "+ u1+","+v1+"  "+ u0+","+v0+"  "+ u1+","+v0);
		//~ g.Log("LuanQuad texcoords="+u0+","+v0+"  "+ u1+","+v0+"  "+ u0+","+v1+"  "+ u1+","+v1);
		LuanGraphics.LuanFillBuffer(vb_Tex,spriteTexFloats); // assuming the sprite thing is always the full texture and not a subthing
	}
	
	/// Sets the texture coordinates according to a viewport.  (the sub-section of the tex-atlas)
	public void setViewport (float x,float y,float w,float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		UpdateTexCoordBuffer();
	}
	
	/// Quad:flip( x, y )  booleans which axis should be flipped
	public void flip (boolean x,boolean y) {
		if (x) bFlippedX = !bFlippedX;
		if (y) bFlippedY = !bFlippedY;
		UpdateTexCoordBuffer();
	}
	
	public static LuaTable CreateMetaTable (final LuanGraphics g) {
		LuaTable mt = LuaValue.tableOf();
		LuaTable t = LuaValue.tableOf();
		mt.set("__index",t);
		
		/// Quad:flip( x, y )  booleans which axis should be flipped
		t.set("flip", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { 
			self(args).flip(args.checkboolean(2),args.checkboolean(3));
			return LuaValue.NONE; 
		} });
		
		/// x, y, w, h = Quad:getViewport( )
		/// Gets the current viewport of this Quad. 
		/// @x The top-left corner along the x-axis. 
		/// @y The top-right corner along the y-axis. 
		/// @w The width of the viewport. 
		/// @h The height of the viewport. 
		t.set("getViewport", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { 
			LuanObjQuad me = self(args);
			return LuaValue.varargsOf( LuaValue.valueOf(me.x), LuaValue.valueOf(me.y), LuaValue.varargsOf( LuaValue.valueOf(me.w), LuaValue.valueOf(me.h) )); 
		} });
		
		/// Quad:setViewport( x, y, w, h )
		/// Sets the texture coordinates according to a viewport. 
		/// @x The top-left corner along the x-axis. 
		/// @y The top-right corner along the y-axis. 
		/// @w The width of the viewport. 
		/// @h The height of the viewport. 
		t.set("setViewport", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { 
			self(args).setViewport((float)args.checkdouble(2),(float)args.checkdouble(3),(float)args.checkdouble(4),(float)args.checkdouble(5));
			return LuaValue.NONE; 
		} });
		
		/// type = Object:type()  , e.g. "Image" or audio:"Source"
		t.set("type", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf("Quad"); } });
		
		/// b = Object:typeOf( name )
		t.set("typeOf", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { 
			String s = args.checkjstring(2); 
			return LuaValue.valueOf(s.equals("Object") || s.equals("Quad")); 
		} });
		
		
		return mt;
	}
}
