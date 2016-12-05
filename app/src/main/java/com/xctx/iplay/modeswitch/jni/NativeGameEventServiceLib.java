package com.xctx.iplay.modeswitch.jni;

public class NativeGameEventServiceLib {

	static {
		System.loadLibrary("native-game-service-lib");
	}
	
    public native int startService();

	public native int stopService();
}
