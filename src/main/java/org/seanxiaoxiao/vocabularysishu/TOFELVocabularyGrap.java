package org.seanxiaoxiao.vocabularysishu;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class TOFELVocabularyGrap {
    private static final String TOFEL_VOCABULARY_PAGE = "http://dict.yqie.com/TOFEL_glossary.htm";

    private static void parseVocabulary(String htmlText) {
        DOMParser parser = new DOMParser();
        try {
            parser.parse(new InputSource(new ByteArrayInputStream(htmlText.getBytes())));
            Document doc = parser.getDocument();
            NodeList wordList = doc.getElementsByTagName("A");
            for (int i = 0; i < wordList.getLength(); i++) {
                Node node = wordList.item(i);
                System.out.println(node.getTextContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void getPage() {
        HttpMethod method = null;
        try {
            HttpClient client = new HttpClient();
            method = new GetMethod(TOFEL_VOCABULARY_PAGE);
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + method.getStatusLine());
            }
            byte[] responseBody = method.getResponseBody();
            parseVocabulary(new String(responseBody));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }
    }

    public static void main(String[] args) {
        getPage();
    }
}
