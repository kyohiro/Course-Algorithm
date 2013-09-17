import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Brute {
    
    //Brute force, iterate over all combinations
    private static void bruteSearch(List<Point> points, SameSlopes sl) {
        int size = points.size();
        for (int i = 0; i < size; ++i) {
            for (int j = i+1; j < size; ++j) {
                for (int k = j+1; k < size; ++k) {
                    for (int l = k+1; l < size; ++l) {
                        if (isOnSameSlope(points, i, j, k, l)) {
                            sl.addSlope(points.get(i), points.get(j), points.get(k), points.get(l));
                        }
                    }
                }
            }
        }
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
    
    private static boolean isOnSameSlope(List<Point> points, int a, int b, int c, int d) {
        Point pa = points.get(a);
        Point pb = points.get(b);
        Point pc = points.get(c);
        if (pa.SLOPE_ORDER.compare(pb, pc) != 0) {
            return false;
        }
        else {
            Point pd = points.get(d);
            return pa.SLOPE_ORDER.compare(pb, pd) == 0;
        }
    }
    
    private static class SameSlopes {
        private Map<Integer, List<Point>> slopes = new HashMap<Integer, List<Point>>();
        
        public void addSlope(Point... list) {
            Arrays.sort(list);
            int key = getHashCode(list);
            if (!slopes.containsKey(key)) {
                slopes.put(key, Arrays.asList(list));
            }
        }
        
        public Map<Integer, List<Point>> getSlope() {
            return slopes;
        }
        
        //Use hashcode to determine if the points comination is already kept
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
        List<Point> points = null;
        SameSlopes sl = new SameSlopes();
        if (args.length != 0) {
            String filename = args[0];
            In fileIn = new In(filename);
            int pSize = fileIn.readInt();
            points = new ArrayList<Point>(pSize);
            
            while (!fileIn.isEmpty()) {
                int x = fileIn.readInt();
                int y = fileIn.readInt();
                points.add(new Point(x, y));
            }
        }
        else {
            int pSize = 90;
            points = new ArrayList<Point>(pSize);
           
            Set<Integer> h = new HashSet<Integer>();
            for (int i = 0; i <  pSize; ++i) {
                boolean canUse = false;
                while (!canUse) {
                    int x = StdRandom.uniform(10);
                    int y = StdRandom.uniform(10);
                    
                    int hashCode = x * 31 + y;
                    if (!h.contains(hashCode)) {
                        canUse = true;
                        points.add(new Point(x, y));
                        h.add(hashCode);
                    }
                }
            }
        }
        
        bruteSearch(points, sl);
        printSlopes(sl);
        draw(points, sl);
    }
}
