package com.rmv.mse.microengine.logging;

import java.net.*;
import java.util.Enumeration;

public class NetworkUtil {
    public String getHostByName() {
        try {
            final InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostName();
        } catch (UnknownHostException e) {
            try {
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                if (interfaces == null) {
                    return "";
                }
                NetworkInterface intf = interfaces.nextElement();
                Enumeration<InetAddress> addresses = intf.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address instanceof Inet4Address) {
                        return address.getHostAddress();
                    }
                }
                interfaces = NetworkInterface.getNetworkInterfaces();
                while (addresses.hasMoreElements()) {
                    return addresses.nextElement().getHostAddress();
                }
                return "unsolvable";
            } catch (SocketException e1) {
                return "unsolvable";
            }
        }
    }
}
