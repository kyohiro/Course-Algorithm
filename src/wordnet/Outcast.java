public class Outcast {
    private WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int maxDist = Integer.MIN_VALUE;
        String outNoun = null;
        for (int i = 0; i < nouns.length; ++i) {
            int sumDist = 0;
            for (int j = 0; j < nouns.length; ++j) {
                if (i != j) {
                    sumDist += wordnet.distance(nouns[i], nouns[j]);
                }
            }
            if (sumDist > maxDist) {
                maxDist = sumDist;
                outNoun = nouns[i];
            }
        }

        return outNoun;
    }

    // for unit testing of this class (such as the one below)
    public static void main(String[] args) {
    }
}
