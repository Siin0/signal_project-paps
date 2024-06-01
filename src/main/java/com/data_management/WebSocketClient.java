package com.data_management;

import java.net.URI;

import org.java_websocket.handshake.ServerHandshake;

import javax.swing.*;

public class WebSocketClient extends org.java_websocket.client.WebSocketClient {
    DataReaderClass reader = new DataReaderClass();
    DataStorage data;
    URI serveruri;
    int numPackets = 0;
    int reconnectAttempts = 10; //number of times to try reconnecting before giving up

    public WebSocketClient(URI serveruri, DataStorage data) {
        super(serveruri);
        this.serveruri = serveruri;
        this.data = data;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Server connected");
        numPackets = 0;
    }

    @Override
    public void onMessage(String message) {
        numPackets++;
        reader.addData(data, message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // The close codes are documented in class org.java_websocket.framing.CloseFrame

        //for some reason this breaks if you use a switch statement instead
        //TODO: reconnect() causes the program to freeze.
        //I tracked this to WebSocketClient.closeLatch, a CountdownLatch in WebSocketClient.closeBlocking()
        //that never reaches 0 and so never continues the thread. Finding out why the latch never counts down is
        //proving much more difficult.
        if(code==1000){
            System.out.println("Connection closed successfully.");
        } else if(code==1006){
            System.out.println("Connection closed unexpectedly.");
            //tryReconnect();
        } else if(code==1011){
            System.out.println("Internal server error encountered. Connection closed.");
        } else if(code==1012){
            System.out.println("Connection refused: Server is restarting.");
            //tryReconnect();
        } else if(code==1013){
            System.out.println("Connection refused: Server is temporarily unavailable.");
            //tryReconnect();
        } else{
            System.out.println("Connection closed: Unknown reason - no code received");
        }

        System.out.println("Packets received: "+numPackets);
        System.out.println("\nConnection closed: " + reason);

    }

    private void tryReconnect() {
        if (reconnectAttempts > 0){
            reconnectAttempts -= 1;
            System.out.println("Reconnecting...");
            reconnect();
            System.out.println("Finish reached.");
        } else {
            System.out.println("Maximum reconnect attempts exceeded.");
        }
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        // if the error is fatal then onClose will be called additionally
    }
}
