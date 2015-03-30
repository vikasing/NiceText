package nicetext;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author vikasing
 */
public class NGrams {
    private Map<String, Double> monoGrams = new TreeMap<>();
    private Map<String, Double> biGrams = new TreeMap<>();
    private Map<String, Double> triGrams = new TreeMap<>();

    /**
     * @return the monoGrams
     */
    public Map<String, Double> getMonoGrams() {
        return monoGrams;
    }

    /**
     * @return the biGrams
     */
    public Map<String, Double> getBiGrams() {
        return biGrams;
    }

    /**
     * @return the triGrams
     */
    public Map<String, Double> getTriGrams() {
        return triGrams;
    }
}
