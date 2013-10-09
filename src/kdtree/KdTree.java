import java.util.HashSet;
import java.util.Set;

public class KdTree {
    private Node root;
    private int size;
    
    // construct an empty set of points
    public KdTree() {
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }
    
    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p) {
        Node current = root;
        if (current == null) {
            root = new Node(p, true);
            ++size;
        }
        else {
            while (true) {
                int cmp = compare(current, p);
                if (cmp > 0) {
                    if (current.left == null) {
                        current.left = new Node(p, !current.isVertical);
                        ++size;
                        break;
                    }
                    else {
                        current = current.left;
                    }
                }
                else if (cmp < 0) {
                    if (current.right == null) {
                        current.right = new Node(p, !current.isVertical);
                        ++size;
                        break;
                    }
                    else {
                        current = current.right;
                    }
                }
                else {
                    break; 
                }
            }
        }  
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
        Node current = root;
        while (current != null) {
            int cmp = compare(current, p);
            if (cmp > 0) {
                current = current.left;
            }
            else if (cmp < 0) {
                current = current.right;
            }
            else {
                return true;
            }
        }
        return false;
    }
    
    private int compare(Node base, Point2D p) {
        //use X order compare first
        //break the tie by Y order
        if (base.isVertical) {
            int cmp = Point2D.X_ORDER.compare(base.point, p);
            if (cmp == 0) {
                return Point2D.Y_ORDER.compare(base.point, p);
            }
            else {
                return cmp;
            }
        }
        else {
            //the compareTo method in Point2D class is Y based
            //so if it's horizontal, just use it
            return base.point.compareTo(p);
        }
    }

    // draw all of the points to standard draw
    public void draw() {
        drawNode(root, getWholeRect());
    }
    
    private void drawNode(Node n, RectHV p) {
        if (n != null) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.point(n.point.x(), n.point.y());
            
            if (n.isVertical) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(n.point.x(), p.ymax(), n.point.x(), p.ymin());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(p.xmax(), n.point.y(), p.xmin(), n.point.y());
            }
            drawNode(n.left, getChildRect(n, p, true));
            drawNode(n.right, getChildRect(n, p, false));
        }
    }
    
    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        Set<Point2D> inrect = new HashSet<Point2D>();
        rangeSubTree(root, getWholeRect(), rect, inrect);
        return inrect;
    }
    
    private Iterable<Point2D> rangeSubTree(Node n, RectHV p, RectHV rect, Set<Point2D> accu) {
        Set<Point2D> inrect = new HashSet<Point2D>();
        if (n != null && rect.intersects(p)) {
            if (rect.contains(n.point)) {
                accu.add(n.point);
            }
            rangeSubTree(n.left, getChildRect(n, p, true), rect, accu);
            rangeSubTree(n.right, getChildRect(n, p, false), rect, accu);
        }
        return inrect;
    }
    
    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
        return getNearestPoint(root, getWholeRect(), p, null, Double.MAX_VALUE);
    }
    
    private Point2D getNearestPoint(Node n, RectHV p, Point2D s, Point2D minP, double minD) {
        Point2D minPoint = minP;
        double minDist = minD; 
        
        if (n != null && p.distanceSquaredTo(s) < minD) {
            double tmpDist = n.point.distanceSquaredTo(s);
            if (tmpDist < minD) {
                minPoint = n.point;
                minDist = tmpDist; 
            }
            
            int cmp = compare(n, s);
            //always check the side where the point's in first
            if (cmp > 0) {
                minPoint = getNearestPoint(n.left, getChildRect(n, p, true), s, minPoint, minDist);
                minPoint = getNearestPoint(n.right, getChildRect(n, p, false), s, minPoint, minPoint.distanceSquaredTo(s));
            }
            else if (cmp < 0) {
                minPoint = getNearestPoint(n.right, getChildRect(n, p, false), s, minPoint, minDist);
                minPoint = getNearestPoint(n.left, getChildRect(n, p, true), s, minPoint, minPoint.distanceSquaredTo(s));
            }
        }
        return minPoint;
    }

    //get the rectangle of the whole coordinate
    private RectHV getWholeRect() {
        return new RectHV(0, 0, 1, 1);
    }
    
    private RectHV getChildRect(Node n, RectHV p, boolean isLeft) {
        if (n.isVertical) {
            if (isLeft) {
                return new RectHV(p.xmin(), p.ymin(), n.point.x(), p.ymax());
            }
            else {
                return new RectHV(n.point.x(), p.ymin(), p.xmax(), p.ymax());
            }
        }
        else {
            if (isLeft) {
                return new RectHV(p.xmin(), p.ymin(), p.xmax(), n.point.y());
            }
            else {
                return new RectHV(p.xmin(), n.point.y(), p.xmax(), p.ymax());
            }
        }
    }
    
    private class Node {
        private Point2D point;
        private Node left;
        private Node right;
        private boolean isVertical;
        
        Node(Point2D point, boolean isVertical) {
            this.isVertical = isVertical;
            this.point = point;
        }
    }
    
    
    public static void main(String[] args) {
        String filename = "circle10.txt";
        In in = new In(filename);

        // initialize the data structures with N points from standard input
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            
            kdtree.insert(p);
            System.out.println(kdtree.size());
        }
        
    }
    
    
}
