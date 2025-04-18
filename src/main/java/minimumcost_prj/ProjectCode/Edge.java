package minimumcost_prj.ProjectCode;

public class Edge {
    public Vertex destination;
    public int petrolCost;
    public int hotelCost;

    public Edge(Vertex destination, int petrolCost, int hotelCost) {
        this.destination = destination;
        this.petrolCost = petrolCost;
        this.hotelCost = hotelCost;
    }

}
