/**
 * 
 */
package com.vikasing.nicetext;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author vikasing
 *
 */
public class NGramExtracter {

	private String[] stopWords;
	
	public NGramExtracter(){
		this.stopWords = defaultStopWords;
	}
	public NGramExtracter(String[] words2Filter){
		this.stopWords = words2Filter;
	}
	
	private final String[] defaultStopWords={
			"i","me","my","myself","we","our","ours","ourselves","you","your","yours","yourself",
			"yourselves","he","him","his","himself","she","her","hers","herself","it","its","itself",
			"they","them","their","theirs","themselves","this","that","these","those","am","is","are",
			"was","were","be","been","being","have","has","had","having","do","does","did","doing","a",
			"an","the","and","if","or","as","while","of","at","by","for","with","about","between","into",
			"through","during","above","below","to","from","in","out","on","over","then","here","there","how",
			"all","any","both","each","few","other","some","such","only","own","too","can","will"
			};
	/**
	 * 
	 * @return 
	 */
	public Map<String, SortedSet<Entry<String, Integer>>> getNGrams(String text){
		
		Map<String, Integer> monoGramMap = new TreeMap<String, Integer>();
		Map<String, Integer> biGramMap = new TreeMap<String, Integer>();
		Map<String, Integer> triGramMap = new TreeMap<String, Integer>();
		Map<String, SortedSet<Entry<String, Integer>>> nGramMap = new HashMap<String, SortedSet<Entry<String,Integer>>>();

		text=text.trim();
		/*		
		 * Uncomment/comment if you want to remove/not remove the special chars from the text.
		 */
		text = text.toLowerCase().trim();
		for (int j = 0; j < stopWords.length; j++) {
			if (text.contains(stopWords[j])) {
				text=text.replaceAll("\\b"+stopWords[j]+"\\b", "");
			}	
		}
		text = text.replaceAll("[^a-zA-Z 0-9]+"," ");
		text = text.replaceAll(" +", " ");
		String[] words = text.split(" ");
		String nGram =null;
		for (int j = 0; j < words.length; j++) {
			words[j]=words[j].replaceAll(" +", "");
			if (!words[j].isEmpty() && words[j].length()>1){
				nGram = words[j];
				if(monoGramMap.containsKey(nGram)){
					monoGramMap.put(nGram, monoGramMap.get(nGram)+1);
				}
				else {
					monoGramMap.put(nGram, 1);
				}
			}
			if (words.length>j+1) {
				words[j+1]=words[j+1].replaceAll(" +", "");
				if (!words[j].isEmpty() && words[j].length()>1 && !words[j+1].isEmpty() && words[j+1].length()>1 ){
					nGram = words[j]+" "+words[j+1];
					if(biGramMap.containsKey(nGram)){
						biGramMap.put(nGram, biGramMap.get(nGram)+1);
					}
					else {
						biGramMap.put(nGram, 1);
					}
				}
				if (words.length>j+2) {
					words[j+2]=words[j+2].replaceAll(" +", "");
					if (!words[j].isEmpty() && words[j].length()>1 && !words[j+1].isEmpty() && words[j+1].length()>1  && !words[j+2].isEmpty() && words[j+2].length()>1){  
						nGram = words[j]+" "+words[j+1]+" "+words[j+2];
						if(triGramMap.containsKey(nGram)){
							triGramMap.put(nGram, triGramMap.get(nGram)+1);
						}
						else {
							triGramMap.put(nGram, 1);
						}
					}
				}
			}
		}					
		nGramMap.put("mono", entriesSortedByValues(monoGramMap));
		nGramMap.put("bi", entriesSortedByValues(biGramMap));
		nGramMap.put("tri", entriesSortedByValues(triGramMap));		
		return nGramMap;
	}
	private <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
	    SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
	        new Comparator<Map.Entry<K,V>>() {
	            @Override public int compare(Map.Entry<K,V> e2, Map.Entry<K,V> e1) {
                    int res = e1.getValue().compareTo(e2.getValue());
                    return res != 0 ? res : 1; 
	            }
	        }
	    );
	    sortedEntries.addAll(map.entrySet());
	    return sortedEntries;
	}
}
