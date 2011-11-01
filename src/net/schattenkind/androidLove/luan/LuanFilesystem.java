package net.schattenkind.androidLove.luan;

import java.io.FileNotFoundException;
import java.io.IOException;

import net.schattenkind.androidLove.LoveStorage;
import net.schattenkind.androidLove.LoveVM;

import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import android.util.Log;

public class LuanFilesystem extends LuanBase {

	protected static final String TAG = "LoveFilesystem";

	public LuanFilesystem(LoveVM vm) {
		super(vm);
	}

	public LuaTable InitLib() {
		LuaTable t = LuaValue.tableOf();

		// files = love.filesystem.enumerate( dir )
		t.set("enumerate", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				// TODO
				return LuaValue.tableOf();
			}
		});

		// e = love.filesystem.exists( filename )
		t.set("exists", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaBoolean.valueOf(vm.getStorage().getFileType(
						args.arg1().toString()) == LoveStorage.FileType.NONE);
			}
		});

		// path = love.filesystem.getAppdataDirectory( )
		t.set("getAppdataDirectory", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaString.valueOf(vm.getStorage().getRootPath());
			}
		});

		// modtime, errormsg = love.filesystem.getLastModified( filename )
		t.set("getLastModified", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				// TODO
				return LuaValue.varargsOf(new LuaValue[] { LuaValue.NIL,
						LuaValue.NIL });
			}
		});

		// dir = love.filesystem.getSaveDirectory( )
		t.set("getSaveDirectory", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaString.valueOf(vm.getStorage().getRootPath());
			}
		});

		// path = love.filesystem.getUserDirectory( )
		t.set("getUserDirectory", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaString.valueOf(vm.getStorage().getRootPath());
			}
		});

		// cwd = love.filesystem.getWorkingDirectory( )
		t.set("getWorkingDirectory", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaString.valueOf(vm.getStorage().getRootPath());
			}
		});

		// love.filesystem.init( )
		t.set("init", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaValue.NONE;
			}
		});

		// is_dir = love.filesystem.isDirectory( filename )
		t.set("isDirectory", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaBoolean.valueOf(vm.getStorage().getFileType(
						args.arg1().toString()) == LoveStorage.FileType.DIR);
			}
		});

		// is_file = love.filesystem.isFile( filename )
		t.set("isFile", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaBoolean.valueOf(vm.getStorage().getFileType(
						args.arg1().toString()) == LoveStorage.FileType.FILE);
			}
		});

		// iterator = love.filesystem.lines( name )

		// chunk = love.filesystem.load( name )
		t.set("load", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				String filename = args.arg1().toString();
				try {
					LuaFunction f = LoadState.load(vm.getStorage()
							.getFileStreamFromSdCard(filename), filename, vm
							.get_G());
					return f;
				} catch (FileNotFoundException e) {
					Log.e(TAG, e.getMessage());
					return LuaValue.NONE;
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
					return LuaValue.NONE;
				}
			}
		});

		// ok = love.filesystem.mkdir( name )

		// file = love.filesystem.newFile( filename )

		// data = love.filesystem.newFileData( contents, name, decoder )

		// contents, size = love.filesystem.read( name, size )

		// ok = love.filesystem.remove( name )

		// love.filesystem.setIdentity( name )

		// love.filesystem.setSource( )
		t.set("init", new VarArgFunction() {
			@Override
			public Varargs invoke(Varargs args) {
				return LuaValue.NONE;
			}
		});
		
		// success = love.filesystem.write( name, data, size )

		return t;
	}

}
