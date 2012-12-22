package org.seanxiaoxiao.vocabularysishu;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class SummaryGrab {
    
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/gre/source/remaining-word")));
        String line = null;
        while ((line = br.readLine()) != null) {
            parseFromCiba(line.split("\t")[0].trim());
        }
        br.close();
    }

    public static void parseFromCiba(String vocabulary) throws IOException {
        String url = String.format("http://www.iciba.com/%s", vocabulary.replaceAll(" ", "_"));
        HttpGet method = null;
        try {
            HttpClient client = new DefaultHttpClient();
            method = new HttpGet(url);
            HttpResponse response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String page = EntityUtils.toString(entity, "gb2312");
                DOMParser parser = new DOMParser();
                InputSource inputSource = new InputSource(new ByteArrayInputStream(page.getBytes()));
                inputSource.setEncoding("utf-8");
                parser.parse(inputSource);
                org.w3c.dom.Document doc = parser.getDocument();
                parseTraslateSimple(doc, vocabulary);
            }
        }
        catch (Exception e) {
            // TODO: handle exception
        }
    }

    private static void parseTraslateSimple(org.w3c.dom.Document doc, String vocabulary) {
        String meaning = null;
        try {
            NodeList divs = doc.getElementsByTagName("DIV");
            boolean find = false;
            for (int i = 0; i < divs.getLength(); i++) {
                try {
                    Node node = divs.item(i);
                    Node classAttribute = node.getAttributes().getNamedItem("class");
                    if (classAttribute != null && "dict_content".equals(classAttribute.getTextContent())) {
                        List<VocabularyMeaning> meanings = new ArrayList<VocabularyMeaning>();
                        VocabularyMeaning vocabularyMeaning = new VocabularyMeaning();
                        meanings.add(vocabularyMeaning);
                        vocabularyMeaning.setSpecial(0);
                        find = true;
                        Node contentP = node.getFirstChild().getNextSibling().getFirstChild().getNextSibling();
                        String raw = contentP.getFirstChild().getNextSibling().getTextContent().trim().replaceAll("\t", "").replaceAll("\n", "");
                        int lastIndex = raw.lastIndexOf(".");
                        if (lastIndex < 0) {
                            meaning = raw;
                        }
                        else {
                            meaning = raw.substring(lastIndex + 1, raw.length());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(String.format("%s\t%s", vocabulary, meaning));
        }
    }
}
