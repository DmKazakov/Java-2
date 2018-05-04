package ru.spbau.mit.kazakov.PairMatch;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.util.Duration;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A game logic class.
 */
public class Board {
    private static final int FREEZE_TIME = 1000;
    private int boardSize;
    private int buttonsLeft;
    private int[][] values;
    private Button pressedButton = null;
    private Button[][] cells;
    private Text gameState;

    /**
     * Creates buttons and generates values.
     *
     * @param gameState indicates current game state
     */
    public Board(int boardSize, @NotNull Text gameState) {
        this.gameState = gameState;
        gameState.setText("");
        this.boardSize = boardSize;
        buttonsLeft = boardSize * boardSize;

        List<Integer> numbers = generateValues();
        values = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                values[i][j] = numbers.get(i * boardSize + j);
            }
        }

        cells = new Button[boardSize][boardSize];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                cells[i][j] = new Button("");
                cells[i][j].setStyle("-fx-text-fill: black; -fx-font-size: 35;");
                cells[i][j].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                cells[i][j].setFocusTraversable(false);
                cells[i][j].setOnAction(new ButtonAction(i, j));
            }
        }
    }

    /**
     * Updates game state and text for user.
     */
    private void updateGameState() {
        buttonsLeft -= 2;
        if (buttonsLeft == 0) {
            gameState.setText("YOU WIN!");
        }
    }

    /**
     * Generates random values for cells so that puzzle can be solved.
     */
    @NotNull
    private List<Integer> generateValues() {
        Random generator = new Random();
        List<Integer> numbers = new ArrayList<>();
        int numbersLeft = boardSize * boardSize;

        while (numbersLeft > 0) {
            int number = generator.nextInt(boardSize * boardSize / 2);
            numbers.add(number);
            numbers.add(number);
            numbersLeft -= 2;
        }

        Collections.shuffle(numbers);
        return numbers;
    }

    @NotNull
    public Button getButton(int row, int col) {
        return cells[row][col];
    }

    /**
     * Button action when it is pressed.
     */
    private class ButtonAction implements EventHandler<ActionEvent> {
        private int row;
        private int col;

        public ButtonAction(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void handle(ActionEvent actionEvent) {
            cells[row][col].setDisable(true);
            cells[row][col].setText(String.valueOf(values[row][col]));

            if (pressedButton == null) {
                pressedButton = cells[row][col];
            } else {
                if (pressedButton.getText().equals(cells[row][col].getText())) {
                    updateGameState();
                } else {
                    final Button button1 = pressedButton;
                    final Button button2 = cells[row][col];
                    Timeline timeline = new Timeline(new KeyFrame(
                            Duration.millis(FREEZE_TIME),
                            action -> {
                                button1.setText("");
                                button1.setDisable(false);
                                button2.setText("");
                                button2.setDisable(false);
                            }
                    ));
                    timeline.play();
                }
                pressedButton = null;
            }
        }
    }
}
