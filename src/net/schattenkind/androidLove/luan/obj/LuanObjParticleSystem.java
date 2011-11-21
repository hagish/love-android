package net.schattenkind.androidLove.luan.obj;

import net.schattenkind.androidLove.LoveVM;
import net.schattenkind.androidLove.luan.LuanBase;
import net.schattenkind.androidLove.luan.module.LuanGraphics;
import net.schattenkind.androidLove.luan.module.LuanRenderer.LuanObjDrawable;

import javax.microedition.khronos.opengles.GL10;

import java.nio.FloatBuffer;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;


public class LuanObjParticleSystem extends LuanObjDrawable {
	protected static final String TAG = "LoveParticleSystem";	
	protected static final int iVerticesPerParticle = 6; // triangle list needed
	private LoveVM			vm;
	
	private LuanGraphics	g;
	private LuanObjImage	img;
	private int				iMaxParticles;
	
	private FloatBuffer	mVB_Pos;
	private FloatBuffer	mVB_Tex;
	private float[]		mFB_Pos;
	private float[]		mFB_Tex;
	
	private Particle[] mParticles;
	private long	miParticlesAlive = 0;
	private long	miParticlesEmitted_Num = 0;
	
	private boolean bEmitterActive = true;
	private float	fEmitterRunT = 0f; ///< how long this emitter has been running, stopped if beyond fLifeTime
	
	private float	x_emit = 0f;
	private float	y_emit = 0f;
	private float	fEmissionRate = 1f; ///< The amount of particles per second. 
	private float	fLifeTime = -1f; ///< Sets how long the particle system should emit particles (if -1 then it emits particles forever).
	private float	fSize_Start = 1f;
	private float	fSize_End = 1f;
	private float	fSize_Var = 1f;
	private float	fPLife_Min = 1f;
	private float	fPLife_Max = 1f;
	private float	fParticleBaseRX = 16f;
	private float	fParticleBaseRY = 16f;
	
	private float fDirection = 0f; ///< Sets the direction the particles will be emitted in. 
	private float fSpread = (float)Math.PI; ///< Sets the amount of spread for the system.
	private float fSpeed_Min = 0f; ///< Sets the speed of the particles. 
	private float fSpeed_Max = 0f; ///< Sets the speed of the particles. 
	
	// ***** ***** ***** ***** ***** constructor 
	
	public LuanObjParticleSystem (LuanGraphics g,LuanObjImage img,int iMaxParticles) {
		super(g.vm); 
		this.g = g; 
		this.vm = g.vm; 
		this.img = img; 
		this.iMaxParticles = iMaxParticles;
		
		fParticleBaseRX = img.getWidth() / 2f;
		fParticleBaseRY = img.getHeight() / 2f;
		mParticles = new Particle[iMaxParticles];
		for (int i=0;i<iMaxParticles;++i) mParticles[i] = new Particle();
		
		// init buffers
		int iMaxVertices = iMaxParticles * iVerticesPerParticle;
		mVB_Pos = LuanGraphics.LuanCreateBuffer(iMaxVertices*2);
		mVB_Tex = LuanGraphics.LuanCreateBuffer(iMaxVertices*2);
		mFB_Pos = new float[iMaxVertices*2];
		mFB_Tex = new float[iMaxVertices*2];
		
		// init texcoord buffer, since they won't change
		float u0 = 0f;
		float v0 = 0f;
		float u1 = 1f;
		float v1 = 1f;
		for (int i=0;i<iMaxParticles;++i) {
			int base = i*iVerticesPerParticle*2;
			mFB_Tex[base+ 0] = u0;
			mFB_Tex[base+ 1] = v0;
			mFB_Tex[base+ 2] = u1;
			mFB_Tex[base+ 3] = v0;
			mFB_Tex[base+ 4] = u0;
			mFB_Tex[base+ 5] = v1;
			
			mFB_Tex[base+ 6] = u1;
			mFB_Tex[base+ 7] = v1;
			mFB_Tex[base+ 8] = u0;
			mFB_Tex[base+ 9] = v1;
			mFB_Tex[base+10] = u1;
			mFB_Tex[base+11] = v0;
		}
		LuanGraphics.LuanFillBuffer(mVB_Tex,mFB_Tex,iMaxVertices*2);
	}
		
