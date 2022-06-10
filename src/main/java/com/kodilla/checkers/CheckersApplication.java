package com.kodilla.checkers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Random;

import static com.kodilla.checkers.MoveType.*;
public class CheckersApplication extends Application {

    private Group tilesGroup = new Group();
    private Group whiteFiguresGroup = new Group();
    private Group blackFiguresGroup = new Group();
    private Tile[][] board = new Tile[widthSpots][heightSpots];
    public final static int tileSize = 80;
    public final static int widthSpots = 8;
    public final static int heightSpots = 8;
    private boolean endOfGame = false;
    private String winner = null;
    private int newX, newY;
    private double mouseX, mouseY;
    private int oldX, oldY;
    private boolean enemyTurn;
    private Label lblStatus = new Label("White turn to play");
    private Button resetButton = new Button("Start new game");



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Scene scene = new Scene(createContent());
            primaryStage.setTitle("Checkers");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private Parent createContent() {
        Pane root = new Pane();

        root.setPrefSize(tileSize * widthSpots, tileSize * heightSpots);
        resetButton.setPrefSize(100,10);
        resetButton.setTranslateX(270);
        lblStatus.setTranslateX(270);
        resetButton.setOnAction(startNewGame());
        resetButton.setVisible(true);
        lblStatus.setPrefSize(200,10);
        BorderPane pane = new BorderPane(root);
        pane.setTop(resetButton);
        pane.setBottom(lblStatus);

        for(int i = 0; i < widthSpots; i++) {
            for(int j = 0; j < heightSpots; j++) {

                Tile tile = new Tile((i + j) % 2 == 0, i, j);
                board[i][j] = tile;
                tilesGroup.getChildren().add(tile);
                Figure figure = null;

                if (j <= 2 && (i + j) % 2 != 0) {
                    figure = makeFigures(FigureType.BLACK, i, j);
                }
                if (j >= 5 && (i + j) % 2 != 0) {
                    figure = makeFigures(FigureType.WHITE, i, j);
                }
                if(figure != null && figure.getType() == FigureType.BLACK) {
                    tile.setFigure(figure);
                    blackFiguresGroup.getChildren().add(figure);
                } else if(figure != null) {
                    tile.setFigure(figure);
                    whiteFiguresGroup.getChildren().add(figure);
                }
            }
        }
        root.getChildren().addAll(tilesGroup, whiteFiguresGroup, blackFiguresGroup);
        return pane;
    }

    private void playerTurn(Figure figure, int newX, int newY, int oldX, int oldY) {
        if(figure != null) {
            while (!endOfGame && !enemyTurn) {

                MoveExecutor moveType = getMoveType(figure, newX, newY, oldX, oldY);

                switch (moveType.getMoveType()) {
                    case ABORT:
                        abortMove(figure, oldX, oldY);
                        break;
                    case NORMAL:
                        moveFigure(figure, newX, newY);
                        board[oldX][oldY].setFigure(null);
                        board[newX][newY].setFigure(figure);
                        break;
                    case KILL:
                        moveFigure(figure, newX, newY);
                        board[oldX][oldY].setFigure(null);
                        board[newX][newY].setFigure(figure);

                        Figure killedFigure = moveType.getFigure();
                        board[getIndex(killedFigure.getLayoutX())][getIndex(killedFigure.getLayoutY())].setFigure(null);
                        blackFiguresGroup.getChildren().remove(killedFigure);
                        break;
                }
                setEnemyTurn(true);
            }
        }

        if(blackFiguresGroup.getChildren().isEmpty()) {
            winner = "WHITE";
            lblStatus.setText(winner + " player won! The game is over");
            endOfGame = true;
        } else {
            enemyTurn();
        }
    }

    private MoveExecutor getMoveType(Figure figure, int newX, int newY, int oldX, int oldY) {
        if(board[newX][newY].hasFigure() || (newX + newY) % 2 == 0) {
            return new MoveExecutor(ABORT);
        }

        if(Math.abs(newX - oldX) == 1 && newY - oldY == figure.getType().getMoveDirection()) {
            return new MoveExecutor(NORMAL);
        } else if(Math.abs(newX - oldX) == 2 && newY - oldY == figure.getType().getMoveDirection() * 2) {

            int x1 = oldX + (newX - oldX) / 2;
            int y1 = oldY + (newY - oldY) / 2;
            Figure otherFigure = board[x1][y1].getFigure();

            if(board[x1][y1].hasFigure() && figure.getType() != otherFigure.getType()) {
                return new MoveExecutor(KILL, otherFigure);
            }
        }
        return new MoveExecutor(ABORT);
    }

