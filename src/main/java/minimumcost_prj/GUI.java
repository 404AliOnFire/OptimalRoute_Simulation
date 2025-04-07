package minimumcost_prj;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("hello-view.fxml"));
        Vertex[] v = ReadFile.loadGraph("C:\\Users\\ALYAMEN.PS\\IdeaProjects\\MinimumCost_Prj\\src\\main\\java\\minimumcost_prj\\input.txt");
        initializeDP(v);
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
    public static void initializeDP(Vertex[] vertexArray){
        int cityNum = vertexArray.length;
        int dp [][] = new int[cityNum][cityNum];
        for (int i = 0; i < cityNum; i++) {
            for( int j = 0; j < cityNum; j++){
                if(j == i) dp[i][j] = 0;
                else dp[i][j] = Integer.MAX_VALUE;
            }
        }
        for(int i = 0; i < cityNum; i++){
            int lengthEdges = vertexArray[i].adjacent.length;
            for(int j = 0; j < lengthEdges; j++){
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
                System.out.print((dp[i][j] == Integer.MAX_VALUE ? "∞" : dp[i][j]) + "\t");  // Optional: show 0s as ∞
            }
            System.out.println();
        }

    }
    public static void main(String[] args) {
        launch();
    }
}