	// ***** ***** ***** ***** ***** utils 
	
	public void Log (String s) { LoveVM.LoveLog(TAG, s); }
	
	// ***** ***** ***** ***** ***** updating
	
	/// Updates the particle system; moving, creating and killing particles. 
	public void update (float dt) {
		//~ Log("update");
		float t = vm.getTime();
		long cnew = 0;
		
		// emitter active
		if (bEmitterActive) {
			fEmitterRunT += dt;
			if (fLifeTime > 0f && fEmitterRunT > fLifeTime) bEmitterActive = false;
				
			// calc emission num 
			//~ Log("RenderSelf:calc emission num ");
			cnew = ((long)(fEmitterRunT * fEmissionRate)) - miParticlesEmitted_Num;
			if (cnew < 0) cnew = 0;
			miParticlesEmitted_Num += cnew;
		}
			
		// update particle pos etc
		//~ Log("RenderSelf:update particle pos etc ");
		for (int i=0;i<iMaxParticles;++i) {
			Particle p = mParticles[i];
			//~ if (p == null) Log("RenderSelf:particle null? "+i); else  // TODO : remove this for performance!!!!
			if (p.bAlive) {
				if (t > p.t_death) {
					p.bAlive = false;
					--miParticlesAlive;
				} else {
					// anim paramaters here
					float f = (t - p.t_death) / p.t_life + 1f; // optimized from (t - (p.t_death - p.t_life)) / p.t_life
					p.r = (f * fSize_End + (1f-f) * p.r_start); // interpolate
					p.x += dt * p.vx;
					p.y += dt * p.vy;
				}
			}
		}
		
		// create new particles 
		//~ Log("RenderSelf:create new particles ");
		for (int i=0;i<cnew && miParticlesAlive < iMaxParticles;++i) {
			Particle p = createNewParticle();
			if (p == null) break;
			++miParticlesAlive;
			p.bAlive = true;
			p.x = x_emit;
			p.y = y_emit;
			p.r_start = fSize_Start + fSize_Var * vm.getRandomFloatBetween(0,fSize_End-fSize_Start);
			p.r = p.r_start;
			p.t_life = vm.getRandomFloatBetween(fPLife_Min,fPLife_Max); // duration, in seconds
			p.t_death = t + p.t_life; // pre calc point of death
			float a = fDirection + vm.getRandomFloatBetween(-fSpread,fSpread);
			float s = vm.getRandomFloatBetween(fSpeed_Min,fSpeed_Max);
			p.vx = s * (float)Math.sin(a);
			p.vy = s * (float)Math.cos(a);
		}
		//~ Log("RenderSelf:done");
	}
	
	public void killAllParticles () {
		for (int i=0;i<iMaxParticles;++i) {
			Particle p = mParticles[i];
			p.bAlive = false;
		}
	}
	
	public Particle createNewParticle () {
		for (int i=0;i<iMaxParticles;++i) {
			Particle p = mParticles[i];
			if (!p.bAlive) return p;
		}
		return null;
	}
	
	/// returns number of active vertices to be drawn
	public int updateGeometry() {
		//~ Log("updateGeometry");
		int fnum = 0;
		for (int i=0;i<iMaxParticles;++i) fnum += mParticles[i].updateGeometry(fnum,mFB_Pos);
		if (fnum == 0) return 0;
		//~ Log("updateGeometry:LuanFillBuffer");
		LuanGraphics.LuanFillBuffer(mVB_Pos,mFB_Pos,fnum);
		//~ Log("updateGeometry:done");
		return fnum/2;
	}
	
	// ***** ***** ***** ***** ***** Particle 
	
	public class Particle {
		float t_death; ///< time of death, in seconds
		float t_life; ///< duration, in seconds
		float x;
		float y;
		float vx;
		float vy;
		float r;
		float r_start;
		boolean bAlive = false;
		
		public Particle () {}
		
