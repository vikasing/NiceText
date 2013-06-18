/**
 * 
 */
package com.vikasing.nicetext;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
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
    private static final Pattern MAIN_BLOCK_CLASSES_IDS = Pattern.compile("article|section|tmp|contententry|page|post|text|blog|story|mainContent|container|content|postContent");
    private static final Pattern NEGATIVE_STYLE = Pattern.compile("hidden|display: ?none|font-size: ?small");
    private static final int CLUSTER_DISTANCE = 4;
	private static final int WORDS_T = 5;
	private static final double RATIO_T = 0.1; 
	private NiceTextType niceTextType;
	
	public HTMLHelper(){
		niceTextType = new NiceTextType();
	}
	public static void main(String[] args) {
		HTMLHelper htmlHelper = new HTMLHelper();		
		NiceTextType niceTextType = htmlHelper.getNiceText("http://www.itwire.com/business-it-news/open-source/60292-red-hat-ditches-mysql-switches-to-mariadb");
		//System.out.println(niceTextType.getArticleText());
	}
	
	public NiceTextType getNiceText(String url){
		try {
			Document document = Jsoup.connect(url).timeout(60000).userAgent(USER_AGENT).get();
			//Document document = Jsoup.parse(new File("art.html"), "UTF-8");
			
			Element bodyElement = removeFat(document.body());
			Elements bodyElements = bodyElement.getAllElements();
			articleFinder(bodyElement);
			niceTextType.setLargestHTMLBlock(findLargestBlock(bodyElements));
			Elements mainElements = calculateBlockSizeRatios(new Elements(flattenDOM(niceTextType.getLargestHTMLBlock())));
			Elements elementsOfInterest = calculateBlockSizeRatios(new Elements(flattenDOM(bodyElement)));
			StringBuffer blockBuffer = new StringBuffer();
			for (Element element : mainElements) {
				if (element!=null && (element.isBlock() || POSSIBLE_TEXT_NODES.matcher(element.tagName()).matches())) {
					blockBuffer.append(element.text()+"\n");
				}
			}
			List<Set<Element>> clusterSet = findClusters(elementsOfInterest);
			int maxCSize = 0;
			Set<Element> largestCluster = null;
			for (Set<Element> c : clusterSet) {
				if (maxCSize<c.size()) {
					maxCSize = c.size();
					largestCluster = c;
				}				
			}
			StringBuffer niceTextBuffer = new StringBuffer();
			for (Element element : largestCluster) {
				niceTextBuffer.append(element.text()+"\n");
			}
			niceTextType.setNiceText(niceTextBuffer.toString());
			niceTextType.setLargestTextBlock(blockBuffer.toString());
			niceTextType.setAllText(bodyElement.text());
			niceTextType.setPageTitle(document.title());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return niceTextType;
	}
	
	private List<Set<Element>> findClusters(Elements elements) {
		int nullCounter = 0;
		List<Set<Element>> clusters = new LinkedList<Set<Element>>();
		Set<Element> htmlElements = null;
		for (Element element : elements) {			
			if (element!=null && (element.isBlock() || POSSIBLE_TEXT_NODES.matcher(element.tagName()).matches())) {
				if (htmlElements!=null) {
					htmlElements.add(element);
				}
				else {
					htmlElements = new LinkedHashSet<Element>();
					htmlElements.add(element);
				}
				nullCounter = 0;
			}
			else if (element==null && htmlElements!=null && htmlElements.size()>0){
				nullCounter++;
			}
			if (nullCounter==CLUSTER_DISTANCE) {
				clusters.add(htmlElements);
				htmlElements = null;
				nullCounter = 0;
			}
		}
		if (clusters.size()==0) {
			clusters.add(htmlElements);
		}		
		return clusters;
	}
	private static String getTextFromElement(Element element){
		return null;		
	}
	private Set<Element> flattenDOM(Element bodyElement) {
		final Set<Element> flatDOM = new LinkedHashSet<Element>();
		bodyElement.traverse(new NodeVisitor() {
			private int parentTextSize = 0;
		    @Override
			public void head(Node node, int depth) {
		    	if (node instanceof Element) {
					Element innerElement = (Element)node;
					Element parentElement = innerElement.parent();
					if (parentElement!=null) {
						parentTextSize = parentElement.ownText().length();
					}
					//if ((innerElement.isBlock() || POSSIBLE_TEXT_NODES.matcher(innerElement.tagName()).matches())&& innerElement.text().length()>50) {
					if (innerElement.ownText().length()>=WORDS_T && parentTextSize==0) {	
						flatDOM.add(innerElement);
					}
				}
		    }
		    @Override
			public void tail(Node node, int depth) {
		        //System.out.println("Exiting tag: " + node.nodeName());
		    }
		});		
		return flatDOM;
	}
	private Element findLargestBlock(Elements bodyElements){
		Map<Integer, Double> sizeMap = calculateSize(bodyElements);
		Map<Integer, Double> k = findMax(sizeMap.values());
		int maxIndex = 0;
		for (Integer j : k.keySet()) {
			maxIndex = j;
		}
		return bodyElements.get(maxIndex);
	}
	public Elements calculateBlockSizeRatios(Elements mainElements){
		Map<Integer, Double> sizeMap = calculateSize(mainElements);
		Map<Integer, Double> k = findMax(sizeMap.values());
		int sizeOfMap = sizeMap.size();
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
				mainElements.set(i, null);
			}
		}
		return mainElements;
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
			sizeMap.put(i, (double)elements.get(i).ownText().length());
		}
		return sizeMap;
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
			String style = nonTextElement.attr("style");
			if (!nonTextElement.hasText() || nonTextElement.text().length()<=WORDS_T || (style != null && !style.isEmpty() && NEGATIVE_STYLE.matcher(style).find())) {
				nonTextElement.remove();
			}
		}
        return doc;
    }
    
    private void articleFinder(Element bodyElement){
    	for (Element el : bodyElement.getAllElements()) {
            if (ARTICLE_NODES.matcher(el.tagName()).matches()||MAIN_BLOCK_CLASSES_IDS.matcher(el.className()).matches() || MAIN_BLOCK_CLASSES_IDS.matcher(el.attr("id")).matches()) {
            	if (el.hasText()) {
            		niceTextType.setArticleHTML(el);
            		niceTextType.setArticleText(el.text());
            		break;
				}                
            }
        }
    }
}
