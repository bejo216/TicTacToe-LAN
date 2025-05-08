package com.example.appliakcija3.SocketsPackage;
import android.content.Intent;
import android.util.Log;

import com.example.appliakcija3.GameActivity;
import com.example.appliakcija3.HostServerActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class SocketHandling {
    public static Socket clientSocket;
    public static ServerSocket hostSocket;



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

    static ServerSocket serverSocket;

    public static boolean startHost() throws InterruptedException {
        AtomicBoolean ABConnected = new AtomicBoolean(false);
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(1234);

                    clientSocket = serverSocket.accept();
                    HostServerActivity.connectionBegan=true;
                    ABConnected.set(true);  // Blocking call
                    Log.d("Server11", "Client connected");

                    //BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    //String message = input.readLine();
                    //Log.d("Server11", "Received: " + message);

                    // Optionally send a response
                    //PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
                    //output.println("Hello from Server");

                    //input.close();
                    //output.close();
                    //clientSocket.close();




            } catch (IOException e) {
                Log.d("SocketConnection", "nope");

            }
        }).start();
        //Thread.sleep(1000);

        Log.d("SocketConnection", "--"+ABConnected);
        return ABConnected.get() ;
    }

    static PrintWriter out;

    public static void SendString(String message) {
        new Thread(() -> {

            try {
                out = new PrintWriter(clientSocket.getOutputStream());
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

    public static void WaitMove(){

        new Thread(() -> {
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                while (reader == null) {
                    Log.d("Debug1111", "s 0");
                    Thread.sleep(50);
                }




                    String message = reader.readLine();
                    if (message.equals("W")){
                        GameActivity.opponentMove=message;
                        return;
                    }else if (message.equals("D")){
                        GameActivity.opponentMove=message;
                        return;
                    }


                Log.d("Debug1111", "s "+message);
                    GameActivity.yourTurn =true;
                    GameActivity.opponentMove=message;
                    GameActivity.opponentSet.add(message);




            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }


        }).start();


    }
    public static void closeConnection() {
        try {
            if (reader != null) reader.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            Log.d("Debug11112", "greska u zatvaranju");
            e.printStackTrace();
        }
    }

}
