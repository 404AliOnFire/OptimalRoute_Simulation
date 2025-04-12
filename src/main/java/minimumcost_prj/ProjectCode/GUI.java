package minimumcost_prj.ProjectCode;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import minimumcost_prj.File.ReadFile;

import java.io.IOException;
import java.util.ArrayList;

public class GUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/StartUI.fxml"));
        Vertex[] v = ReadFile.loadGraph("C:\\Users\\ALYAMEN.PS\\IdeaProjects\\MinimumCost_Prj\\src\\main\\java\\minimumcost_prj\\File\\input.txt");
        initializeDP(v);
        Scene scene = new Scene(fxmlLoader.load(), 944, 642);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void initializeDP(Vertex[] vertexArray) {
        int cityNum = vertexArray.length;
        int dp[][] = new int[cityNum][cityNum];
        ArrayList<String>[][] pathsArray = new ArrayList[cityNum][cityNum];
        ArrayList<Integer>[][] costsArray = new ArrayList[cityNum][cityNum];

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
