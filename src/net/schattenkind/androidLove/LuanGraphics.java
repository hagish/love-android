package net.schattenkind.androidLove;

import java.io.FileNotFoundException;
import java.io.InputStream;

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
	private LoveVM vm;

	LuanGraphics (LoveVM vm,Activity activity) { this.vm = vm; mActivity = activity; }
	
	public GL10		getGL () { return vm.getGL(); }
	public Activity	getActivity () { return mActivity; }
	
	public void Log (String s) { Log.i("LuanGraphics", s); }
	public void LogException (Exception e) { Log.e("LuanGraphics",e.getMessage()); }
	
	public LuaTable InitLib () {
		LuaTable t = LuaValue.tableOf();

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
					return LuaValue.userdataOf(new LuanImage(LuanGraphics.this,s));
				} catch (Exception e) {
					// TODO : throw lua error ?
					LogException(e);
				}
				return LuaValue.NONE;
			}
		});

		// love.graphics.draw(img,x,y)
		t.set("draw", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				LuanImage img = (LuanImage)args.checkuserdata(1,LuanImage.class);
				int x = args.checkint(2);
				int y = args.checkint(3);
				img.Draw(x,y);
				return LuaValue.NONE;
			}
		});
		
		return t;
	}
	
		
	// ***** ***** ***** ***** *****  LuanImage

	public class LuanImage {
		private LuanGraphics	g;
		public int				miTextureID = 0;
		private FloatBuffer		triangleVB;
		private FloatBuffer		texCoordBuffer;
		
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
			
			
			
			// just a test hack
				
			float triangleCoords[] = {
					// X, Y, Z
						-0.6f, -0.35f, 0,
						 0.6f, -0.35f, 0, 
						 0.0f, 0.659016994f, 0 };

			// initialize vertex Buffer for triangle
			ByteBuffer vbb = ByteBuffer.allocateDirect(
			// (# of coordinate values * 4 bytes per float)
					triangleCoords.length * 4);
			vbb.order(ByteOrder.nativeOrder());// use the device hardware's native
												// byte order
			triangleVB = vbb.asFloatBuffer(); // create a floating point buffer from
												// the ByteBuffer
			triangleVB.put(triangleCoords); // add the coordinates to the
											// FloatBuffer
			triangleVB.position(0); // set the buffer to read the first coordinate
						 
			// texcoords
			float texCoords[] = { 0f,0f, 1f,0f, 0.5f,1f };
			ByteBuffer byteBuffer = ByteBuffer.allocateDirect(texCoords.length * 4);
			byteBuffer.order(ByteOrder.nativeOrder());
			texCoordBuffer = byteBuffer.asFloatBuffer();
			texCoordBuffer.put(texCoords);
			texCoordBuffer.position(0);
		}
		
		public LuanImage (LuanGraphics g,String filepath) throws FileNotFoundException {
			this.g = g;
			//~ http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/graphics/CompressedTextureActivity.html
			//~ von ressource : InputStream input = getResources().openRawResource(R.raw["bla.png"]);  // NICHT M�GLICH!!!! ->> sd card
			//~ von ressource : InputStream input = getResources().openRawResource(R.raw.androids);
			//~ von sd laden : InputStreamODERSO input = openFileInput("lala.lua");
			//~ GLUtils.texImage2D : http://gamedev.stackexchange.com/questions/10829/loading-png-textures-for-use-in-android-opengl-es1		(see also comments/answers)
			//~ static Drawable 	Drawable.createFromStream(InputStream is, String srcName)
			
			Log.i("LuanImage","constructor:"+filepath);
			// TODO : throw lua error if file not found ?
			InputStream input = g.getActivity().openFileInput(filepath);
			//~ Drawable d = Drawable.createFromStream(input,filepath);
			Log.i("LuanImage","InputStream ok");
			
			BitmapDrawable bmd = new BitmapDrawable(g.getActivity().getResources(),input); // ressources needed for "density" / dpi etc ?  no idea
			Log.i("LuanImage","BitmapDrawable ok");
			Bitmap bm = bmd.getBitmap();
			Log.i("LuanImage","Bitmap ok w="+bm.getWidth()+",h="+bm.getHeight());
			// TODO : auto-scale to 2^n resolution ? naaaah.
			// bitmap loaded into ram
			
			// load into texture
			LoadFromBitmap(bm);
			Log.i("LuanImage","LoadFromBitmap done.");
			
			// release bitmap ram
			bm.recycle();
			
			Log.i("LuanImage","constructor done.");
		}
		
		public void Draw (int x,int y) {
			GL10 gl = g.getGL();
			// Draw the triangle
			
			gl.glEnable(GL10.GL_TEXTURE_2D);            //Enable Texture Mapping ( NEW )
			//~ gl.glShadeModel(GL10.GL_SMOOTH);            //Enable Smooth Shading
			//~ gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);    //Black Background
			//~ gl.glClearDepthf(1.0f);                     //Depth Buffer Setup
			//~ gl.glEnable(GL10.GL_DEPTH_TEST);            //Enables Depth Testing
			//~ gl.glDepthFunc(GL10.GL_LEQUAL);             //The Type Of Depth Testing To Do

			
			gl.glBindTexture(GL10.GL_TEXTURE_2D, miTextureID);
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			
			gl.glColor4f(0.8f, 0.2f, 0.2f, 0.0f);
				 
			
			// Point to our vertex buffer
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, triangleVB);
			//~ gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordBuffer);
			
			// Draw the vertices as triangle strip
			//~ gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
			
			//Disable the client state before leaving
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);



		}
	}
}

