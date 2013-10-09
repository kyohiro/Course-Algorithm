import java.util.Set;
import java.util.TreeSet;

public class PointSET {

    private Set<Point2D> points; 
    // construct an empty set of points
    public PointSET() {
        points = new TreeSet<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p) {
        points.add(p);
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
        return points.contains(p);
    }

    // draw all of the points to standard draw
    public void draw() {
        for (Point2D p : points) {
            StdDraw.point(p.x(), p.y());
        }
    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        Set<Point2D> inrect = new TreeSet<Point2D>();
        for (Point2D p : points) {
            if (rect.contains(p))
                inrect.add(p);
        }
        return inrect; 
    }

    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
        Point2D minPoint = null;
        double minDist = Double.MAX_VALUE;
        for (Point2D d : points) {
           double dist = d.distanceTo(p); 
           if (dist < minDist) {
               minPoint = d;
               minDist = dist;
           }
        }
        return minPoint;
    }
    
    public static void main(String[] args) {
        PointSET ps = new PointSET();
        ps.insert(new Point2D(0.1, 0.1));
        ps.insert(new Point2D(0.1, 0.1));
        System.out.println(ps.contains(new Point2D(0.1, 0.1)));
    }
}
