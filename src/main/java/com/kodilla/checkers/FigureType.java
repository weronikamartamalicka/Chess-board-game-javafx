package com.kodilla.checkers;

public enum FigureType {
    WHITE(-1),BLACK(1);
    private final int moveDirection;

    FigureType(int moveDirection) {
        this.moveDirection = moveDirection;
    }

    public int getMoveDirection() {
        return moveDirection;
    }
}
