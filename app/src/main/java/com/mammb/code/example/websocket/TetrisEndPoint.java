package com.mammb.code.example.websocket;

import com.mammb.code.example.websocket.tetris.Tetris;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ServerEndpoint("/tetris")
public class TetrisEndPoint {

    private static final Map<Session, Tetris> sessions = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    static {
        Runnable command = () -> sessions.entrySet().forEach(s -> {
            final Tetris tetris = s.getValue();
            synchronized (tetris) {
                if (!tetris.isStarted()) {
                    return;
                }
                tetris.tick();
                TetrisEndPoint.send(s.getKey(), tetris);
            }
        });
        executor.scheduleWithFixedDelay(command, 0, 500, TimeUnit.MILLISECONDS);
    }

    @OnOpen
    public void onOpen(Session session) {
        final Tetris tetris = new Tetris();
        sessions.put(session, tetris);
        send(session, tetris);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        Tetris tetris = sessions.get(session);
        synchronized (tetris) {
            if (!tetris.isStarted()) {
                if (message.equals("83")) { // s:start
                    tetris.start();
                }
            } else {
                tetris.keyPressed(Integer.parseInt(message));
            }
            send(session, tetris);
        }
    }

    private static void send(Session session, Tetris tetris) {
        try {
            OutputStream os = session.getBasicRemote().getSendStream();
            tetris.write(os);
            os.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
