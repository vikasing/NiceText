/**
 * 
 */
package com.vikasing.nicetext;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
    private static final Pattern POSSIBLE_TEXT_NODES = Pattern.compile("p|div|td|h1|h2|h3|article|section|span|tmp");
    private static final Pattern ARTICLE_NODES = Pattern.compile("article|section|tmp");
    private static final Pattern MAIN_BLOCK_CLASSES_IDS = Pattern.compile("article|section|tmp|main|contententry|page|post|text|blog|story|mainContent");

	private static final int SENT_T = 70;
	private static final int WORDS_T = 5;
	private static final double RATIO_T = 0.15; 
	private NiceTextType niceTextType;
	
	public static void main(String[] args) {
		HTMLHelper htmlHelper = new HTMLHelper();		
		htmlHelper.getHTML("http://my.opera.com/chooseopera/blog/2013/06/11/introducing-opera-mail");
	}
	
	public void getHTML(String url){
		try {
			Document document = Jsoup.connect(url).timeout(60000).userAgent(USER_AGENT).get();
			//Document document = Jsoup.parse(new File("art.html"), "UTF-8");
			niceTextType = new NiceTextType();
			Element bodyElement = document.body();
			removeFat(bodyElement);
			articleFinder(niceTextType,bodyElement);
			niceTextType.setMainElements(new Elements(flattenDOM(bodyElement)));
			System.out.println("Art "+ niceTextType.getArticle());
			calculateBlockSizeRatios(niceTextType);
			System.out.println(niceTextType.getLargestTextBlock());
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
			StringBuffer allText = new StringBuffer();
			StringBuffer allHTML = new StringBuffer();
			for (Element element : niceTextType.getMainElements()) {
				if (element!=null) {					
					allText.append(element.text()+"\n");
					allHTML.append(element.toString());
				}
			}
			System.out.println(allText.toString());
			System.out.println(allHTML);
			niceTextType.setAllText(allText.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Set<Element> flattenDOM(Element bodyElement) {
		final Set<Element> flatDOM = new LinkedHashSet<Element>();
		bodyElement.traverse(new NodeVisitor() {
		    public void head(Node node, int depth) {
		    	if (node instanceof Element) {
					Element innerElement = (Element)node;
					//if ((innerElement.isBlock() || POSSIBLE_TEXT_NODES.matcher(innerElement.tagName()).matches())&& innerElement.text().length()>50) {
					if (innerElement.ownText().length()>=WORDS_T) {	
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

	public NiceTextType calculateBlockSizeRatios(NiceTextType niceTextType){
		Elements mainElements = niceTextType.getMainElements();
		Map<Integer, Double> sizeMap = calculateSize(mainElements);
		Map<Integer, Double> k = findMax(sizeMap.values());
		int sizeOfMap = sizeMap.size();
		List<Integer> elemIndexForRemoval = new ArrayList<Integer>();
		Set<Integer> keySet = sizeMap.keySet();
		int maxIndex = 0;
		for (Integer j : k.keySet()) {
			maxIndex = j;
		}		
		for (Integer key : keySet) {
			sizeMap.put(key, sizeMap.get(key)/k.get(maxIndex));
		}
		for (int i =0; i<sizeOfMap;i++) { 
			if (sizeMap.get(i)<RATIO_T) {
				elemIndexForRemoval.add(i);
			}
		}
		for (int index : elemIndexForRemoval) {
			mainElements.set(index, null);
		}
		niceTextType.setMainElements(mainElements);
		niceTextType.setLargestHTMLBlock(mainElements.get(maxIndex).toString());
		niceTextType.setLargestTextBlock(mainElements.get(maxIndex).text());
		return niceTextType;
	}
	private Map<Integer, Double> findMax(Collection<Double> values) {
		double max = 0;
		int maxIndex = 0;
		Map<Integer, Double> maxElement = new HashMap<Integer, Double>();
		Object[] valuesArr = values.toArray();
		for (int i=0; i< valuesArr.length;i++) {
			if (max<=(Double)valuesArr[i]) {
				max = (Double)valuesArr[i];
				maxIndex= i;
			}
		}
		maxElement.put(maxIndex, max);
		return maxElement;
	}

	private Map<Integer, Double> calculateSize(Elements elements) {
		Map<Integer, Double> sizeMap = new LinkedHashMap<Integer, Double>();
		for (int i=0;i< elements.size();i++) {
			sizeMap.put(i, (double)elements.get(i).text().length());
		}
		return sizeMap;
	}
	private Set<Element> removeFat2(Element e) {
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
    private Element removeFat(Element doc) {
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
        Elements nonTextElements = doc.getAllElements();
        for (Element nonTextElement : nonTextElements) {
			if (!nonTextElement.hasText() || nonTextElement.text().length()<=WORDS_T) {
				nonTextElement.remove();
			}
		}
        return doc;
    }
    
    private void articleFinder(NiceTextType niceTextType,Element bodyElement){
    	for (Element el : bodyElement.getAllElements()) {
            if (ARTICLE_NODES.matcher(el.tagName()).matches()) {
            	if (el.hasText()) {
            		niceTextType.setArticle(el.text());
            		break;
				}                
            }
            else if (MAIN_BLOCK_CLASSES_IDS.matcher(el.className()).matches() || MAIN_BLOCK_CLASSES_IDS.matcher(el.attr("id")).matches()) {
            	if (el.hasText()) {
            		niceTextType.setArticle(el.text());
            		break;
				} 
			}
        }
    }
}
