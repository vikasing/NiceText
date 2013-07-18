/**
 * 
 */
package com.vikasing.nicetext;


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
		String text = htmlHelper.getText("http://reviews.cnet.com/smartphones/htc-one-mini/4505-6452_7-35822951.html").getNiceText();
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
