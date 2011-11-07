package net.schattenkind.androidLove.luan;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import net.schattenkind.androidLove.LoveVM;
import net.schattenkind.androidLove.luan.LuanGraphics;
import net.schattenkind.androidLove.luan.LuanImage;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLUtils;
import android.util.Log;


public class LuanFont {
	private LuanGraphics	g;
	public LuanImage		img;
	public float w_space = 0f; // TODO: set from letter 'a' ? 
	public float font_h = 0f; // TODO: set from letter 'a' ? probably just the height of the whole image
	public float line_h = 1f; ///< Gets the line height. This will be the value previously set by Font:setLineHeight, or 1.0 by default. 
	
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
	public HashMap mGlyphInfos = new HashMap();
	
	public GlyphInfo getGlyphInfo (char c) { return (GlyphInfo)mGlyphInfos.get(c); }
	
	public enum AlignMode {
		CENTER, LEFT, RIGHT
	};
	
	public static AlignMode	Text2Align (String s) {
		if (s == "center") return AlignMode.CENTER;
		if (s == "right") return AlignMode.RIGHT;
		return AlignMode.LEFT;
	}
	
	public static String	Align2Text (AlignMode a) {
		if (a == AlignMode.CENTER) return "center";
		if (a == AlignMode.RIGHT) return "right";
		return "left";
	}
	
	
	/// ttf font
	public LuanFont (LuanGraphics g,String ttf_filename,int iSize) { this.g = g; g.vm.NotImplemented("font:ttf"); }
	
	/// ttf font, default ttf_filename to verdana sans
	public LuanFont (LuanGraphics g,int iSize) { this.g = g; g.vm.NotImplemented("font:ttf"); } 
	
	/// imageFont
	public LuanFont (LuanGraphics g,String filename,String glyphs) throws FileNotFoundException { this(g,new LuanImage(g,filename),glyphs); }
	
	/// imageFont
	public LuanFont (LuanGraphics g,LuanImage img,String glyphs) {
		this.g = g;
		/*
		The imagefont file is an image file in a format that Löve can load. It can contain transparent pixels, so a PNG file is preferable, and it also needs to contain spacer color that will separate the different font glyphs.
		The upper left pixel of the image file is always taken to be the spacer color. All columns that have this color as their uppermost pixel are interpreted as separators of font glyphs. The areas between these separators are interpreted as the actual font glyphs.
		The width of the separator areas affect the spacing of the font glyphs. It is possible to have more areas in the image than are required for the font in the love.graphics.newImageFont() call. The extra areas are ignored. 
		*/
		int col = img.getColAtPos(0,0);
		int x = 0;
		int imgw = (int)img.mWidth;
		font_h = (int)img.mHeight;
		while (x < imgw && img.getColAtPos(x,0) == col) ++x; // skip first separator column
		
		for (int i=0;i<glyphs.length();++i) {
			char c = glyphs.charAt(i);
			
			// calc the size of the glyph
			int w = 1;
			while (x+w < imgw && img.getColAtPos(x+w,0) != col) ++w;
				
			// calc the size of the separator
			int spacing = 0;
			while (x+w+spacing < imgw && img.getColAtPos(x+w+spacing,0) == col) ++spacing;
			
			// register glyph
			//~ Log.i("LuanFont","glyph:"+c+":x="+x+",w="+w+",spacing="+spacing);
			mGlyphInfos.put(c,new GlyphInfo(w,w+spacing,(float)x/(float)imgw,(float)(x+w)/(float)imgw));
			
			x += w+spacing;
		}
	}
	
	public boolean isWhiteSpace (char c) { return c == ' ' || c == '\t' || c == '\r' || c == '\n'; }
	public float getGlyphMoveX (char c) { 
		if (c == ' ') return w_space;
		if (c == '\t') return 4f*w_space;
		GlyphInfo gi = getGlyphInfo(c); 
		if (gi != null) return gi.movex;
		return 0f; // TODO! GlyphInfo ? 
	}
	
	
	public void prepareBuffer (int maxglyphs) { prepareBuffer(maxglyphs,0f); }
	public void prepareBuffer (int maxglyphs,float fRotate) {
		// TODO: alloc/resize float buffers
	}
	public void addCharToBuffer(char c,float draw_x,float draw_y) { addCharToBuffer(c,draw_x,draw_y,1f,1f); }
	public void addCharToBuffer(char c,float draw_x,float draw_y, float sx, float sy) {
		// TODO: add geometry to float buffers if possible
	}
	public void drawBuffer () {
		// TODO: send geometry to ogre
	}
	
	public void print		(String text, float param_x, float param_y, float r, float sx, float sy) {
		g.vm.NotImplemented("love.graphics.print");
		
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
					x = 0f;
					y += line_h*font_h;
				}
			}
			addCharToBuffer(c,draw_x,draw_y,sx,sy);
		}
		drawBuffer();
	}
	
	
	
	/// NOTE: not related to c printf, rather wordwrap etc
	public void printf		(String text, float param_x, float param_y, float limit, AlignMode align) {
		g.vm.NotImplemented("love.graphics.printf");
		int len = text.length();
		prepareBuffer(len);
		float x = param_x; // TODO: align here
		float y = param_y;
		// TODO: ignores word boundaries for now, lookahead ? 
		for (int i=0;i<len;++i) {
			char c = text.charAt(i);
			float draw_x = x;
			float draw_y = y;
			if (!isWhiteSpace(c)) {
				float mx = getGlyphMoveX(c);
				if (x + mx < limit) {
					x += mx;
				} else {
					draw_x = 0; // TODO: align here
					draw_y = y + line_h*font_h;
					x = draw_x + mx;
					y = draw_y;
				}
			} else {
				if (c == ' ' ) x += getGlyphMoveX(c);
				if (c == '\t') x += getGlyphMoveX(c);
				if (c == '\n') {
					x = 0f; // TODO: align here
					y += line_h*font_h;
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
		for (int i=0;i<text.length();++i) x += getGlyphMoveX(text.charAt(i));
		return x;
	}
	
	public static LuanFont self (Varargs args) { return (LuanFont)args.checkuserdata(1,LuanFont.class); }
	
	public static LuaTable CreateMetaTable (final LuanGraphics g) {
		LuaTable mt = LuaValue.tableOf();
		LuaTable t = LuaValue.tableOf();
		mt.set("__index",t);
		
		/// height = Font:getHeight( )
		/// TODO:dummy
		t.set("getHeight",		new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("Font:getHeight"); return LuaValue.valueOf(self(args).font_h); } });
		
		/// height = Font:getLineHeight( )
		/// TODO:dummy
		t.set("getLineHeight",	new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("Font:getLineHeight"); return LuaValue.valueOf(self(args).line_h); } });
		
		/// width = Font:getWidth( line )
		/// TODO:dummy
		t.set("getWidth", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("Font:getWidth"); return LuaValue.valueOf(self(args).getLineW(args.checkjstring(2))); } });
		
		/// Font:setLineHeight( height )
		/// TODO:dummy
		t.set("setLineHeight", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("Font:setLineHeight"); self(args).line_h = (float)args.checkdouble(2); return LuaValue.NONE; } });
		
		/// Font:getWrap(text, width)
		/// TODO:dummy
		t.set("getWrap", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("Font:getWrap"); return LuaValue.valueOf(123); } });
		
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
