package com.mammb.code.example.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/echo")
public class EchoEndPoint {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println(" ## onOpen [" + session.getId() + "]");
    }

    @OnMessage
    public String onMessage(String message, Session session) {
        System.out.println(" ## onMessage [" + message + "][" + session.getId() + "]");
        return message;
    }

    @OnError
    public void onError(Session session, Throwable cause) {
        System.out.println(" ## onError [" + cause.getMessage() + "][" + session.getId() + "]");
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println(" ## onClose [" + session.getId() + "]");
    }
}
