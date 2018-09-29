package realexample;

import java.net.URL;
import java.util.Collections;
import java.util.Set;

public class WordFinderImpl implements WordFinder {
    @Override
    public Set<String> getSentence(URL resource) {
        return Collections.emptySet();
    }

    @Override
    public boolean checkIfWordInSentence(String sentence, String word) {
        return false;
    }

    @Override
    public void writeSentenceToResult(String sentence) {
        // some code
    }
}
