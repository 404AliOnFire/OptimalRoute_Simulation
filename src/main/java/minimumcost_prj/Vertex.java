package minimumcost_prj;

public class Vertex {
    public String name;
    public int stage = -1;
    public int[] adjacentsIndex;
    public int adjCount = 0;

    public Vertex(String name, int maxAdj) {
        this.name = name;
        this.adjacentsIndex = new int[maxAdj];
    }

    public void addAdjacent(int index) {
        this.adjacentsIndex[adjCount++] = index;
    }
}
