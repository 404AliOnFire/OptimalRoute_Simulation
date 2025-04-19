package minimumcost_prj.ProjectCode;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import minimumcost_prj.File.ReadFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class Driver {
    public static Vertex[] vertexArray;

    public static int[][] arrayForFX;
    @FXML
    private Label welcomeText;

    @FXML
    private TextArea textAreaKeyboard;

    @FXML
    private AnchorPane startPane;

    @FXML
    private AnchorPane keyboardPane;

    @FXML
    private Text endVertexText;

    @FXML
    private Text startVertexText;

    @FXML
    private Text numVertexText;

    @FXML
    private Text endVertexText1;

    @FXML
    private Text startVertexText1;

    @FXML
    private Text numVertexText1;

    @FXML
    private VBox vboxHead;

    @FXML
    private HBox hboxHead;

    @FXML
    private ComboBox<Vertex> startComboBox;

    @FXML
    private ComboBox<Vertex> endComboBox;

    @FXML
    public TableView<ObservableList<String>> dpTable;

    @FXML
    public TableView<ObservableList<String>> dpTable1;

    @FXML
    public VBox vboxPaths;

    static Driver controller;
    static int[][] dp;
    static String[][] pathsArray;
    static String[][] costsArray;
    static PathResultCell[][] pathResultCells;


    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public static void main(String[] args) {
        System.out.println("Hello");
    }

    @FXML
    public void initialize() {

    }

    public void fileAction(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Graph File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        File selectedFile = fileChooser.showOpenDialog(((Node) mouseEvent.getSource()).getScene().getWindow());

        if (selectedFile != null) {
            try {
                String filePath = selectedFile.getAbsolutePath();

                vertexArray = ReadFile.loadGraph(filePath);

                System.out.println("✅ Graph loaded successfully from: " + filePath);
                System.out.println("Number of vertices: " + vertexArray.length);

            } catch (IOException e) {
                showAlert("File Error", "❌ Failed to load file:\n" + e.getMessage());
            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void initializeDP(Vertex[] vertexArray) {
        int cityNum = vertexArray.length;
        dp = new int[cityNum][cityNum];
        pathResultCells = new PathResultCell[cityNum][cityNum];

        for (int i = 0; i < cityNum; i++) {
            for (int j = 0; j < cityNum; j++) {
                if (i == j) {
                    dp[i][j] = 0;
                } else if (vertexArray[i].stage == vertexArray[j].stage) {
                    dp[i][j] = -1;
                } else {
                    dp[i][j] = Integer.MAX_VALUE;
                }

                pathResultCells[i][j] = new PathResultCell(40);
            }
        }
        for (int i = 0; i < cityNum; i++) {
            int lengthEdges = vertexArray[i].adjacent.length;
            for (int j = 0; j < lengthEdges; j++) {
                Edge adj = vertexArray[i].adjacent[j];
                int k = adj.destination.indexVertex;
                int petrol = adj.petrolCost;
                int hotel = adj.hotelCost;

                dp[i][k] = petrol + hotel;
                pathResultCells[i][k].add(vertexArray[i].name + " -> " + vertexArray[k].name, dp[i][k]);
            }
        }

        arrayForFX = new int[cityNum][cityNum];
        for (int i = 0; i < cityNum; i++) {
            arrayForFX[i] = dp[i].clone();
        }

        // O(n² * E)
        for (int i = 0; i < cityNum; i++) {
            for (int j = i + 1; j < cityNum; j++) {
                for (Edge edge : vertexArray[j].adjacent) {
                    int k = edge.destination.indexVertex;

                    if (vertexArray[i].stage == vertexArray[j].stage || dp[i][j] == -1) continue;
                    if (dp[i][j] != Integer.MAX_VALUE && dp[j][k] != Integer.MAX_VALUE) {
                        dp[i][k] = Math.min(dp[j][k] + dp[i][j], dp[i][k]); // relationship

                        pathResultCells[i][k].add(vertexArray[k].name, dp[j][k] + dp[i][j], pathResultCells[i][j]);
                    }
                }
            }
        }
        System.out.println("---- Testing pathResultCells ----");

        for (int i = 0; i < cityNum; i++) {
            for (int j = 0; j < cityNum; j++) {
                System.out.println("From vertex " + i + " to vertex " + j + ":");
                PathResultCell cell = pathResultCells[i][j];
                for (int k = 0; k < cell.counter; k++) {
                    System.out.println("   Path: " + cell.paths[k].getPath() + " | Cost: " + cell.paths[k].getCost());
                }
                System.out.println("--------------------------");
            }
        }
    }

    public void keyboardAction(MouseEvent mouseEvent) {
        startPane.setVisible(false);
        keyboardPane.setVisible(true);
    }

    public void loadKeyboard(ActionEvent actionEvent) {
        // Get the text from the text area
        String input = textAreaKeyboard.getText();
        if (input == null || input.trim().isEmpty()) {
            showAlert("Input Error", "No graph data was entered. Please paste your graph data.");
            return;
        }
        try {
            File tempFile = File.createTempFile("graphData", ".txt");
            FileWriter writer = new FileWriter(tempFile);
            writer.write(input);
            writer.close();

            vertexArray = ReadFile.loadGraph(tempFile.getAbsolutePath());
            initializeDP(vertexArray);

            System.out.println("✅ Graph successfully loaded from keyboard input (via temp file): " + tempFile.getAbsolutePath());
            displayDPTableInGrid();
            tempFile.deleteOnExit();
        } catch (IOException e) {
            showAlert("Input Error", e.getMessage());
        }
    }

    public void deleteKeyboard(ActionEvent actionEvent) {
    }

    public static void showNextStage() throws IOException {
        GUI.mainStage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource("/FXML/dpTable.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1289, 727);
        controller = fxmlLoader.getController();
        Stage dpStage = new Stage();
        dpStage.setTitle("Optimal Route");
        dpStage.setScene(scene);
        dpStage.show();
    }


    public PathResult[] getPathsWithCosts(int startIndex, int endIndex) {
        PathResultCell cell = pathResultCells[startIndex][endIndex];
        PathResult[] results = Arrays.copyOf(cell.getPaths(), cell.counter);
        Arrays.sort(results, Comparator.comparingInt(PathResult::getCost));

        return results;
    }

    public void displayDPTableInGrid() throws IOException {
        showNextStage();

        int cityNum = vertexArray.length;
        controller.numVertexText.setText(cityNum + "");
        controller.startVertexText.setText(ReadFile.startVertex);
        controller.endVertexText.setText(ReadFile.endVertex);

        controller.numVertexText1.setText(cityNum + "");
        controller.startVertexText1.setText(ReadFile.startVertex);
        controller.endVertexText1.setText(ReadFile.endVertex);

        for (Vertex v : vertexArray) {
            HBox hBox = new HBox(100);
            Text text1 = new Text(v.name);
            Text text2 = new Text(v.stage + "");
            text1.setFont(new Font("Courier new", 21));
            text2.setFont(new Font("Courier new", 21));
            hBox.getChildren().addAll(text1, text2);
            hBox.setAlignment(Pos.CENTER);
            controller.vboxHead.getChildren().addAll(hBox);
        }


        dpInitTableFx(cityNum);
        dpAfterTableFX(cityNum);
        fillStartAndEnd();
    }

    public void printPathsToFX(int startIndex, int endIndex) {
        PathResult[] results = getPathsWithCosts(startIndex, endIndex);
        controller.vboxPaths.getChildren().clear();

        for (PathResult pr : results) {
            HBox hboxPath = new HBox(50);
            Text txt = new Text(pr.toString());
            hboxPath.getChildren().add(txt);
            controller.vboxPaths.getChildren().add(hboxPath);
        }
    }

    public void fillStartAndEnd() {
        controller.startComboBox.valueProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                handleStartSelection();

                controller.vboxPaths.getChildren().clear();
            }
        });

        controller.endComboBox.valueProperty().addListener((obs, oldV, newV) -> {
            Vertex start = controller.startComboBox.getValue();
            Vertex end = newV;
            if (start != null && end != null) {
                printPathsToFX(start.indexVertex, end.indexVertex);

            }
        });
        controller.startComboBox.getItems().addAll(vertexArray);
        controller.startComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Vertex vertex) {
                return vertex == null ? "" : vertex.name;
            }

            @Override
            public Vertex fromString(String string) {
                return null;
            }
        });
        controller.endComboBox.setConverter(controller.startComboBox.getConverter());
    }

    @FXML
    private void handleStartSelection() {
        Vertex selectedStart = controller.startComboBox.getValue();
        if (selectedStart != null) {
            int selectedStage = selectedStart.stage;
            controller.endComboBox.getItems().clear();
            for (Vertex vertex : vertexArray) {
                if (vertex.stage > selectedStage) {
                    controller.endComboBox.getItems().add(vertex);
                }
            }
        }
    }

    private void dpAfterTableFX(int cityNum) {
        controller.dpTable1.getColumns().clear();

        TableColumn<ObservableList<String>, String> firstCol = new TableColumn<>("City");
        firstCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().get(0)));
        firstCol.setCellFactory(column -> new TableCell<ObservableList<String>, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-background-color: orange;");
                    setStyle("-fx-text-fill: red");
                }
            }
        });
        controller.dpTable1.getColumns().add(firstCol);


        for (int i = 0; i < cityNum; i++) {
            final int colIndex = i + 1;
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(vertexArray[i].name);
            col.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().get(colIndex)));
            controller.dpTable1.getColumns().add(col);
        }


        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        for (int row = 0; row < cityNum; row++) {
            ObservableList<String> rowData = FXCollections.observableArrayList();
            rowData.add(vertexArray[row].name);
            for (int col = 0; col < cityNum; col++) {
                int value = dp[row][col];
                rowData.add(value == Integer.MAX_VALUE ? "∞" : String.valueOf(value));
            }
            data.add(rowData);
        }

        controller.dpTable1.setItems(data);


        controller.dpTable1.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private static void dpInitTableFx(int cityNum) {
        controller.dpTable.getColumns().clear();

        TableColumn<ObservableList<String>, String> firstCol = new TableColumn<>("City");
        firstCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().get(0)));
        firstCol.setCellFactory(column -> new TableCell<ObservableList<String>, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-background-color: orange;");
                    setStyle("-fx-text-fill: red");
                }
            }
        });
        controller.dpTable.getColumns().add(firstCol);


        for (int i = 0; i < cityNum; i++) {
            final int colIndex = i + 1;
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(vertexArray[i].name);
            col.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().get(colIndex)));
            controller.dpTable.getColumns().add(col);
        }


        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        for (int row = 0; row < cityNum; row++) {
            ObservableList<String> rowData = FXCollections.observableArrayList();
            rowData.add(vertexArray[row].name);
            for (int col = 0; col < cityNum; col++) {
                int value = arrayForFX[row][col];
                rowData.add(value == Integer.MAX_VALUE ? "∞" : String.valueOf(value));
            }
            data.add(rowData);
        }

        controller.dpTable.setItems(data);

        controller.dpTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void backKeyboard(ActionEvent actionEvent) {
        startPane.setVisible(true);
        keyboardPane.setVisible(false);
    }
}