package minimumcost_prj;

public class Vertex {
    public String name;
    public int stage = -1;
    public int indexVertex = -1;
    public Edge[] adjacent;
    public int adjCount = 0;

    public Vertex(String name, int maxAdj) {
        this.name = name;
        this.adjacent = new Edge[maxAdj];
    }

    public void addAdjacent(Vertex dest, int petrolCost, int hotelCost) {
        Edge edge = new Edge(dest, petrolCost, hotelCost);
        this.adjacent[adjCount++] = edge;
    }
}
