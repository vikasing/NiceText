/**
 * 
 */
package com.vikasing.nicetext;

/**
 * @author vikasing
 *
 */
public class SimilaritySummarizer extends Summarize {

	/* (non-Javadoc)
	 * @see com.vikasing.nicetext.Summariize#summarizeText()
	 */
	@Override
	public String summarizeText() {
		HTMLHelper htmlHelper = new HTMLHelper();
		String text = htmlHelper.getNiceText("http://opinionator.blogs.nytimes.com/2013/06/15/i-know-what-you-think-of-me/").getNiceText();
		String[] textA = text.split("\\.");
		for (int i = 0; i < textA.length-1; i++) {
			double sim = findSimilarity(textA[i],textA[i+1]);
			if (sim>0.1) {
				System.out.println(textA[i]);
			}
		}
		return null;
	}

	private double findSimilarity(String textA, String textB) {
		String[] wordsA = textA.split(" ");
		String[] wordsB = textB.split(" ");
		int matchC = 0;
		for (int i = 0; i < wordsA.length; i++) {
			for (int j = 0; j < wordsB.length; j++) {
				if (wordsA[i].equalsIgnoreCase(wordsB[j])) {
					matchC++;
				}
			}
		}
		return (double)matchC/(double)(wordsA.length+wordsB.length);		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimilaritySummarizer similaritySummarizer = new SimilaritySummarizer();
		similaritySummarizer.summarizeText();
	}

}
