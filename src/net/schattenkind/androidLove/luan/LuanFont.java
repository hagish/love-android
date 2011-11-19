package net.schattenkind.androidLove.luan;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import net.schattenkind.androidLove.LoveVM;
import net.schattenkind.androidLove.R;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

//~ import android.renderscript.Font; // TTF try : android.renderscript cannot be resolved =(


public class LuanFont {
	protected static final String TAG = "LoveFont";
	public static final int kMaxGlyphsPerString = 1024;
	
	private LuanGraphics	g;
	public LuanImage		img;
	public float w_space = 0f; // TODO: set from letter 'a' ? 
	public float font_h = 0f; // TODO: set from letter 'a' ? probably just the height of the whole image
	public float line_h = 1.5f; ///< Gets the line height. This will be the value previously set by Font:setLineHeight, or 1.0 by default. 
	public boolean bForceLowerCase = false;
	
	public enum AlignMode {
		CENTER, LEFT, RIGHT
	};
	
	public class GlyphInfo {
		public float w;
		public float movex;
		public float u0;
		public float v0;
		public float u1;
		public float v1;
		public GlyphInfo(float w,float movex,float u0,float u1) {
			this.w = w;
			this.movex = movex;
			this.u0 = u0;
			this.v0 = 0f;
			this.u1 = u1;
			this.v1 = 1f;
		}
	};
	public HashMap<Character, GlyphInfo> mGlyphInfos = new HashMap<Character, GlyphInfo>();
	
	public GlyphInfo getGlyphInfo (char c) { return (GlyphInfo)mGlyphInfos.get(c); }
	
	
	public static AlignMode	Text2Align (String s) {
		if (s.equals("center")) return AlignMode.CENTER;
		if (s.equals("right")) return AlignMode.RIGHT;
		return AlignMode.LEFT;
	}
	
	public static String	Align2Text (AlignMode a) {
		if (a == AlignMode.CENTER) return "center";
		if (a == AlignMode.RIGHT) return "right";
		return "left";
	}
	
	// ***** ***** ***** ***** *****
	
	/// ttf font
	public LuanFont (LuanGraphics g,String ttf_filename,int iSize) throws IOException { this(g); this.g = g; g.vm.NotImplemented("font:ttf"); }
	
	/// ttf font
	public LuanFont (LuanGraphics g,int ttf_ResID,int iSize) throws IOException { this(g); this.g = g; g.vm.NotImplemented("font:ttf"); }
	
	/// ttf font, default ttf_filename to verdana sans
	public LuanFont (LuanGraphics g,int iSize) throws IOException { this(g); this.g = g; g.vm.NotImplemented("font:ttf with size"); } 
	
	/// fall back to image font in resources
	public LuanFont (LuanGraphics g) throws IOException { this(g,new LuanImage(g, R.raw.imgfont_w)," abcdefghijklmnopqrstuvwxyz0123456789.!'-:·"); this.g = g; bForceLowerCase = true; } 
	
	/// imageFont
	public LuanFont (LuanGraphics g,String filename,String glyphs) throws FileNotFoundException { this(g,new LuanImage(g,filename),glyphs); }
	
