/**
 * 
 */
package com.vikasing.nicetext;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @author vikasing
 *
 */
public class NiceText {
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0";

	public NiceTextType getNiceText(String url) {
		NiceTextType nTextType = null;
		try {
			Document document = Jsoup.connect(url).timeout(60000).userAgent(USER_AGENT).get();
			nTextType = getNiceText(document);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nTextType;
	}
	
	public NiceTextType getNiceText(Document document) {
		HTMLHelper htmlHelper = new HTMLHelper();
		return htmlHelper.getText(document);
	}
}
