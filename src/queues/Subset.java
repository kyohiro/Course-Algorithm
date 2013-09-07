public class Subset {
    public static void main(String[] args) {
        if(args.length != 1) {
            throw new IllegalArgumentException();
        }
        
        int times = Integer.parseInt(args[0]);
        
        RandomizedQueue<String> rQueue = new RandomizedQueue<String>();
        while (StdIn.hasNextLine() && !StdIn.isEmpty()) {
            rQueue.enqueue(StdIn.readString());
        }
        while (--times >= 0) {
            StdOut.println(rQueue.dequeue());
        }
    }
}
