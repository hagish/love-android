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
	
	public GL10		getGL () { return vm.getGL(); }
	
	public void Log (String s) { Log.i("LuanGraphics", s); }
	public void LogException (Exception e) { Log.e("LuanGraphics",e.getMessage()); }
	
	
	public LuaTable InitLib () {
		InitSpriteBuffer();
		LuaTable t = LuaValue.tableOf();
		LuaValue _G = vm.get_G();
		
		_G.set(sMetaName_LuanImage,LuanImage.CreateMetaTable());
		_G.set(sMetaName_LuanQuad,LuanQuad.CreateMetaTable());
		_G.set(sMetaName_LuanFont,LuanFont.CreateMetaTable());

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
		
		
		/// font = love.graphics.newImageFont( image, glyphs )
		t.set("newImageFont", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				if (args.isstring(1)) {
					String filename = args.checkjstring(1);
					String glyphs = args.checkjstring(2);
					return LuaValue.userdataOf(new LuanFont(LuanGraphics.this,filename,glyphs),vm.get_G().get(sMetaName_LuanFont));
				} else {
					LuanImage img = (LuanImage)args.checkuserdata(1,LuanImage.class);
					String glyphs = args.checkjstring(2);
					return LuaValue.userdataOf(new LuanFont(LuanGraphics.this,img,glyphs),vm.get_G().get(sMetaName_LuanFont));
				}
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
		

		/// love.graphics.draw(drawable, x, y, r=0, sx=1, sy=1, ox=0, oy=0)
		t.set("draw", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				LuanImage img = (LuanImage)args.checkuserdata(1,LuanImage.class);
				int n = args.narg();
				float x = (float)args.checkdouble(2);
				float y = (float)args.checkdouble(3);
				float r  = (n >= 4) ? ((float)args.checkdouble(4)) : 0.0f;
				float sx = (n >= 5) ? ((float)args.checkdouble(5)) : 1.0f;
				float sy = (n >= 6) ? ((float)args.checkdouble(6)) : 1.0f;
				float ox = (n >= 7) ? ((float)args.checkdouble(7)) : 0.0f;
				float oy = (n >= 8) ? ((float)args.checkdouble(8)) : 0.0f;
				
				DrawSprite(img.miTextureID,img.mWidth,img.mHeight,x,y,r,sx,sy,ox,oy);
				return LuaValue.NONE;
			}
		});
		
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
		
		return t;
	}
	
	// ***** ***** ***** ***** *****  LuanColor
	
	public static class LuanColor {
		public float r;
		public float g;
		public float b;
		public float a;
		
		public LuanColor (Varargs args) { init(args,1); }
		
		public LuanColor (Varargs args,int i) { init(args,i); }
		
		private void init (Varargs args,int i) {
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
				a = (args.narg() >= i+3) ? (((float)args.checkdouble(i+3)) / 255f) : 1f;
			}
		}
	}
	
	// ***** ***** ***** ***** *****  DrawSprite function, rotate calc etc
	
	private float[]		spritePosFloats = new float[4*2];
	//~ private float[]		spriteTexFloats = { 0f,0f, 1f,0f, 0f,1f, 1f,1f }; // pirate test ok
	//~ private float[]		spriteTexFloats = { 0f,0f, 1f,0f, 0f,1f, 1f,1f }; // pirate test ok
	private float[]		spriteTexFloats = { 0f,1f, 1f,1f, 0f,0f, 1f,0f }; // cloud test ok ? my
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
	
	public void notifyFrameStart	(GL10 gl) {
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
	}
	
	public void notifyFrameEnd		(GL10 gl) {
		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_BLEND 	);
		//~ Log("notifyFrameEnd");
	}
	
	public void DrawSprite	(int iTextureID,float w,float h,float x,float y,float r,float sx,float sy,float ox,float oy) {
		//~ float e = 0.5f;
		//~ w = e;
		//~ h = e; // no coordinate system in place yet
		
		//~ sx = 2;
		//~ sy = 1;
		
		float mycos = (float)Math.cos(r);
		float mysin = (float)Math.sin(r);
		
		float vx_x = w*mycos;
		float vx_y = w*mysin;
		
		float vy_x =  h*mysin;
		float vy_y = -h*mycos; 

		vx_x *= sx;
		vx_y *= sx;
		
		vy_x *= sy;
		vy_y *= sy;
		
		//~ Log("DrawSprite vx="+vx_x+","+vx_y+" vy="+vy_x+","+vy_y);
		
		//~ float x0 = -0.5f*vx_x -0.5f*vy_x; // center
		//~ float y0 = -0.5f*vx_y -0.5f*vy_y; 
		
		float x0 = x - vx_x*ox/w - vy_x*oy/h; // top-left ?
		float y0 = y - vx_y*ox/w - vy_y*oy/h; 
		
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
		
		public LuanFont (LuanGraphics g,String filename,String glyphs) {}
		public LuanFont (LuanGraphics g,LuanImage img,String glyphs) {}
			
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
		private FloatBuffer	spriteVB_Tex;
		
		public static LuanQuad self (Varargs args) { return (LuanQuad)args.checkuserdata(1,LuanQuad.class); }
		
		/// called from love.graphics.newQuad ( x, y, width, height, sw, sh )
		/// see also http://love2d.org/wiki/love.graphics.newQuad
		/// e.g. top left 32x32 pixels of a 64x64 image : top_left = love.graphics.newQuad(0, 0, 32, 32, 64, 64)
		public LuanQuad (LuanGraphics g,float x,float y,float w,float h,float sw,float sh) {
			this.g = g;
			this.sw = sw;
			this.sh = sh;
			spriteVB_Tex = LuanCreateBuffer(4*2);
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
			float[]		spriteTexFloats = { u0,v1, u1,v1, u0,v0, u1,v0 }; // cloud test ok ? my
			LuanFillBuffer(spriteVB_Tex,spriteTexFloats); // assuming the sprite thing is always the full texture and not a subthing
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
		public int				miTextureID = 0;
		public float			mWidth;
		public float			mHeight;
			
		public static LuanImage self (Varargs args) { return (LuanImage)args.checkuserdata(1,LuanImage.class); }
		
		public static LuaTable CreateMetaTable () {
			LuaTable mt = LuaValue.tableOf();
			LuaTable t = LuaValue.tableOf();
			mt.set("__index",t);
			
			/// min, mag = Image:getFilter( )
			/// TODO: not yet implemented, "linear" , "nearest"
			t.set("getFilter", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.varargsOf( LuaValue.valueOf("linear") , LuaValue.valueOf("linear") ); } });
			
			/// horiz, vert = Image:getWrap( )
			/// TODO: not yet implemented, "repeat" , "clamp"
			t.set("getWrap", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.varargsOf( LuaValue.valueOf("repeat") , LuaValue.valueOf("repeat") ); } });
			
			/// Image:setFilter( min, mag )
			/// TODO: not yet implemented, "linear" , "nearest"
			t.set("setFilter", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } });
			
			/// Image:setWrap( horiz, vert )
			/// TODO: not yet implemented, "repeat" , "clamp"
			t.set("setWrap", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } });
			
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
		
		public void LoadFromBitmap (Bitmap bm) {
			GL10 gl = g.getGL();
			
			// Generate one texture pointer
			int[] textureIds = new int[1];
			gl.glGenTextures( 1, textureIds, 0 );
			miTextureID = textureIds[0];

			// bind this texture
			gl.glBindTexture( GL10.GL_TEXTURE_2D, miTextureID );

			// Create Nearest Filtered Texture
			gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR );
			gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR );

			gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT );
			gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT );

			GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, bm, 0 ); // texImage2D(int target, int level, Bitmap bitmap, int border
		}
		
		public LuanImage (LuanGraphics g,String filepath) throws FileNotFoundException {
			this.g = g;
			
			// todo : remember filepath so textureid can be reconstructed if lost during context-switch ?
			
			//~ http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/graphics/CompressedTextureActivity.html
			//~ von ressource : InputStream input = getResources().openRawResource(R.raw["bla.png"]);  // NICHT M�GLICH!!!! ->> sd card
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

