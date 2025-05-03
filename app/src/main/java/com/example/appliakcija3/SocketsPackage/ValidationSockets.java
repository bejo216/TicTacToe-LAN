package com.example.appliakcija3.SocketsPackage;
import java.net.InetAddress;
public class ValidationSockets {

    public static boolean IsIPAddressFormat(String IPAddress){
        try {
            InetAddress address = InetAddress.getByName(IPAddress);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
