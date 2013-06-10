/**
 * 
 */
package com.vikasing.nicetext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;

/**
 * @author vikasing
 *
 */
public class HTMLHelper {
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0";
    private static final Pattern POSSIBLE_TEXT_NODES = Pattern.compile("p|div|td|h1|h2|h3|article|section|span");
	private static final int SENT_T = 50;
	private static final int WORDS_T = 15;
	private static final double RATIO_T = 0.15; 
	
	public static void getHTML(String url){
		try {
			Document document = Jsoup.connect(url).timeout(60000).userAgent(USER_AGENT).get();
			Element bodyElement = document.body(); 

			Elements mainElements = new Elements();
			Set<Element> refElements = flattenDOM(bodyElement);
			mainElements.addAll(refElements);
			calculateBlockSizeRatios(mainElements);
			for (Element element : mainElements) {
				if (element!=null) {					
					System.out.println(element.text());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static Set<Element> flattenDOM(Element bodyElement) {
		final Set<Element> flatDOM = new LinkedHashSet<Element>();
		bodyElement.traverse(new NodeVisitor() {
		    public void head(Node node, int depth) {
		    	if (node instanceof Element) {
					Element innerElement = (Element)node;
					if ((innerElement.isBlock() || POSSIBLE_TEXT_NODES.matcher(innerElement.tagName()).matches())&& innerElement.ownText().length()>0) {
						flatDOM.add(innerElement);
					}
				}
		    }
		    public void tail(Node node, int depth) {
		        //System.out.println("Exiting tag: " + node.nodeName());
		    }
		});		
		return flatDOM;
	}

	public static Elements calculateBlockSizeRatios(Elements mainElements){
		Map<Integer, Double> sizeMap = calculateSize(mainElements);
		double maxElement = findMax(sizeMap.values());
		int sizeOfMap = sizeMap.size();
		List<Integer> elemIndexForRemoval = new ArrayList<Integer>();
		Set<Integer> keySet = sizeMap.keySet();
		for (Integer key : keySet) {
			sizeMap.put(key, sizeMap.get(key)/maxElement);
		}
		for (int i =0; i<sizeOfMap;i++) { 
			if (sizeMap.get(i)<RATIO_T) {
				elemIndexForRemoval.add(i);
			}
		}
		for (int index : elemIndexForRemoval) {
			mainElements.set(index, null);
		}
		return mainElements;
	}
	private static double findMax(Collection<Double> values) {
		double max = 0;
		for (Double val : values) {
			if (max<=val) {
				max = val;
			}
		}
		return max;
	}

	private static Map<Integer, Double> calculateSize(Elements elements) {
		Map<Integer, Double> sizeMap = new LinkedHashMap<Integer, Double>();
		for (int i=0;i< elements.size();i++) {
			sizeMap.put(i, (double)elements.get(i).text().length());
		}
		return sizeMap;
	}
	private  static Set<Element> removeFat(Element e) {
        Set<Element> nodes = new LinkedHashSet<Element>(64);
        Elements allBodyElements = e.children();
        for (Element el : allBodyElements) {
            if (POSSIBLE_TEXT_NODES.matcher(el.tagName()).matches()) {
            	if (el.hasText()) {
            		nodes.add(el);
				}                
            }
        }
        return nodes;
    }
    private  static Element removeScriptsAndStyles(Element doc) {
        Elements scripts = doc.getElementsByTag("script");
        for (Element item : scripts) {
            item.remove();
        }
        
        Elements noscripts = doc.getElementsByTag("noscript");
        for (Element item : noscripts) {
            item.remove();
        }

        Elements styles = doc.getElementsByTag("style");
        for (Element style : styles) {
            style.remove();
        }

        return doc;
    }
	public static void main(String[] args) {
		getHTML("http://alumniconnect.wordpress.com/2013/06/04/a-monk-who-didnt-care-for-ferrari-teaching-to-serve-society/");
	}
}