		public int updateGeometry (int base,float[] mFB_Pos) {
			if (!bAlive) return 0;
			float x0 = x-r*fParticleBaseRX;
			float y0 = y-r*fParticleBaseRY;
			float x1 = x+r*fParticleBaseRX;
			float y1 = y+r*fParticleBaseRY;
			mFB_Pos[base+ 0] = x0;
			mFB_Pos[base+ 1] = y0;
			mFB_Pos[base+ 2] = x1;
			mFB_Pos[base+ 3] = y0;
			mFB_Pos[base+ 4] = x0;
			mFB_Pos[base+ 5] = y1;
			
			mFB_Pos[base+ 6] = x1;
			mFB_Pos[base+ 7] = y1;
			mFB_Pos[base+ 8] = x0;
			mFB_Pos[base+ 9] = y1;
			mFB_Pos[base+10] = x1;
			mFB_Pos[base+11] = y0;
			return 12;
		}
	}
	
	// ***** ***** ***** ***** ***** render 
	
	@Override public void RenderSelf (float x,float y,float r,float sx,float sy,float ox,float oy) {
		//~ Log("RenderSelf");
		// construct triangles
		int vnum = updateGeometry();
		if (vnum == 0) return;
		
		//~ Log("RenderSelf:getGL");
		GL10 gl = g.getGL();
		
		// apply transform parameters
		//~ Log("RenderSelf:glTranslatef");
		if (x != 0f && y != 0f) gl.glTranslatef(x,y,0);
			
		// render vertexbuffer
		//~ Log("RenderSelf:setVertexBuffersToCustom");
		g.setVertexBuffersToCustom(mVB_Pos,mVB_Tex);
		//~ Log("RenderSelf:GetTextureID");
		gl.glBindTexture(GL10.GL_TEXTURE_2D, img.GetTextureID());
		//~ Log("RenderSelf:glDrawArrays");
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vnum);
		
