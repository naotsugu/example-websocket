package com.mammb.code.example.websocket.tetris;

import java.awt.Color;
import java.util.SplittableRandom;

public enum Tetrominoe {
    NoShape   (new int[][] { { 0,  0 }, { 0,  0 }, { 0, 0 }, { 0, 0 } }, new Color(  0,   0,   0)),
    ZShape    (new int[][] { { 0, -1 }, { 0,  0 }, {-1, 0 }, {-1, 1 } }, new Color(204, 102, 102)),
    SShape    (new int[][] { { 0, -1 }, { 0,  0 }, { 1, 0 }, { 1, 1 } }, new Color(102, 204, 102)),
    LineShape (new int[][] { { 0, -1 }, { 0,  0 }, { 0, 1 }, { 0, 2 } }, new Color(102, 102, 204)),
    TShape    (new int[][] { {-1,  0 }, { 0,  0 }, { 1, 0 }, { 0, 1 } }, new Color(204, 204, 102)),
    SqShape   (new int[][] { { 0,  0 }, { 1,  0 }, { 0, 1 }, { 1, 1 } }, new Color(204, 102, 204)),
    LShape    (new int[][] { {-1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } }, new Color(102, 204, 204)),
    MLShape   (new int[][] { { 1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } }, new Color(218, 170,   0)),
    ;

    private static final SplittableRandom random = new SplittableRandom();
    private final int[][] points;
    private final Color color;

    Tetrominoe(int[][] points, Color color) {
        this.points = points;
        this.color = color;
    }

    public static Tetrominoe random() {
        return Tetrominoe.values()[Math.abs(random.nextInt()) % 7 + 1];
    }

    public int[][] getPoints() {
        var ret = new int[4][2];
        for (int i = 0; i < 4 ; i++) {
            for (int j = 0; j < 2; ++j) {
                ret[i][j] = points[i][j];
            }
        }
        return ret;
    }

    public Color getColor() {
        return color;
    }

}
