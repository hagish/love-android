// handles non-trivial opengl interactions for love.graphics functionality separate from lua wrapper boilerplate
// e.g. vertex buffer allocation, polygon/line rendering etc 
// might be a good place to abstract for using OpenGL ES 1.1 if available

package net.schattenkind.androidLove.luan;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import net.schattenkind.androidLove.LoveVM;

// note : LuanGraphics extends LuanRenderer
public abstract class LuanRenderer extends LuanBase {
	protected	GL10		gl; // only valid after notifyFrameStart
	public static final int kMaxBasicGeoVertices = 128;
	
	public boolean bResolutionOverrideActive = false;
	public int mfResolutionOverrideX;
	public int mfResolutionOverrideY;
	
	public LuanObjFont mFont;
	public LuanObjFont mDefaultFont;
	
	public FloatBuffer	mVB_Pos_font;
	public FloatBuffer	mVB_Tex_font;
	public float[]		mVB_Pos2_font;
	public float[]		mVB_Tex2_font;	
	
	public FloatBuffer	mVB_BasicGeo;
	public float[]		mFB_BasicGeo;
	public int			mi_BasicGeo_Vertices;
	
	public static String	DrawMode2Str	(DrawMode a) { return (a == DrawMode.FILL)?"fill":"line"; }
	public static DrawMode	Str2DrawMode	(String a) { return (a.equals("fill"))?DrawMode.FILL:DrawMode.LINE; }
		
	public enum DrawMode {
		FILL,LINE
	};
	
	
	// ***** ***** ***** ***** *****  constructor
	
	public LuanRenderer (LoveVM vm) { super(vm); }
	
	// ***** ***** ***** ***** *****  init
	
	public void InitRenderer() {
		InitSpriteBuffer();
		mVB_BasicGeo = LuanGraphics.LuanCreateBuffer(kMaxBasicGeoVertices*2);
		mFB_BasicGeo = new float[kMaxBasicGeoVertices*2];
	}
	
	// ***** ***** ***** ***** *****  utils
	
	public float getScreenW () { return bResolutionOverrideActive ? mfResolutionOverrideX : vm.mfScreenW; }
	public float getScreenH () { return bResolutionOverrideActive ? mfResolutionOverrideY : vm.mfScreenH; }
	
	public GL10		getGL () { return vm.getGL(); }
	
	
	public float LOVE_TODEG (float fRadians) { return 360f*fRadians/(float)Math.PI; }
	
	// ***** ***** ***** ***** *****  basic geometry buffers
	
	public void BasicGeo_Prepare (int vnum) {
		assert(vnum <= kMaxBasicGeoVertices);
		mi_BasicGeo_Vertices = 0;
	}
	
	public void BasicGeo_Vertex (float x,float y) {
		int i = mi_BasicGeo_Vertices*2;
		++mi_BasicGeo_Vertices;
		mFB_BasicGeo[i  ] = x;
		mFB_BasicGeo[i+1] = y;
	}
	
	/// mode: e.g. GL10.GL_TRIANGLES
	///  GL_POINTS, GL_LINE_STRIP,GL_LINE_LOOP, GL_LINES, GL_TRIANGLE_STRIP, GL_TRIANGLE_FAN, and GL_TRIANGLES
	public void BasicGeo_Draw (int mode) {
		assert(mi_BasicGeo_Vertices <= kMaxBasicGeoVertices);
		LuanGraphics.LuanFillBuffer(mVB_BasicGeo,mFB_BasicGeo,mi_BasicGeo_Vertices*2);
		setVertexBuffersToCustom(mVB_BasicGeo);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
		gl.glDrawArrays(mode, 0, mi_BasicGeo_Vertices);
	}
	
	
	// ***** ***** ***** ***** *****  basic geometry

	
	/// love.graphics.circle( mode, x, y, radius, segments = 10 )
	public void renderCircle		(DrawMode mode,float x,float y,float radius,int segments) {
		BasicGeo_Prepare(segments);
		for (int i=0;i<segments;++i) {
			float ang = (float)Math.PI * 2f * ((float) i) / ((float)segments);
			float x1 = x + radius * (float)Math.sin(ang);
			float y1 = y + radius * (float)Math.cos(ang);
			BasicGeo_Vertex(x1,y1);
		}
		BasicGeo_Draw((mode == DrawMode.FILL) ? GL10.GL_TRIANGLE_FAN : GL10.GL_LINE_LOOP);
	}
	
	
	/// love.graphics.triangle( mode, x1, y1, x2, y2, x3, y3 )
	public void renderTriangle		(DrawMode mode,float x1,float y1,float x2,float y2,float x3,float y3) {
		if (mode == DrawMode.FILL) {
			BasicGeo_Prepare(3);
			BasicGeo_Vertex(x1,y1);
			BasicGeo_Vertex(x2,y2);
			BasicGeo_Vertex(x3,y3);
			BasicGeo_Draw(GL10.GL_TRIANGLES);
		} else {
			BasicGeo_Prepare(4);
			BasicGeo_Vertex(x1,y1);
			BasicGeo_Vertex(x2,y2);
			BasicGeo_Vertex(x3,y3);
			BasicGeo_Vertex(x1,y1);
			BasicGeo_Draw(GL10.GL_LINE_STRIP);
		}
	}
	
