package com.example.appliakcija3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appliakcija3.SocketsPackage.SocketHandling;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class HostServerActivity extends AppCompatActivity {


    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        String ip = inetAddress.getHostAddress();
                        if (ip.startsWith("192.168.") || ip.startsWith("10.") || ip.startsWith("172.")) {
                            return ip;  // Return only private IPs
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return "No IP found";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hostserver);

        connectionBegan=false;
        TextView textview1= findViewById(R.id.HostServer_IPAddress_TextView);
        textview1.setText(getLocalIpAddress());
        try {
            SocketHandling.startHost();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        WaitForConnectUIUpdateThread();



    }

    public static boolean connectionBegan;
    public void WaitForConnectUIUpdateThread(){

        new Thread(() -> {

            while(!connectionBegan) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            Intent intent = new Intent(HostServerActivity.this, GameActivity.class);
            intent.putExtra("vsPlayer", false);
            intent.putExtra("yourTurn", false);
            runOnUiThread(() ->{
                intent.putExtra("vsPlayer", true);
                intent.putExtra("yourTurn", false);
                startActivity(intent);

            });
        }).start();
    }
}
