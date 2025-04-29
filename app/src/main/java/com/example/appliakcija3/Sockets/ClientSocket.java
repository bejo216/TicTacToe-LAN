package com.example.appliakcija3.Sockets;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientSocket {

    public boolean startClient(String ipAddress) throws InterruptedException {
        AtomicBoolean ABConnected = new AtomicBoolean(false);
        new Thread(() -> {
            try {
                Socket socket = new Socket(ipAddress, 1234); // server IP and port
                socket.getOutputStream().write("Hello from client11".getBytes());
                //Log.d("SocketConnection", "yes");
                ABConnected.set(true);
                Log.d("SocketConnection", "-"+ABConnected);
                socket.close();
            } catch (IOException e) {
                Log.d("SocketConnection", "nope");

            }
        }).start();
            Thread.sleep(1000);

        Log.d("SocketConnection", "--"+ABConnected);
        return ABConnected.get() ;
    }
}
