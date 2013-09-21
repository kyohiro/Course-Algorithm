import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Board {
    
    //store elements
    private int[] elements; 
    
    //width or height
    private int size;
    
    //where is the 0 elements?
    private int slotPos;
    
    //cached Manhattan distance 
    private int cacheManhattan = -1;
    
    //cached Hamming distance
    private int cacheHamming = -1;
    
    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        size = blocks.length;
        elements = new int[size * size];
        int count = 0;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                elements[count++] = blocks[i][j];
                if (blocks[i][j] == 0)
                    slotPos = count - 1;
            }
        }
    }
    
    // board dimension N                                      
    public int dimension() {
        return size;
    }

    // number of blocks out of place
    public int hamming() {
        if (cacheHamming == -1) {
            int dist = 0;
            for (int i = 0; i < size * size; ++i) {
                if (elements[i] != i + 1 && i != slotPos) {
                    ++dist;
                }
            }
            cacheHamming = dist;
        }
        return cacheHamming;
    }
    
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        if (cacheManhattan == -1) {
            int dist = 0;
            for (int row = 0; row < size; ++row) {
                for (int col = 0; col < size; ++col) {
                    int value = elements[toOneD(row, col)];
                    if (value != 0)
                        dist += diffManhattan(value, row, col);
                }
            } 
            cacheManhattan = dist; 
        }
        return cacheManhattan;
    }
    
    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < size * size - 1; ++i) {
            if (elements[i] != i + 1) {
                return false;
            }
        }
        return true;
    }
    
    // a board obtained by exchanging two adjacent blocks in the same row
    public Board twin() {
        int[][] array = copyElements(); 
       
        for (int i = 0; i < size; ++i) {
            if (array[i][0] != 0 && array[i][1] != 0) {
                swap(array, i, 0, i, 1); 
                break;
            }
        }
        
        return new Board(array); 
    }
    
    // does this board equal y? 
    @Override
    public boolean equals(Object y) {
        if (!(y instanceof Board))
            return false;
        Board b = (Board) y;
        if (b.dimension() != this.dimension())
            return false;
        for (int i = 0; i < size * size; ++i) {
            if (this.elements[i] != b.elements[i])
                return false;
        }
        return true;
    }
    
    // all neighboring boards
    public Iterable<Board> neighbors() {
        int row = slotPos / size;
        int col = slotPos % size;
        
        return new BoardIterable(row, col);
    }
    
    // string representation of the board (in the output format specified below)
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(size + "\n");
        for (int i = 0; i < size * size; ++i) {
            if (size <= 10) {
                sb.append(String.format("%2d", elements[i]));
            }
            else {
                sb.append(String.format("%3d", elements[i]));
            }
            sb.append(" ");
            if ((i+1) % size == 0) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
    
    // from 2D position to 1D index
    private int toOneD(int row, int col) {
        return row * size + col;
    }
    
    // diff of current value and where it should be
    private int diffManhattan(int value, int cRow, int cCol) {
        int row = (value - 1) / size;
        int col = (value - 1) % size;
        
        return Math.abs(row - cRow) + Math.abs(col - cCol);
    }
    
    private int[][] copyElements() {
        int[][] array = new int[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                array[i][j] = elements[toOneD(i, j)];
            }
        }
        return array;
    }
    
    private void swap(int[][] array, int row1, int col1, int row2, int col2) {
        int tmp = array[row1][col1];
        array[row1][col1] = array[row2][col2];
        array[row2][col2] = tmp;
    }
    
    private class BoardIterable implements Iterable<Board> {
        private List<Board> list = new ArrayList<Board>();
        
        public BoardIterable(int row, int col) {
            //has left
            if (col >= 1) {
                int[][] array = copyElements();
                swap(array, row, col, row, col - 1);
                list.add(new Board(array));
            }
            //has right
            if (col < size - 1) {
                int[][] array = copyElements();
                swap(array, row, col, row, col + 1);
                list.add(new Board(array));
            }
            //has top
            if (row >= 1) {
                int[][] array = copyElements();
                swap(array, row, col, row - 1, col);
                list.add(new Board(array));
            }
            //has bottom
            if (row < size - 1) {
                int[][] array = copyElements();
                swap(array, row, col, row + 1, col);
                list.add(new Board(array));
            }
        }
        
        @Override
        public Iterator<Board> iterator() {
            return list.iterator();
        }
        
    }
} 