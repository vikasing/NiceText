package nicetext;

/**
 * @author vikasing
 */
public class NGramExtracter {

    public static NGrams extract(String text) {
        NGrams nGrams = new NGrams();
        text = text.toLowerCase().trim();
        text = removeSpecialChars(text);
        text = text.replaceAll(" +", " ");
        String[] words = text.split(" ");
        String nGram;
        for (int j = 0; j < words.length; j++) {
            words[j] = words[j].replaceAll(" +", "");
            if (!words[j].isEmpty() && words[j].length() > 1) {
                nGram = words[j];
                if (nGrams.getMonoGrams().containsKey(nGram)) {
                    nGrams.getMonoGrams().put(nGram, nGrams.getMonoGrams().get(nGram) + 1);
                } else {
                    nGrams.getMonoGrams().put(nGram, 1.0);
                }
            }
            if (words.length > j + 1) {
                words[j + 1] = words[j + 1].replaceAll(" +", "");
                if (!words[j].isEmpty() && words[j].length() > 1 && !words[j + 1].isEmpty() && words[j + 1].length() > 1) {
                    nGram = words[j] + " " + words[j + 1];
                    if (nGrams.getBiGrams().containsKey(nGram)) {
                        nGrams.getBiGrams().put(nGram, nGrams.getBiGrams().get(nGram) + 1);
                    } else {
                        nGrams.getBiGrams().put(nGram, 1.0);
                    }
                }
                if (words.length > j + 2) {
                    words[j + 2] = words[j + 2].replaceAll(" +", "");
                    if (!words[j].isEmpty() && words[j].length() > 1 && !words[j + 1].isEmpty() && words[j + 1].length() > 1 && !words[j + 2].isEmpty() && words[j + 2].length() > 1) {
                        nGram = words[j] + " " + words[j + 1] + " " + words[j + 2];
                        if (nGrams.getTriGrams().containsKey(nGram)) {
                            nGrams.getTriGrams().put(nGram, nGrams.getTriGrams().get(nGram) + 1);
                        } else {
                            nGrams.getTriGrams().put(nGram, 1.0);
                        }
                    }
                }
            }
        }
        return nGrams;
    }

    private static String removeSpecialChars(String text) {
        text = text.replaceAll("[^a-zA-Z 0-9]+", "");
        return text;
    }
}
