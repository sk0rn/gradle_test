package realexample;

import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

public class WordFinderUser {
    WordFinder wordFinder;
    private static final Logger LOGGER = Logger.getLogger(WordFinderUser.class);

    public WordFinderUser(WordFinder wordFinder) {
        this.wordFinder = wordFinder;
    }

    public void doWord(String resource, String word) throws MalformedURLException {
        URL url;
        try {
            url = new URL(resource);
        } catch (MalformedURLException e) {
            LOGGER.error(e);
            throw e;
        }
        Set<String> sentences = wordFinder.getSentence(url);
        if (sentences != null) {
            for (String sentence : sentences) {
                if (wordFinder.checkIfWordInSentence(sentence, word)) {
                    wordFinder.writeSentenceToResult(sentence);
                }
            }
        }
    }
}