	/// love.graphics.quad( mode, x1, y1, x2, y2, x3, y3, x4, y4 )
	public void renderQuad			(DrawMode mode,float x1,float y1,float x2,float y2,float x3,float y3,float x4,float y4) {
		if (mode == DrawMode.FILL) {
			BasicGeo_Prepare(4);
			BasicGeo_Vertex(x1,y1);
			BasicGeo_Vertex(x2,y2);
			BasicGeo_Vertex(x3,y3);
			BasicGeo_Vertex(x4,y4);
			BasicGeo_Draw(GL10.GL_TRIANGLE_FAN);
		} else {
			BasicGeo_Prepare(5);
			BasicGeo_Vertex(x1,y1);
			BasicGeo_Vertex(x2,y2);
			BasicGeo_Vertex(x3,y3);
			BasicGeo_Vertex(x4,y4);
			BasicGeo_Vertex(x1,y1);
			BasicGeo_Draw(GL10.GL_LINE_STRIP);
		}
	}
	
	
	/// love.graphics.rectangle( mode, x, y, width, height ) 
	public void renderRectangle		(DrawMode mode,float x,float y,float w,float h) {
		if (mode == DrawMode.FILL) {
			BasicGeo_Prepare(4);
			BasicGeo_Vertex(x  ,y  );
			BasicGeo_Vertex(x+w,y  );
			BasicGeo_Vertex(x+w,y+h);
			BasicGeo_Vertex(x  ,y+h);
			BasicGeo_Draw(GL10.GL_TRIANGLE_FAN);
		} else {
			BasicGeo_Prepare(5);
			BasicGeo_Vertex(x  ,y  );
			BasicGeo_Vertex(x+w,y  );
			BasicGeo_Vertex(x+w,y+h);
			BasicGeo_Vertex(x  ,y+h);
			BasicGeo_Vertex(x  ,y  );
			BasicGeo_Draw(GL10.GL_LINE_STRIP);
		}
	}
	
	/// love.graphics.point( x, y )
	public void renderPoint			(float x,float y) {
		BasicGeo_Prepare(1);
		BasicGeo_Vertex(x  ,y  );
		BasicGeo_Draw(GL10.GL_POINTS);
	}
	
	/// love.graphics.line( x1, y1, x2, y2, ... )
	public void renderLine			(float x1,float y1,float x2,float y2) {
		BasicGeo_Prepare(2);
		BasicGeo_Vertex(x1,y1);
		BasicGeo_Vertex(x2,y2);
		BasicGeo_Draw(GL10.GL_LINE_STRIP);
	}
	
	/// love.graphics.line( x1, y1, x2, y2, ... )
	public void renderPolyLine		(float[] arr) {
		BasicGeo_Prepare(arr.length/2);
		for (int i=0;i<2*(arr.length/2);i+=2) BasicGeo_Vertex(arr[i],arr[i+1]);
		BasicGeo_Draw(GL10.GL_LINE_STRIP);
	}
	
	/// love.graphics.polygon( mode, ... )
	public void renderPolygon		(DrawMode mode,float[] arr) {
		if (mode == DrawMode.FILL) {
			BasicGeo_Prepare(arr.length/2);
			for (int i=0;i<2*(arr.length/2);i+=2) BasicGeo_Vertex(arr[i],arr[i+1]);
			BasicGeo_Draw(GL10.GL_TRIANGLE_FAN);
		} else {
			BasicGeo_Prepare(arr.length/2+1);
			for (int i=0;i<2*(arr.length/2);i+=2) BasicGeo_Vertex(arr[i],arr[i+1]);
			BasicGeo_Vertex(arr[0],arr[1]);
			BasicGeo_Draw(GL10.GL_LINE_STRIP);
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
	
	public static void		LuanFillBuffer (FloatBuffer b,float[] data,int len) {
		b.put(data,0,len); // add the coordinates to the FloatBuffer
		b.position(0); // set the buffer to read the first coordinate
	}
	
	protected void InitSpriteBuffer () {
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
	
	public boolean bVertexBuffersSprite = false;
	public void setVertexBuffersToSprite () {
		if (bVertexBuffersSprite) return;
		bVertexBuffersSprite = true;
		// Point to our vertex buffer
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, spriteVB_Pos);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, spriteVB_Tex);
	}
	
	/// texcoords disabled
	public void setVertexBuffersToCustom (FloatBuffer pos) {
		bVertexBuffersSprite = false;
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, pos);
		//~ gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, tex);
	}
	
	/// texcoords enabled
	public void setVertexBuffersToCustom (FloatBuffer pos,FloatBuffer tex) {
		bVertexBuffersSprite = false;
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, pos);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, tex);
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
			 
		
		//~ Log("notifyFrameStart");
		bVertexBuffersSprite = false;
		setVertexBuffersToSprite();
		
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
	
	public void DrawSprite	(int iTextureID,LuanObjQuad quad,float w,float h,float x,float y,float r,float sx,float sy,float ox,float oy) {
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
		setVertexBuffersToSprite();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, iTextureID);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	}
		
	// ***** ***** ***** ***** *****  LuanDrawable
	
	public static class LuanObjDrawable extends LuanObjBase {
		public boolean IsImage () { return false; }
		public void RenderSelf (float x,float y,float r,float sx,float sy,float ox,float oy) { }
	}
}
