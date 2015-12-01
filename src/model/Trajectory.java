package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Trajectory {
    List<Point> points;
    
    public Trajectory() {
        this.points = new ArrayList<Point>();
    }
    
    public Trajectory(List<Point> points) {
        this.points = new ArrayList<Point>(points);
    }
    
    public List<Point> getPoints() {
        return this.points;
    }
    
    public boolean addPoint(Point p) {
        return this.points.add(p);
    }
    
    public int size() {
        return this.points.size();
    }

    public void reverse() {
        Collections.reverse(this.points);
    }

    public void sparsify(double d) {
        int step = (int) (1.0/d);
        List<Point> toBeKept = new ArrayList<Point>();
        for (int i = 0; i < this.points.size(); i = i + step)
            toBeKept.add(this.points.get(i));
        this.points = toBeKept;
    }
    
}
