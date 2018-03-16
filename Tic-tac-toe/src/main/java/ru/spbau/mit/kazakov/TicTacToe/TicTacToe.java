package ru.spbau.mit.kazakov.TicTacToe;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


/**
 * Tic Tac Toe application with single and multi player.
 */
public class TicTacToe extends Application {
    private static final int BOARD_SIZE = 3;
    private Stage primaryStage;
    private Scene mainMenuScene;
    private Scene boardScene;
    private Button[][] cells;
    private Text gameResult;
    private Scene statsScene;
    private final ObservableList<String> stats = FXCollections.observableArrayList();
    private Board board;

    /**
     * @see Application#start(Stage)
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initializeMainMenuScene();
        initializeBoardScene();
        initializeStatsScene();
        primaryStage.setScene(mainMenuScene);
        primaryStage.setTitle("Nevermind");
        primaryStage.show();
        primaryStage.requestFocus();
    }

    /**
     * Creates and initializes main menu scene.
     */
    private void initializeMainMenuScene() {
        Text sceneTitle = new Text("Welcome");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        sceneTitle.setFill(Color.WHITE);

        HBox singlePlayerMenu = new HBox();
        singlePlayerMenu.setPadding(new Insets(0, 0, 0, 0));
        singlePlayerMenu.setSpacing(0);

        ComboBox<String> AILevelComboBox = new ComboBox<>();
        AILevelComboBox.setFocusTraversable(false);
        AILevelComboBox.setMaxWidth(Double.MAX_VALUE);
        AILevelComboBox.getItems().addAll("Medium", "Easy");
        AILevelComboBox.setValue("Medium");

        Button singlePlayerButton = new Button("SinglePlayer");
        singlePlayerButton.setFocusTraversable(false);
        singlePlayerButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        singlePlayerButton.setOnAction(value -> {
            switch (AILevelComboBox.getValue()) {
                case "Medium":
                    board = new AIBoard(BOARD_SIZE, AILevel.MEDIUM);
                    break;
                case "Easy":
                    board = new AIBoard(BOARD_SIZE, AILevel.EASY);
                    break;
            }
            gameResult.setText("");
            updateBoardScene(board.reset());
            this.primaryStage.setScene(boardScene);
        });

        HBox.setHgrow(AILevelComboBox, Priority.ALWAYS);
        HBox.setHgrow(singlePlayerButton, Priority.ALWAYS);
        singlePlayerMenu.getChildren().addAll(singlePlayerButton, AILevelComboBox);

        Button multiPlayerButton = new Button("MultiPlayer");
        multiPlayerButton.setFocusTraversable(false);
        multiPlayerButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        multiPlayerButton.setOnAction(value -> {
            board = new HotSeatBoard(BOARD_SIZE);
            gameResult.setText("");
            updateBoardScene(board.reset());
            primaryStage.setScene(boardScene);
        });

        Button statsButton = new Button("Stats");
        statsButton.setFocusTraversable(false);
        statsButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        statsButton.setOnAction(value -> primaryStage.setScene(statsScene));

        Button exitButton = new Button("Exit");
        exitButton.setFocusTraversable(false);
        exitButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        exitButton.setOnAction(value -> Platform.exit());

        VBox menu = new VBox();
        menu.setPadding(new Insets(25, 25, 25, 25));
        menu.setSpacing(10);
        menu.setAlignment(Pos.CENTER);
        menu.setStyle("-fx-background-color: #ffad33;");
        menu.getChildren().addAll(sceneTitle, singlePlayerMenu, multiPlayerButton, statsButton, exitButton);
        mainMenuScene = new Scene(menu, 300, 275);
    }

