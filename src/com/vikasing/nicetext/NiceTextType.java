/**
 * 
 */
package com.vikasing.nicetext;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author vikasing
 *
 */
public class NiceTextType {
 private String largestTextBlock;
 private String largestHTMLBlock;
 private String allText;
 private Elements mainElements;
 private String article;
/**
 * @return the largestTextBlock
 */
public String getLargestTextBlock() {
	return largestTextBlock;
}
/**
 * @param largestTextBlock the largestTextBlock to set
 */
public void setLargestTextBlock(String largestTextBlock) {
	this.largestTextBlock = largestTextBlock;
}
/**
 * @return the allText
 */
public String getAllText() {
	return allText;
}
/**
 * @param allText the allText to set
 */
public void setAllText(String allText) {
	this.allText = allText;
}
/**
 * @return the largestHTMLBlock
 */
public String getLargestHTMLBlock() {
	return largestHTMLBlock;
}
/**
 * @param largestHTMLBlock the largestHTMLBlock to set
 */
public void setLargestHTMLBlock(String largestHTMLBlock) {
	this.largestHTMLBlock = largestHTMLBlock;
}
/**
 * @return the mainElements
 */
public Elements getMainElements() {
	return mainElements;
}
/**
 * @param mainElements the mainElements to set
 */
public void setMainElements(Elements mainElements) {
	this.mainElements = mainElements;
}
/**
 * @return the article
 */
public String getArticle() {
	return article;
}
/**
 * @param article the article to set
 */
public void setArticle(String article) {
	this.article = article;
}

 
}
