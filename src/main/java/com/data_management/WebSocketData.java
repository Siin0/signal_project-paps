package com.data_management;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class WebSocketData extends WebSocketClient {
    DataReaderClass reader = new DataReaderClass();
    DataStorage data;

    public WebSocketData(URI serveruri, DataStorage data) {
        super(serveruri);
        this.data = data;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Server connected");
    }

    @Override
    public void onMessage(String message) {
        reader.addData(data, message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // The close codes are documented in class org.java_websocket.framing.CloseFrame
        System.out.println("Connection closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        // if the error is fatal then onClose will be called additionally
    }
}
