package minimumcost_prj.ProjectCode;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import minimumcost_prj.File.ReadFile;
import minimumcost_prj.ProjectCode.Edge;
import minimumcost_prj.ProjectCode.PathResultCell;
import minimumcost_prj.ProjectCode.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.Comparator;

import static minimumcost_prj.File.ReadFile.hotelCosts;

public class MinimumCostPathVisualizer extends AnchorPane {
    private static Vertex[] vertexArray;
    private static int[][] dp;
    private static PathResultCell[][] pathResultCells;
    private static boolean dataInitialized = false;

    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;
    private static final int NODE_RADIUS = 25;
    private static final int HORIZONTAL_PADDING = 150;
    private static final int VERTICAL_PADDING = 100;
    private static final int LEGEND_HEIGHT = 50;

    private Map<String, Circle> nodeCircles = new HashMap<>();
    private Map<String, Double[]> nodePositions = new HashMap<>();

    public MinimumCostPathVisualizer() {
        setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    }


    public static AnchorPane createVisualizerPane(Vertex[] userVertices) {
        visualizeFromInput(userVertices);
        MinimumCostPathVisualizer pane = new MinimumCostPathVisualizer();
        pane.drawGridBackground();
        pane.drawGraph();
        pane.addLegend();
        pane.addSignature();
        return pane;
    }

    private void addSignature() {
        Label signature = new Label("Ali Hassoneh");

        signature.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        signature.setTextFill(Color.BLACK);
        signature.setRotate(0);

        AnchorPane.setBottomAnchor(signature, 10.0);
        AnchorPane.setRightAnchor(signature, 10.0);
        this.getChildren().add(signature);
    }
    private static void visualizeFromInput(Vertex[] userVertices) {
        vertexArray = userVertices;
        int cityNum = vertexArray.length;
        dp = new int[cityNum][cityNum];
        pathResultCells = new PathResultCell[cityNum][cityNum];
        int capacity = cityNum * cityNum;

        for (int i = 0; i < cityNum; i++) {
            for (int j = 0; j < cityNum; j++) {
                dp[i][j] = (i == j ? 0 :
                        vertexArray[i].stage == vertexArray[j].stage ? -1 :
                                Integer.MAX_VALUE);
                pathResultCells[i][j] = new PathResultCell(capacity);
            }
        }
        for (int i = 0; i < cityNum; i++) {
            for (int e = 0; e < vertexArray[i].adjCount; e++) {
                Edge adj = vertexArray[i].adjacent[e];
                int k = adj.destination.indexVertex;
                int cost = adj.petrolCost;
                dp[i][k] = cost;
                pathResultCells[i][k].add(vertexArray[i].name + " -> " + vertexArray[k].name, cost);
            }
        }
        for (int i = 0; i < cityNum; i++) {
            for (int j = i + 1; j < cityNum; j++) {
                if (dp[i][j] < 0 || dp[i][j] == Integer.MAX_VALUE) continue;
                for (int e = 0; e < vertexArray[j].adjCount; e++) {
                    Edge edge = vertexArray[j].adjacent[e];
                    int k = edge.destination.indexVertex;
                    if (vertexArray[j].stage <= vertexArray[i].stage) continue;
                    if (dp[j][k] == Integer.MAX_VALUE) continue;
                    int newCost = dp[i][j] + dp[j][k];
                    int oldCost = dp[i][k];
                    if (newCost < oldCost) {
                        dp[i][k] = newCost;
                        pathResultCells[i][k] = new PathResultCell(capacity);
                        pathResultCells[i][k].add(vertexArray[k].name, dp[j][k], pathResultCells[i][j]);
                    } else if (newCost == oldCost) {
                        pathResultCells[i][k].add(vertexArray[k].name, dp[j][k], pathResultCells[i][j]);
                    }
                }
            }
        }
        dataInitialized = true;
    }

    private void drawGridBackground() {
        for (int x = 0; x < WINDOW_WIDTH; x += 50) {
            Line v = new Line(x, 0, x, WINDOW_HEIGHT - LEGEND_HEIGHT);
            v.setStroke(Color.rgb(220, 220, 220));
            v.setStrokeWidth(0.5);
            getChildren().add(v);
        }
        for (int y = 0; y < WINDOW_HEIGHT - LEGEND_HEIGHT; y += 50) {
            Line h = new Line(0, y, WINDOW_WIDTH, y);
            h.setStroke(Color.rgb(220, 220, 220));
            h.setStrokeWidth(0.5);
            getChildren().add(h);
        }
    }


    private void drawGraph() {
        nodeCircles.clear();
        nodePositions.clear();
        int maxStage = Arrays.stream(vertexArray).mapToInt(v -> v.stage).max().orElse(0);
        for (int s = 0; s <= maxStage; s++) drawStageColumn(s, maxStage);
        for (Vertex v : vertexArray) drawEdgesFromVertex(v);
    }

