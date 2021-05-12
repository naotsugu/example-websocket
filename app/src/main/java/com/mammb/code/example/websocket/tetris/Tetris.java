package com.mammb.code.example.websocket.tetris;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

public class Tetris {

    private final int UNIT_SIZE = 20;
    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 22;

    private final BufferedImage img;
    private Tetrominoe[] board;

    private int curX;
    private int curY;
    private Block curBlock;

    private long score;
    private boolean stopped;
    private boolean isFallingFinished = false;

    public Tetris() {
        stopped = true;
        board = new Tetrominoe[BOARD_WIDTH * BOARD_HEIGHT];
        img = new BufferedImage(
                UNIT_SIZE * BOARD_WIDTH,
                UNIT_SIZE * BOARD_HEIGHT,
                BufferedImage.TYPE_INT_BGR);
    }

    public void start() {
        board = new Tetrominoe[BOARD_WIDTH * BOARD_HEIGHT];
        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++) {
            board[i] = Tetrominoe.NoShape;
        }
        score = 0;
        stopped = false;
        newPiece();
    }

    public void tick() {
        update();
        draw();
    }

    public void keyPressed(int keycode) {
        if (curBlock.getType() == Tetrominoe.NoShape || stopped) {
            return;
        }
        switch (keycode) {
            case 37 -> tryMove(curBlock, curX - 1, curY); // left arrow
            case 39 -> tryMove(curBlock, curX + 1, curY); // right arrow
            case 40 -> tryMove(curBlock.rotateRight(), curX, curY); // up arrow
            case 38 -> tryMove(curBlock.rotateLeft(), curX, curY); // down arrow
            case 32 -> dropDown(); // Space
        }
    }

    public void write(OutputStream os) throws IOException {
        ImageIO.write(img, "png", os);
        os.flush();
    }

    public boolean isStarted() {
        return !stopped;
    }

    private void draw() {
        Graphics g = img.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());

        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                Tetrominoe shape = shapeAt(j, BOARD_HEIGHT - i - 1);
                if (shape != Tetrominoe.NoShape) {
                    drawSquare(g, j * UNIT_SIZE,
                            i * UNIT_SIZE, shape);
                }
            }
        }
        if (curBlock.getType() != Tetrominoe.NoShape) {
            for (int i = 0; i < 4; i++) {
                int x = curX + curBlock.x(i);
                int y = curY - curBlock.y(i);
                drawSquare(g, x * UNIT_SIZE,
                        (BOARD_HEIGHT - y - 1) * UNIT_SIZE,
                        curBlock.getType());
            }
        }
        g.setColor(Color.WHITE);
        g.drawString("Score:" + score, 10, 15);
        g.dispose();
    }


    private void dropDown() {
        int newY = curY;
        while (newY > 0) {
            if (!tryMove(curBlock, curX, newY - 1)) {
                break;
            }
            newY--;
        }
        blockDropped();
    }

    private void oneLineDown() {
        if (!tryMove(curBlock, curX, curY - 1)) {
            blockDropped();
        }
    }


    private void blockDropped() {
        for (int i = 0; i < 4; i++) {
            int x = curX + curBlock.x(i);
            int y = curY - curBlock.y(i);
            board[(y * BOARD_WIDTH) + x] = curBlock.getType();
        }
        removeCompleteLines();
        if (!isFallingFinished) {
            newPiece();
        }
    }

    private void newPiece() {
        curBlock = Block.randomOf();
        curX = BOARD_WIDTH / 2 + 1;
        curY = BOARD_HEIGHT - 1 + curBlock.minY();
        if (!tryMove(curBlock, curX, curY)) {
            curBlock.setType(Tetrominoe.NoShape);
            stopped = true;
        }
    }

    private boolean tryMove(Block newBlock, int newX, int newY) {
        for (int i = 0; i < 4; i++) {
            int x = newX + newBlock.x(i);
            int y = newY - newBlock.y(i);
            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {
                return false;
            }
            if (shapeAt(x, y) != Tetrominoe.NoShape) {
                return false;
            }
        }

        curBlock = newBlock;
        curX = newX;
        curY = newY;

        draw();
        return true;
    }

    private void removeCompleteLines() {

        int completeLineCount = 0;

        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
            boolean complete = true;
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (shapeAt(j, i) == Tetrominoe.NoShape) {
                    complete = false;
                    break;
                }
            }

            if (complete) {
                completeLineCount++;
                for (int k = i; k < BOARD_HEIGHT - 1; k++) {
                    for (int j = 0; j < BOARD_WIDTH; j++) {
                        board[(k * BOARD_WIDTH) + j] = shapeAt(j, k + 1);
                    }
                }
            }
        }

        if (completeLineCount > 0) {
            score += completeLineCount * completeLineCount * 100L;
            isFallingFinished = true;
            curBlock.setType(Tetrominoe.NoShape);
        }
    }

    private void drawSquare(Graphics g, int x, int y, Tetrominoe shape) {

        g.setColor(shape.getColor());
        g.fillRect(x + 1, y + 1, UNIT_SIZE - 2, UNIT_SIZE - 2);

        g.setColor(shape.getColor().brighter());
        g.drawLine(x, y + UNIT_SIZE - 1, x, y);
        g.drawLine(x, y, x + UNIT_SIZE - 1, y);

        g.setColor(shape.getColor().darker());
        g.drawLine(x + 1, y + UNIT_SIZE - 1,
                x + UNIT_SIZE - 1, y + UNIT_SIZE - 1);
        g.drawLine(x + UNIT_SIZE - 1, y + UNIT_SIZE - 1,
                x + UNIT_SIZE - 1, y + 1);
    }


    private void update() {
        if (isFallingFinished) {
            isFallingFinished = false;
            newPiece();
        } else {
            oneLineDown();
        }
    }

    private Tetrominoe shapeAt(int x, int y) {
        return board[(y * BOARD_WIDTH) + x];
    }

}
