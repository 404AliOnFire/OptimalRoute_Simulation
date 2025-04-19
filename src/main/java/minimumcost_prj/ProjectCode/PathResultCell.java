package minimumcost_prj.ProjectCode;

import java.util.Arrays;

public class PathResultCell {
    PathResult[] paths;
    int counter;
    public PathResultCell(int capacity) {
        paths = new PathResult[capacity];
    }

    boolean add(String v, int cost){
        if (counter >= paths.length) return false;
        paths[counter++] = new PathResult(v,cost);
        return true;
    }
    boolean add(String v, int stepCost, PathResultCell cell) {
        if (counter >= paths.length) return false;
        for (int i = 0; i < cell.counter; i++) {
            int prevCost = cell.paths[i].cost;
            int fullCost = prevCost + stepCost;
            paths[counter++] = new PathResult(
                    cell.paths[i].getPath() + " -> " + v,
                    fullCost
            );
        }
        return true;
    }

    public PathResult[] getPaths(){
        return paths;
    }
}