    private void drawStageColumn(int stage, int maxStage) {
        double x = HORIZONTAL_PADDING + stage * ((WINDOW_WIDTH - 2 * HORIZONTAL_PADDING) / (double) maxStage);
        ArrayList<Vertex> vs = new ArrayList<>();
        for (Vertex v : vertexArray) if (v.stage == stage) vs.add(v);
        String lbl = stage == 0 ? "Stage 0 (Start)" : stage == maxStage ? "Stage " + stage + " (End)" : "Stage " + stage;
        Text title = new Text(lbl);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        title.setX(x - title.getBoundsInLocal().getWidth() / 2);
        title.setY(VERTICAL_PADDING / 2);
        getChildren().add(title);
        int cnt = vs.size();
        double avail = WINDOW_HEIGHT - 2 * VERTICAL_PADDING - LEGEND_HEIGHT;
        double spacing = cnt > 1 ? avail / (cnt - 1) : avail / 2;
        for (int i = 0; i < cnt; i++) {
            Vertex v = vs.get(i);
            double y = VERTICAL_PADDING + i * spacing;
            drawVertex(v, x, y);
            nodePositions.put(v.name, new Double[]{x, y});
        }
    }

    private void drawVertex(Vertex v, double x, double y) {
        Circle c = new Circle(x, y, NODE_RADIUS, Color.WHITE);
        c.setStroke(Color.BLACK);
        c.setStrokeWidth(2);
        DropShadow ds = new DropShadow(5, 3, 3, Color.color(0.4, 0.4, 0.4, 0.3));
        c.setEffect(ds);
        Text name = new Text(v.name);
        name.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        name.setX(x - name.getBoundsInLocal().getWidth() / 2);
        name.setY(y + 5);

        Text hotelCostText = new Text(Integer.toString(ReadFile.hotelCosts[v.indexVertex]));
        hotelCostText.setFont(Font.font("Arial", 16));
        hotelCostText.setFill(Color.RED);
        hotelCostText.setX(x + NODE_RADIUS - 5);
        hotelCostText.setY(y - 30);

        nodeCircles.put(v.name, c);
        getChildren().addAll(c, name, hotelCostText);
    }

    private void drawEdgesFromVertex(Vertex src) {
        Double[] sp = nodePositions.get(src.name);
        if (sp == null) return;
        for (int i = 0; i < src.adjCount; i++) {
            Edge e = src.adjacent[i];
            Vertex d = e.destination;
            Double[] dpPos = nodePositions.get(d.name);
            if (dpPos == null) continue;
            double ang = Math.atan2(dpPos[1] - sp[1], dpPos[0] - sp[0]);
            double sx = sp[0] + NODE_RADIUS * Math.cos(ang);
            double sy = sp[1] + NODE_RADIUS * Math.sin(ang);
            double ex = dpPos[0] - NODE_RADIUS * Math.cos(ang);
            double ey = dpPos[1] - NODE_RADIUS * Math.sin(ang);
            Line line = new Line(sx, sy, ex, ey);

            if (d.stage == Arrays.stream(vertexArray).mapToInt(v->v.stage).max().orElse(0)) {
                line.setStroke(Color.MEDIUMPURPLE);
                line.getStrokeDashArray().setAll(10.0, 10.0);
            } else {
                line.setStroke(Color.BLACK);
                line.getStrokeDashArray().clear();
            }
            line.setStrokeWidth(1.5);

            double mx = (sx + ex) / 2;
            double my = (sy + ey) / 2;
            Text petrolText = new Text(Integer.toString(e.petrolCost));
            petrolText.setFont(Font.font("Arial", 10));
            petrolText.setFill(Color.BLACK);
            petrolText.setX(mx);
            petrolText.setY(my);

            getChildren().addAll(line, petrolText);
        }
    }

    private void addLegend() {
        HBox legend = new HBox(20);
        legend.setPadding(new Insets(10));
        legend.setAlignment(Pos.CENTER);
        legend.setBackground(new Background(new BackgroundFill(Color.rgb(245, 245, 245), CornerRadii.EMPTY, Insets.EMPTY)));
        legend.setBorder(new Border(new BorderStroke(Color.rgb(200, 200, 200), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1, 0, 0, 0))));
        legend.getChildren().addAll(
                createLegendItem("Black = Petrol Cost", Color.BLACK),
                createLegendItem("Gray = Hotel Cost", Color.GRAY),
                createLegendItem("Red = DP Cost", Color.RED)
        );
        setBottomAnchor(legend, 0.0);
        setLeftAnchor(legend, 0.0);
        setRightAnchor(legend, 0.0);
        getChildren().add(legend);
    }

    private HBox createLegendItem(String text, Color color) {
        Circle c = new Circle(6, color);
        Label l = new Label(text);
        l.setFont(Font.font("Arial", 12));
        HBox box = new HBox(10, c, l);
        box.setAlignment(Pos.CENTER);
        return box;
    }
}
