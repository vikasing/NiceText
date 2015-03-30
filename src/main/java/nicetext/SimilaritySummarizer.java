/**
 *
 */
package nicetext;

/**
 * @author vikasing
 */
public class SimilaritySummarizer extends Summarize {

    /**
     * @param args
     */
    public static void main(String[] args) {
        SimilaritySummarizer similaritySummarizer = new SimilaritySummarizer();
        similaritySummarizer.summarizeText();
    }

    /* (non-Javadoc)
     * @see com.vikasing.crow.nicetext.Summarize#summarizeText()
     */
    @Override
    public String summarizeText() {
        NiceText niceText = new NTImpl();
        String text = niceText.extract("http://paulgraham.com/before.html");
        System.out.println(text);
        String[] textA = text.split("\\.");
        StringBuilder summBuffer = new StringBuilder();
        for (int i = 0; i < textA.length - 1; i++) {
            double sim = findSimilarity(textA[i], textA[i + 1]);
            if (sim >= 0.25) {
                summBuffer.append(textA[i].trim());
                summBuffer.append(". ");
            }
        }
        System.out.println("===============================");
        System.out.println(summBuffer.toString());
        return null;
    }

    private double findSimilarity(String textA, String textB) {
        String[] wordsA = textA.split(" ");
        String[] wordsB = textB.split(" ");
        int matchC = 0;
        for (String aWordsA : wordsA) {
            for (String aWordsB : wordsB) {
                if (aWordsA.equalsIgnoreCase(aWordsB)) {
                    matchC++;
                }
            }
        }
        return (double) matchC / (double) (wordsA.length + wordsB.length);
    }
}
