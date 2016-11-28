package com.xctx.iplay.modeswitch;

public class NativeUartLib {

	static {
		System.loadLibrary("native-uart-lib");
	}
	
    public native int startService();

	public native int stopService();
}