	/// imageFont
	public LuanFont (LuanGraphics g,LuanImage img,String glyphs) {
		this.g = g;
		this.img = img;
		
		// debug log
		//~ PrintWriter	pLog = null;
		//~ File fTempDir = g.vm.getStorage().getWritableSdCardDir(); // TODO: remove, DEBUG only
		//~ File fLog = null;
		//~ try {
			//~ fLog = File.createTempFile("fontlog",".txt",fTempDir); // TODO: remove, DEBUG only
			//~ pLog = new PrintWriter(fLog);
			//~ pLog.println("FontConstr: start "+fLog.getPath());
		//~ } catch (IOException e) {
		//~ }
		
		
		/*
		The imagefont file is an image file in a format that L�ve can load. It can contain transparent pixels, so a PNG file is preferable, and it also needs to contain spacer color that will separate the different font glyphs.
		The upper left pixel of the image file is always taken to be the spacer color. All columns that have this color as their uppermost pixel are interpreted as separators of font glyphs. The areas between these separators are interpreted as the actual font glyphs.
		The width of the separator areas affect the spacing of the font glyphs. It is possible to have more areas in the image than are required for the font in the love.graphics.newImageFont() call. The extra areas are ignored. 
		*/
		int col = img.getColAtPos(0,0);
		int x = 0;
		int imgw = (int)img.mWidth;
		font_h = (int)img.mHeight;
		w_space = 0f;
		while (x < imgw && img.getColAtPos(x,0) == col) ++x; // skip first separator column
			
		//~ if (pLog != null) pLog.println("FontConstr: img="+img.getDebugSource()+" col="+col+" w="+imgw+" h="+font_h+" x0="+x); // TODO: remove, DEBUG only
		//~ LoveVM.LoveLog(TAG,"FontConstr: img="+img.getDebugSource()+" col="+col+" w="+imgw+" h="+font_h+" x0="+x); // TODO: remove, DEBUG only
		
		for (int i=0;i<glyphs.length();++i) {
			char c = glyphs.charAt(i);
			
			// calc the size of the glyph
			int w = 1;
			while (x+w < imgw && img.getColAtPos(x+w,0) != col) ++w;
				
			// calc the size of the separator
			int spacing = 0;
			while (x+w+spacing < imgw && img.getColAtPos(x+w+spacing,0) == col) ++spacing;
			
			// register glyph
			//~ LoveVM.LoveLog(TAG,"glyph:"+c+":x="+x+",w="+w+",spacing="+spacing);
			mGlyphInfos.put(c,new GlyphInfo(w,w+spacing,(float)x/(float)imgw,(float)(x+w)/(float)imgw));
			
			//~ if (pLog != null) pLog.println("glyph="+c+" x="+x+" w="+w+" spacing="+spacing); // TODO: remove, DEBUG only
			//~ LoveVM.LoveLog(TAG,"glyph="+c+" x="+x+" w="+w+" spacing="+spacing); // TODO: remove, DEBUG only
			
			if (w_space == 0f) w_space = w;
			x += w+spacing;
		}
		
		GlyphInfo gi = getGlyphInfo(' '); 
		if (gi != null) w_space = gi.movex;
			
		//~ if (pLog != null) pLog.flush();
		//~ if (pLog != null) pLog.close();
	}
	
	public boolean isWhiteSpace (char c) { return c == ' ' || c == '\t' || c == '\r' || c == '\n'; }
	public float getGlyphMoveX (char c) { 
		if (c == ' ') return w_space;
		if (c == '\t') return 4f*w_space;
		GlyphInfo gi = getGlyphInfo(c); 
		if (gi != null) return gi.movex;
		return 0f; // TODO! GlyphInfo ? 
	}
	
	
	// render buffer 
	
	private FloatBuffer	mVB_Pos;
	private FloatBuffer	mVB_Tex;
	private float[]		mVB_Pos2;
	private float[]		mVB_Tex2;
	
	int					mBufferVertices;
	
