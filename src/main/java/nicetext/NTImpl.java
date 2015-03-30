package nicetext;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * @author vikasing
 */
public class NTImpl implements NiceText {

    public String extract(String url) {
        String t = null;
        try {
            Connection connection = Jsoup.connect(url).userAgent(Constants.USER_AGENT).header("Accept", "text/html,application/xhtml+xml,application/xml").header("Accept-Encoding", "gzip,deflate,sdch").followRedirects(true).timeout(Constants.CONN_TIMEOUT);
            Connection.Response response = connection.execute();
            Document document = response.parse();
            t = extract(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

    public String extract(Document document) {
        NTHelper NTHelper = new NTHelper();
        return NTHelper.getText(document);
    }
}
