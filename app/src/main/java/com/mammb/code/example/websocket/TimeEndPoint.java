package com.mammb.code.example.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.time.LocalDateTime;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@ServerEndpoint("/time")
public class TimeEndPoint {

    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
    static {
        Runnable command = () -> sessions.forEach(session ->
            session.getAsyncRemote().sendText(LocalDateTime.now().toString()));
        Executors.newSingleThreadScheduledExecutor()
            .scheduleWithFixedDelay(command, 1, 1, TimeUnit.SECONDS);
    }

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

}
