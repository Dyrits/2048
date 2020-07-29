package com.codegym.games.snake;

import com.codegym.engine.cell.Color;
import com.codegym.engine.cell.Game;

public class SlowGreenApple extends Apple {
    protected static final String APPLE_SIGN = "\uD83C\uDF4F";
    public boolean displayed;
    public boolean willAppear;

    SlowGreenApple(int x, int y) {
        super(x, y);
        this.willAppear = false;
        this.displayed = true;
    }


    @Override
    public void draw(Game game) {
        game.setCellValueEx(this.x, this.y, Color.NONE, APPLE_SIGN, Color.GREEN, 75);
    }
}
