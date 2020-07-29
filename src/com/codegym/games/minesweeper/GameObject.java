package com.codegym.games.minesweeper;

public class GameObject {
    public int x;
    public int y;
    public boolean isMine, isOpen, isFlag;
    public int countMineNeighbors;

    public GameObject(int x, int y, boolean isMine) {
        setX(x);
        setY(y);
        setIsMine(isMine);
        setIsOpen(false);
        setIsFlag(false);
        this.countMineNeighbors = 0;
    }

    public void setIsFlag(boolean isFlag) { this.isFlag = isFlag; }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public void setX(int x) { this.x = x; }

    public void setY(int y) { this.y = y; }

    public int getX() { return this.x; }

    public int getY() { return this.y; }

    public boolean getIsOpen() { return this.isOpen; }

    public boolean getIsFlag() { return this.isFlag; }

    public void setIsMine(boolean isMine) { this.isMine = isMine; }
}
