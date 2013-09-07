public class Percolation {
    private NMatrix mat;
    
    public Percolation(int sizeN) {
        if (sizeN > 0) {
            mat = new NMatrix(sizeN); }
        else
            throw new IllegalArgumentException("The size must be greater than 0.");
    }
    
    //The original index is 1 base, turn it to 0 based.
    public void open(int i, int j) {
        mat.open(i - 1, j - 1);
    }
    
    public boolean isOpen(int i, int j) {
        return mat.isOpen(i - 1, j - 1);
    }
    
    public boolean isFull(int i, int j) {
        return mat.isFull(i - 1, j - 1);
    }

    
    public boolean percolates() {
        return mat.percolates();
    }
    
    private class NMatrix extends WeightedQuickUnionUF {
        private int size;
        private int[] openSites;
        private boolean[] connectedToTop;
        private boolean[] connectedToBot;
        private boolean isPercolated = false;
        
        public NMatrix(int sizeN) {
            super(sizeN * sizeN);
            int total = sizeN * sizeN;
            openSites = new int[total];
            size = sizeN;
            
            connectedToTop = new boolean[total];
            for (int i = 0; i < sizeN; ++i) {
                connectedToTop[i] = true;
            }
            
            connectedToBot = new boolean[total];
            for (int i = 0; i < sizeN; ++i) {
                connectedToBot[total - i - 1] = true;
            }
        }
        
        
        @Override 
        public void union(int p, int q) {
            int i = find(p);
            int j = find(q);
            boolean hasTop = connectedToTop[i] || connectedToTop[j];
            boolean hasBot = connectedToBot[i] || connectedToBot[j];
            super.union(p, q);
            int newTop = find(p);
            connectedToTop[newTop] = hasTop;
            connectedToBot[newTop] = hasBot;
            if (hasTop && hasBot)
                isPercolated = true;
        }
        
        public void open(int i, int j) {
            if (!isOpen(i, j)) {
                int openIdx = getIndex(i, j);
                openSites[openIdx] = 1;
                
                if (i > 0 && isOpen(i - 1, j)) {
                    union(openIdx, openIdx - size);
                }
                if (i < size - 1 && isOpen(i + 1, j)) {
                    union(openIdx, openIdx + size);
                }
                if (j > 0 && isOpen(i, j - 1)) {
                    union(openIdx, openIdx - 1);
                }
                if (j < size - 1 && isOpen(i, j + 1)) {
                    union(openIdx, openIdx + 1);
                }
                //The only case no union will be called is N=1
                if (size == 1) {
                    union(openIdx, openIdx);
                }
            }
        }
        
        public boolean isOpen(int i, int j) {
            return openSites[getIndex(i, j)] == 1;
        }
        
        public boolean isFull(int i, int j) {
            if (!isOpen(i, j))
                return false;
            int topNode = find(getIndex(i, j));
            return connectedToTop[topNode];
        }
        
        public boolean percolates() {
            return isPercolated;
        }
        
        private int getIndex(int i, int j) {
            checkBoundaries(i, j);
            return i * size + j;
        }
        
        private void checkBoundaries(int i, int j) {
            if (i < 0 || i >= size || j < 0 || j >= size) {
                throw new IndexOutOfBoundsException("Row " + i + " , "
                + "Column " + j + " is out of boundary.");
            }
        }
        
    }
}
