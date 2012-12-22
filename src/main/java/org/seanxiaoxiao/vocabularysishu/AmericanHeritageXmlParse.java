package org.seanxiaoxiao.vocabularysishu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.cyberneko.html.parsers.DOMParser;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.google.gson.Gson;

public class AmericanHeritageXmlParse {

    private static List<String> vocabularyList = Utils.getCET6VocabularyList();

    private static Map<String, List<VocabularyMeaning>> meaningMap = new HashMap<String, List<VocabularyMeaning>>();
    
    private static Map<String, String> infoMap = Utils.getRawAHInfo();

    private static int count = 0;

    private static void writeToFile() throws IOException {
//        List<String> vocabularyList = new ArrayList<String>();
//        for (String vocabulary : meaningMap.keySet()) {
//            vocabularyList.add(vocabulary);
//        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/cet6/target/vocabulary-meaning")));
        Collections.sort(vocabularyList, String.CASE_INSENSITIVE_ORDER);
        Gson gson = new Gson();
        for (String vocabulary : vocabularyList) {
            if (infoMap.containsKey(vocabulary)) {
                bw.append(infoMap.get(vocabulary));
            }
            else {
                bw.append(vocabulary);
                bw.append("::");
                bw.append(gson.toJson(meaningMap.get(vocabulary)));
            }
            bw.append("\n");
        }
        bw.close();
    }

    public static void main(String[] args) throws Exception {
        for (String vocabulary : vocabularyList) {
            System.out.println(vocabulary);
            if (!infoMap.containsKey(vocabulary)) {
                parseVocabularyXml(vocabulary);
            }
        }
        writeToFile();
        System.out.println(count);
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
                parseTraslate(page, vocabulary);
            }
        }
        catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void parseTraslate(String pageContent, String vocabulary) {
        DOMParser parser = new DOMParser();
        try {
            InputSource inputSource = new InputSource(new ByteArrayInputStream(pageContent.getBytes()));
            inputSource.setEncoding("utf-8");
            parser.parse(inputSource);
            org.w3c.dom.Document doc = parser.getDocument();
            NodeList divs = doc.getElementsByTagName("DIV");
            boolean find = false;
            List<VocabularyMeaning> meanings = new ArrayList<VocabularyMeaning>();

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
                                String attr = "";
                                for (int k = 0; k < contentParts.getLength(); k++) {
                                    Node contentNode = contentParts.item(k);
                                    if (contentNode.getNodeName().equals("SPAN")) {
                                        NamedNodeMap attrsForSpan = contentNode.getAttributes();
                                        if (attrsForSpan != null) {
                                            if ("st".equals(attrsForSpan.getNamedItem("class").getTextContent())) {
                                                attr = contentNode.getTextContent().trim().replaceAll("\t","").replaceAll("\n", "");
                                            }
                                            else if ("text_blue".equals(attrsForSpan.getNamedItem("class").getTextContent())) {
                                                VocabularyMeaning vocabularyMeaning = new VocabularyMeaning();
                                                vocabularyMeaning.setSpecial(0);
                                                vocabularyMeaning.setMeaning(contentNode.getTextContent().trim());
                                                vocabularyMeaning.setAttribute(attr);
                                                meanings.add(vocabularyMeaning);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (find == false) {
                parseTraslateSimple(doc, vocabulary);
            }
            else {
                meaningMap.put(vocabulary, meanings);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseTraslateSimple(org.w3c.dom.Document doc, String vocabulary) {
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
                            vocabularyMeaning.setAttribute("UNKNOWN");
                            vocabularyMeaning.setMeaning(raw);
                        }
                        else {
                            vocabularyMeaning.setAttribute(raw.substring(0, lastIndex + 1));
                            vocabularyMeaning.setMeaning(raw.substring(lastIndex + 1, raw.length()));
                        }
                        meaningMap.put(vocabulary, meanings);
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
    
    private static void parseTraslateNet(org.w3c.dom.Document doc, String vocabulary) {
        try {
            NodeList divs = doc.getElementsByTagName("H4");
            boolean find = false;
            List<VocabularyMeaning> meanings = new ArrayList<VocabularyMeaning>();
            for (int i = 0; i < divs.getLength(); i++) {
                try {
                    Node node = divs.item(i);
                    Node classAttribute = node.getAttributes().getNamedItem("class");
                    if (classAttribute != null && "ee_mean_switch bg_down".equals(classAttribute.getTextContent())) {
                        find = true;
                        VocabularyMeaning vocabularyMeaning = new VocabularyMeaning();
                        meanings.add(vocabularyMeaning);
                        vocabularyMeaning.setSpecial(0);
                        vocabularyMeaning.setMeaning(node.getTextContent().trim());
                        vocabularyMeaning.setAttribute("UNKNOWN");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (find == false) {
                //System.err.println(vocabulary);
            }
            meaningMap.put(vocabulary, meanings);
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
    private static void parseVocabularyXml(String vocabulary) throws Exception {
        
        File vocabularyXmlFile = new File("/Users/xiaoxiao/workspace/resource-dic/" + vocabulary.charAt(0) + "/" + vocabulary + ".xml");
        if (vocabularyXmlFile.exists() == false) {
            parseFromCiba(vocabulary);
            return;
        }
        SAXReader reader = new SAXReader();
        Document document = reader.read(vocabularyXmlFile);
        List<VocabularyMeaning> meanings = new ArrayList<VocabularyMeaning>();
        for (Iterator i = document.getRootElement().nodeIterator(); i.hasNext(); ) {
            Element cElement = (Element) i.next();
            Element fElement = (Element) cElement.elements().get(0);
            List subElements = fElement.elements();
            for (int j = 0; j < subElements.size(); j++) {
                Element iElement = (Element)subElements.get(j);
                if (iElement.getName().equals("I")) {
                    List nElements = iElement.elements();
                    for (Object object : nElements) {
                        Element nElement = (Element)object;
                        if (nElement.getName().equals("N")) {
                            List subElementsInN = nElement.elements();
                            boolean needParse = false;
                            for (Object object2 : subElementsInN) {
                                Element subElementInN = (Element)object2;
                                if (subElementInN.getName().equals("Q")) {
                                    needParse = true;
                                }
                            }
                            if (needParse == true) {
                                String attribute = null;
                                for (Object object2 : subElementsInN) {
                                    Element subElementInN = (Element)object2;
                                    if (subElementInN.getName().equals("P")) {
                                        List subElementsInP = subElementInN.elements();
                                        for (Object object3 : subElementsInP) {
                                            if (((Element)object3).getName().equals("U")) {
                                                attribute = ((Element)object3).getText().trim();
                                            }
                                        }
                                    }
                                    else if (subElementInN.getName().equals("Q")) {
                                        List subElementsInQ = subElementInN.elements();
                                        for (Object object3 : subElementsInQ) {
                                            if (((Element)object3).getName().equals("X")) {
                                                VocabularyMeaning vocabularyMeaning = new VocabularyMeaning();
                                                vocabularyMeaning.setAttribute(attribute);
                                                vocabularyMeaning.setMeaning(((Element)object3).getText().trim());
                                                meanings.add(vocabularyMeaning);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        meaningMap.put(vocabulary, meanings);
    }
}
