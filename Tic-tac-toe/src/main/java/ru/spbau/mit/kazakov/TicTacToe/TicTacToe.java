package ru.spbau.mit.kazakov.TicTacToe;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class TicTacToe extends Application{
    @Override
    public void start(Stage primaryStage) {
        Button button1 = new Button("Click me");
        button1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button1.setOnAction(value -> Platform.exit());
        Button button2 = new Button("Click me");
        button2.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button2.setOnAction(value -> Platform.exit());
        Button button3 = new Button("Click me");
        button3.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button3.setOnAction(value -> Platform.exit());
        Button button4 = new Button("Click me");
        button4.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button4.setOnAction(value -> Platform.exit());
        Button button5 = new Button("Click me");
        button5.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button5.setOnAction(value -> Platform.exit());
        Button button6 = new Button("Click me");
        button6.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button6.setOnAction(value -> Platform.exit());
        Button button7 = new Button("Click me");
        button7.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button7.setOnAction(value -> Platform.exit());
        Button button8 = new Button("Click me");
        button8.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button8.setOnAction(value -> Platform.exit());
        Button button9 = new Button("Click me");
        button9.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button9.setOnAction(value -> Platform.exit());


        GridPane pane = new GridPane();
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(50);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(50);
        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPercentWidth(50);

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(100);
        row1.setFillHeight(true);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(100);
        row2.setFillHeight(true);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(100);
        row3.setFillHeight(true);
        pane.getColumnConstraints().addAll(column1, column2, column3);
        pane.getRowConstraints().addAll(row1, row2, row3);

        pane.add(button1,0,0);
        pane.add(button2,1,0);
        pane.add(button3,2,0);
        pane.add(button4,0,1);
        pane.add(button5,1,1);
        pane.add(button6,2,1);
        pane.add(button7,0,2);
        pane.add(button8,1,2);
        pane.add(button9,2,2);
        Scene scene = new Scene(pane, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