	public void prepareBuffer (int maxglyphs) { prepareBuffer(maxglyphs,0f); }
	public void prepareBuffer (int maxglyphs,float fRotate) {
		// alloc/resize float buffers
		//~ mVB_Pos = LuanGraphics.LuanCreateBuffer(maxglyphs*6*2); // TODO: memleak?  reuse/clear existing possible ? 
		//~ mVB_Tex = LuanGraphics.LuanCreateBuffer(maxglyphs*6*2);
		//~ mVB_Pos2 = new float[maxglyphs*6*2];
		//~ mVB_Tex2 = new float[maxglyphs*6*2];
		
		if (g.mVB_Pos_font == null) {
			g.mVB_Pos_font = LuanGraphics.LuanCreateBuffer(kMaxGlyphsPerString*6*2); // TODO: memleak?  reuse/clear existing possible ? 
			g.mVB_Tex_font = LuanGraphics.LuanCreateBuffer(kMaxGlyphsPerString*6*2);
			g.mVB_Pos2_font = new float[kMaxGlyphsPerString*6*2];
			g.mVB_Tex2_font = new float[kMaxGlyphsPerString*6*2];
		}
		mVB_Pos = g.mVB_Pos_font; // TODO: memleak?  reuse/clear existing possible ? 
		mVB_Tex = g.mVB_Tex_font;
		mVB_Pos2 = g.mVB_Pos2_font;
		mVB_Tex2 = g.mVB_Tex2_font;
		
		
		
		mBufferVertices = 0;
	}
	public void addCharToBuffer(char c,float draw_x,float draw_y) { addCharToBuffer(c,draw_x,draw_y,1f,1f); }
	public void addCharToBuffer(char c,float draw_x,float draw_y, float sx, float sy) {
		GlyphInfo gi = getGlyphInfo(c);
		if (gi == null) return;
		if (mVB_Pos == null) { LoveVM.LoveLog(TAG,"addCharToBuffer:mVB_Pos = null"); return; }
		if (mVB_Tex == null) { LoveVM.LoveLog(TAG,"addCharToBuffer:mVB_Tex = null"); return; }
			
		// add geometry to float buffers if possible
		
		float ax = draw_x;
		float ay = draw_y;
		float vx_x = gi.w*sx;
		float vx_y = 0f; // todo : rotate ?
		float vy_x = 0f; // todo : rotate ?
		float vy_y = font_h*sy;
		
		int i = mBufferVertices*2;
		mBufferVertices += 6;
		
		// triangle1  lt-rt-lb
		if (mBufferVertices < kMaxGlyphsPerString) {
			mVB_Tex2[i+0] = gi.u0; mVB_Pos2[i+0] = ax;
			mVB_Tex2[i+1] = gi.v0; mVB_Pos2[i+1] = ay;
			mVB_Tex2[i+2] = gi.u1; mVB_Pos2[i+2] = ax + vx_x;
			mVB_Tex2[i+3] = gi.v0; mVB_Pos2[i+3] = ay + vx_y;
			mVB_Tex2[i+4] = gi.u0; mVB_Pos2[i+4] = ax + vy_x;
			mVB_Tex2[i+5] = gi.v1; mVB_Pos2[i+5] = ay + vy_y;
			
			// triangle2 lb-rt-rb
			mVB_Tex2[i+6] = gi.u0;  mVB_Pos2[i+6] = ax + vy_x;
			mVB_Tex2[i+7] = gi.v1;  mVB_Pos2[i+7] = ay + vy_y;
			mVB_Tex2[i+8] = gi.u1;  mVB_Pos2[i+8] = ax + vx_x;
			mVB_Tex2[i+9] = gi.v0;  mVB_Pos2[i+9] = ay + vx_y;
			mVB_Tex2[i+10] = gi.u1; mVB_Pos2[i+10] = ax + vx_x + vy_x;
			mVB_Tex2[i+11] = gi.v1; mVB_Pos2[i+11] = ay + vx_y + vy_y;
		}
		
		/*
		// triangle1  lt-rt-lb
		mVB_Tex.put(gi.u0); mVB_Pos.put(ax);
		mVB_Tex.put(gi.v0); mVB_Pos.put(ay);
		mVB_Tex.put(gi.u1); mVB_Pos.put(ax + vx_x);
		mVB_Tex.put(gi.v0); mVB_Pos.put(ay + vx_y);
		mVB_Tex.put(gi.u0); mVB_Pos.put(ax + vy_x);
		mVB_Tex.put(gi.v1); mVB_Pos.put(ay + vy_y);
		
		// triangle2 lb-rt-rb
		mVB_Tex.put(gi.u0); mVB_Pos.put(ax + vy_x);
		mVB_Tex.put(gi.v1); mVB_Pos.put(ay + vy_y);
		mVB_Tex.put(gi.u1); mVB_Pos.put(ax + vx_x);
		mVB_Tex.put(gi.v0); mVB_Pos.put(ay + vx_y);
		mVB_Tex.put(gi.u1); mVB_Pos.put(ax + vx_x + vy_x);
		mVB_Tex.put(gi.v1); mVB_Pos.put(ay + vx_y + vy_y);
		*/
		
	}
	public void drawBuffer () {
		if (mVB_Pos == null) { LoveVM.LoveLog(TAG,"drawBuffer:mVB_Pos = null"); return; }
		if (mVB_Tex == null) { LoveVM.LoveLog(TAG,"drawBuffer:mVB_Tex = null"); return; }
		if (g == null) { LoveVM.LoveLog(TAG,"drawBuffer:g = null"); return; }
		GL10 gl = g.getGL();
		if (gl == null) { LoveVM.LoveLog(TAG,"drawBuffer:gl = null"); return; }
		if (img == null) { LoveVM.LoveLog(TAG,"drawBuffer:img = null"); return; }
		// TODO: send geometry to ogre
		//~ mVB_Pos.position(0); // set the buffer to read the first coordinate
		//~ mVB_Tex.position(0); // set the buffer to read the first coordinate
		LuanGraphics.LuanFillBuffer(mVB_Pos,mVB_Pos2,mBufferVertices*2);
		LuanGraphics.LuanFillBuffer(mVB_Tex,mVB_Tex2,mBufferVertices*2);
		g.setVertexBuffersToCustom(mVB_Pos,mVB_Tex);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, img.GetTextureID());
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, mBufferVertices);
	}
	
	public void print		(String text, float param_x, float param_y, float r, float sx, float sy) {
		if (r != 0f) g.vm.NotImplemented("love.graphics.print !rotation!");
		if (bForceLowerCase) text = text.toLowerCase();
		
		int len = text.length();
		prepareBuffer(len,r);
		float x = param_x;
		float y = param_y;
		// TODO: rotate code here rather than in prepareBuffer? x,y
		for (int i=0;i<len;++i) {
			char c = text.charAt(i);
			float draw_x = x;
			float draw_y = y;
			if (!isWhiteSpace(c)) {
				float mx = getGlyphMoveX(c);
				x += mx;
			} else {
				if (c == ' ' ) x += getGlyphMoveX(c);
				if (c == '\t') x += getGlyphMoveX(c);
				if (c == '\n') {
					x = param_x;
					y += line_h*font_h;
				}
			}
			addCharToBuffer(c,draw_x,draw_y,sx,sy);
		}
		drawBuffer();
	}
	
	
	
	/// NOTE: not related to c printf, rather wordwrap etc
	public void printf		(String text, float param_x, float param_y, float limit, AlignMode align) {
		if (bForceLowerCase) text = text.toLowerCase();
		int len = text.length();
		prepareBuffer(len);
		float x = param_x; // TODO: align here
		float y = param_y;
		boolean bAlignRecalcNeeded = true;
		// TODO: wrap ignores word boundaries for now, lookahead ? 
		//~ LoveVM.LoveLog(TAG,"printf:"+param_x+","+param_y+","+limit+","+Align2Text(align)+" :"+text); 
		for (int i=0;i<len;++i) {
			char c = text.charAt(i);
			if (bAlignRecalcNeeded) {
				bAlignRecalcNeeded = false;
				if (align != AlignMode.LEFT) {
					float linew = getLineW((i > 0) ? text : text.substring(i)); // getLineW automatically stops at newline
					//~ LoveVM.LoveLog(TAG,"printf:["+i+"] linew="+linew+","+Align2Text(align)+" :"+text); 
					if (linew > limit) linew = limit; // small inaccuracy here, but shouldn't matter much
					if (align == AlignMode.RIGHT) x += (limit - linew); 
					if (align == AlignMode.CENTER) x += (limit - linew)/2f; // text is in the middle between param_x and param_x+limit
				}
			}
			
			float draw_x = x;
			float draw_y = y;
			if (!isWhiteSpace(c)) {
				float mx = getGlyphMoveX(c);
				if (x + mx < param_x + limit) {
					x += mx;
				} else {
					draw_x = param_x; // TODO: align here
					draw_y = y + line_h*font_h;
					x = draw_x + mx;
					y = draw_y;
					bAlignRecalcNeeded = true;
				}
			} else {
				if (c == ' ' ) x += getGlyphMoveX(c);
				if (c == '\t') x += getGlyphMoveX(c);
				if (c == '\n') {
					x = param_x; // TODO: align here
					y += line_h*font_h;
					bAlignRecalcNeeded = true;
				}
			}
			addCharToBuffer(c,draw_x,draw_y);
		}
		// TODO: center/right align line-wise : getLineW(substr(... till next newline))
		drawBuffer();
	}
	
	/// doesn't support newlines
	public float getLineW (String text) {
		float x = 0f;
		for (int i=0;i<text.length();++i) {
			char c = text.charAt(i);
			x += getGlyphMoveX(c);
			if (c == '\n') return x; // early out
		}
		return x;
	}
	
	public static LuanFont self (Varargs args) { return (LuanFont)args.checkuserdata(1,LuanFont.class); }
	
	public static LuaTable CreateMetaTable (final LuanGraphics g) {
		LuaTable mt = LuaValue.tableOf();
		LuaTable t = LuaValue.tableOf();
		mt.set("__index",t);
		
		/// height = Font:getHeight( )
		t.set("getHeight",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(self(args).font_h); } });
		
		/// height = Font:getLineHeight( )
		t.set("getLineHeight",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(self(args).line_h); } });
		
		/// width = Font:getWidth( line )
		t.set("getWidth", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(self(args).getLineW(args.checkjstring(2))); } });
		
		/// Font:setLineHeight( height )
		t.set("setLineHeight", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { self(args).line_h = (float)args.checkdouble(2); return LuaValue.NONE; } });
		
		/// Font:getWrap(text, width)
		/// Returns how many lines text would be wrapped to. This function accounts for newlines correctly (i.e. '\n') 
		/// TODO:dummy
		t.set("getWrap", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("Font:getWrap"); return LuaValue.valueOf(1); } });
		
		/// type = Object:type()  , e.g. "Image" or audio:"Source"
		t.set("type", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf("Font"); } });
		
		/// b = Object:typeOf( name )
		t.set("typeOf", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { 
			String s = args.checkjstring(2); 
			return LuaValue.valueOf(s.equals("Object") || s.equals("Font")); 
		} });
		
		
		return mt;
	}
}
