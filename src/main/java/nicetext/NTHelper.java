package nicetext;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author vikasing
 */
public class NTHelper {
    private static final Pattern POSSIBLE_TEXT_NODES = Pattern.compile("p|div|td|h1|h2|h3|article|section|span|tmp|li|font|em");
    private static final String[] UNWRAP_TAGS = {"b", "u", "i", "font", "em"};
    private static final Pattern ARTICLE_NODES = Pattern.compile("article|section|tmp");
    private static final Pattern MAIN_CLASSES_IDS = Pattern.compile("article|section|post|text|blog|story|mainContent");
    private static final Pattern CUSTOM_CLASSES_IDS = Pattern.compile("articleBody|article-text|story__content|article-body|artText|postContent|blogContentContainer|story-body|WNStoryBody|articl_cont|storytext|detail_content");
    private static final int CL_DIST = 4;
    private static final int WORDS_T = 5;
    private static final double DEFAULT_RATIO = 0.1;

    public String getText(Document document) {
        removeFat(document);
        Element bodyElement = document.body();
        Element articleE = articleFinder(bodyElement);
        //niceText.setAllText(bodyElement.text());
        //niceText.setPageTitle(document.title());
        String text = "";
        if (articleE != null) {
            text = getText(articleE);
        }
        if (text.length() < 500) {
            text = getText(bodyElement);
        }
        return text;
    }

    private String getText(Element bodyElement) {
        String text = "";
        Elements flattenElements = new Elements(flattenDOM(bodyElement));
        if (!flattenElements.isEmpty()) {
            Elements elementsOfInterest = calculateBlockSizeRatios(flattenElements);
            Set<Element> bestCluster = null;
            double maxScore = 0.0;
            List<Set<Element>> clusterSet = findClusters(elementsOfInterest);
            for (Set<Element> c : clusterSet) {
                double aTags = 0, largeATags = 1;
                int textSize = 0;
                for (Element elem : c) {
                    textSize += elem.text().length();
                    if (elem.tagName().equals("a")) {
                        String t = elem.text();
                       /* if (t.length() > 30) {
                            largeATags++;
                        } else */if (t.split(" ").length > 3) {
                            aTags++;
                        }
                    } else {
                        Elements aElems = elem.children();
                        for (Element aElem : aElems) {
                            if (aElem.tagName().equals("a")) {//get inner <a> elements
                                String t = aElem.text();
                               /* if (aElem.text().length() > 30) {
                                    largeATags++;
                                } else */if (t.split(" ").length > 3) {
                                    aTags++;
                                }
                            }
                        }
                    }
                }
                double score = textSize / (aTags == 0 ? 1 : aTags);
                if (maxScore < score) {
                    maxScore = score;
                    bestCluster = c;
                }
            }
            if (bestCluster != null) {
                StringBuilder niceTextBuffer = new StringBuilder();
                for (Element element : bestCluster) {
                    niceTextBuffer.append(element.text()).append("\n");
                }
                text = niceTextBuffer.toString();
            }
        }
        return text;
    }

    private Element articleFinder(Element bodyElement) {
        Element ae = null;
        int maxSize = 0;
        Elements elems = bodyElement.select("[itemprop=articleBody]");
        if (elems.size() > 0) {
            return elems.get(0);
        }
        for (Element elem : bodyElement.getAllElements()) {
            String tag = elem.tagName().toLowerCase();
            int textLength = elem.text().length();
            if (ARTICLE_NODES.matcher(tag).matches()) {
                if (textLength > maxSize) {
                    maxSize = textLength;
                    ae = elem;
                }
            } else {
                Set<String> idclsSet = new HashSet<>();
                String id = elem.id();
                if (!id.isEmpty()) {
                    idclsSet.add(id);
                }
                String cls = elem.className();
                if (cls.contains(" ")) {
                    idclsSet.addAll(Arrays.asList(cls.split(" ")));
                } else if (!cls.isEmpty()) {
                    idclsSet.add(cls);
                }
                for (String idcls : idclsSet) {
                    if (CUSTOM_CLASSES_IDS.matcher(idcls).matches() || CUSTOM_CLASSES_IDS.matcher(idcls).matches()) {
                        return elem;
                    } else if (MAIN_CLASSES_IDS.matcher(idcls).matches() || MAIN_CLASSES_IDS.matcher(idcls).matches()) {
                        if (textLength > maxSize) {
                            maxSize = textLength;
                            ae = elem;
                        }
                    }
                }
            }
        }
        return ae;
    }

