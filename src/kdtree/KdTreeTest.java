import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class KdTreeTest {
    PointSET ps = new PointSET();
    KdTree kt = new KdTree();
    
    @Before
    public void init() {
       
    }
    @Test
    public void testSize() {
        int size = 0;
        while (size++ < 100) {
            Point2D p = new Point2D(StdRandom.random(), StdRandom.random());
            ps.insert(p);
            kt.insert(p);
            assertEquals(ps.contains(p), kt.contains(p));
        }
        assertEquals(ps.size(), kt.size());
        
        while (size++ < 100) {
            Point2D p = new Point2D(StdRandom.random(), StdRandom.random());
            assertEquals(ps.contains(p), kt.contains(p));
        }
    }
    
    @Test
    public void testContains() {
        KdTree t = new KdTree();
        t.insert(new Point2D(3, 4));
        assertEquals(true, t.contains(new Point2D(3, 4)));
        assertEquals(false, t.contains(new Point2D(4, 3)));
    }
    
    
    @Test
    public void testRange() {
        KdTree t = new KdTree();
        t.insert(new Point2D(1, 1));
        t.insert(new Point2D(0.2391, 0.5353));
        System.out.println(t.range(new RectHV(0.0347, 0.3687, 0.2884, 0.5835)));
    }
    
    @Test
    public void testNearest() {
        KdTree t = new KdTree();
        PointSET ps = new PointSET();
        int size = 0;
        
        while (size++ < 100000) {
            Point2D p = new Point2D(StdRandom.random(), StdRandom.random());
            ps.insert(p);
            t.insert(p);
        }
        size = 0;
        while (size++ < 100) {
            Point2D p = new Point2D(StdRandom.random(), StdRandom.random());
            assertEquals(t.nearest(p), ps.nearest(p));
        }
    }

}
