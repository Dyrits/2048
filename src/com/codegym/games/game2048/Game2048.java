package com.codegym.games.game2048;
import com.codegym.engine.cell.*;

public class Game2048 extends Game {
    private static final int SIDE = 4;
    private int [][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped;
    private int target, score;

    /**
     * Set the size of the game board, decide whether to display the grid, etc...
     */
    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }

    private void createGame() {
        target = 2048;
        score = 0;
        setScore(score);
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();
    }

    /**
     * Draw the game field and colors all the cells.
     */
    private void drawScene() {
        for (int x = 0; x < SIDE; x ++) {
            for (int y = 0; y < SIDE; y ++) {
                setCellColoredNumber(x, y, gameField[y][x]);
            }
        }
    }

    /**
     * Selects a random cell whose value is 0 in the gameField matrix.
     * If getRandomNumber(10) returns the number 9, the cell is assigned the value 4;
     * If getRandomNumber(10) returns a number between 0 and 8, the cell is assigned the value 2.
     */
    private void createNewNumber() {
        if (getMaxTileValue() == target) { win(); }
        int x, y, cell;
        do {
            x = getRandomNumber(SIDE);
            y = getRandomNumber(SIDE);
            cell = gameField[x][y];
        } while(cell != 0);
        int newNumber = getRandomNumber(10) == 9 ? 4 : 2;
        if (getRandomNumber(100) == 7) { newNumber = getMaxTileValue(); }
        gameField[x][y] = newNumber;
    }

    /**
     * Takes as arguments a cell's coordinates and value, and...
     * ...calculate the cell color (using the getColorByValue() method).
     * ...display the cell value and color on the game board.
     */
    private void setCellColoredNumber(int x, int y, int value) {
        setCellValueEx(x, y, getColorByValue(value), value == 0 ? "" : String.valueOf(value));
    }

    /**
     * Return a color based on the cell value passed to method
     */
    private Color getColorByValue(int value) {
        switch (value) {
            case 2:
                return Color.SILVER;
            case 4:
                return Color.LIGHTGREEN;
            case 8:
                return Color.MEDIUMSEAGREEN;
            case 16:
                return Color.GREEN;
            case 32:
                return Color.TEAL;
            case 64:
                return Color.STEELBLUE;
            case 128:
                return Color.DODGERBLUE;
            case 256:
                return Color.BLUEVIOLET;
            case 512:
                return Color.MEDIUMVIOLETRED;
            case 1024:
                return Color.CRIMSON;
            case 2048:
                return Color.FIREBRICK;
            default:
                return Color.LIGHTGRAY;
        }
    }

    /**
     * Shifts all non-zero elements of the row array to the left (towards the zero index).
     * Zero elements are moved to the right.
     * If at least one element was moved, the method returns true, otherwise – false.
     */
    private boolean compressRow(int[] row) {
        int zeros = 0;
        int shifts = 0;
        for (int index = 0; index < row.length; index ++) {
            if (row[index] == 0) { zeros ++; }
            else if (zeros > 0) {
                row[index - zeros] = row[index];
                row[index] = 0;
                shifts ++;
            }
        }
        return shifts > 0;
    }

    /**
     * Merges adjacent pairs of identical non-zero elements of the row array.
     */
    private boolean mergeRow(int[] row) {
        boolean merge = false;
        for (int index = 0; index < row.length - 1; index ++) {
            if (row[index] == 0) { continue; }
            if (row[index] == row [index + 1]) {
                row[index] += row[index + 1];
                row[index + 1] = 0;
                score += row[index];
                setScore(score);
                merge = true;
            }
        }
        return merge;
    }

    /**
     * Tests if a merge is possible without merging.
     */
    private boolean testMergeRow(int[] row) {
        for (int index = 0; index < row.length - 1; index ++) {
            if (row[index] == 0) { continue; }
            if (row[index] == row [index + 1]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the maximum value in the gameField matrix.
     */
    private int getMaxTileValue() {
        int max = gameField[0][0];
        for (int[] row : gameField) {
            for (int value : row) {
                max = Math.max(max, value);
            }
        }
        return max;
    }

    /**
     * Returns true if there are no zero elements but the gameField matrix has at least two adjacent (horizontally or vertically) cells with the same value.
     */
    private boolean canUserMove() {
        int[][] copy = copyGameField();
        for (int rotation = 0; rotation < 4; rotation ++) {
            for (int[] row : copy) {
                if (compressRow(row)) { return true; }
                if (testMergeRow(row)) { return true; }
                for (int index = 0; index < SIDE; index ++) {
                    if (row[index] == 0) { return true; }
                }
            }
            rotateClockwise(copy);
        }
        return false;
    }

    /**
     * When the player win, the game is stopped and a message is displayed.
     */
    private void win() {
        isGameStopped = true;
        showMessageDialog(
                Color.TEAL,
                "You reached " + target + "!" + "\n" + "Score: " + score +"\n" + "Press [Space] to continue.",
                Color.WHITE,
                30
        );
    }

    /**
     * When the player win, the game is stopped and a message is displayed.
     */
    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(
                Color.FIREBRICK,
                "You lost!" + "\n" + "Score: " + score +"\n" + "Press [Space] to restart.",
                Color.WHITE,
                30
        );
    }

    /**
     * When a key is pressed, the corresponding action happens (shift, merge).
     */
    @Override
    public void onKeyPress(Key key) {
        if (!canUserMove()) { gameOver(); }
        if (key == Key.SPACE && isGameStopped) {
            if (getMaxTileValue() == target) { target += target; }
            else {
                createGame();
                drawScene();
            }
            isGameStopped = false;
        } else if (!isGameStopped) {
            switch (key) {
                case UP:
                    moveUp();
                    drawScene();
                    break;
                case DOWN:
                    moveDown();
                    drawScene();
                    break;
                case LEFT:
                    moveLeft();
                    drawScene();
                    break;
                case RIGHT:
                    moveRight();
                    drawScene();
                    break;
            }
        }
    }


    // MOVES

    private void moveUp() {
        for (int rotation = 0; rotation < 4; rotation ++) {
            if (rotation == 3) { moveLeft(); }
            rotateClockwise(gameField);
        }
    }

    private void moveDown() {
        for (int rotation = 0; rotation < 4; rotation ++) {
            if (rotation == 1) { moveLeft(); }
            rotateClockwise(gameField);
        }
    }

    private void moveLeft() {
        boolean hasMoved = false;
        for (int[] row : gameField) {
            if (compressRow(row)) { hasMoved = true; }
            if (mergeRow(row)) { hasMoved = true; }
            compressRow(row);
        }
        if (hasMoved) { createNewNumber(); }
    }

    private void moveRight() {
        for (int rotation = 0; rotation < 4; rotation ++) {
            if (rotation == 2) { moveLeft(); }
            rotateClockwise(gameField);
        }
    }

    /**
     * Rotates a matrix clockwise by 90 degrees.
     */
    private void rotateClockwise(int[][] matrix) {
        int[][] copy = copyGameField();
        for (int indexRow = 0; indexRow < SIDE; indexRow ++) {
            for (int indexColumn = 0; indexColumn < SIDE; indexColumn ++) {
                int rotateIndex = SIDE - 1 - indexColumn;
                matrix[indexRow][indexColumn] = copy[rotateIndex][indexRow];
            }
        }
    }

    /**
     * Returns a copy of the gameField matrix.
     */
    private int[][] copyGameField() {
        int[][] copy = new int[SIDE][SIDE];
        for (int indexRow = 0; indexRow < SIDE; indexRow++) {
            copy[indexRow] = gameField[indexRow].clone();
        }
        return copy;
    }
}
