package com.kodilla.checkers;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import static com.kodilla.checkers.CheckersApplication.tileSize;

public class Figure extends StackPane {
    FigureType type;

    public Figure(FigureType type, int x, int y) {

        this.type = type;

        relocate(x * tileSize, y * tileSize);

        Ellipse backEllipse = new Ellipse(tileSize * 0.37, tileSize * 0.27);
        backEllipse.setFill(Color.BLACK);
        backEllipse.setStroke(Color.BLACK);
        backEllipse.setStrokeWidth(tileSize * 0.04);
        setTranslateX((tileSize - tileSize * 0.37 * 2) / 2);
        setTranslateY((tileSize - tileSize * 0.27 * 2) / 2);
        backEllipse.setTranslateY(8);

        Ellipse ellipse = new Ellipse(tileSize * 0.37, tileSize * 0.27);
        ellipse.setFill(type == FigureType.BLACK ? Color.GRAY : Color.WHITE);
        ellipse.setStroke(Color.BLACK);
        ellipse.setStrokeWidth(tileSize * 0.04);
        setTranslateX((tileSize - tileSize * 0.37 * 2) / 2);
        setTranslateY((tileSize - tileSize * 0.27 * 2) / 2);

        getChildren().addAll(backEllipse, ellipse);
    }
    public FigureType getType() {
        return type;
    }
}
