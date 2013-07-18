/**
 * 
 */
package com.vikasing.nicetext;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author vikasing
 *
 */
public class NGrams {

	private Map<String, Integer> monoGramMap;
	private Map<String, Integer> biGramMap;
	private Map<String, Integer> triGramMap;
	private Map<String, Integer> combinedGramMap;
	/**
	 * @return the monoGramMap
	 */
	public Map<String, Integer> getMonoGramMap() {
		return monoGramMap;
	}
	/**
	 * @param monoGramMap the monoGramMap to set
	 */
	public void setMonoGramMap(Map<String, Integer> monoGramMap) {
		this.monoGramMap = monoGramMap;
	}
	/**
	 * @return the biGramMap
	 */
	public Map<String, Integer> getBiGramMap() {
		return biGramMap;
	}
	/**
	 * @param biGramMap the biGramMap to set
	 */
	public void setBiGramMap(Map<String, Integer> biGramMap) {
		this.biGramMap = biGramMap;
	}
	/**
	 * @return the triGramMap
	 */
	public Map<String, Integer> getTriGramMap() {
		return triGramMap;
	}
	/**
	 * @param triGramMap the triGramMap to set
	 */
	public void setTriGramMap(Map<String, Integer> triGramMap) {
		this.triGramMap = triGramMap;
	}
	/**
	 * @return the combinedGramMap
	 */
	public Map<String, Integer> getCombinedGramMap() {
		return combinedGramMap;
	}
	/**
	 * @param combinedGramMap the combinedGramMap to set
	 */
	public void setCombinedGramMap(Map<String, Integer> combinedGramMap) {
		this.combinedGramMap = combinedGramMap;
	}
}
