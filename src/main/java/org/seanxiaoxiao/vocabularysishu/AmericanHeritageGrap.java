package org.seanxiaoxiao.vocabularysishu;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class AmericanHeritageGrap {

    private static String pageFormat = "http://dict.yqie.com/english/%c/%s.htm";

    private static Pattern traslatePattern = Pattern.compile("\\(\\d+\\)");

    public static void main(String[] args) {
        getTranslate("abacus");
        getTranslate("yell");
        getTranslate("zone");
    }

    private static void parseTraslate(String pageContent) {
        DOMParser parser = new DOMParser();
        try {
            InputSource inputSource = new InputSource(new ByteArrayInputStream(pageContent.getBytes()));
            inputSource.setEncoding("utf-8");
            parser.parse(inputSource);
            Document doc = parser.getDocument();
            NodeList tableRows = doc.getElementsByTagName("TR");
            boolean reachRecord = false;
            for (int i = 0; i < tableRows.getLength(); i++) {
                try {
                    Node node = tableRows.item(i);
                    int childCount = node.getChildNodes().getLength();
                    Node contentNode = node.getChildNodes().item(0);
                    if (contentNode.getTextContent().trim().startsWith("美国传统词典")) {
                        reachRecord = true;
                    }
                    else if (reachRecord && childCount == 1) {
                        break;
                    }
                    Matcher matcher = traslatePattern.matcher(node.getChildNodes().item(1).getTextContent());
                    
                    if (reachRecord && matcher.matches()) {
                        Node nextNode = tableRows.item(i + 1);
                        Node nextContent = nextNode.getChildNodes().item(3);
                        System.out.println(nextContent.getTextContent().trim());
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void getTranslate(String vocabulary) {
        String pageUrl = String.format(pageFormat, vocabulary.charAt(0), vocabulary);
        HttpGet method = null;
        try {
            HttpClient client = new DefaultHttpClient();
            method = new HttpGet(pageUrl);
            HttpResponse response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String page = EntityUtils.toString(entity, "gb2312");
                parseTraslate(page);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
