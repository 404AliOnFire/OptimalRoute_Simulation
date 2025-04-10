package minimumcost_prj;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ReadFile {
    /**
     * Reads the graph from a file and initializes a Vertex[] array.
     * Validates:
     * - Correct city count
     * - Unique vertex names
     * - Valid start/end
     * - Proper adjacency format (handles missing commas/brackets)
     * - Defined destinations
     * - Correct vertex order
     */
    public static int[] stageIndex;

    public static Vertex[] loadGraph(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // 1) Read number of cities
            line = br.readLine();
            if (line == null) {
                throw new IOException("❌ Input file is empty");
            }
            int cityNum;
            try {
                cityNum = Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                throw new IOException("❌ First line must be a valid integer representing number of cities");
            }

            // Prepare arrays
            Vertex[] vertices = new Vertex[cityNum];
            String[] names = new String[cityNum];
            String[] rawLines = new String[cityNum];

            // 2) Read start and end line
            line = br.readLine();
            if (line == null) {
                throw new IOException("❌ Missing start/end line (Start, End)");
            }
            String[] se = line.split(",");
            if (se.length < 2) {
                throw new IOException("❌ Start/End format invalid. Expected: Start, End");
            }
            String startName = se[0].trim();
            String endName = se[1].trim();

            // 3) Read exactly cityNum adjacency lines
            int count = 0;
            while ((line = br.readLine()) != null && count < cityNum) {
                line = line.trim();
                if (line.isEmpty()) continue;
                rawLines[count++] = line;
            }
            if (count < cityNum) {
                throw new IOException("❌ Expected " + cityNum + " adjacency lines, but got " + count);
            }

            // 4) First pass: create vertices and check duplicates
            Set<String> vertexSet = new HashSet<>();
            for (int i = 0; i < cityNum; i++) {
                String[] parts = rawLines[i].split(",", 2);
                String name = parts[0].trim();
                if (vertexSet.contains(name)) {
                    throw new IOException("❌ Duplicate vertex name: " + name + " at line " + (i + 3));
                }
                vertexSet.add(name);
                names[i] = name;

                // Calculate the number of edges for this vertex
                int edgeCount = 0;
                if (parts.length > 1) {
                    String adjacents = parts[1].trim();
                    // Split by '], [' or count standalone entries (handling missing brackets)
                    String[] adjacentsArray = adjacents.split("\\],\\s*\\[");
                    edgeCount = adjacentsArray.length;
                    // Adjust for missing brackets if needed
                    if (!adjacents.startsWith("[") || !adjacents.endsWith("]")) {
                        edgeCount = adjacents.split("[,\\s]+").length / 3; // Assuming each edge has 3 parts: name, petrol, hotel
                    }
                }

                // Create vertex with exact edge array size
                vertices[i] = new Vertex(name, edgeCount);
                vertices[i].indexVertex = i;
            }
            // 5) Validate that start and end exist
            int startIdx = -1, endIdx = -1;
            for (int i = 0; i < cityNum; i++) {
                if (names[i].equals(startName)) startIdx = i;
                if (names[i].equals(endName)) endIdx = i;
            }
            if (startIdx < 0 || endIdx < 0) {
                throw new IOException("❌ Start or End vertex is not defined among the city list");
            }

            // 6) Second pass: parse adjacents and validate format
            int adjCount = 0;
            int stage = 1;
            for (int i = 0; i < cityNum; i++) {
                String[] parts = rawLines[i].split(",", 2);
                String fromName = parts[0].trim();
                Vertex fromV = vertices[i];
                if (parts.length > 1) {
                    String adjacents = parts[1].trim();
                    // Split by '], [' to handle multiple adjacents
                    String[] adjacentsArray = adjacents.split("\\],\\s*\\[");
                    if (i - adjCount == 1 && i >= 1) {
                        stage++;
                        adjCount = adjacentsArray.length;
                    } else if(i == 0) adjCount = adjacentsArray.length;
                    for (int j = 0; j < adjacentsArray.length; j++) {
                        String adjacent = adjacentsArray[j].trim();
                        // Handle missing brackets by checking and cleaning
                        if (j == 0 && adjacent.startsWith("[")) {
                            adjacent = adjacent.substring(1);
                        } else if (!adjacent.startsWith("[")) {
                            adjacent = adjacent.trim(); // Handle cases like "End, 4, 7"
                        }
                        if (j == adjacentsArray.length - 1 && adjacent.endsWith("]")) {
                            adjacent = adjacent.substring(0, adjacent.length() - 1);
                        } else if (!adjacent.endsWith("]")) {
                            adjacent = adjacent.trim();
                        }
                        // Split by comma or space to handle missing commas
                        String[] ap = adjacent.split("[,\\s]+");
                        if (ap.length == 3) {
                            String toName = ap[0].trim();
                            int petrol, hotel;
                            try {
                                petrol = Integer.parseInt(ap[1].trim());
                                hotel = Integer.parseInt(ap[2].trim());
                            } catch (NumberFormatException e) {
                                throw new IOException("❌ Invalid cost values at " + fromName + " → " + adjacent);
                            }
                            int destIdx = -1;
                            for (int k = 0; k < cityNum; k++) {
                                if (names[k].equals(toName)) {
                                    destIdx = k;
                                    break;
                                }
                            }
                            if (destIdx < 0) {
                                throw new IOException("❌ Destination vertex not defined: " + toName + " in " + fromName);
                            }
                            // Check topological order (simplified: destination index > source index)
                            if (destIdx <= i && !toName.equals(endName)) {
                                throw new IOException("❌ Vertex order violation: " + fromName + " → " + toName);
                            }
                            fromV.addAdjacent(vertices[destIdx], petrol, hotel);
                            fromV.stage = stage;
                            if(fromV.indexVertex == 0) fromV.stage = 0;
                        } else {
                            throw new IOException("❌ Invalid adjacency format at " + fromName + " → " + adjacent);
                        }
                    }
                }
            }
            vertices[cityNum - 1].stage = stage + 1;
            System.out.println("✅ File successfully loaded and graph is ready!");
            setStageIndex(vertices);
            return vertices;
        }
    }

    public static void printGraph(Vertex[] graph) {
        System.out.println("\n🔍 Adjacency list:");
        for (Vertex v : graph) {
            System.out.print(v.name + " → ");
            for (int i = 0; i < v.adjCount; i++) {
                Edge e = v.adjacent[i];
                System.out.print("[" + e.destination.name + ", " + e.petrolCost + ", " + e.hotelCost + "] ");
            }
            System.out.println();
        }
    }
    public static void setStageIndex(Vertex[] graph){
        int length = graph.length;
        stageIndex = new int[graph[length - 1].stage + 1];

        for (int i = 1; i < length; i++) {
            Vertex prev = graph[i - 1];
            Vertex v = graph[i];


            if (prev != null && prev.stage != v.stage) {
                stageIndex[v.stage] = stageIndex[prev.stage];
            }

            stageIndex[v.stage]++;
            System.out.println(v.name + " -> " + stageIndex[v.stage]);
        }

    }

    public static void main(String[] args) {
        try {
            Vertex[] graph = loadGraph("input.txt");
            printGraph(graph);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}