/**
 * 
 */
package com.vikasing.nicetext;

/**
 * @author vksing3
 *
 */
public class SimilaritySummarizer implements Summariize {

	/* (non-Javadoc)
	 * @see com.vikasing.nicetext.Summariize#summarizeText()
	 */
	@Override
	public String summarizeText() {
		HTMLHelper htmlHelper = new HTMLHelper();
		String text = htmlHelper.getNiceText("http://opinionator.blogs.nytimes.com/2013/06/15/i-know-what-you-think-of-me/").getNiceText();
		String[] textA = text.split("./");
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
