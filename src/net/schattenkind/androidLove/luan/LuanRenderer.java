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
	public boolean bResolutionOverrideActive = false;
	public int mfResolutionOverrideX;
	public int mfResolutionOverrideY;
	
	public LuanFont mFont;
	public LuanFont mDefaultFont;
	
	public FloatBuffer	mVB_Pos_font;
	public FloatBuffer	mVB_Tex_font;
	public float[]		mVB_Pos2_font;
	public float[]		mVB_Tex2_font;	
	
	protected	GL10		gl; // only valid after notifyFrameStart
	
	// ***** ***** ***** ***** *****  constructor
	
	public LuanRenderer (LoveVM vm) { super(vm); }
	
	// ***** ***** ***** ***** *****  utils
	
	public float getScreenW () { return bResolutionOverrideActive ? mfResolutionOverrideX : vm.mfScreenW; }
	public float getScreenH () { return bResolutionOverrideActive ? mfResolutionOverrideY : vm.mfScreenH; }
	
	public GL10		getGL () { return vm.getGL(); }
	
	
	public float LOVE_TODEG (float fRadians) { return 360f*fRadians/(float)Math.PI; }
	
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
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, spriteVB_Pos);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, spriteVB_Tex);
	}
	public void setVertexBuffersToCustom (FloatBuffer pos,FloatBuffer tex) {
		bVertexBuffersSprite = false;
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
		setVertexBuffersToSprite();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, iTextureID);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	}
		
	// ***** ***** ***** ***** *****  LuanDrawable
	
	public static class LuanDrawable {
		public boolean IsImage () { return false; }
		public void RenderSelf (float x,float y,float r,float sx,float sy,float ox,float oy) { }
	}
}
