import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class BoardTest {
    private Board board;
    
    @Before
    public void init() {
        In in = new In("input3.txt");
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        board = new Board(blocks);
    }

    @Test
    public void testHamming() {
        int hamming = board.hamming();
        assertEquals(5, hamming);
    }
    
    @Test
    public void testManhattan() {
        int manhattan = board.manhattan();
        assertEquals(10, manhattan);
    }
    
    @Test
    public void testGoal() {
        assertEquals(false, board.isGoal());
    }
    
    @Test
    public void testIterable() {
        System.out.println("Original");
        System.out.println(board);
        
        Iterator<Board> nb = board.neighbors().iterator();
        System.out.println("One neighbour");
        Board b = nb.next();
        System.out.println(b);
        
        System.out.println("Its neighbours");
        Iterator<Board> nb2 = b.neighbors().iterator();
        int counts = 0;
        while (nb2.hasNext()) {
            ++counts;
            System.out.println(nb2.next());
        }
        assertEquals(3, counts);
    }
}