    /**
     * Creates and initializes board scene.
     */
    private void initializeBoardScene() {
        Button mainMenuButton = new Button("Main menu");
        mainMenuButton.setFocusTraversable(false);
        mainMenuButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        mainMenuButton.setOnAction(value -> primaryStage.setScene(mainMenuScene));

        Button restartButton = new Button("Restart");
        restartButton.setFocusTraversable(false);
        restartButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        restartButton.setOnAction(value -> {
            gameResult.setText("");
            updateBoardScene(board.reset());
        });

        Text gameResult = new Text();
        gameResult.setTextAlignment(TextAlignment.CENTER);
        gameResult.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        gameResult.setFill(Color.WHITE);
        this.gameResult = gameResult;

        HBox navigationBar = new HBox();
        navigationBar.setPadding(new Insets(0, 0, 0, 0));
        navigationBar.setSpacing(0);
        navigationBar.setAlignment(Pos.CENTER);
        navigationBar.setStyle("-fx-background-color: #ffad33;");
        navigationBar.getChildren().addAll(mainMenuButton, restartButton);

        Button[][] cells = new Button[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                int row = i;
                int col = j;
                cells[i][j] = new Button(" ");
                cells[i][j].setStyle("-fx-text-fill: black; -fx-font-size: 35;");
                cells[i][j].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                cells[i][j].setFocusTraversable(false);
                cells[i][j].setOnAction(value -> move(row, col));
            }
        }
        this.cells = cells;

        ColumnConstraints[] cols = new ColumnConstraints[BOARD_SIZE];
        for (int i = 0; i < cols.length; i++) {
            cols[i] = new ColumnConstraints();
            cols[i].setPercentWidth(50);
        }

        RowConstraints[] rows = new RowConstraints[BOARD_SIZE];
        for (int i = 0; i < cols.length; i++) {
            rows[i] = new RowConstraints();
            rows[i].setPercentHeight(100);
            rows[i].setFillHeight(true);
        }

        GridPane grid = new GridPane();
        grid.getColumnConstraints().addAll(cols);
        grid.getRowConstraints().addAll(rows);
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                grid.add(cells[i][j], j, i);
            }
        }

        BorderPane border = new BorderPane();
        BorderPane.setAlignment(gameResult, Pos.CENTER);
        border.setStyle("-fx-background-color: #ffad33;");
        border.setBottom(navigationBar);
        border.setCenter(grid);
        border.setTop(gameResult);
        boardScene = new Scene(border, 300, 275);
    }

    /**
     * Creates and initializes stats scene.
     */
    private void initializeStatsScene() {
        VBox statsPane = new VBox();
        statsPane.setAlignment(Pos.CENTER);
        statsPane.setStyle("-fx-background-color: #ffad33;");
        statsPane.setPadding(new Insets(10, 25, 10, 25));
        statsPane.setSpacing(10);

        Text sceneTitle = new Text("Stats");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        sceneTitle.setFill(Color.WHITE);

        ListView<String> listView = new ListView<>(stats);
        listView.setFocusTraversable(false);

        Button mainMenuButton = new Button("Main menu");
        mainMenuButton.setFocusTraversable(false);
        mainMenuButton.setOnAction(value -> primaryStage.setScene(mainMenuScene));

        statsPane.getChildren().addAll(sceneTitle, listView, mainMenuButton);
        statsScene = new Scene(statsPane, 300, 275);
    }

    /**
     * Handles player's turn.
     *
     * @param row witch player've chose
     * @param col witch player've chose
     */
    private void move(int row, int col) {
        updateBoardScene(board.move(row, col));
        switch (board.getState()) {
            case O_WINS:
                gameResult.setText("O WINS!");
                stats.add("O's victory");
                break;
            case X_WINS:
                gameResult.setText("X WINS!");
                stats.add("X's victory");
                break;
            case DRAW:
                gameResult.setText("DRAW!");
                stats.add("Draw");
                break;
        }
        if (!board.getState().equals(State.RUNNING)) {
            disableBoard();
        }
    }

    /**
     * Prevents all new turns.
     */
    private void disableBoard() {
        for (Button[] cell : cells) {
            for (int j = 0; j < cells.length; j++) {
                cell[j].setDisable(true);
            }
        }
    }

    /**
     * Reflects specified state of the board for player.
     *
     * @param boardState specified state of the board
     */
    private void updateBoardScene(String[][] boardState) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                cells[i][j].setDisable(!boardState[i][j].equals(" "));
                cells[i][j].setText(boardState[i][j]);
            }
        }
    }

    /**
     * Runs application.
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
