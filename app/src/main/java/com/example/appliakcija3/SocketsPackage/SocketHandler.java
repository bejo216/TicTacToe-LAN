package com.example.appliakcija3.SocketsPackage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketHandler {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private volatile boolean running = true;

    public SocketHandler(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        Thread readThread = new Thread(() -> {
            try {
                String line;
                while (running && (line = in.readLine()) != null) {
                    System.out.println("Received: " + line);
                    // You can handle the incoming message here (e.g., update UI via Handler)
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stop();
            }
        });

        Thread writeThread = new Thread(() -> {
            try {
                // Example: Sending message every 5 seconds
                while (running) {
                    out.println("hello1");
                    Thread.sleep(5000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stop();
            }
        });

        readThread.start();
        writeThread.start();
    }

    public void stop() {
        running = false;
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}