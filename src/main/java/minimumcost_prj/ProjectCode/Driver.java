package minimumcost_prj.ProjectCode;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import minimumcost_prj.File.ReadFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

public class Driver {
    public static Vertex[] vertexArray;
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
    private VBox vboxHead;

    @FXML
    private HBox hboxHead;


    static Driver controller;

    static int[][] dp;
    static ArrayList<String>[][] pathsArray;
    static ArrayList<Integer>[][] costsArray;
    @FXML
    public TableView<ObservableList<String>> dpTable;


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

    // Helper function for alert
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
        pathsArray = new ArrayList[cityNum][cityNum];
        costsArray = new ArrayList[cityNum][cityNum];

        for (int i = 0; i < cityNum; i++) {
            for (int j = 0; j < cityNum; j++) {
                pathsArray[i][j] = new ArrayList<>();
                costsArray[i][j] = new ArrayList<>();
            }
        }

        for (int i = 0; i < cityNum; i++) {
            for (int j = 0; j < cityNum; j++) {
                if (i == j) {
                    dp[i][j] = 0;
                } else if (vertexArray[i].stage == vertexArray[j].stage) {
                    dp[i][j] = -1;
                } else {
                    dp[i][j] = Integer.MAX_VALUE;
                }
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
            }
        }
        System.out.println("🔍 DP Matrix (Petrol + Hotel Costs):");
        for (int i = 0; i < cityNum; i++) {
            for (int j = 0; j < cityNum; j++) {
                System.out.print((dp[i][j] == Integer.MAX_VALUE ? "∞" : dp[i][j]) + "\t");
            }
            System.out.println();
        }
        for (int i = 0; i < cityNum; i++) {
            for (int j = i + 1; j < cityNum; j++) {
                for (Edge edge : vertexArray[j].adjacent) {
                    int k = edge.destination.indexVertex;

                    if (vertexArray[i].stage == vertexArray[j].stage || dp[i][j] == -1) continue;
                    if (dp[i][j] != Integer.MAX_VALUE && dp[j][k] != Integer.MAX_VALUE) {
                        dp[i][k] = Math.min(dp[j][k] + dp[i][j], dp[i][k]);
                        pathsArray[i][k].add(vertexArray[j].name + " ");
                        costsArray[i][k].add(dp[j][k] + dp[i][j]);
                    }
                }
            }
        }
        System.out.println("🔍 DP Matrix (Petrol + Hotel Costs):");
        for (int i = 0; i < cityNum; i++) {
            for (int j = 0; j < cityNum; j++) {
                System.out.print((dp[i][j] == Integer.MAX_VALUE ? "∞" : dp[i][j]) + "\t");
            }
            System.out.println();
        }
        System.out.println("\n📌 Paths Array:");
        for (int i = 0; i < cityNum; i++) {
            for (int j = 0; j < cityNum; j++) {
                System.out.print("From " + vertexArray[i].name + " to " + vertexArray[j].name + ": ");
                System.out.println(pathsArray[i][j]);
            }
        }

        System.out.println("\n💰 Costs Array:");
        for (int i = 0; i < cityNum; i++) {
            for (int j = 0; j < cityNum; j++) {
                System.out.print("From " + vertexArray[i].name + " to " + vertexArray[j].name + ": ");
                System.out.println(costsArray[i][j]);
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

    public void displayDPTableInGrid() throws IOException {
        showNextStage();

        int cityNum = vertexArray.length;


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
                int value = dp[row][col];
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