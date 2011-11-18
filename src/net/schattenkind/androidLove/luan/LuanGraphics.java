package net.schattenkind.androidLove.luan;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import net.schattenkind.androidLove.LoveVM;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;


public class LuanGraphics extends LuanRenderer {
	protected static final String TAG = "LoveGraphics";
	
	public LuanGraphics (LoveVM vm) { super(vm); }
	public static final String sMetaName_LuanImage = "__MetaLuanImage";
	public static final String sMetaName_LuanQuad = "__MetaLuanQuad";
	public static final String sMetaName_LuanFont = "__MetaLuanFont";
	public static final String sMetaName_LuanParticleSystem = "__MetaLuanParticleSystem";
	
	public void Log (String s) { LoveVM.LoveLog(TAG, s); }
	
	
	public LuaTable InitLib () {
		InitSpriteBuffer();
		try {
			mDefaultFont = new LuanFont(this);
			mFont = mDefaultFont;
		} catch (Exception e) {
			Log("warning, failed to load default font in LuanGraphics:InitLib");
			//~ vm.handleError(e);
		}
		LuaTable t = LuaValue.tableOf();
		LuaValue _G = vm.get_G();
		
		_G.set(sMetaName_LuanImage,LuanImage.CreateMetaTable(this));
		_G.set(sMetaName_LuanQuad,LuanQuad.CreateMetaTable(this));
		_G.set(sMetaName_LuanFont,LuanFont.CreateMetaTable(this));
		_G.set(sMetaName_LuanParticleSystem,LuanParticleSystem.CreateMetaTable(this));

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
				//~ Log("print:"+s);
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
				//~ Log("printf:"+align+":"+s);
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
					try {
						return LuaValue.userdataOf(new LuanFont(LuanGraphics.this,iSize),vm.get_G().get(sMetaName_LuanFont));
					} catch (Exception e) {
						vm.handleError(e);
					}
					return LuaValue.NONE;
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
		
		/// system = love.graphics.newParticleSystem( image, buffer )
		t.set("newParticleSystem",	new VarArgFunction() { 
			@Override
			public Varargs invoke(Varargs args) {
				LuanImage img = (LuanImage)args.checkuserdata(1,LuanImage.class);
				int iBufferSize = args.checkint(2);
				try {
					return LuaValue.userdataOf(new LuanParticleSystem(LuanGraphics.this,img,iBufferSize),vm.get_G().get(sMetaName_LuanParticleSystem));
				} catch (Exception e) {
					vm.handleError(e);
				}
				return LuaValue.NONE;
			}
		});
		
		/// love.graphics.setFont( font )
		t.set("setFont", new VarArgFunction() {
			@Override public Varargs invoke(Varargs args) {
				mFont = IsArgSet(args,1) ? (LuanFont)args.checkuserdata(1,LuanFont.class) : mDefaultFont;
				return LuaValue.NONE;
			}
		});

		/// love.graphics.draw(drawable, x, y, r=0, sx=1, sy=1, ox=0, oy=0)
		t.set("draw", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				LuanDrawable drawable = (LuanDrawable)args.checkuserdata(1,LuanDrawable.class);
				float x = (float)args.checkdouble(2);
				float y = (float)args.checkdouble(3);
				float r  = (IsArgSet(args,4)) ? ((float)args.checkdouble(4)) : 0.0f;
				float sx = (IsArgSet(args,5)) ? ((float)args.checkdouble(5)) : 1.0f;
				float sy = (IsArgSet(args,6)) ? ((float)args.checkdouble(6)) : 1.0f;
				float ox = (IsArgSet(args,7)) ? ((float)args.checkdouble(7)) : 0.0f;
				float oy = (IsArgSet(args,8)) ? ((float)args.checkdouble(8)) : 0.0f;
				if (drawable.IsImage()) {
					LuanImage img = (LuanImage)drawable;
					DrawSprite(img.GetTextureID(),img.mWidth,img.mHeight,x,y,r,sx,sy,ox,oy);
				} else {
					drawable.RenderSelf(x,y,r,sx,sy,ox,oy);
				}
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
				// TODO: idea : if w>h and natural w<h , flip 90� ?
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
	
}

