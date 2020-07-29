package com.codegym.games.snake;

import com.codegym.engine.cell.*;

import java.util.Random;

public class SnakeGame extends Game {
    public static final int WIDTH = 20, HEIGHT = 20;
    private static final int GOAL = 100;
    private Snake snake;
    private Apple apple;
    private SlowGreenApple slowGreenApple;
    private int turnDelay, score;
    private boolean isGameStopped;

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private void createGame() {
        this.score = 0;
        setScore(this.score);
        isGameStopped = false;
        snake = new Snake(WIDTH / 2, HEIGHT / 2);
        createNewApple();
        createNewApple(true);
        turnDelay = 250;
        setTurnTimer(turnDelay);
        drawScene();
    }

    private void drawScene() {
        for (int x = 0; x < WIDTH; x ++) {
            for (int y = 0; y < HEIGHT; y ++) {
                setCellValueEx(x, y, Color.LIGHTGREY, "");
            }
        }
        snake.draw(this);
        apple.draw(this);
        if (slowGreenApple.isAlive && slowGreenApple.displayed) { slowGreenApple.draw(this); }
    }

    private void createNewApple(boolean snowFlake) {
        int x;
        int y;
        Apple randomApple = null;
        SlowGreenApple specialRandomApple = null;
        do {
            x = getRandomNumber(WIDTH);
            y = getRandomNumber(HEIGHT);
            if (snowFlake) { specialRandomApple = new SlowGreenApple(x, y);}
            else { randomApple = new Apple(x, y); }
        } while (snake.checkCollision(snowFlake ? specialRandomApple : randomApple));
        if (snowFlake) { slowGreenApple = specialRandomApple; }
        else { apple = randomApple; }
    }

    private void createNewApple() {
        createNewApple(false);
    }


    private void gameOver() {
        isGameStopped = true;
        setTurnTimer(turnDelay = 25);
        showMessageDialog(Color.FIREBRICK, "GAME OVER!\nSCORE: " + this.score, Color.WHITE, 25);
    }

    private void win() {
        isGameStopped = true;
        setTurnTimer(turnDelay = 25);
        showMessageDialog(Color.DARKGREEN, "VICTORY!\nTHE SNAKE IS NOT HUNGRY ANYMORE!\nSCORE: " + this.score, Color.WHITE, 25);
    }

    @Override
    public void onTurn(int integer) {
        if (!isGameStopped) {
            System.out.println(turnDelay);
            snake.move(apple, slowGreenApple);
            if (!apple.isAlive) {
                setScore(this.score += 5);
                setTurnTimer(this.turnDelay -= this.turnDelay < 100 ? 5 : 10);
                createNewApple();
            }
            addSlowGreenApple();
        }
        if (!snake.isAlive) { gameOver(); }
        if (snake.getLength() > GOAL) { win(); }
        drawScene();
    }

    public void addSlowGreenApple() {
        if (this.score % 45 == 0 && this.score != 0) { slowGreenApple.willAppear = true; }
        boolean highSpeed = this.turnDelay < 50 && !slowGreenApple.displayed;
        boolean isAvailable = this.score % 50 == 0 && slowGreenApple.willAppear && !slowGreenApple.displayed;
        if (isAvailable || highSpeed) {
            createNewApple(true);
            while (slowGreenApple.x == apple.x && slowGreenApple.y == apple.y) { createNewApple(true); }
        }
        if (!slowGreenApple.isAlive && slowGreenApple.displayed) {
            setTurnTimer(this.turnDelay += (new Random().nextInt(75)) + 1);
            slowGreenApple.x = -1;
            slowGreenApple.y = -1;
            slowGreenApple.displayed = false;
        }
    }

    @Override
    public void onKeyPress(Key key) {
        switch (key) {
            case UP:
                if (snake.getSnakeParts(0).x != snake.getSnakeParts(1).x) { snake.setDirection(Direction.UP); }
                break;
            case DOWN:
                if (snake.getSnakeParts(0).x != snake.getSnakeParts(1).x) { snake.setDirection(Direction.DOWN);; }
                break;
            case LEFT:
                if (snake.getSnakeParts(0).y != snake.getSnakeParts(1).y) { snake.setDirection(Direction.LEFT); }
                break;
            case RIGHT:
                if (snake.getSnakeParts(0).y != snake.getSnakeParts(1).y) { snake.setDirection(Direction.RIGHT); }
                break;
            case SPACE:
                if (isGameStopped) { createGame(); }
                break;
        }
    }
}
