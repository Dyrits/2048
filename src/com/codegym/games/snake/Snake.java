package com.codegym.games.snake;

import com.codegym.engine.cell.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Snake {
    private final List<GameObject> snakeParts;
    private static final String HEAD_SIGN = "\uD83D\uDC7E";
    private static final String BODY_SIGN  = "\u26AB";
    public boolean isAlive = true;
    private Direction direction;


    public Snake(int x, int y) {
        this.snakeParts = new ArrayList<>(
                Arrays.asList(
                        new GameObject(x, y),
                        new GameObject(x + 1, y),
                        new GameObject(x + 2, y)
                )
        );
        setDirection(Direction.LEFT);
    }

    public void draw(Game snakeGame) {
        for (int index = 0; index < this.snakeParts.size(); index ++) {
            snakeGame.setCellValueEx(
                    this.snakeParts.get(index).x,
                    this.snakeParts.get(index).y,
                    Color.NONE,
                    index == 0 ? HEAD_SIGN : BODY_SIGN,
                    this.isAlive ? Color.BLACK : Color.RED,
                    75);
        }
    }

    public void move(Apple apple, SlowGreenApple slowGreenApple) {
        GameObject newHead = createNewHead();
        boolean outOfAxisX = newHead.x < 0 || newHead.x >= SnakeGame.WIDTH;
        boolean outOfAxisY = newHead.y < 0 || newHead.y >= SnakeGame.HEIGHT;
        this.isAlive = !(outOfAxisX || outOfAxisY) && !checkCollision(newHead);
        if (this.isAlive) {
            snakeParts.add(0, newHead);
            if (this.snakeParts.get(0).x == apple.x && this.snakeParts.get(0).y == apple.y) { apple.isAlive = false; }
            else { this.removeTail(); }
            if (this.snakeParts.get(0).x == slowGreenApple.x && this.snakeParts.get(0).y == slowGreenApple.y) { slowGreenApple.isAlive = false; }
        }
    }

    public GameObject createNewHead() {
        GameObject newHead = new GameObject(this.snakeParts.get(0).x, this.snakeParts.get(0).y);
        newHead.y = this.direction == Direction.UP ? newHead.y - 1 : this.direction == Direction.DOWN ? newHead.y + 1 : newHead.y;
        newHead.x = this.direction == Direction.LEFT ? newHead.x - 1 : this.direction == Direction.RIGHT ? newHead.x + 1 : newHead.x;
        return newHead;
    }

    public void removeTail() {
        this.snakeParts.remove(this.snakeParts.size() - 1);
    }

    public void setDirection(Direction direction) {
        boolean canGoUp = direction == Direction.UP && this.direction != Direction.DOWN;
        boolean canGoDown = direction == Direction.DOWN && this.direction != Direction.UP;
        boolean canGoLeft = direction == Direction.LEFT && this.direction != Direction.RIGHT;
        boolean canGoRight = direction == Direction.RIGHT && this.direction != Direction.LEFT;
        if(canGoUp || canGoDown || canGoLeft || canGoRight) {
            this.direction = direction;
        }
    }

    public boolean checkCollision(GameObject object) {
        for (GameObject part : this.snakeParts) {
            if (part.x == object.x && part.y == object.y) {
                return true;
            }
        }
        return false;
    }

    public int getLength() {
        return this.snakeParts.size();
    }

    public GameObject getSnakeParts(int index) { return this.snakeParts.get(index); }
}
