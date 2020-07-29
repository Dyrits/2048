package com.codegym.games.minesweeper;

import com.codegym.engine.cell.Color;
import com.codegym.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperGame extends Game {
    private static final int SIDE = 18;
    private GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private int countMinesOnField, countFlags, countClosedTiles = SIDE * SIDE, score =0;
    private static final String MINE = "\uD83D\uDCA3", FLAG = "\uD83D\uDEA9";
    private boolean isGameStopped;

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
    }

    private void createGame() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                setCellValue(x, y, "");
                boolean isMine = getRandomNumber(10) < 1;
                if (isMine) {
                    countMinesOnField++;
                }
                gameField[y][x] = new GameObject(x, y, isMine);
                setCellColor(x, y, Color.DIMGREY);
            }
        }
        countMineNeighbors();
        countFlags = countMinesOnField;
    }

    private List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> result = new ArrayList<>();
        for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
            for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
                if (y < 0 || y >= SIDE) {
                    continue;
                }
                if (x < 0 || x >= SIDE) {
                    continue;
                }
                if (gameField[y][x] == gameObject) {
                    continue;
                }
                result.add(gameField[y][x]);
            }
        }
        return result;
    }

    private void countMineNeighbors() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                if (!gameField[y][x].isMine) {
                    for (GameObject neighbor: getNeighbors(gameField[y][x])) {
                        gameField[y][x].countMineNeighbors += neighbor.isMine ? 1 : 0;
                    };
                }
            }
        }
    }

    private void openTile(int x, int y) {
        if (!gameField[y][x].getIsOpen() && !isGameStopped && !gameField[y][x].getIsFlag()) {
            gameField[y][x].setIsOpen(true);
            setCellColor(x, y, Color.GAINSBORO);
            countClosedTiles --;
            if (gameField[y][x].isMine) {
                setCellValueEx(x, y, Color.FIREBRICK, MINE);
                gameOver();
            }
            else if (countClosedTiles == countMinesOnField) {
                win();
            } else if (gameField[y][x].countMineNeighbors == 0) {
                setCellValue(x, y, "");
                setCellColor(x, y, Color.WHITE);
                for (GameObject neighbor : getNeighbors(gameField[y][x])) {
                    openTile(neighbor.getX(), neighbor.getY());
                }
            } else {
                setCellNumber(x, y, gameField[y][x].countMineNeighbors);
            }
            if (!isGameStopped) {
                score += 5;
                setScore(score);
            }
        }
    }

    private void markTile(int x, int y) {
        if (!gameField[y][x].getIsOpen() && !isGameStopped) {
            boolean isFlag = gameField[y][x].getIsFlag();
            if (countFlags == 0 && !isFlag) { return; }
            countFlags += isFlag ? 1 : -1;
            setCellValue(x, y, isFlag ? "" : FLAG);
            setCellColor(x, y, isFlag ? Color.DIMGREY : Color.ORANGE);
            gameField[y][x].setIsFlag(!isFlag);
        }
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.GHOSTWHITE, "You lost!", Color.FIREBRICK, 36);
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.GHOSTWHITE, "You won!", Color.DARKGREEN,36);
    }

    private void restart() {
        isGameStopped = false;
        countClosedTiles = SIDE * SIDE;
        countMinesOnField = 0;
        score = 0;
        setScore(score);
        createGame();
    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        if (isGameStopped) { restart(); }
        else { openTile(x, y); }
    }

    @Override
    public void onMouseRightClick(int x, int y) {
        markTile(x, y);
    }
}