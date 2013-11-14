import java.util.HashSet;
import java.util.Set;

public class SAP {
    // constructor takes a digraph (not necessarily a DAG)
    private Digraph dg;

    public SAP(Digraph G) {
        dg = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        Queue<Integer> p = new Queue<Integer>();
        Queue<Integer> q = new Queue<Integer>();
        p.enqueue(v);
        q.enqueue(w);
        return length(p, q);
    }

    // a common ancestor of v and w that participates in a shortest ancestral
    // path; -1 if no such path
    public int ancestor(int v, int w) {
        Queue<Integer> p = new Queue<Integer>();
        Queue<Integer> q = new Queue<Integer>();
        p.enqueue(v);
        q.enqueue(w);
        return ancestor(p, q);
    }

    // length of shortest ancestral path between any vertex in v and any vertex
    // in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths bf1 = new BreadthFirstDirectedPaths(dg, v);
        BreadthFirstDirectedPaths bf2 = new BreadthFirstDirectedPaths(dg, w);
        Queue<Integer> q = new Queue<Integer>();
        for (int i : v) {
            q.enqueue(i);
        }
        for (int i : w) {
            q.enqueue(i);
        }
        int ma = minAncetor(bf1, bf2, q);
        if (ma == -1) {
            return -1;
        }
        else {
            return bf1.distTo(ma) + bf2.distTo(ma);
        }
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no
    // such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths bf1 = new BreadthFirstDirectedPaths(dg, v);
        BreadthFirstDirectedPaths bf2 = new BreadthFirstDirectedPaths(dg, w);
        Queue<Integer> q = new Queue<Integer>();
        for (int i : v) {
            q.enqueue(i);
        }
        for (int i : w) {
            q.enqueue(i);
        }
        return minAncetor(bf1, bf2, q);
    }

    private int minAncetor(BreadthFirstDirectedPaths bf1,
            BreadthFirstDirectedPaths bf2, Queue<Integer> q) {
        int minDistance = Integer.MAX_VALUE;
        int minAncestor = -1;
        Set<Integer> tested = new HashSet<Integer>();

        // check all source points first
        for (int n : q) {
            if (!tested.contains(n)) {
                int dist = distance(bf1, bf2, n);
                if (dist < minDistance) {
                    minDistance = dist;
                    minAncestor = n;
                }
                tested.add(n);
            }
        }

        // do BFS for all adjacent
        while (!q.isEmpty()) {
            int m = q.dequeue();
            for (int n : dg.adj(m)) {
                if (!tested.contains(n)) {
                    int dist = distance(bf1, bf2, n);
                    if (dist < minDistance) {
                        minDistance = dist;
                        minAncestor = n;
                    }
                    q.enqueue(n);
                    tested.add(n);
                }
            }
        }
        return minAncestor;
    }

    // the sum of distance from two searching paths to one point
    private int distance(BreadthFirstDirectedPaths bf1,
            BreadthFirstDirectedPaths bf2, int point) {
        int d1 = bf1.distTo(point);
        int d2 = bf2.distTo(point);
        if (d1 != Integer.MAX_VALUE && d2 != Integer.MAX_VALUE) {
            return d1 + d2;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    // for unit testing of this class (such as the one below)
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
