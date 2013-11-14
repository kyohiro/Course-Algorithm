import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordNet {

    private Digraph dg;
    private List<Synset> synsetMap;
    private Map<String, Set<Integer>> nounMap;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        int vCount = 0;
        synsetMap = new ArrayList<Synset>();
        nounMap = new HashMap<String, Set<Integer>>();
        Set<Integer> pointed = new HashSet<Integer>();

        In synsetsIn = new In(synsets);
        while (!synsetsIn.isEmpty()) {
            String[] elems = synsetsIn.readLine().split(",");
            Synset s = new Synset(Integer.valueOf(elems[0]), elems[1]);
            synsetMap.add(s);
            for (String n : s.nouns()) {
                if (!nounMap.containsKey(n)) {
                    nounMap.put(n, new HashSet<Integer>());
                }
                nounMap.get(n).add(s.id);
            }
            ++vCount;
        }

        dg = new Digraph(vCount);
        In hyperIn = new In(hypernyms);
        while (!hyperIn.isEmpty()) {
            String[] elems = hyperIn.readLine().split(",");
            int w = Integer.valueOf(elems[0]);
            for (int i = 1; i < elems.length; ++i) {
                int v = Integer.valueOf(elems[i]);
                dg.addEdge(w, v);
                pointed.add(w);
            }
        }

        if (!isRootedDAG(pointed, vCount)) {
            throw new IllegalArgumentException();
        }

        sap = new SAP(dg);
    }

    // the set of nouns (no duplicates), returned as an Iterable
    public Iterable<String> nouns() {
        return nounMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return nounMap.get(word) != null;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        return sap.length(nounMap.get(nounA), nounMap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of
    // nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        int ancestor = sap.ancestor(nounMap.get(nounA), nounMap.get(nounB));
        return synsetMap.get(ancestor).synset;
    }

    private boolean isRootedDAG(Set<Integer> pointed, int count) {
        int rootCnt = 0;
        for (int i = 0; i < count; ++i) {
            if (!pointed.contains(i)) {
                ++rootCnt;
            }
        }
        return rootCnt == 1;
    }

    // for unit testing of this class
    public static void main(String[] args) {
        WordNet net = new WordNet("synsets.txt", "hypernyms.txt");
        System.out.println(net.distance("water", "soda"));
    }

    private class Synset {
        private int id;
        private String synset;

        Synset(int id, String synset) {
            this.id = id;
            this.synset = synset;
        }
        
        private Iterable<String> nouns() {
            List<String> nouns = new ArrayList<>();
            for (String s : synset.split(" ")) {
                nouns.add(s);
            }
            return nouns;
        }
    }
}