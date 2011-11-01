package net.schattenkind.androidLove.luan;

import java.io.FileNotFoundException;
import java.io.InputStream;

import net.schattenkind.androidLove.LoveVM;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLUtils;
import android.util.Log;

import javax.microedition.khronos.opengles.GL10;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class LuanGraphics {
	private Activity mActivity;
	public LoveVM vm;
	static final String sMetaName_LuanImage = "__MetaLuanImage";

	public LuanGraphics (LoveVM vm,Activity activity) { this.vm = vm; mActivity = activity; }
	
	public GL10		getGL () { return vm.getGL(); }
	public Activity	getActivity () { return mActivity; }
	
	public void Log (String s) { Log.i("LuanGraphics", s); }
	public void LogException (Exception e) { Log.e("LuanGraphics",e.getMessage()); }
	
	
	// TODO: warning, the final needed for the param here is so it can be used to get image:metatable in inner function, might cause problems if we ever use more than one lua state ?
	public LuaTable InitLib (final LuaValue _G) {
		InitSpriteBuffer();
		LuaTable t = LuaValue.tableOf();
		
		_G.set(sMetaName_LuanImage,LuanImage.CreateMetaTable());

		// love.graphics.print(sText,x,y)
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

		// img = love.graphics.newImage(sFileName)
		t.set("newImage", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				String s = args.checkjstring(1);
				try {
					return LuaValue.userdataOf(new LuanImage(LuanGraphics.this,s),_G.get(sMetaName_LuanImage));
				} catch (Exception e) {
					// TODO : throw lua error ?
					LogException(e);
				}
				return LuaValue.NONE;
			}
		});

		// love.graphics.draw(drawable, x, y, r=0, sx=1, sy=1, ox=0, oy=0)
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
		
		// love.graphics.setBackgroundColor( red, green, blue )  // 0-255
		t.set("setBackgroundColor", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				float r = (float)args.checkdouble(1);
				float g = (float)args.checkdouble(2);
				float b = (float)args.checkdouble(3);
				getGL().glClearColor(r/255f, g/255f, b/255f, 1.0f);
				// TODO : remember for recreate surface after mode switch
				return LuaValue.NONE;
			}
		});
		
		// love.graphics.setColor( red, green, blue, alpha )  // 0-255
		t.set("setColor", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				float r = (float)args.checkdouble(1);
				float g = (float)args.checkdouble(2);
				float b = (float)args.checkdouble(3);
				float a = (float)args.checkdouble(4);
				getGL().glColor4f(r/255f, g/255f, b/255f, a/255f);
				// TODO : remember for recreate surface after mode switch
				return LuaValue.NONE;
			}
		});
		
		return t;
	}
	
	// ***** ***** ***** ***** *****  DrawSprite function, rotate calc etc
	
	private float[]		spritePosFloats = new float[4*2];
	//~ private float[]		spriteTexFloats = { 0f,0f, 1f,0f, 0f,1f, 1f,1f }; // pirate test ok
	//~ private float[]		spriteTexFloats = { 0f,0f, 1f,0f, 0f,1f, 1f,1f }; // pirate test ok
	private float[]		spriteTexFloats = { 0f,1f, 1f,1f, 0f,0f, 1f,0f }; // cloud test ok ? my
	private FloatBuffer	spriteVB_Pos;
	private FloatBuffer	spriteVB_Tex;
	final static int	kBytesPerFloat = 4;
	private	GL10		gl; // only valid after notifyFrameStart
	
	private FloatBuffer	LuanCreateBuffer (int iNumFloats) {
		ByteBuffer bb = ByteBuffer.allocateDirect(iNumFloats * kBytesPerFloat);
		bb.order(ByteOrder.nativeOrder());// use the device hardware's native byte order
		return bb.asFloatBuffer(); // create a floating point buffer from the ByteBuffer
	}
	
	//~ WARNING: probably array sent by VALUE, e.g. copy -> slow
	private void		LuanFillBuffer (FloatBuffer b,float[] data) {
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
		
	// ***** ***** ***** ***** *****  LuanImage

	public static class LuanImage {
		private LuanGraphics	g;
		public int				miTextureID = 0;
		public float			mWidth;
		public float			mHeight;
			
		public static LuanImage self (Varargs args) { return (LuanImage)args.checkuserdata(1,LuanImage.class); }
		
		public static LuaTable CreateMetaTable () {
			LuaTable t = LuaValue.tableOf();
			//~ getHeight
			
			// min, mag = Image:getFilter( )
			// TODO : not yet implemented, "linear" , "nearest"
			t.set("getFilter", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.varargsOf( LuaValue.valueOf("linear") , LuaValue.valueOf("linear") ); } });
			
			// horiz, vert = Image:getWrap( )
			// TODO : not yet implemented, "repeat" , "clamp"
			t.set("getWrap", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.varargsOf( LuaValue.valueOf("repeat") , LuaValue.valueOf("repeat") ); } });
			
			// Image:setFilter( min, mag )
			// TODO : not yet implemented, "linear" , "nearest"
			t.set("setFilter", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } });
			
			// Image:setWrap( horiz, vert )
			// TODO : not yet implemented, "repeat" , "clamp"
			t.set("setWrap", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.NONE; } });
			
			// w = Image:getWidth( )
			t.set("getWidth", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(self(args).mWidth); } });
			
			// h = Image:getHeight( )
			t.set("getHeight", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(self(args).mWidth); } });
			
			// type = Object:type()  , e.g. "Image" or audio:"Source"
			t.set("type", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf("Image"); } });
			
			// b = Object:typeOf( name )
			t.set("typeOf", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { 
				String s = args.checkjstring(2); 
				return LuaValue.valueOf(s == "Object" || s == "Drawable" || s == "Image"); 
			} });
			
			
			return t;
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
			InputStream input = g.vm.getFileStreamFromSdCard(filepath);
			//g.getActivity().openFileInput(filepath);
			//~ Drawable d = Drawable.createFromStream(input,filepath);
			Log.i("LuanImage","InputStream ok");
			
			BitmapDrawable bmd = new BitmapDrawable(g.getActivity().getResources(),input); // ressources needed for "density" / dpi etc ?  no idea
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