    private List<Set<Element>> findClusters(Elements elements) {
        int nullCounter = 0;
        List<Set<Element>> clusters = new LinkedList<>();
        Set<Element> htmlElements = null;
        for (Element element : elements) {
            if (element != null && !checkChildren(element) && (element.isBlock() || POSSIBLE_TEXT_NODES.matcher(element.tagName()).matches())) {
                if (htmlElements != null) {
                    htmlElements.add(element);
                } else {
                    htmlElements = new LinkedHashSet<>();
                    htmlElements.add(element);
                }
                nullCounter = 0;
            } else if (element == null && htmlElements != null && htmlElements.size() > 0) {
                nullCounter++;
            }
            if (nullCounter == CL_DIST) {
                clusters.add(htmlElements);
                htmlElements = null;
                nullCounter = 0;
            }
        }
        // below condition handles the case when there is only one cluster without any null elements, e.g. first big chunk of the text
        // it also handles the last cluster of text when nulls are less than CL_DIST
        if (htmlElements != null && (clusters.size() == 0 || nullCounter < CL_DIST)) {
            clusters.add(htmlElements);
        }
        return clusters;
    }

    private boolean checkChildren(Element element) {
        boolean bad = false;
        int at = 0;
        for (Element children : element.children()) {
            if (children.tagName().equals("a")) {
                at += children.text().length();
            }
        }
        double ratio = at / (double) (element.ownText().length() + 1);
        if (ratio > 0.8) {
            bad = true;
        }
        return bad;
    }

    private Set<Element> flattenDOM(Element bodyElement) {
        final Set<Element> flatDOM = new LinkedHashSet<>();
        bodyElement.traverse(new NodeVisitor() {
            private int parentTextSize = 0;

            @Override
            public void head(Node node, int depth) {
                if (node instanceof Element) {
                    Element innerElement = (Element) node;
                    Element parentElement = innerElement.parent();
                    if (parentElement != null) {
                        parentTextSize = parentElement.ownText().length();
                    }
                    //if ((innerElement.isBlock() || POSSIBLE_TEXT_NODES.matcher(innerElement.tagName()).matches())&& innerElement.text().length()>50) {
                    if (innerElement.ownText().length() >= WORDS_T && parentTextSize == 0) {
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

    public Elements calculateBlockSizeRatios(Elements mainElements) {
        Map<Integer, Double> sizeMap = calculateSize(mainElements);
        Map<Integer, Double> k = findMaxAndAvg(sizeMap.values());
        int sizeOfMap = sizeMap.size();
        Set<Integer> keySet = sizeMap.keySet();
        int maxIndex = 0;
        for (Integer j : k.keySet()) {
            maxIndex = j;
        }
        for (Integer key : keySet) {
            sizeMap.put(key, sizeMap.get(key) / k.get(maxIndex));
        }
        for (int i = 0; i < sizeOfMap; i++) {
            if (sizeMap.get(i) < DEFAULT_RATIO) {
                mainElements.set(i, null);
            }
        }
        return mainElements;
    }

    private Map<Integer, Double> findMaxAndAvg(Collection<Double> values) {
        double max = 0;
        int maxIndex = 0;
        Map<Integer, Double> maxElement = new HashMap<>();
        Object[] valuesArr = values.toArray();
        double total = 0.0;
        for (int i = 0; i < valuesArr.length; i++) {
            if (max <= (Double) valuesArr[i]) {
                max = (Double) valuesArr[i];
                maxIndex = i;
            }
            total = total + (Double) valuesArr[i];
        }
        maxElement.put(maxIndex, max);
        return maxElement;
    }

    private Map<Integer, Double> calculateSize(Elements elements) {
        Map<Integer, Double> sizeMap = new LinkedHashMap<>();
        for (int i = 0; i < elements.size(); i++) {
            sizeMap.put(i, (double) elements.get(i).text().length());
        }
        return sizeMap;
    }

    private void removeFat(Document doc) {
        //String[] commonLinks = new String[] {"subscribe",""}
        for (String UNWRAP_TAG : UNWRAP_TAGS) {
            doc.select(UNWRAP_TAG).unwrap();
        }
        for (Element element : doc.body().getElementsByTag("br")) {
            if (element != null && element.tagName().equalsIgnoreCase("br")) {
                element.replaceWith(new TextNode("\n", null));
            }
        }
        for (Element element : doc.body().getAllElements()) {
            String tagName = element.tagName();
            if (tagName.equalsIgnoreCase("script") || tagName.equalsIgnoreCase("noscript") || tagName.equalsIgnoreCase("style")) {
                element.remove();
            } else if (tagName.equalsIgnoreCase("a")) {
                if (element.text().length() > 40) {
                    element.remove();
                } else if (!POSSIBLE_TEXT_NODES.matcher(element.parent().tagName()).matches() || element.parent().ownText().length() == 0) {
                    element.remove();
                }
            } else if (element.text().length() < WORDS_T) {
                element.remove();
            } else if (element.ownText().split("\\|").length > 3 ) {
                element.remove();
            } /*else if (element.ownText().contains("...")) {
                element.remove();
            }*/
        }
    }
}
