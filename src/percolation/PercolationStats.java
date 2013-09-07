public class PercolationStats {
    private double[] openFraction;
    private int numberOfTest = 0;
    private double meanVal = 0;
    private double stddevVal = 0; 
    private static final double CONFIDENT_LEVEL = 1.96;
    
    public PercolationStats(int N, int T) {
        if(N > 0 && T > 0) {
            numberOfTest = T;
            openFraction = new double[T];
            
            int grids = N * N;
            for (int i = 0; i < T; ++i) {
                DoPercolation dp = new DoPercolation(N);
                openFraction[i] = (double)dp.getPercolationCount() / grids; 
            }
        }
        else {
            throw new IllegalArgumentException("N and T should be greater than 0.");
        }
    }
       
    public double mean() {
        meanVal = StdStats.mean(openFraction, 0, numberOfTest - 1);
        return meanVal;
    }
       
    public double stddev() {
        stddevVal = StdStats.stddev(openFraction);
        return stddevVal;
    }
       
    public double confidenceLo() {
        double miu = (meanVal == 0 ? mean() : meanVal);
        double std = (stddevVal == 0 ? stddev() : stddevVal);
        
        return miu - CONFIDENT_LEVEL*std/Math.sqrt(numberOfTest);
    }
       
    public double confidenceHi() {
        double miu = (meanVal == 0 ? mean() : meanVal);
        double std = (stddevVal == 0 ? stddev() : stddevVal);
        
        return miu + CONFIDENT_LEVEL*std/Math.sqrt(numberOfTest);
    }
    
    private class DoPercolation{
        private Percolation pc;
        private int size;
        
        public DoPercolation(int N) {
            pc = new Percolation(N);
            size = N;
        }
        
        public int getPercolationCount() {
            int count = 0;
            do {
                int row = StdRandom.uniform(size) + 1;
                int col = StdRandom.uniform(size) + 1; 
                if (!pc.isOpen(row, col)) {
                    pc.open(row, col);
                    ++count;
                }
            } while(!pc.percolates());
            return count; 
        }
    }
       
    public static void main(String[] args) {
        if(args.length != 2)
            throw new IllegalArgumentException("We need two input parameters.");
        int N = Integer.valueOf(args[0]);
        int T = Integer.valueOf(args[1]);
        PercolationStats ps = new PercolationStats(N, T);
        
        StdOut.println("mean                    = " + ps.mean());    
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = " + ps.confidenceLo() 
                + ", " + ps.confidenceHi());
    }
}