		// revert transform
		//~ Log("RenderSelf:glTranslatef");
		if (x != 0f && y != 0f) gl.glTranslatef(-x,-y,0);
		//~ Log("RenderSelf:done");
	}
	
	// ***** ***** ***** ***** ***** internal
	
	public void pause () { bEmitterActive = false; } ///< Pauses the particle emitter. 	
	public void reset () { killAllParticles(); fEmitterRunT = 0f; miParticlesEmitted_Num = 0; } ///< Resets the particle emitter, removing any existing particles and resetting the lifetime counter. 
	public void start () { bEmitterActive = true; } ///< Starts the particle emitter. 
	public void stop () { bEmitterActive = false; fEmitterRunT = 0f; miParticlesEmitted_Num = 0; } ///< Stops the particle emitter, resetting the lifetime counter
		
	public void setPosition (float x,float y) { this.x_emit = x; this.y_emit = y; } ///< Sets the position of the emitter. 

	///< Sets the size of the particle (1.0 being normal size). The particles will grow/shrink from the starting size to the ending size. The variation affects starting size only. 
	public void setSize (float fStart,float fEnd,float fVar) { fSize_Start = fStart; fSize_End = fEnd; fSize_Var = fVar; } 
	
	public void setParticleLife (float fMin,float fMax) { fPLife_Min = fMin; fPLife_Max = fMax;} ///< Sets the life of the particles. 
		
	public void setLifetime (float fSeconds) { fLifeTime = fSeconds; } ///< Sets how long the particle system should emit particles (if -1 then it emits particles forever). 
	
	public void setEmissionRate (float fEmissionRate) {
		this.fEmissionRate = fEmissionRate;
	}
	
	// ***** ***** ***** ***** ***** lua api 
	
	public static LuanObjParticleSystem self (Varargs args) { return (LuanObjParticleSystem)args.checkuserdata(1,LuanObjParticleSystem.class); }
	
	public static LuaTable CreateMetaTable (final LuanGraphics g) {
		LuaTable mt = LuaValue.tableOf();
		LuaTable t = LuaValue.tableOf();
		mt.set("__index",t);
		
		t.set("count"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(self(args).miParticlesAlive); } }); // TODO: not yet implemented
		t.set("getDirection"				,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("ParticleSystem:"+"getDirection"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getOffsetX"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("ParticleSystem:"+"getOffsetX"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getOffsetY"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("ParticleSystem:"+"getOffsetY"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("getSpread"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("ParticleSystem:"+"getSpread"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("isActive"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("ParticleSystem:"+"isActive"					); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("isEmpty"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("ParticleSystem:"+"isEmpty"					); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("isFull"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("ParticleSystem:"+"isFull"					); return LuaValue.NONE; } }); // TODO: not yet implemented
		
		t.set("setBufferSize"				,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("ParticleSystem:"+"setBufferSize"			); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setColor"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("ParticleSystem:"+"setColor"					); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setGravity"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("ParticleSystem:"+"setGravity"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setOffset"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("ParticleSystem:"+"setOffset"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setRadialAcceleration"		,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("ParticleSystem:"+"setRadialAcceleration"	); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setRotation"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("ParticleSystem:"+"setRotation"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setSizeVariation"			,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("ParticleSystem:"+"setSizeVariation"			); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setSpin"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("ParticleSystem:"+"setSpin"					); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setSpinVariation"			,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("ParticleSystem:"+"setSpinVariation"			); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setSprite"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("ParticleSystem:"+"setSprite"				); return LuaValue.NONE; } }); // TODO: not yet implemented
		t.set("setTangentialAcceleration"	,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { g.vm.NotImplemented("ParticleSystem:"+"setTangentialAcceleration"); return LuaValue.NONE; } }); // TODO: not yet implemented
				
		t.set("setDirection"				,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { self(args).fDirection = (float)args.checkdouble(2); return LuaValue.NONE; } });
		t.set("setSpread"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { self(args).fSpread = (float)args.checkdouble(2); return LuaValue.NONE; } });
		
		t.set("setSpeed"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) {
			self(args).fSpeed_Min = (float)args.checkdouble(2);
			self(args).fSpeed_Max = LuanBase.IsArgSet(args,3) ? (float)args.checkdouble(3) : self(args).fSpeed_Min;
			return LuaValue.NONE; 
		} });
		
		t.set("setEmissionRate"				,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { self(args).setEmissionRate((float)args.checkdouble(2)); return LuaValue.NONE; } });
		
		t.set("setParticleLife"				,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { 
			float fMin = (float)args.checkdouble(2);
			float fMax = LuanBase.IsArgSet(args,3) ? (float)args.checkdouble(3) : fMin;
			self(args).setParticleLife(fMin,fMax); 
			return LuaValue.NONE; 
		} });
			
		t.set("setSize"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { 
			float fStart = (float)args.checkdouble(2);
			float fEnd = LuanBase.IsArgSet(args,3) ? (float)args.checkdouble(3) : fStart;
			float fVar = LuanBase.IsArgSet(args,4) ? (float)args.checkdouble(4) : 1f;
			self(args).setSize(fStart,fEnd,fVar); 
			return LuaValue.NONE; 
		} });
		
		t.set("setLifetime"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { self(args).setLifetime((float)args.checkdouble(2)); return LuaValue.NONE; } });
		
		t.set("setPosition"					,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { self(args).setPosition((float)args.checkdouble(2),(float)args.checkdouble(3));	return LuaValue.NONE; } });
		t.set("getX"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(self(args).x_emit); } });
		t.set("getY"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { return LuaValue.valueOf(self(args).y_emit); } });
		
		t.set("pause"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { self(args).pause();	return LuaValue.NONE; } });
		t.set("reset"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { self(args).reset();	return LuaValue.NONE; } });
		t.set("start"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { self(args).start();	return LuaValue.NONE; } });
		t.set("stop"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { self(args).stop();	return LuaValue.NONE; } });
		
		/// ParticleSystem:update( dt )
		/// Updates the particle system; moving, creating and killing particles. 
		t.set("update"						,new VarArgFunction() { @Override public Varargs invoke(Varargs args) { self(args).update((float)args.checkdouble(2)); return LuaValue.NONE; } }); // TODO: not yet implemented
		
		/// b = Object:typeOf( name )
		t.set("typeOf", new VarArgFunction() { @Override public Varargs invoke(Varargs args) { 
			String s = args.checkjstring(2); 
			return LuaValue.valueOf(s.equals("Object") || s.equals("Drawable") || s.equals("ParticleSystem")); 
		} });
		
		
		return mt;
	}
	
	// ***** ***** ***** ***** ***** gfx-reinit
	
	@Override
	public void onGfxReinit(GL10 gl, float w, float h) {
		// TODO XXX
	}
}
