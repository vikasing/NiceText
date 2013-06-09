/**
 * 
 */
package com.vikasing.nicetext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author vikasing
 *
 */
public class HTMLHelper {
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0";
    private static final Pattern NODES = Pattern.compile("p|div|td|h1|h2|h3|article|section|span");
	private static final int SENT_T = 100;
	private static final int WORDS_T = 15;
	private static final double RATIO_T = 0.15; 
	
	public static void getHTML(String url){
		try {
			Document document = Jsoup.connect(url).timeout(60000).userAgent(USER_AGENT).get();
			Element bodyElement = document.body(); 
			Element newBodyElement = removeScriptsAndStyles(bodyElement);
			Set<Element> refElements = removeFat(newBodyElement);
			Elements mainElements = new Elements();
			/*for (Element element : allElements) {
				if (element.isBlock() && element.hasText()) {
					mainElements.add(element);
				}
			}*/
			mainElements.addAll(refElements);
			Elements elements = calculateBlockSizeRatios(mainElements);
			mainElements.clear();
			for (Element element : elements) {
				for (Element innerElement : element.children()) {
					if (innerElement.isBlock() && innerElement.hasText() && innerElement.text().length()>SENT_T) {
						mainElements.add(innerElement);
					}
				}

			}
			Elements elements2 = calculateBlockSizeRatios(mainElements);
			for (Element element2 : elements2) {
				System.out.println(element2.text());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Elements calculateBlockSizeRatios(Elements mainElements){
		Elements elementsOfInterest = new Elements();
		Map<Integer, Double> sizeMap = calculateSize(mainElements);
		int sizeOfMap = sizeMap.size();
		List<Integer> elemIndexForRemoval = new ArrayList<Integer>();
		double maxElement = findMax(sizeMap.values());
		Set<Integer> keySet = sizeMap.keySet();
		for (Integer key : keySet) {
			sizeMap.put(key, sizeMap.get(key)/maxElement);
		}
		for (int i =0; i<sizeOfMap-1;i++) {
 
			//else if (ratio>1/RATIO_T) {
				//sizeMap.remove(i+1);
				//mainElements.remove(i+1);
				elemIndexForRemoval.add(i);
			//}
		}
		for (Integer index : elemIndexForRemoval) {
			sizeMap.remove(index);
		}
		
		/*Object[] mainElementsArray = mainElements.toArray();
		for (int j = 0;j<mainElementsArray.length-1;j++) {
			if (secondElSize>0 && firstElSize>0){ 
				
				double ratio = 0.0;
				
				if (mainTextLen==0.0) {
					ratio = firstElSize/secondElSize;
				}
				else {
					ratio = mainTextLen/secondElSize;
				}				
				if (ratio<1/RATIO_T && ratio>RATIO_T) {
					elementsOfInterest.add(fElement);
					elementsOfInterest.add(sElement);										
				}
				else if (ratio<RATIO_T) {
					elementsOfInterest.add(sElement);
				}
				else if (ratio>=1/RATIO_T) {
					elementsOfInterest.add(fElement);
				}
				if (mainTextLen<Math.max(firstElSize,secondElSize)) {
					mainTextLen = Math.max(firstElSize,secondElSize);
					fElement = sElement;
					sElement = (Element) mainElementsArray[j+1];
					firstElSize = fElement.toString().length();
					secondElSize = sElement.toString().length();
				}
				else {
					sElement =(Element) mainElementsArray[j+1];
					secondElSize = sElement.toString().length();
				}
			}
			else {
				fElement = (Element) mainElementsArray[j];
				sElement = (Element) mainElementsArray[j+1];
				firstElSize = fElement.toString().length();
				secondElSize = sElement.toString().length();
			}
		}*/
		return elementsOfInterest;
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
    public static Set<Element> removeFat(Element e) {
        Set<Element> nodes = new LinkedHashSet<Element>(64);
        Elements allBodyElements = e.children();
        for (Element el : allBodyElements) {
            if (NODES.matcher(el.tagName()).matches()) {
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
		getHTML("http://www.firstpost.com/fwire/monsoon-showers-cannot-hide-modis-grin-854383.html?utm_source=fwire&utm_medium=hp");
	}
}
