package net.schattenkind.androidLove.luan;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import net.schattenkind.androidLove.LoveVM;
import net.schattenkind.androidLove.luan.LuanFont;
import net.schattenkind.androidLove.luan.LuanImage;
import net.schattenkind.androidLove.luan.LuanQuad;


import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLUtils;
import android.util.Log;


public class LuanGraphics extends LuanBase {
	protected static final String TAG = "LoveGraphics";
	
	public LuanGraphics (LoveVM vm) { super(vm); }
	static final String sMetaName_LuanImage = "__MetaLuanImage";
	static final String sMetaName_LuanQuad = "__MetaLuanQuad";
	static final String sMetaName_LuanFont = "__MetaLuanFont";
	
	public boolean bResolutionOverrideActive = false;
	public int mfResolutionOverrideX;
	public int mfResolutionOverrideY;
	
	public GL10		getGL () { return vm.getGL(); }
	
	public void Log (String s) { Log.i(TAG, s); }
	
	public LuanFont mFont;
	
	public float LOVE_TODEG (float fRadians) { return 360f*fRadians/(float)Math.PI; }
	
	public LuaTable InitLib () {
		InitSpriteBuffer();
		LuaTable t = LuaValue.tableOf();
		LuaValue _G = vm.get_G();
		
		_G.set(sMetaName_LuanImage,LuanImage.CreateMetaTable(this));
		_G.set(sMetaName_LuanQuad,LuanQuad.CreateMetaTable(this));
		_G.set(sMetaName_LuanFont,LuanFont.CreateMetaTable(this));

		

		t.set("checkMode",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"checkMode"			); return LuaValue.NONE; } }); // TODO: not yet implemented	
		t.set("circle",				new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"circle"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("clear",				new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"clear"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getBackgroundColor",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"getBackgroundColor"	); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getBlendMode",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"getBlendMode"		); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getCaption",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"getCaption"			); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getColor",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"getColor"			); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getColorMode",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"getColorMode"		); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getFont",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"getFont"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getLineStipple",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"getLineStipple"		); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getLineStyle",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"getLineStyle"		); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getLineWidth",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"getLineWidth"		); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getMaxPointSize",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"getMaxPointSize"		); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getModes",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"getModes"			); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getPointSize",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"getPointSize"		); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getPointStyle",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"getPointStyle"		); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getScissor",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"getScissor"			); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("isCreated",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"isCreated"			); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("line",				new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"line"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("newFramebuffer",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"newFramebuffer"		); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("newParticleSystem",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"newParticleSystem"	); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("newScreenshot",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"newScreenshot"		); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("newSpriteBatch",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"newSpriteBatch"		); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("point",				new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"point"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("polygon",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"polygon"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("pop",				new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"pop"					); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("present",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"present"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("push",				new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"push"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("quad",				new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"quad"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("rectangle",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"rectangle"			); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setBlendMode",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"setBlendMode"		); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setCaption",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"setCaption"			); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setColorMode",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"setColorMode"		); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setIcon",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"setIcon"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setLine",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"setLine"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setLineStipple",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"setLineStipple"		); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setLineStyle",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"setLineStyle"		); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setLineWidth",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"setLineWidth"		); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setPoint",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"setPoint"			); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setPointSize",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"setPointSize"		); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setPointStyle",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"setPointStyle"		); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setRenderTarget",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"setRenderTarget"		); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setScissor",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"setScissor"			); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("toggleFullscreen",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"toggleFullscreen"	); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("triangle",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { vm.NotImplemented("love.graphics."+"triangle"			); return LuaValue.NONE; } }); // TODO: not yet implemented
		
		
		/// love.graphics.print( text, x, y, r, sx, sy )
		/// Draws text on screen.
		t.set("print", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				String s = args.checkjstring(1);
				float x = (float)args.checkdouble(2);
				float y = (float)args.checkdouble(3);
				float r = (IsArgSet(args,4)) ? ((float)args.checkdouble(4)) : 0f;
				float sx = (IsArgSet(args,5)) ? ((float)args.checkdouble(5)) : 1f;
				float sy = (IsArgSet(args,6)) ? ((float)args.checkdouble(6)) : sx;
				Log("print:"+s);
				if (mFont != null) mFont.print(s, x, y, r, sx, sy);
				return LuaValue.NONE;
			}
		});
		
		/// love.graphics.printf( text, x, y, limit, align )
		/// NOTE: not related to c printf, rather wordwrap etc
		/// Draws formatted text, with word wrap and alignment. 
		t.set("printf", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				String s = args.checkjstring(1);
				float x = (float)args.checkdouble(2);
				float y = (float)args.checkdouble(3);
				float limit = (float)args.checkdouble(4);
				String align = (IsArgSet(args,5)) ? args.checkjstring(5) : "left";
				Log("printf:"+s);
				if (mFont != null) mFont.printf(s,x,y,limit,LuanFont.Text2Align(align));
				return LuaValue.NONE;
			}
		});

		/// img = love.graphics.newImage(sFileName)
		t.set("newImage", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				String s = args.checkjstring(1);
				try {
					return LuaValue.userdataOf(new LuanImage(LuanGraphics.this,s),vm.get_G().get(sMetaName_LuanImage));
				} catch (Exception e) {
					vm.handleError(e);
				}
				return LuaValue.NONE;
			}
		});
		
		/// quad = love.graphics.newQuad( x, y, width, height, sw, sh )
		t.set("newQuad", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				float x  = (float)args.checkdouble(1);
				float y  = (float)args.checkdouble(2);
				float w  = (float)args.checkdouble(3);
				float h  = (float)args.checkdouble(4);
				float sw = (float)args.checkdouble(5);
				float sh = (float)args.checkdouble(6);
				try {
					return LuaValue.userdataOf(new LuanQuad(LuanGraphics.this,x,y,w,h,sw,sh),vm.get_G().get(sMetaName_LuanQuad));
				} catch (Exception e) {
					vm.handleError(e);
				}
				return LuaValue.NONE;
			}
		});
		
		/// font = love.graphics.newFont( filename, size ) 
		/// font = love.graphics.newFont( size )   (default font (Vera Sans))
		t.set("newFont", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				if (args.isstring(1)) {
					String filename = args.checkjstring(1);
					int iSize = IsArgSet(args,2) ? args.checkint(2) : 12;
					try {
						return LuaValue.userdataOf(new LuanFont(LuanGraphics.this,filename,iSize),vm.get_G().get(sMetaName_LuanFont));
					} catch (Exception e) {
						vm.handleError(e);
					}
					return LuaValue.NONE;
				} else {
					int iSize = (IsArgSet(args,1)) ? args.checkint(1) : 12;
					return LuaValue.userdataOf(new LuanFont(LuanGraphics.this,iSize),vm.get_G().get(sMetaName_LuanFont));
				}
			}
		});
		
		/// font = love.graphics.newImageFont( image, glyphs )
		t.set("newImageFont", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				if (args.isstring(1)) {
					String filename = args.checkjstring(1);
					String glyphs = args.checkjstring(2);
					try {
						return LuaValue.userdataOf(new LuanFont(LuanGraphics.this,filename,glyphs),vm.get_G().get(sMetaName_LuanFont));
					} catch (Exception e) {
						vm.handleError(e);
					}
					return LuaValue.NONE;
				} else {
					LuanImage img = (LuanImage)args.checkuserdata(1,LuanImage.class);
					String glyphs = args.checkjstring(2);
					return LuaValue.userdataOf(new LuanFont(LuanGraphics.this,img,glyphs),vm.get_G().get(sMetaName_LuanFont));
				}
			}
		});
		
		
		/// love.graphics.setFont( font )
		t.set("setFont", new VarArgFunction() {
			@Override public Varargs invoke(Varargs args) {
				mFont = (LuanFont)args.checkuserdata(1,LuanFont.class);
				return LuaValue.NONE;
			}
		});

		/// love.graphics.draw(drawable, x, y, r=0, sx=1, sy=1, ox=0, oy=0)
		t.set("draw", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				LuanImage img = (LuanImage)args.checkuserdata(1,LuanImage.class);
				float x = (float)args.checkdouble(2);
				float y = (float)args.checkdouble(3);
				float r  = (IsArgSet(args,4)) ? ((float)args.checkdouble(4)) : 0.0f;
				float sx = (IsArgSet(args,5)) ? ((float)args.checkdouble(5)) : 1.0f;
				float sy = (IsArgSet(args,6)) ? ((float)args.checkdouble(6)) : 1.0f;
				float ox = (IsArgSet(args,7)) ? ((float)args.checkdouble(7)) : 0.0f;
				float oy = (IsArgSet(args,8)) ? ((float)args.checkdouble(8)) : 0.0f;
				
				DrawSprite(img.GetTextureID(),img.mWidth,img.mHeight,x,y,r,sx,sy,ox,oy);
				return LuaValue.NONE;
			}
		});
		
		
		/// love.graphics.drawq( image, quad, x, y, r, sx, sy, ox, oy )
		t.set("drawq", new VarArgFunction() { 
			@Override public Varargs invoke(Varargs args) {
				LuanImage img = (LuanImage)args.checkuserdata(1,LuanImage.class);
				LuanQuad quad = (LuanQuad)args.checkuserdata(2,LuanQuad.class);
				float x = (float)args.checkdouble(3);
				float y = (float)args.checkdouble(4);
				float r  = (IsArgSet(args,5)) ? ((float)args.checkdouble(5)) : 0.0f;
				float sx = (IsArgSet(args,6)) ? ((float)args.checkdouble(6)) : 1.0f;
				float sy = (IsArgSet(args,7)) ? ((float)args.checkdouble(7)) : 1.0f;
				float ox = (IsArgSet(args,8)) ? ((float)args.checkdouble(8)) : 0.0f;
				float oy = (IsArgSet(args,9)) ? ((float)args.checkdouble(9)) : 0.0f;
				
				DrawSprite(img.GetTextureID(),quad,quad.w,quad.h,x,y,r,sx,sy,ox,oy);
				return LuaValue.NONE; 
			} 
		});
		
		
		/// love.graphics.scale( sx, sy )
		/// Scaling lasts until love.draw() exits. 
		t.set("scale", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				float sx = (float)args.checkdouble(1);
				float sy = (float)args.checkdouble(2);
				getGL().glScalef(sx,sy,1);
				return LuaValue.NONE;
			}
		});
		
		/// love.graphics.translate( dx, dy )
		/// Translates the coordinate system in two dimensions. 
		t.set("translate", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				float dx = (float)args.checkdouble(1);
				float dy = (float)args.checkdouble(2);
				getGL().glTranslatef(dx,dy,1);
				return LuaValue.NONE;
			}
		});
		
		/// Rotates the coordinate system in two dimensions. 
		/// Calling this function affects all future drawing operations by rotating the coordinate system around the origin by the given amount of radians. This change lasts until love.draw() exits. 
		/// love.graphics.rotate( angle )
		t.set("rotate", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				float a = (float)args.checkdouble(1);
				getGL().glRotatef(LOVE_TODEG(a), 0, 0, 1);
				return LuaValue.NONE;
			}
		});
		
		
		
		/// love.graphics.reset( )
		/// Calling reset makes the current drawing color white, the current background color black, the window title empty and removes any scissor settings. It sets the BlendMode to alpha and ColorMode to modulate. 
		/// It also sets both the point and line drawing modes to smooth and their sizes to 1.0 . Finally, it removes any stipple settings. 
		t.set("reset",				new VarArgFunction() { @Override public Varargs invoke(Varargs args) { 
			gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
			gl.glColor4f(1f, 1f, 1f, 1f);
			resetTransformMatrix(getGL());
			vm.NotImplemented("love.graphics.reset (lots of settings)");
			// TODO: not yet implemented
			return LuaValue.NONE; 
			} });
		

		/// love.graphics.setBackgroundColor( red, green, blue )  // 0-255
		t.set("setBackgroundColor", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				LuanColor rgba = new LuanColor(args);
				getGL().glClearColor(rgba.r, rgba.g, rgba.b, rgba.a);
				// TODO : remember for recreate surface after mode switch
				return LuaValue.NONE;
			}
		});
		
		/// love.graphics.setColor( red, green, blue, alpha )  // 0-255
		t.set("setColor", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				LuanColor rgba = new LuanColor(args);
				getGL().glColor4f(rgba.r, rgba.g, rgba.b, rgba.a);
				// TODO : remember for recreate surface after mode switch
				return LuaValue.NONE;
			}
		});
		
		/// success = love.graphics.setMode( width, height, fullscreen, vsync, fsaa )
		t.set("setMode", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				bResolutionOverrideActive = true;
				mfResolutionOverrideX = args.checkint(1);
				mfResolutionOverrideY = args.checkint(2);
				// TODO: idea : if w>h and natural w<h , flip 90° ?
				Log("love.graphics.setMode requested resolution = "+mfResolutionOverrideX+" x "+mfResolutionOverrideY);
				return LuaValue.TRUE;
			}
		});
		
		/// width = love.graphics.getWidth( )
		/// Gets the width of the window. 
		t.set("getWidth",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) {
			float w = getScreenW();
			Log("love.graphics.getWidth = "+w);
			return LuaValue.valueOf(w);
			} });
		
		/// height = love.graphics.getHeight( )
		/// Gets the height of the window.
		t.set("getHeight",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { 
			float h = getScreenH();
			Log("love.graphics.getHeight = "+h);
			return LuaValue.valueOf(h);
			} });
		
		
		
		return t;
	}
	
	public float getScreenW () { return bResolutionOverrideActive ? mfResolutionOverrideX : vm.mfScreenW; }
	public float getScreenH () { return bResolutionOverrideActive ? mfResolutionOverrideY : vm.mfScreenH; }
	
	// ***** ***** ***** ***** *****  LuanColor
	
	public static class LuanColor {
		public float r;
		public float g;
		public float b;
		public float a;
		
		public LuanColor (Varargs args) { this(args,1); }
		
		public LuanColor (Varargs args,int i) {
			if (args.istable(i)) {
				LuaTable t = args.checktable(i);
				r = t.rawget(1).tofloat() / 255f;
				g = t.rawget(2).tofloat() / 255f;
				b = t.rawget(3).tofloat() / 255f;
				a = (t.length() >= 4) ? (t.rawget(4).tofloat() / 255f) : 1f;
			} else {
				r = ((float)args.checkdouble(i+0)) / 255f;
				g = ((float)args.checkdouble(i+1)) / 255f;
				b = ((float)args.checkdouble(i+2)) / 255f;
				a = (IsArgSet(args,i+3)) ? (((float)args.checkdouble(i+3)) / 255f) : 1f;
			}
		}
	}
	
	// ***** ***** ***** ***** *****  DrawSprite function, rotate calc etc
	
	private float[]		spritePosFloats = new float[4*2];
	//~ private float[]		spriteTexFloats = { 0f,0f, 1f,0f, 0f,1f, 1f,1f }; // pirate test ok
	//~ private float[]		spriteTexFloats = { 0f,0f, 1f,0f, 0f,1f, 1f,1f }; // pirate test ok
	//~ private float[]		spriteTexFloats = { 0f,1f, 1f,1f, 0f,0f, 1f,0f }; // cloud test ok ? my  pre 2011-11-04
	private float[]		spriteTexFloats = { 0f,0f, 1f,0f, 0f,1f, 1f,1f }; // 2011-11-04 direct coord system test
	private FloatBuffer	spriteVB_Pos; // TODO: since OpenGL ES 1.1 it is possible to use vertex-buffer objects directly in vram, this would improve performance
	private FloatBuffer	spriteVB_Tex;
	final static int	kBytesPerFloat = 4;
	private	GL10		gl; // only valid after notifyFrameStart
	
	// TODO: since OpenGL ES 1.1 it is possible to use vertex-buffer objects directly in vram, this would improve performance
	public static FloatBuffer	LuanCreateBuffer (int iNumFloats) {
		ByteBuffer bb = ByteBuffer.allocateDirect(iNumFloats * kBytesPerFloat);
		bb.order(ByteOrder.nativeOrder());// use the device hardware's native byte order
		return bb.asFloatBuffer(); // create a floating point buffer from the ByteBuffer
	}
	
	//~ WARNING: probably array sent by VALUE, e.g. copy -> slow
	public static void		LuanFillBuffer (FloatBuffer b,float[] data) {
		b.put(data); // add the coordinates to the FloatBuffer
		b.position(0); // set the buffer to read the first coordinate
	}
	
	private void InitSpriteBuffer () {
		spriteVB_Pos = LuanCreateBuffer(spritePosFloats.length);
		spriteVB_Tex = LuanCreateBuffer(spriteTexFloats.length);
		LuanFillBuffer(spriteVB_Tex,spriteTexFloats); // assuming the sprite thing is always the full texture and not a subthing
	}
	
	
	public int convertMouseX(int mouseX,int mouseY) { 
		if (bResolutionOverrideActive) return (int)( ((float)mouseX) * ((float)mfResolutionOverrideX) / ((float)vm.mfScreenW) );
		return mouseX;
	}
	
	public int convertMouseY(int mouseX,int mouseY) { 
		if (bResolutionOverrideActive) return (int)( ((float)mouseY) * ((float)mfResolutionOverrideY) / ((float)vm.mfScreenH) );
		return mouseY;
	}
	
	public void resetTransformMatrix	(GL10 gl) {
		// init pixel coordinatesystem
		gl.glLoadIdentity();
		gl.glTranslatef(-1,1,0);
		if (bResolutionOverrideActive) {
			gl.glScalef(2f/(mfResolutionOverrideX),-2f/(mfResolutionOverrideY),1);
		} else {
			gl.glScalef(2f/(vm.mfScreenW),-2f/(vm.mfScreenH),1);
		}
	}
	
	public void notifyFrameStart		(GL10 gl) {
		this.gl = gl;
		
		// prepare fore rendering textured quads
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND 	);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		//~ gl.glShadeModel(GL10.GL_SMOOTH);            //Enable Smooth Shading
		//~ gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);    //Black Background
		//~ gl.glClearDepthf(1.0f);                     //Depth Buffer Setup
		//~ gl.glEnable(GL10.GL_DEPTH_TEST);            //Enables Depth Testing
		//~ gl.glDepthFunc(GL10.GL_LEQUAL);             //The Type Of Depth Testing To Do		always true for love2d

		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		//~ gl.glColor4f(1f, 1f, 1f, 1f); // todo : love global color ?
			 
		
		// Point to our vertex buffer
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, spriteVB_Pos);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, spriteVB_Tex);
		//~ Log("notifyFrameStart");
		
		// init pixel coordinatesystem
		resetTransformMatrix(gl);
	}
	
	public void notifyFrameEnd		(GL10 gl) {
		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_BLEND 	);
		//~ Log("notifyFrameEnd");
	}
	
	public void DrawSprite	(int iTextureID,LuanQuad quad,float w,float h,float x,float y,float r,float sx,float sy,float ox,float oy) {
		DrawSprite	(iTextureID,quad.vb_Tex,w,h,x,y,r,sx,sy,ox,oy);
	}
		
	public void DrawSprite	(int iTextureID,float w,float h,float x,float y,float r,float sx,float sy,float ox,float oy) {
		DrawSprite	(iTextureID,spriteVB_Tex,w,h,x,y,r,sx,sy,ox,oy);
	}
	
	public void DrawSprite	(int iTextureID,FloatBuffer vb_texcoords,float w,float h,float x,float y,float r,float sx,float sy,float ox,float oy) {
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, vb_texcoords);
		//~ float e = 0.5f;
		//~ w = e;
		//~ h = e; // no coordinate system in place yet
		//~ Log("DrawSprite "+w+","+h+"  "+x+","+y+"  "+r+"  "+sx+","+sy+"  "+ox+","+oy);
		
		/*
		love.graphics.draw(imgTerrain,0,0)
		11-04 22:12:59.797: I/LuanGraphics(1235): DrawSprite 512.0,512.0  0.0,0.0  0.0  1.0,1.0  0.0,0.0
		11-04 22:12:59.797: I/LuanGraphics(1235):  + 0.0,0.0  512.0,0.0  0.0,-512.0

		*/
		//~ sx = 2;
		//~ sy = 1;
		
		float mycos = (float)Math.cos(r);
		float mysin = (float)Math.sin(r);
		
		// coord sys with 0,0 = left,top, and +,+ = right,bottom
		// vx_ x/y = clockwise rotation starting at the right   (rot=0 : x=1,y=0)
		// vy_ x/y = clockwise rotation starting at the bottom  (rot=0 : x=0,y=1)
		
		float vx_x = w*mycos; // rot= 0:1  90:0  180:-1   270:0  = cos
		float vx_y = w*mysin; // rot= 0:0  90:1  180:0   270:-1  = sin
		
		float vy_x = -h*mysin; // rot= 0:0  90:-1  180:0   270:1  = -sin
		float vy_y =  h*mycos; // rot= 0:1  90:0  180:-1   270:0  = cos

		vx_x *= sx;
		vx_y *= sx;
		
		vy_x *= sy;
		vy_y *= sy;
		
		//~ Log("DrawSprite vx="+vx_x+","+vx_y+" vy="+vy_x+","+vy_y);
		
		//~ float x0 = -0.5f*vx_x -0.5f*vy_x; // center
		//~ float y0 = -0.5f*vx_y -0.5f*vy_y; 
		
		float x0 = x - vx_x*ox/w - vy_x*oy/h; // top-left ?
		float y0 = y - vx_y*ox/w - vy_y*oy/h; 
		//~ Log(" + "+x0+","+y0+"  "+vx_x+","+vx_y+"  "+vy_x+","+vy_y);
		
		spritePosFloats[0*2 + 0] = x0; 
		spritePosFloats[0*2 + 1] = y0; 
		
		spritePosFloats[1*2 + 0] = x0 + vx_x; 
		spritePosFloats[1*2 + 1] = y0 + vx_y; 
		
		spritePosFloats[2*2 + 0] = x0 + vy_x;  
		spritePosFloats[2*2 + 1] = y0 + vy_y;  
		
		spritePosFloats[3*2 + 0] = x0 + vx_x + vy_x;  
		spritePosFloats[3*2 + 1] = y0 + vx_y + vy_y;  
		
		/*
		// old hardcoded centered quad
		spritePosFloats[0*2 + 0] = -e; 
		spritePosFloats[0*2 + 1] =  e; 
		
		spritePosFloats[1*2 + 0] =  e; 
		spritePosFloats[1*2 + 1] =  e; 
		
		spritePosFloats[2*2 + 0] = -e; 
		spritePosFloats[2*2 + 1] = -e; 
		
		spritePosFloats[3*2 + 0] =  e; 
		spritePosFloats[3*2 + 1] = -e; 
		*/
		
		
		
		LuanFillBuffer(spriteVB_Pos,spritePosFloats);
	
		gl.glBindTexture(GL10.GL_TEXTURE_2D, iTextureID);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	}
		
	// ***** ***** ***** ***** *****  LuanDrawable
	
	public static class LuanDrawable {
		
	}
}