    private Figure makeFigures(FigureType type, int x, int y) {
        Figure figure = new Figure(type, x, y);

        if(figure.getType() == FigureType.WHITE) {
            figure.setOnMousePressed(event -> {
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                oldX = getIndex(figure.getLayoutX());
                oldY = getIndex(figure.getLayoutY());
            });

            figure.setOnMouseDragged(event -> {
                figure.setTranslateX(event.getSceneX() - mouseX + 20);
                figure.setTranslateY(event.getSceneY() - mouseY + 20);
            });

            figure.setOnMouseReleased(event -> {
                newX = getIndex(event.getSceneX());
                newY = getIndex(event.getSceneY());
                playerTurn(figure, newX, newY, oldX, oldY);
            });
        }
        return figure;
    }

    private void enemyTurn() {
        while(enemyTurn && !endOfGame) {

            boolean moveResult = false;
            Random random = new Random();
            int oldX = 0;
            int oldY = 0;
            int newX = 0;
            int newY = 0;
            Figure enemyFigure = null;

            while(moveResult == false) {
                Figure randomEnemyFigure =(Figure)blackFiguresGroup.getChildren().get(random.nextInt(blackFiguresGroup.getChildren().size()));
                int x = getIndex(randomEnemyFigure.getLayoutX());
                int y = getIndex(randomEnemyFigure.getLayoutY());

                int x1 = random.nextInt(8);
                int y1 = random.nextInt(8);

                MoveExecutor randomMoveType = getMoveType(randomEnemyFigure, x1, y1, x, y);

                if(randomMoveType.getMoveType() != ABORT) {
                    oldX = x;
                    oldY = y;
                    newX = x1;
                    newY = y1;
                    enemyFigure = randomEnemyFigure;
                    moveResult = true;
                }
            }

            MoveExecutor moveType = getMoveType(enemyFigure, newX, newY, oldX, oldY);

            switch (moveType.getMoveType()) {

                case NORMAL:
                    moveFigure(enemyFigure, newX, newY);
                    board[oldX][oldY].setFigure(null);
                    board[newX][newY].setFigure(enemyFigure);
                    break;
                case KILL:
                    moveFigure(enemyFigure, newX, newY);
                    board[oldX][oldY].setFigure(null);
                    board[newX][newY].setFigure(enemyFigure);

                    Figure killedFigure = moveType.getFigure();
                    board[getIndex(killedFigure.getLayoutX())][getIndex(killedFigure.getLayoutY())].setFigure(null);
                    whiteFiguresGroup.getChildren().remove(killedFigure);
                    break;
            }

            setEnemyTurn(false);
        }

        if(whiteFiguresGroup.getChildren().isEmpty()) {
            winner = "BLACK";
            lblStatus.setText(winner + " player won! The game is over");
            endOfGame = true;
        }
    }

    private int getIndex(double pixel) {
        return (int) pixel / tileSize;
    }

    private void moveFigure(Figure figure, int x, int y) {
        figure.setLayoutX(x * tileSize);
        figure.setLayoutY(y * tileSize);
        figure.setTranslateX((tileSize - tileSize * 0.37 * 2) / 2);
        figure.setTranslateY((tileSize - tileSize * 0.27 * 2) / 2);
    }

    private void abortMove(Figure figure, int x, int y) {
        figure.setLayoutX(x * tileSize);
        figure.setLayoutY(y * tileSize);
        figure.setTranslateX((tileSize - tileSize * 0.37 * 2) / 2);
        figure.setTranslateY((tileSize - tileSize * 0.27 * 2) / 2);
    }

    private EventHandler<ActionEvent> startNewGame() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                lblStatus.setVisible(false);
                whiteFiguresGroup.getChildren().clear();
                blackFiguresGroup.getChildren().clear();

                for(int i = 0; i < widthSpots; i++) {
                    for(int j = 0; j < heightSpots; j++) {
                        board[i][j].setFigure(null);
                    }
                }

                for(int i = 0; i < widthSpots; i++) {
                    for(int j = 0; j < heightSpots; j++) {

                        Figure figure = null;

                        if (j <= 2 && (i + j) % 2 != 0) {
                            figure = makeFigures(FigureType.BLACK, i, j);
                        }
                        if (j >= 5 && (i + j) % 2 != 0) {
                            figure = makeFigures(FigureType.WHITE, i, j);
                        }
                        if(figure != null &&  figure.getType() == FigureType.BLACK) {
                            board[i][j].setFigure(figure);
                            blackFiguresGroup.getChildren().add(figure);
                        } else if(figure != null) {
                            board[i][j].setFigure(figure);
                            whiteFiguresGroup.getChildren().add(figure);
                        }
                    }
                }
                lblStatus.setVisible(true);
                lblStatus.setText("White player turn");
                endOfGame = false;
            }
        };
    }

    private void setEnemyTurn(boolean enemyTurn) {
        this.enemyTurn = enemyTurn;
    }
}
