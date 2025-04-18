package minimumcost_prj.ProjectCode;

public class PathResult {
    String path;
    int cost;
    int counter;
    public PathResult(String path, int cost) {
        this.path = path;
        this.cost = cost;
    }

    public String getPath() {
        return path.toString();
    }

    public int getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return path + " = " + cost;
    }
}
