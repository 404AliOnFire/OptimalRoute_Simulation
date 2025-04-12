package minimumcost_prj.ProjectCode;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import minimumcost_prj.File.ReadFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

public class Driver {
    public static Vertex[] vertexArray;
    @FXML
    private Label welcomeText;

    public static Vertex [] vertices;

    @FXML
    private TextArea textAreaKeyboard;

    @FXML
    private AnchorPane startPane;

    @FXML
    private AnchorPane keyboardPane;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public static void main(String[] args) {
        System.out.println("Hello");
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

                Vertex[] vertices = ReadFile.loadGraph(filePath);

                this.vertices = vertices;

                System.out.println("✅ Graph loaded successfully from: " + filePath);
                System.out.println("Number of vertices: " + vertices.length);

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

            Vertex[] loadedVertices = ReadFile.loadGraph(tempFile.getAbsolutePath());
            this.vertices = loadedVertices;

            System.out.println("✅ Graph successfully loaded from keyboard input (via temp file): " + tempFile.getAbsolutePath());

            tempFile.deleteOnExit();
        } catch (IOException e) {
            showAlert("Input Error",   e.getMessage());
        }
    }

    public void deleteKeyboard(ActionEvent actionEvent) {
    }

    public void backKeyboard(ActionEvent actionEvent) {
        startPane.setVisible(true);
        keyboardPane.setVisible(false);
    }
}