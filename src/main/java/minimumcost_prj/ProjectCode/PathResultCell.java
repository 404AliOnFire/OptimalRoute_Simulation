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
    boolean add(String v, int cost, PathResultCell pathResultCell){
        if(counter >= paths.length) return  false;
        for (int i = 0; i < pathResultCell.counter; i++) {
            paths[counter++] = new PathResult(pathResultCell.paths[i].getPath() + " -> " + v, cost);
        }
        return true;
    }
    public PathResult[] getPaths(){
        return paths;
    }
}

