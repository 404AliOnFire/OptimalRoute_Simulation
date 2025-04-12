package minimumcost_prj.ProjectCode;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import minimumcost_prj.File.ReadFile;

import java.io.IOException;
import java.util.ArrayList;

public class GUI extends Application {

    public static Stage mainStage;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/StartUI.fxml"));
        Vertex[] v = ReadFile.loadGraph("C:\\Users\\ALYAMEN.PS\\IdeaProjects\\MinimumCost_Prj\\src\\main\\java\\minimumcost_prj\\File\\input.txt");
       // initializeDP(v);
        Scene scene = new Scene(fxmlLoader.load(), 944, 642);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        mainStage = stage;
    }

    public static String get2DArrayPrint(int[][] matrix) {
        String output = new String();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                output = output + (matrix[i][j] + "\t");
            }
            output = output + "\n";
        }
        return output;
    }

    public static void main(String[] args) {
        launch();
    }
}
