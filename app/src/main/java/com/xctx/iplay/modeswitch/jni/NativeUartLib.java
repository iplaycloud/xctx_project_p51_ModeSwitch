package com.xctx.iplay.modeswitch.jni;

public class NativeUartLib {

	static {
		System.loadLibrary("native-uart-lib");
	}

	public native int openUart();

    public native byte[] readUart();

	public native int writeUart(byte[] buf, int len);

	public native int closeUart();
}
