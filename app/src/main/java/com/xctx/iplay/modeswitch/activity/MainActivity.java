package com.xctx.iplay.modeswitch.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;

import com.xctx.iplay.modeswitch.IUartServer;
import com.xctx.iplay.modeswitch.R;
import com.xctx.iplay.modeswitch.jni.NativeGameEventServiceLib;
import com.xctx.iplay.modeswitch.jni.NativeUartLib;
import com.xctx.iplay.modeswitch.service.GameSDKService;

public class MainActivity extends Activity {

    NativeGameEventServiceLib mNativeGameEventServiceLib;
    NativeUartLib mNativeUartLib;

    private IUartServer mService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNativeGameEventServiceLib = new NativeGameEventServiceLib();
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
    * JoystickMode手柄模式：关闭event服务
    * 并通知game sdk
    * */
    private void JoystickMode() {

        /*
        * 1.先停止事件处理服务
        * */
        mNativeGameEventServiceLib.stopService();

        Intent intent = new Intent(GameSDKService.class.getName());

        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        try {

            mService.startServer();

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /*
    * 绑定服务时创建的Connection
    * */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IUartServer.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    /*
    * shield_mode掌机模式：开启event服务
    * 并通知game sdk
    * */
    private void ShieldMode() {

        /*
        * 1.停止串口服务器
        * */
        try {

            mService.stopServer();

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        /*
        * 2.开启本地服务
        * */
        mNativeGameEventServiceLib.startService();
    }
}
