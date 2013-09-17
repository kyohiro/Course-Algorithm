import java.util.Comparator;

public class Point implements Comparable<Point> {
    // compare points by slope to this point
    public final Comparator<Point> SLOPE_ORDER;
    
    private final int x;
    private final int y;
    
    // construct the point (x, y)
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        SLOPE_ORDER = new PointComparator(this);
    }
    
    // draw this point
    public void draw() {
        StdDraw.point(x, y);
    }
    
    // draw the line segment from this point to that point
    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }
    
    // string representation
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
    
    // is this point lexicographically smaller than that point?
    public int compareTo(Point that) {
        int diff = this.y - that.y;
        if (diff != 0)
            return diff;
        else
            return this.x - that.x;
    }
    
    // the slope between this point and that point
    public double slopeTo(Point that) {
        int dx = that.x - this.x;
        int dy = that.y - this.y;
        if (dx == 0) {
            //degenerate to point
            if (dy == 0) {
                return Double.NEGATIVE_INFINITY;
            }
            else {
                return Double.POSITIVE_INFINITY;
            }
        }
        else if (dy == 0) {
            return 0;
        }
        else {
            return (double) dy/dx;
        }
    }
    
    private class PointComparator implements Comparator<Point> {
        private Point basePoint;
        
        public PointComparator(Point bPoint) {
            basePoint = bPoint;
        }
        @Override
        public int compare(Point o1, Point o2) {
            double s1 = basePoint.slopeTo(o1);
            double s2 = basePoint.slopeTo(o2);
            if (s2 > s1)
                return -1;
            else if (s1 > s2)
                return 1;
            else
                return 0;
        }
        
    }
}