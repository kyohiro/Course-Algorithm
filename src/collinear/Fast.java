import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fast {
    
    private static void fastSearch(List<Point> points, SameSlopes sl) {
        for (Point p : points) {
            Point[] cpy = getListCopyFor(points, p);
            Arrays.sort(cpy, p.SLOPE_ORDER);
            
            int idx = 0, same = 1;
            while (idx < cpy.length) {
                int start = idx;
                //scan through all consecutive points having the same slope
                while (idx < cpy.length - 1 && p.SLOPE_ORDER.compare(cpy[idx], cpy[idx+1]) == 0) {
                   ++idx;
                   ++same; 
                }
                if (same >= 3) {
                    sl.addSlope(cpy, start, same, p);
                }
                same = 1;
                ++idx;
            }
        }
    }
    
    private static Point[] getListCopyFor(List<Point> points, Point p) {
        Point[] cpy = new Point[points.size()-1]; 
        int i = 0;
        for (Point p2 : points) {
            if (p2 != p) {
                cpy[i++] = p2;
            }
        }
        return cpy;
    }
    
    
    private static void printSlopes(SameSlopes sl) {
        sl.printSlopes();
    }
    
    private static void draw(List<Point> points, SameSlopes sl) {
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        for (List<Point> list : sl.getSlope().values()) {
            Point first = list.get(0);
            Point last = list.get(list.size()-1);
            first.drawTo(last);
        }
        
    }
    
    private static class SameSlopes {
        private Map<Integer, List<Point>> slopes = new HashMap<Integer, List<Point>>();
        
        public void addSlope(Point[] copies, int start, int count, Point base) {
            Point[] list = new Point[count + 1];
            int i = 0;
            //Copy all the points, and don't forget the base points
            while (i < count) {
                list[i] = copies[start + i];
                ++i;
            }
            list[count] = base;
            
            Arrays.sort(list);
            int key = getHashCode(list);
            if (!slopes.containsKey(key)) {
                slopes.put(key, Arrays.asList(list));
            }
        }
        
        public Map<Integer, List<Point>> getSlope() {
            return slopes;
        }
        
        private int getHashCode(Point[] ps) {
            int l = 17;
            for (Point p : ps) {
                l += 31 * p.hashCode();
            }
            return l;
        }
        
        public void printSlopes() {
            for (List<Point> ps : slopes.values()) {
                for (int i = 0; i < ps.size(); ++i) {
                    StdOut.print(ps.get(i));
                    if (i != ps.size() - 1) {
                        StdOut.print(" -> ");
                    }
                    else {
                        StdOut.print("\n");
                    }
                }
            }
        }
    }
    
    public static void main(String[] args) {
        String filename = args[0];
        In fileIn = new In(filename);
        int pSize = fileIn.readInt();
        List<Point> points = new ArrayList<Point>(pSize);
        SameSlopes sl = new SameSlopes();
        
        while (!fileIn.isEmpty()) {
            int x = fileIn.readInt();
            int y = fileIn.readInt();
            points.add(new Point(x, y));
        }
        
        fastSearch(points, sl);
        printSlopes(sl);
        draw(points, sl);
    }
}
