package com.codegym.games.snake;

import com.codegym.engine.cell.*;

public class Apple extends GameObject {
    private static final String APPLE_SIGN = "\uD83C\uDF4E";
    public boolean isAlive = true;

    Apple(int x, int y) {
        super(x, y);
    }

    public void draw(Game game) {
        game.setCellValueEx(this.x, this.y, Color.NONE, APPLE_SIGN, Color.RED, 75);
    }
}
