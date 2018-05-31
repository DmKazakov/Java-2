package ru.spbau.mit.kazakov.GUI;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.SepiaTone;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.UnaryOperator;


/**
 * GUI for FTP-client.
 */
public class ClientGUI extends Application {
    private static final String APP_TITLE = "FTP";
    private static final int NUMBER_OF_FILES_IN_ROW = 3;

    private Stage primaryStage;
    private Client client;
    private Scene fileBrowserScene;
    private Scene connectionScene;

    private HBox folderStack;
    private GridPane folderContent;
    private ScrollPane scrollContent;
    private BorderPane border;

    /**
     * @see Application#start(Stage)
     */
    @Override
    public void start(@NotNull Stage stage) throws IOException {
        primaryStage = stage;
        initializeConnectionScene();
        initializeFileBrowserScene();
        primaryStage.setScene(connectionScene);
        primaryStage.setTitle(APP_TITLE);
        primaryStage.show();
        primaryStage.requestFocus();
    }

    /**
     * Creates and initializes connection scene.
     */
    private void initializeConnectionScene() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text title = new Text("FTP-client");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(title, 0, 0, 2, 1);

        Label host = new Label("Host:");
        grid.add(host, 0, 1);
        TextField hostField = new TextField();
        grid.add(hostField, 1, 1);

