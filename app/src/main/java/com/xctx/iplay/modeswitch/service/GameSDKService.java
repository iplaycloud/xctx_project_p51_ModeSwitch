package com.xctx.iplay.modeswitch.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.xctx.iplay.modeswitch.IUartServer;
import com.xctx.iplay.modeswitch.jni.NativeUartLib;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class GameSDKService extends Service {

    NativeUartLib mNativeUartLib;

    public GameSDKService() {
        mNativeUartLib = new NativeUartLib();
    }

    private ReadServerThread mReadServerThread;
    private boolean enableServer = true;

    private final IUartServer.Stub mBinder = new IUartServer.Stub() {

        public void basicTypes(int anInt, long aLong, boolean aBoolean,
                               float aFloat, double aDouble, String aString) {
            // Does nothing
        }

        @Override
        public boolean startServer() throws RemoteException {

            if(mReadServerThread == null)
            {
                enableServer = true;

                mReadServerThread = new ReadServerThread();
            }
            mReadServerThread.start();

            return true;
        }

        @Override
        public boolean stopServer() throws RemoteException {

            enableServer = false;

            return true;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    private class ReadServerThread extends Thread {

        ServerSocket server;
        @Override
        public void run() {

            try {
                /* 启动本地服务器 */
                server = new ServerSocket(12345, 5, InetAddress.getLocalHost());

                /* 等待客户端连接 */
                Socket socket = server.accept();

                Thread clientReadThread = new HandleClientReadThread(socket);
                clientReadThread.start();
                Thread clientWriteThread = new HandleClientWriteThread(socket);
                clientWriteThread.start();

                /* 等待当前线程结束 */
                clientReadThread.join();
                clientWriteThread.join();

                socket.close();
                server.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private class HandleClientReadThread extends Thread {

            private Socket socket;
            private OutputStream os;

            HandleClientReadThread(Socket socket) {
                this.socket = socket;
            }

            @Override
            public void run() {

                try {

                    os = socket.getOutputStream();
                    mNativeUartLib.openUart();

                    while (enableServer) {

                        /* 从串口读取数据传给客户端*/
                        byte[] buf = mNativeUartLib.readUart();

                        os.write(buf);

                        if (false)
                            break;
                    }

                    os.close();
                    mNativeUartLib.closeUart();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private class HandleClientWriteThread extends Thread {

            private Socket socket;
            private InputStream is;

            HandleClientWriteThread(Socket socket) {
                this.socket = socket;
            }

            @Override
            public void run() {

                byte [] buf = new byte[128];
                int len;

                try {

                    is = socket.getInputStream();
                    mNativeUartLib.openUart();

                    while (enableServer) {

                        /* 从客户端读数据写入到串口*/

                        len = is.read(buf);

                        /*这里可能有问题，需要验证*/
                        mNativeUartLib.writeUart(buf, len);

                        if (false)
                            break;
                    }

                    is.close();
                    mNativeUartLib.closeUart();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
