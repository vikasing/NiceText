package com.vikasing.nicetext;

import crow.global.GC;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * @author vikasing
 */
public class NTFactory {

    public static String getNiceText(String url) {
        String t = null;
        try {
            Connection connection = Jsoup.connect(url).userAgent(GC.USER_AGENT).header("Accept", "text/html,application/xhtml+xml,application/xml").header("Accept-Encoding", "gzip,deflate,sdch").followRedirects(true).timeout(GC.CONN_TIMEOUT);
            Connection.Response response = connection.execute();
            Document document = response.parse();
            t = getNiceText(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

    public static String getNiceText(Document document) {
        NTHelper NTHelper = new NTHelper();
        return NTHelper.getText(document).getNiceText();
    }
}