        Label port = new Label("Port:");
        grid.add(port, 0, 2);
        TextField portField = new TextField();
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String input = change.getText();
            if (input.matches("[0-9]*")) {
                return change;
            }
            return null;
        };
        portField.setTextFormatter(new TextFormatter<String>(integerFilter));
        grid.add(portField, 1, 2);

        Button connectButton = new Button("Connect");
        connectButton.setOnAction(e -> {
            try {
                folderStack.getChildren().clear();
                folderContent.getChildren().clear();
                Socket clientSocket = new Socket(hostField.getText(), Integer.parseInt(portField.getText()));
                client = new Client(clientSocket);
                primaryStage.setScene(fileBrowserScene);
            } catch (Exception exception) {
                showAlertDialog("Unable to connect to server.");
            }
        });
        HBox connectHBox = new HBox(10);
        connectHBox.setAlignment(Pos.BOTTOM_RIGHT);
        connectHBox.getChildren().add(connectButton);
        grid.add(connectHBox, 1, 4);

        connectionScene = new Scene(grid, 300, 275);
    }

    /**
     * Creates and initializes file browser scene.
     */
    private void initializeFileBrowserScene() {
        TextField gotoField = new TextField();
        gotoField.setPromptText("Path");
        Button gotoButton = new Button("Goto");
        gotoButton.setOnAction(new FolderButtonAction(gotoField));
        HBox gotoHBox = new HBox(5);
        gotoHBox.setAlignment(Pos.CENTER);
        gotoHBox.getChildren().addAll(gotoButton, gotoField);

        folderStack = new HBox();
        VBox navigationPanel = new VBox(5);
        navigationPanel.getChildren().addAll(gotoHBox, folderStack);

        folderContent = new GridPane();
        folderContent.setScaleShape(true);
        folderContent.setAlignment(Pos.CENTER);
        folderContent.setHgap(10);
        folderContent.setVgap(10);
        folderContent.setGridLinesVisible(true);
        folderContent.setPadding(new Insets(25, 25, 25, 25));

        ColumnConstraints[] cols = new ColumnConstraints[NUMBER_OF_FILES_IN_ROW];
        for (int i = 0; i < cols.length; i++) {
            cols[i] = new ColumnConstraints(100, 100, Double.MAX_VALUE,
                    Priority.ALWAYS, HPos.CENTER, true);
            cols[i].setHgrow(Priority.ALWAYS);
            cols[i].setFillWidth(true);
        }
        folderContent.getColumnConstraints().addAll(cols);

        scrollContent = new ScrollPane();
        scrollContent.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollContent.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollContent.setFitToWidth(true);
        scrollContent.setFitToHeight(true);
        scrollContent.setContent(folderContent);

        Button reconnectButton = new Button("Change server");
        reconnectButton.setOnAction(e -> primaryStage.setScene(connectionScene));
        HBox reconnectHBox = new HBox(10);
        reconnectHBox.setAlignment(Pos.BOTTOM_RIGHT);
        reconnectHBox.getChildren().add(reconnectButton);

        border = new BorderPane();
        border.setCenter(scrollContent);
        border.setTop(navigationPanel);
        border.setBottom(reconnectHBox);
        border.setPadding(new Insets(15, 20, 10, 10));
        fileBrowserScene = new Scene(border, 400, 275);
    }

    /**
     * Updates scene with content of specified folder.
     */
    private void updateFileBrowserScene(@NotNull Path path) throws ConnectionException {
        folderContent.getChildren().clear();
        folderContent.getRowConstraints().clear();
        folderStack.getChildren().clear();

        List<Button> parentFolders = getParentButtons(path);
        Collections.reverse(parentFolders);
        folderStack.getChildren().addAll(parentFolders);

        List<FileDescription> folderContent = client.list(path.toString());
        int numberOfRows = (folderContent.size() + NUMBER_OF_FILES_IN_ROW - 1) / NUMBER_OF_FILES_IN_ROW;
        if (numberOfRows == 0) {
            return;
        }

        Button[][] gridContent = new Button[numberOfRows][NUMBER_OF_FILES_IN_ROW];
        for (int i = 0; i < folderContent.size(); i++) {
            gridContent[i / NUMBER_OF_FILES_IN_ROW][i % NUMBER_OF_FILES_IN_ROW] = getButtonByFileDescription(folderContent.get(i));
        }

        RowConstraints[] rows = new RowConstraints[numberOfRows];
        for (int i = 0; i < rows.length; i++) {
            rows[i] = new RowConstraints(20, 20, Double.MAX_VALUE,
                    Priority.ALWAYS, VPos.CENTER, true);
            rows[i].setVgrow(Priority.ALWAYS);
            rows[i].setFillHeight(true);
        }

        this.folderContent.getRowConstraints().addAll(rows);
        for (int i = 0; i < gridContent.length; i++) {
            for (int j = 0; j < gridContent[0].length; j++) {
                if (gridContent[i][j] != null) {
                    this.folderContent.add(gridContent[i][j], j, i);
                }
            }
        }
    }

    /**
     * Extracts all parents and creates buttons for them.
     *
     * @return stack of crated buttons
     */
    @NotNull
    private List<Button> getParentButtons(@NotNull Path path) {
        List<Button> parentsButtons = new ArrayList<>();

        do {
            Button parentButton = getButtonByPath(path);
            parentsButtons.add(parentButton);
            path = path.getParent();
        } while (path != null);

        return parentsButtons;
    }

    /**
     * Creates button by specified path.
     */
    @NotNull
    private Button getButtonByPath(@NotNull Path path) {
        String normalizedPath;
        if (path.getParent() == null) {
            normalizedPath = path.toString();
        } else {
            normalizedPath = path.getFileName().normalize().toString();
        }
        Button pathButton = new Button(normalizedPath);
        pathButton.setEffect(new SepiaTone(0.5));
        pathButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        pathButton.setOnAction(new FolderButtonAction(path));
        return pathButton;
    }

    /**
     * Creates button by specified file description.
     */
    @NotNull
    private Button getButtonByFileDescription(@NotNull FileDescription fileDescription) {
        Path filePath = Paths.get(fileDescription.getPath());
        Button button = new Button(filePath.getFileName().toString());
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        if (fileDescription.isDirectory()) {
            button.setEffect(new SepiaTone(0.5));
            button.setOnAction(new FolderButtonAction(filePath));
        } else {
            button.setOnAction(new FileButtonAction(filePath));
        }

        return button;
    }

    /**
     * Folder button action when it is pressed.
     */
    private class FolderButtonAction implements EventHandler<ActionEvent> {
        private Path path = null;
        private TextField fieldPath = null;

        public FolderButtonAction(@NotNull Path path) {
            this.path = path;
        }

        public FolderButtonAction(@NotNull TextField path) {
            fieldPath = path;
        }

        @Override
        public void handle(ActionEvent actionEvent) {
            try {
                updateFileBrowserScene(getPath());
            } catch (ConnectionException e) {
                showAlertDialog("Unable to get folder content from server.");
            }
        }

        @NotNull
        private Path getPath() {
            if (path != null) {
                return path;
            } else {
                return Paths.get(fieldPath.getText());
            }
        }
    }

    /**
     * File button action when it is pressed.
     */
    private class FileButtonAction implements EventHandler<ActionEvent> {
        private Path path;

        public FileButtonAction(@NotNull Path path) {
            this.path = path;
        }

        @Override
        public void handle(ActionEvent actionEvent) {
            TextInputDialog pathDialog = new TextInputDialog();
            pathDialog.setHeaderText(null);
            pathDialog.setContentText("Please enter destination file:");
            Optional<String> destinationFolder = pathDialog.showAndWait();

            if (!destinationFolder.isPresent()) {
                return;
            }
            try {
                client.get(path.toString(), destinationFolder.get());
            } catch (ConnectionException e) {
                showAlertDialog("Unable to download file from server.");
            } catch (IOException e) {
                showAlertDialog("Unable to save file.");
            }
        }
    }

    /**
     * Shows alert dialog with specified message.
     */
    private void showAlertDialog(@NotNull String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Runs application.
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
