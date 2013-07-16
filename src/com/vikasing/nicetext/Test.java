/**
 * 
 */
package com.vikasing.nicetext;

import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;

/**
 * @author vikasing
 *
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HTMLHelper htmlHelper = new HTMLHelper();
		String text = htmlHelper.getText("http://www.nature.com/news/proof-mooted-for-quantum-uncertainty-1.13270").getNiceText();
		System.out.println(text);
		
/*		NGramExtracter nExtracter = new NGramExtracter();
		Map<String, SortedSet<Entry<String, Integer>>> nGramMap = nExtracter.getNGrams(text);
		SortedSet<Entry<String, Integer>> bigrams = nGramMap.get("bi");
		for (Entry<String, Integer> entry : bigrams) {
			if (entry.getValue()>1) {
				System.out.println(entry.getKey() +" "+entry.getValue());
			}
		}
		SortedSet<Entry<String, Integer>> trigrams = nGramMap.get("tri");
		for (Entry<String, Integer> entry : trigrams) {
			if (entry.getValue()>1) {
				System.out.println(entry.getKey() +" "+entry.getValue());
			}
		}
		
		SortedSet<Entry<String, Integer>> monograms = nGramMap.get("mono");
		for (Entry<String, Integer> entry : monograms) {
			if (entry.getValue()>1) {
				System.out.println(entry.getKey() +" "+entry.getValue());
			}
		}*/
	}

}
