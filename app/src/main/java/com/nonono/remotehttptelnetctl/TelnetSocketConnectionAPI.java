package com.nonono.remotehttptelnetctl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TelnetSocketConnectionAPI {
    public String telnetcmd(String ip, String cmd) {
        StringBuffer response = new StringBuffer();
        Socket pingSocket;
        PrintWriter out;
        BufferedReader in;
        pingSocket = new Socket();
        try {
            pingSocket.connect(new InetSocketAddress(ip, 23), 500);
            out = new PrintWriter(pingSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(pingSocket.getInputStream()));
            out.println(cmd);
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            out.close();
            in.close();
            pingSocket.close();
        } catch (Exception e) {
            return e.toString();
        }
        return response.toString();
    }
}