package com.kodilla.checkers;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import static com.kodilla.checkers.CheckersApplication.tileSize;

public class Tile extends Rectangle {
    Figure figure;

    public Tile(boolean white, int x, int y) {
        setWidth(tileSize);
        setHeight(tileSize);
        setFill(white ? Color.valueOf("#f5f5dc") : Color.valueOf("#964b00"));
        relocate(x * tileSize, y * tileSize);
    }

    public Figure getFigure() {
        return figure;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    public boolean hasFigure() {
        return figure != null;
    }
}
