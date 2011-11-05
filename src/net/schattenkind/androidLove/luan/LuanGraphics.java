package net.schattenkind.androidLove.luan;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import net.schattenkind.androidLove.LoveVM;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLUtils;
import android.util.Log;


public class LuanGraphics extends LuanBase {
	public LuanGraphics (LoveVM vm) { super(vm); }
	static final String sMetaName_LuanImage = "__MetaLuanImage";
	static final String sMetaName_LuanQuad = "__MetaLuanQuad";
	static final String sMetaName_LuanFont = "__MetaLuanFont";
	
	public boolean bResolutionOverrideActive = false;
	public int mfResolutionOverrideX;
	public int mfResolutionOverrideY;
	
	public GL10		getGL () { return vm.getGL(); }
	
	public void Log (String s) { Log.i("LuanGraphics", s); }
	public void LogException (Exception e) { Log.e("LuanGraphics",e.getMessage()); }
	
	public LuanFont mFont; // TODO: not yet used/implemented
	
	public float LOVE_TODEG (float fRadians) { return 360f*fRadians/(float)Math.PI; }
	
	public LuaTable InitLib () {
		InitSpriteBuffer();
		LuaTable t = LuaValue.tableOf();
		LuaValue _G = vm.get_G();
		
		_G.set(sMetaName_LuanImage,LuanImage.CreateMetaTable());
		_G.set(sMetaName_LuanQuad,LuanQuad.CreateMetaTable());
		_G.set(sMetaName_LuanFont,LuanFont.CreateMetaTable());

		

		t.set("checkMode",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented	
		t.set("circle",				new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("clear",				new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getBackgroundColor",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getBlendMode",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getCaption",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getColor",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getColorMode",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getFont",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getLineStipple",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getLineStyle",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getLineWidth",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getMaxPointSize",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getModes",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getPointSize",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getPointStyle",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getScissor",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("isCreated",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("line",				new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("newFramebuffer",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("newParticleSystem",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("newScreenshot",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("newSpriteBatch",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("point",				new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("polygon",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("pop",				new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("present",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("printf",				new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("push",				new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("quad",				new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("rectangle",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setBlendMode",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setCaption",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setColorMode",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setIcon",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setLine",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setLineStipple",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setLineStyle",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setLineWidth",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setPoint",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setPointSize",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setPointStyle",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setRenderTarget",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setScissor",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("toggleFullscreen",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("triangle",			new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented
		
		
		
		
		
		
		/// love.graphics.print(sText,x,y)
		t.set("print", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				String s = args.checkjstring(1);
				int x = args.checkint(2);
				int y = args.checkint(3);
				Log("print:"+s);
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
					// TODO : throw lua error ?
					LogException(e);
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
					// TODO : throw lua error ?
					LogException(e);
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
						// TODO : throw lua error ?
						LogException(e);
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
						// TODO : throw lua error ?
						LogException(e);
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
		t.set("glRotatef", new VarArgFunction() {
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
			Log("TODO:love.graphics.reset (lots of settings)");
			return LuaValue.NONE; 
			} }); // TODO: not yet implemented
		
		t.set("rotate",				new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } }); // TODO: not yet implemented

		
		
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
	
	
	// ***** ***** ***** ***** *****  LuanFont
	
	public static class LuanFont {
		private LuanGraphics	g;
		public LuanImage		img;
		
		/// ttf font
		public LuanFont (LuanGraphics g,String ttf_filename,int iSize) {  }
		
		/// ttf font, default ttf_filename to verdana sans
		public LuanFont (LuanGraphics g,int iSize) {  }
		
		/// imageFont
		public LuanFont (LuanGraphics g,String filename,String glyphs) throws FileNotFoundException { this(g,new LuanImage(g,filename),glyphs); }
		
		/// imageFont
		public LuanFont (LuanGraphics g,LuanImage img,String glyphs) {
			/*
			The imagefont file is an image file in a format that Löve can load. It can contain transparent pixels, so a PNG file is preferable, and it also needs to contain spacer color that will separate the different font glyphs.
			The upper left pixel of the image file is always taken to be the spacer color. All columns that have this color as their uppermost pixel are interpreted as separators of font glyphs. The areas between these separators are interpreted as the actual font glyphs.
			The width of the separator areas affect the spacing of the font glyphs. It is possible to have more areas in the image than are required for the font in the love.graphics.newImageFont() call. The extra areas are ignored. 
			*/
			
		}
			
		public static LuaTable CreateMetaTable () {
			LuaTable mt = LuaValue.tableOf();
			LuaTable t = LuaValue.tableOf();
			mt.set("__index",t);
			
			/// height = Font:getHeight( )
			/// TODO:dummy
			t.set("getHeight", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(123); } });
			
			/// height = Font:getLineHeight( )
			/// TODO:dummy
			t.set("getLineHeight", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(123); } });
			
			/// width = Font:getWidth( line )
			/// TODO:dummy
			t.set("getWidth", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(123); } });
			
			/// Font:setLineHeight( height )
			/// TODO:dummy
			t.set("setLineHeight", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } });
			
			/// Font:getWrap(text, width)
			/// TODO:dummy
			t.set("getWrap", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(123); } });
			
			/// type = Object:type()  , e.g. "Image" or audio:"Source"
			t.set("type", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf("Font"); } });
			
			/// b = Object:typeOf( name )
			t.set("typeOf", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { 
				String s = args.checkjstring(2); 
				return LuaValue.valueOf(s == "Object" || s == "Font"); 
			} });
			
			
			return mt;
		}
	}
	
	// ***** ***** ***** ***** *****  LuanQuad
	
	/// A quadrilateral with texture coordinate information.
	/// Quads can be used to select part of a texture to draw. In this way, one large Texture Atlas can be loaded, and then split up into sub-images. 
	/// see also love.graphics.newQuad 	Creates a new Quad.
	/// see also love.graphics.drawq(myimg, myquad, x, y)
	/// see also http://love2d.org/wiki/love.graphics.newQuad
	public static class LuanQuad {
		private LuanGraphics	g;
		public float	x;
		public float	y;
		public float	w;
		public float	h;
		public float	sw;
		public float	sh;
		public boolean	bFlippedX = false;
		public boolean	bFlippedY = false;
		private FloatBuffer	vb_Tex;
		
		public static LuanQuad self (Varargs args) { return (LuanQuad)args.checkuserdata(1,LuanQuad.class); }
		
		/// called from love.graphics.newQuad ( x, y, width, height, sw, sh )
		/// see also http://love2d.org/wiki/love.graphics.newQuad
		/// e.g. top left 32x32 pixels of a 64x64 image : top_left = love.graphics.newQuad(0, 0, 32, 32, 64, 64)
		public LuanQuad (LuanGraphics g,float x,float y,float w,float h,float sw,float sh) {
			this.g = g;
			this.sw = sw;
			this.sh = sh;
			vb_Tex = LuanCreateBuffer(4*2);
			g.Log("LuanQuad request="+x+","+y+"  "+ w+","+h+"  "+ sw+","+sh);
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
			g.Log("LuanQuad texcoords="+u0+","+v0+"  "+ u1+","+v0+"  "+ u0+","+v1+"  "+ u1+","+v1);
			LuanFillBuffer(vb_Tex,spriteTexFloats); // assuming the sprite thing is always the full texture and not a subthing
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
		
		public static LuaTable CreateMetaTable () {
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
				LuanQuad me = self(args);
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
				return LuaValue.valueOf(s == "Object" || s == "Quad"); 
			} });
			
			
			return mt;
		}
	}
	
	// ***** ***** ***** ***** *****  LuanImage
	
	public static class LuanImage extends LuanDrawable {
		private LuanGraphics	g;
		private int				miTextureID = 0;
		public float			mWidth;
		public float			mHeight;
		public int				mFilterMin = GL10.GL_LINEAR;
		public int				mFilterMag = GL10.GL_LINEAR;
		public int				mWrapH = GL10.GL_REPEAT;
		public int				mWrapV = GL10.GL_REPEAT;
			
		public static LuanImage self (Varargs args) { return (LuanImage)args.checkuserdata(1,LuanImage.class); }
		
		public static String	Filter2Str	(int	a) { return (a == GL10.GL_LINEAR)?"linear":"nearest"; }
		public static int		Str2Filter	(String a) { return (a == "linear")?GL10.GL_LINEAR:GL10.GL_NEAREST; }
		
		public static String	Wrap2Str	(int	a) { return (a == GL10.GL_CLAMP_TO_EDGE)?"clamp":"repeat"; }
		public static int		Str2Wrap	(String a) { return (a == "clamp")?GL10.GL_CLAMP_TO_EDGE:GL10.GL_REPEAT; }
		
		
		public static LuaTable CreateMetaTable () {
			LuaTable mt = LuaValue.tableOf();
			LuaTable t = LuaValue.tableOf();
			mt.set("__index",t);
			
			/// min, mag = Image:getFilter( )	"linear" , "nearest"
			t.set("getFilter", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { 
				return LuaValue.varargsOf(	
					LuaValue.valueOf(Filter2Str(self(args).mFilterMin)) , 
					LuaValue.valueOf(Filter2Str(self(args).mFilterMag)) ); } });
			
			/// Image:setFilter( min, mag )		"linear" , "nearest"
			t.set("setFilter", new VarArgFunction() { @Override public Varargs invoke(Varargs args) {
				self(args).setFilter(Str2Filter(args.checkjstring(2)),Str2Filter(args.checkjstring(3)));
				return LuaValue.NONE;
			} });
			
			/// horiz, vert = Image:getWrap( )	"repeat" , "clamp"
			t.set("getWrap", new VarArgFunction() { @Override public Varargs invoke(Varargs args) {
				return LuaValue.varargsOf(
					LuaValue.valueOf(Wrap2Str(self(args).mWrapH)) , 
					LuaValue.valueOf(Wrap2Str(self(args).mWrapV)) ); } });
			
			/// Image:setWrap( horiz, vert )	"repeat" , "clamp"
			t.set("setWrap", new VarArgFunction() { @Override public Varargs invoke(Varargs args) {
				self(args).setWrap(Str2Wrap(args.checkjstring(2)),Str2Wrap(args.checkjstring(3)));
				return LuaValue.NONE;
			} });
			
			/// w = Image:getWidth( )
			t.set("getWidth", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(self(args).mWidth); } });
			
