package com.xctx.iplay.modeswitch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    NativeUartLib mNativeUartLib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNativeUartLib = new NativeUartLib();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                JoystickMode();
                break;
            case R.id.btn_2:
                ShieldMode();
                break;
        }
    }

    /*
    * shield_mode掌机模式：开启event服务
    * 并通知game sdk
    * */
    private void ShieldMode() {
        mNativeUartLib.startService();
    }

    /*
    * shield_mode掌机模式：关闭event服务
    * 并通知game sdk
    * */
    private void JoystickMode() {
        mNativeUartLib.stopService();
    }

    public native void startService();
    public native void stopService();
}
