package minimumcost_prj.ProjectCode;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Driver {
    public static Vertex[] vertexArray;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public static void main(String[] args) {
        System.out.println("Hello");
    }
}