			/// h = Image:getHeight( )
			t.set("getHeight", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(self(args).mWidth); } });
			
			/// type = Object:type()  , e.g. "Image" or audio:"Source"
			t.set("type", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf("Image"); } });
			
			/// b = Object:typeOf( name )
			t.set("typeOf", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { 
				String s = args.checkjstring(2); 
				return LuaValue.valueOf(s == "Object" || s == "Drawable" || s == "Image"); 
			} });
			
			
			return mt;
		}
				
		public void setFilter (int min, int mag) {
			mFilterMin = min;
			mFilterMag = mag;
			GL10 gl = g.getGL();
			gl.glBindTexture( GL10.GL_TEXTURE_2D, GetTextureID() );
			gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, mFilterMin );
			gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, mFilterMag );
		}
		
		public void setWrap (int h, int v) {
			mWrapH = h;
			mWrapV = v;
			GL10 gl = g.getGL();
			gl.glBindTexture( GL10.GL_TEXTURE_2D, GetTextureID() );
			gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, mWrapH );
			gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, mWrapV );
		}
		
		public int GetTextureID () { 
			// TODO: reload if contextswitch detected
			return miTextureID;
		}
		
		public void LoadFromBitmap (Bitmap bm) {
			GL10 gl = g.getGL();
			
			// Generate one texture pointer
			int[] textureIds = new int[1];
			gl.glGenTextures( 1, textureIds, 0 );
			miTextureID = textureIds[0];

			// bind this texture
			gl.glBindTexture( GL10.GL_TEXTURE_2D, GetTextureID() );

			// Create Nearest Filtered Texture
			gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, mFilterMin );
			gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, mFilterMag );

			gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, mWrapH );
			gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, mWrapV );

			GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, bm, 0 ); // texImage2D(int target, int level, Bitmap bitmap, int border
		}
		
		public LuanImage (LuanGraphics g,String filepath) throws FileNotFoundException {
			this.g = g;
			
			// todo : remember filepath so textureid can be reconstructed if lost during context-switch ?
			
			//~ http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/graphics/CompressedTextureActivity.html
			//~ von ressource : InputStream input = getResources().openRawResource(R.raw["bla.png"]);  // NICHT Mï¿½GLICH!!!! ->> sd card
			//~ von ressource : InputStream input = getResources().openRawResource(R.raw.androids);
			//~ von sd laden : InputStreamODERSO input = openFileInput("lala.lua");
			//~ GLUtils.texImage2D : http://gamedev.stackexchange.com/questions/10829/loading-png-textures-for-use-in-android-opengl-es1		(see also comments/answers)
			//~ static Drawable 	Drawable.createFromStream(InputStream is, String srcName)
			
			Log.i("LuanImage","constructor:"+filepath);
			// TODO : throw lua error if file not found ?
			InputStream input = g.vm.getStorage().getFileStreamFromSdCard(filepath);
			//g.getActivity().openFileInput(filepath);
			//~ Drawable d = Drawable.createFromStream(input,filepath);
			Log.i("LuanImage","InputStream ok");
			
			BitmapDrawable bmd = new BitmapDrawable(g.vm.getResources(),input); // ressources needed for "density" / dpi etc ?  no idea
			Log.i("LuanImage","BitmapDrawable ok");
			Bitmap bm = bmd.getBitmap();
			Log.i("LuanImage","Bitmap ok w="+bm.getWidth()+",h="+bm.getHeight());
			mWidth = bm.getWidth();
			mHeight = bm.getHeight();
			// TODO : auto-scale to 2^n resolution ? naaaah.
			// bitmap loaded into ram
			
			// load into texture
			LoadFromBitmap(bm);
			Log.i("LuanImage","LoadFromBitmap done.");
			
			// release bitmap ram
			bm.recycle();
			
			Log.i("LuanImage","constructor done.");
		}
	}
}

