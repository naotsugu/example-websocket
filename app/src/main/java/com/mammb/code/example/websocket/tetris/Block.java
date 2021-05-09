package com.mammb.code.example.websocket.tetris;

public class Block {

    private Tetrominoe type;
    private final int points[][];

    private Block(Tetrominoe type) {
        this.type = type;
        this.points = type.getPoints();
    }

    public static Block randomOf() {
        return new Block(Tetrominoe.random());
    }

    public Block rotateLeft() {
        if (type == Tetrominoe.SqShape) {
            return this;
        }
        var result = new Block(type);
        for (int i = 0; i < 4; ++i) {
            result.setX(i, y(i));
            result.setY(i, -x(i));
        }
        return result;
    }

    public Block rotateRight() {
        if (type == Tetrominoe.SqShape) {
            return this;
        }

        var result = new Block(type);
        for (int i = 0; i < 4; ++i) {
            result.setX(i, -y(i));
            result.setY(i, x(i));
        }

        return result;
    }

    public int x(int index) {
        return points[index][0];
    }

    public int y(int index) {
        return points[index][1];
    }

    public Tetrominoe getType()  {
        return type;
    }

    public void setType(Tetrominoe type)  {
        this.type = type;
    }

    private void setX(int index, int x) {
        points[index][0] = x;
    }

    private void setY(int index, int y) {
        points[index][1] = y;
    }

    public int minY() {
        int m = points[0][1];
        for (int i = 0; i < 4; i ++) {
            m = Math.min(m, points[i][1]);
        }
        return m;
    }

}
