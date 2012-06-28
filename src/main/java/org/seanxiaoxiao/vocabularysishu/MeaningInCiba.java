package org.seanxiaoxiao.vocabularysishu;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class MeaningInCiba {

    public static void main(String[]args) throws IOException {
        List<String> remainingList = remainingList();
            String url = String.format("http://www.iciba.com/%s", "abreast");
            HttpGet method = null;
            try {
                HttpClient client = new DefaultHttpClient();
                method = new HttpGet(url);
                HttpResponse response = client.execute(method);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String page = EntityUtils.toString(entity, "gb2312");
                    parseTraslate(page, "abreast");
                }
            }
            catch (Exception e) {
                // TODO: handle exception
            }
    }

    public static List<VocabularyMeaning> parseTraslate(String pageContent, String vocabulary) {
        List<VocabularyMeaning> meanings = new ArrayList<VocabularyMeaning>();
        DOMParser parser = new DOMParser();
        try {
            InputSource inputSource = new InputSource(new ByteArrayInputStream(pageContent.getBytes()));
            inputSource.setEncoding("utf-8");
            parser.parse(inputSource);
            Document doc = parser.getDocument();
            NodeList divs = doc.getElementsByTagName("DIV");
            boolean find = false;
            for (int i = 0; i < divs.getLength(); i++) {
                try {
                    Node node = divs.item(i);
                    Node classAttribute = node.getAttributes().getNamedItem("class");
                    if (classAttribute != null && "collins_en_cn".equals(classAttribute.getTextContent())) {
                        find = true;
                        NodeList meaningParts = node.getChildNodes();
                        for (int j = 0; j < meaningParts.getLength(); j++) {
                            Node elementInPart = meaningParts.item(j);
                            NamedNodeMap namedNodeMap = elementInPart.getAttributes();
                            if (namedNodeMap == null) {
                                continue;
                            }
                            Node classAttr = namedNodeMap.getNamedItem("class");
                            if (classAttr != null && "caption".equals(classAttr.getTextContent())) {
                                NodeList contentParts = elementInPart.getChildNodes();
                                for (int k = 0; k < contentParts.getLength(); k++) {
                                    Node contentNode = contentParts.item(k);
                                    if (contentNode.getNodeName().equals("SPAN")) {
                                        VocabularyMeaning vocabularyMeaning = new VocabularyMeaning();
                                        vocabularyMeaning.setSpecial(0);
                                        NamedNodeMap attrsForSpan = contentNode.getAttributes();
                                        if (attrsForSpan != null) {
                                            if ("st".equals(attrsForSpan.getNamedItem("class").getTextContent())) {
                                                vocabularyMeaning.setAttribute(contentNode.getTextContent().trim().replaceAll("\t","").replaceAll("\n", ""));
                                                System.out.println(vocabularyMeaning.getAttribute());
                                            }
                                            else if ("text_blue".equals(attrsForSpan.getNamedItem("class").getTextContent())) {
                                                vocabularyMeaning.setMeaning(contentNode.getTextContent().trim());
                                                System.out.println(vocabularyMeaning.getMeaning());
                                            }
                                        }
                                        meanings.add(vocabularyMeaning);
                                    }
                                }
                            }
//                            if (classAttr != null && "caption".equals(classAttr.getTextContent())) {
//                                NodeList contentParts = elementInPart.getChildNodes();
//                                for (int k = 0; k < contentParts.getLength(); k++) {
//                                    Node contentNode = contentParts.item(k);
//                                    if (contentNode.getNodeName().equals("SPAN")) {
//                                        NamedNodeMap attrsForSpan = contentNode.getAttributes();
//                                        if (attrsForSpan != null) {
//                                            if ("st".equals(attrsForSpan.getNamedItem("class").getTextContent())) {
//                                                System.out.println(contentNode.getTextContent().trim().replaceAll("\t","").replaceAll("\n", ""));
//                                                //System.out.println("###############");
//                                            }
//                                            else if ("text_blue".equals(attrsForSpan.getNamedItem("class").getTextContent())) {
//                                                System.out.println(contentNode.getTextContent().trim());
//                                                //System.out.println("###############!!!");
//                                            }
//                                        }
//                                    }
//                                }
//                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (find == false) {
                parseTraslateSimple(doc, vocabulary);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return meanings;
    }

    private static void parseTraslateSimple(Document doc, String vocabulary) {
        try {
            NodeList divs = doc.getElementsByTagName("DIV");
            boolean find = false;
            for (int i = 0; i < divs.getLength(); i++) {
                try {
                    Node node = divs.item(i);
                    Node classAttribute = node.getAttributes().getNamedItem("class");
                    if (classAttribute != null && "dict_content".equals(classAttribute.getTextContent())) {
                        find = true;
                        Node contentP = node.getFirstChild().getNextSibling().getFirstChild().getNextSibling();
                        String rawString = contentP.getFirstChild().getNextSibling().getTextContent().trim().replaceAll("\t", "").replaceAll("\n", "");
                        int index = rawString.lastIndexOf(".");
                        if (index < 0) {
                            
                        }
                        else {
                            
                        }
                        //System.out.println(contentP.getLastChild().getPreviousSibling().getFirstChild().getNextSibling().getTextContent().trim());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (find == false) {
                parseTraslateNet(doc, vocabulary);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void parseTraslateNet(Document doc, String vocabulary) {
        try {
            NodeList divs = doc.getElementsByTagName("H4");
            boolean find = false;
            for (int i = 0; i < divs.getLength(); i++) {
                try {
                    Node node = divs.item(i);
                    Node classAttribute = node.getAttributes().getNamedItem("class");
                    if (classAttribute != null && "ee_mean_switch bg_down".equals(classAttribute.getTextContent())) {
                        find = true;
                        //System.out.println(node.getTextContent().trim());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (find == false) {
                //System.err.println(vocabulary);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<String> remainingList() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/not-in-heritage"));
        String line = null;
        List<String> remainingList = new ArrayList<String>();
        while ((line = br.readLine()) != null) {
            remainingList.add(line);
        }
        return remainingList;
    }
}
