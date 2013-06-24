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
		String text = htmlHelper.getNiceText("http://online.wsj.com/article/SB10001424127887324577904578554672659491946.html").getNiceText();
		String[] textA = text.split("\\.");
		StringBuffer summBuffer = new StringBuffer();
		for (int i = 0; i < textA.length-1; i++) {
			double sim = findSimilarity(textA[i],textA[i+1]);
			if (sim>0.2 ) {
				summBuffer.append(textA[i].trim()+". ");
			}
		}
		System.out.println(summBuffer.toString());
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
