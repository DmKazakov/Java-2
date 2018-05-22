package ru.spbau.mit.kazakov.PairMatch;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.apache.commons.lang.StringUtils;


/**
 * Implementation of matching pairs game.
 */
public class PairMatch extends Application {
    private static int boardSize;
    private Scene boardScene;

    /**
     * @see Application#start(Stage)
     */
    @Override
    public void start(Stage stage) {
        initializeBoardScene();
        stage.setScene(boardScene);
        stage.setTitle("Nevermind");
        stage.show();
        stage.requestFocus();
    }

    /**
     * Creates and initializes board scene.
     */
    private void initializeBoardScene() {
        Text gameResult = new Text();
        gameResult.setTextAlignment(TextAlignment.CENTER);
        gameResult.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        gameResult.setFill(Color.WHITE);
        Board board = new Board(boardSize, gameResult);

        ColumnConstraints[] cols = new ColumnConstraints[boardSize];
        for (int i = 0; i < cols.length; i++) {
            cols[i] = new ColumnConstraints();
            cols[i].setPercentWidth(50);
        }

        RowConstraints[] rows = new RowConstraints[boardSize];
        for (int i = 0; i < cols.length; i++) {
            rows[i] = new RowConstraints();
            rows[i].setPercentHeight(100);
            rows[i].setFillHeight(true);
        }

        GridPane grid = new GridPane();
        grid.getColumnConstraints().addAll(cols);
        grid.getRowConstraints().addAll(rows);
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                grid.add(board.getButton(i, j), j, i);
            }
        }

        BorderPane border = new BorderPane();
        BorderPane.setAlignment(gameResult, Pos.CENTER);
        border.setStyle("-fx-background-color: #ffad33;");
        border.setCenter(grid);
        border.setTop(gameResult);
        boardScene = new Scene(border, 325, 300);
    }

    /**
     * Runs application.
     */
    public static void main(String[] args) {
        if (args.length == 0 || !StringUtils.isNumeric(args[0])) {
            System.out.println("Board size isn't specified.");
            return;
        }
        int size = Integer.parseInt(args[0]);
        if (size == 0 || size % 2 == 1) {
            System.out.println("Boards size must be positive even number.");
            return;
        } else if (size > 16) {
            System.out.println("Boards size is too big.");
            return;
        }
        boardSize = size;
        Application.launch(args);
    }

}
