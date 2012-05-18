package org.seanxiaoxiao.vocabularysishu;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    private static Pattern traslatePattern = Pattern.compile("[\u4E00-\u9FA5]");

    private static Pattern splitPattern = Pattern.compile("，|：|；");

    private static Pattern characterPattern = Pattern.compile("([a-z]+.)（\\S*）");

    public static void main(String[] args) throws IOException {
        List<String> vocabularyList = Utils.getVocabularyList();
        //BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/vocabulary-meaning")));
        for (String vocabulary : vocabularyList) {
            System.out.println(vocabulary);
            if (vocabulary.indexOf(" ") < 0) {
                List<String> meanings = getTranslate(vocabulary);
                //bw.append(vocabulary).append("\t");
                for (String meaning : meanings) {
                    //bw.append(meaning + "\t");
                    System.out.println(meaning);
                }
            }
            //bw.append("\n");
        }
        //bw.close();
    }

    private static List<String> parseTraslate(String pageContent) {
        List<String> meanings = new ArrayList<String>();
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
                    String content = node.getChildNodes().item(3).getTextContent().trim();
                    System.out.println(content);
                    Matcher matcher = traslatePattern.matcher(content);
                    Matcher splitMatcher = splitPattern.matcher(content);
                    if (matcher.find() && splitMatcher.find()) {
                        meanings.add(content);
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return meanings;
    }
    
    private static List<String> getTranslate(String vocabulary) {
        String pageUrl = String.format(pageFormat, vocabulary.charAt(0), vocabulary);
        HttpGet method = null;
        try {
            HttpClient client = new DefaultHttpClient();
            method = new HttpGet(pageUrl);
            HttpResponse response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String page = EntityUtils.toString(entity, "gb2312");
                return parseTraslate(page);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
