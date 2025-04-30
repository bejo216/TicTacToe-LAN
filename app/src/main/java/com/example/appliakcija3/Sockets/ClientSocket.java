package com.example.appliakcija3.Sockets;
import android.util.Log;

import com.example.appliakcija3.GameActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientSocket {
    public static Socket clientSocket;



    public static boolean startClient(String ipAddress) throws InterruptedException {
        AtomicBoolean ABConnected = new AtomicBoolean(false);
        new Thread(() -> {
            try {
                clientSocket = new Socket(ipAddress, 1234); // server IP and port

                //Log.d("SocketConnection", "yes");
                ABConnected.set(true);
                Log.d("SocketConnection", "-"+ABConnected);
                Log.d("SocketConnection", "CONNECTED?");


            } catch (IOException e) {
                Log.d("SocketConnection", "nope");

            }
        }).start();
            Thread.sleep(1000);

        Log.d("SocketConnection", "--"+ABConnected);
        return ABConnected.get() ;
    }

    public void SendMove(String gameMove) throws InterruptedException {
        new Thread(() -> {
            try {

                clientSocket.getOutputStream().write(gameMove.getBytes());

                Log.d("SocketConnection", "SENT PACKAGE");


            } catch (IOException e) {
                Log.d("SocketConnection", "nope PACKAGE");

            }
        }).start();


    }

    public static void SendString(String message) {
        new Thread(() -> {

            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
                out.println(message);
                out.flush();
                //out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            Log.d("SocketConnection", "SENT PACKAGE -message");


        }).start();


    }
    private static BufferedReader reader;
    private static boolean isRunning = true;
    public static void WaitMove(){
        new Thread(() -> {

            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                while (reader == null) {
                    Log.d("Debug1111", "s 0");
                    Thread.sleep(50);
                }



                while (isRunning) {
                    String message = reader.readLine();
                    Log.d("Debug1111", "s "+message);
                    GameActivity.YourTurn=true;
                    break;

                }
            } catch (InterruptedException e) {
                Log.d("Debug1111", "s 1");
                throw new RuntimeException(e);
            } catch (IOException e) {
                Log.d("Debug1111", "s 2");
                throw new RuntimeException(e);
            }


        }).start();


    }
}
