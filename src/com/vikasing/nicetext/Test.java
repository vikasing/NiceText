/**
 * 
 */
package com.vikasing.nicetext;


//import org.crow.utils.HtmlUtils;


/**
 * @author vikasing
 *
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NiceText niceText = new NiceText();

		/*HtmlUtils htmlUtils = new HtmlUtils();
		Set<String> urlSet = htmlUtils.getURLsFromHTML("https://news.ycombinator.com/news");
		for (String url : urlSet) {
			System.out.println(url+" +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			String text = htmlHelper.getText(url).getNiceText();
			System.out.println(text);
		}*/
		String text = niceText.getNiceText("http://techcrunch.com/2013/06/24/google-launches-cloud-playground-a-browser-based-environment-for-trying-its-cloud-platform/").getNiceText();
		System.out.println(text);
		/*
		NGramExtracter nExtracter = new NGramExtracter();
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
