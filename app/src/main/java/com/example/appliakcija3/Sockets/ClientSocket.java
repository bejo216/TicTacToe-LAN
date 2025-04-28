package com.example.appliakcija3.Sockets;
import android.util.Log;
import android.widget.TextView;

import com.example.appliakcija3.R;

import java.io.BufferedReader;
import java.io.IOException;
import android.view.View;
import android.widget.TextView;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocket {

    public void startClient(String ipAddress) {
        new Thread(() -> {
            try {
                Socket socket = new Socket(ipAddress, 1234); // server IP and port
                socket.getOutputStream().write("Hello from client11".getBytes());


                socket.close();
            } catch (IOException e) {

            }
        }).start();
    }
}
