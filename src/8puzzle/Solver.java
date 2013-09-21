import java.util.Comparator;
import java.util.ArrayDeque;

public class Solver {
    
    //Priority queue for input
    private MinPQ<SearchBoard> pq;
    
    private MinPQ<SearchBoard> twinPq;
    
    //Reconstruct the result
    private ArrayDeque<Board> goalQueue;
    
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        BoardComparator cmp = new BoardComparator();
        
        pq = new MinPQ<SearchBoard>(cmp);
        pq.insert(new SearchBoard(initial, null, 0));
        
        twinPq = new MinPQ<SearchBoard>(cmp);
        twinPq.insert(new SearchBoard(initial.twin(), null, 0));
        
        while (!pq.isEmpty()) {
            //Solve the input board
            SearchBoard goal = solveBoardOneStep(pq);
            if (goal != null) {
                setGoalQueue(goal);
                break;
            }
            
            //Solve twin board
            SearchBoard twinGoal = solveBoardOneStep(twinPq);
            if (twinGoal != null) {
                break;
            }
        }
    }
    
    private SearchBoard solveBoardOneStep(MinPQ<SearchBoard> queue) {
        SearchBoard current = queue.delMin();
        if (current.board.isGoal()) {
            return current;
        }
        else {
            SearchBoard previous = current.previous;
            for (Board board : current.board.neighbors()) {
                if (previous == null || !board.equals(previous.board)) {
                    queue.insert(new SearchBoard(board, current, current.moves + 1));
                }
            }
            return null;
        }
    }
    
    // is the initial board solvable?
    public boolean isSolvable() {
        return moves() != -1;
    }
    
    // min number of moves to solve initial board; -1 if no solution
    public int moves() {
        if (goalQueue != null) {
            return goalQueue.size() - 1;
        }
        else {
            return -1;
        }
    }
    
    // sequence of boards in a shortest solution; null if no solution
    public Iterable<Board> solution() {
        return goalQueue;
    }
    
    private void setGoalQueue(SearchBoard goal) {
        goalQueue = new ArrayDeque<Board>();
        SearchBoard now = goal;
        do {
            goalQueue.push(now.board);
            now = now.previous;
        }
        while (now != null);
    }
    
    //Use manhattan distance
    private class BoardComparator implements Comparator<SearchBoard> {
        @Override
        public int compare(SearchBoard o1, SearchBoard o2) {
            return o1.priority() - o2.priority();
        }
    }
    
    private class SearchBoard {
        private Board board;
        private SearchBoard previous;
        private int moves;
        
        SearchBoard(Board newBoard, SearchBoard prevBoard, int moves) {
            this.board = newBoard;
            this.previous = prevBoard;
            this.moves = moves;
        }
        
        private int priority() {
            return board.manhattan() + moves;
        }
    }
    
    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}