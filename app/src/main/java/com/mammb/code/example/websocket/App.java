package com.mammb.code.example.websocket;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.tyrus.server.Server;
import java.awt.Desktop;
import java.net.URI;

public class App {

    public static final String RED = "\u001B[31m%s\u001B[0m";
    private final HttpServer staticServer;
    private final Server socketServer;

    public App() {
        this.staticServer = HttpServer.createSimpleServer("docroot", 8080);
        this.socketServer = new Server("localhost", 8025, "/websockets", null,
                EchoEndPoint.class,
                TimeEndPoint.class,
                ImageEndPoint.class,
                TetrisEndPoint.class);
    }

    public void run() {
        try {
            staticServer.start();
            socketServer.start();
            System.out.println(String.format(RED, "Press any key to stop the server..."));
            browse();
            System.in.read();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            staticServer.shutdown();
            socketServer.stop();
        }
    }

    public void browse() throws Exception {
        Object headless = System.getProperties().getOrDefault("java.awt.headless", "false");
        System.getProperties().put("java.awt.headless", "false");
        Desktop.getDesktop().browse(new URI("http://localhost:8080/index.html"));
        System.getProperties().put("java.awt.headless", headless);
    }

    public static void main(String[] args) {
        var app = new App();
        app.run();
    }

}
