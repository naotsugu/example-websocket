package com.mammb.code.example.websocket;

import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

@ServerEndpoint("/image")
public class ImageEndPoint {

    @OnOpen
    public void onOpen(Session session) {
        session.getUserProperties().put("point", new Point());
        onMessage("", session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        Point point = (Point) session.getUserProperties().get("point");
        switch (message) {
            case "37" -> point.x -= 4; // left arrow
            case "38" -> point.y -= 4; // up arrow
            case "39" -> point.x += 4; // right arrow
            case "40" -> point.y += 4; // down arrow
            default -> { point.x = 10; point.y = 10; }
        }
        send(point, session);
    }

    private static void send(Point point, Session session) {

        try {

            BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_BGR);

            Graphics g = img.getGraphics();
            g.setColor(Color.ORANGE);
            g.fillOval(point.x, point.y, 10, 10);
            g.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "png", baos);

            ByteBuffer buf = ByteBuffer.wrap(baos.toByteArray());
            session.getBasicRemote().sendBinary(buf);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
