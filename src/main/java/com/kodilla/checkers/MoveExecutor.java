package com.kodilla.checkers;

public class MoveExecutor {

    private MoveType moveType;
    private Figure figure;

    public MoveExecutor(MoveType moveType) {
        this.moveType = moveType;
    }

    public MoveExecutor(MoveType moveType, Figure figure) {
        this.moveType = moveType;
        this.figure = figure;
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public Figure getFigure() {
        return figure;
    }
}
