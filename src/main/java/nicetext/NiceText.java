package nicetext;

import org.jsoup.nodes.Document;

/**
 * @author vikasing
 */
public interface NiceText {
   public String extract(String url);
   public String extract(Document document);
}
