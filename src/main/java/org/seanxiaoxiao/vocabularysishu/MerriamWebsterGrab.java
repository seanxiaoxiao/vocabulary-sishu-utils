package org.seanxiaoxiao.vocabularysishu;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;

public class MerriamWebsterGrab {

    private static List<VocabularyMeaning> parseTraslate(String vocabulary) {
        DOMParser parser = new DOMParser();
        String page = "http://www.merriam-webster.com/dictionary/" + vocabulary.replaceAll(" ", "%20");
        try {
            parser.parse(page);
            Document doc = parser.getDocument();
            NodeList ols = doc.getElementsByTagName("OL");
            boolean find = false;
            for (int i = 0; i < ols.getLength(); i++) {
                Node ol = ols.item(i);
                Node classNode = ol.getAttributes().getNamedItem("class");
                if (classNode != null && classNode.getTextContent().equals("results")) {
                    find = true;
                    NodeList lis = ol.getChildNodes();
                    List<VocabularyMeaning> meanings = new ArrayList<VocabularyMeaning>();
                    for (int j = 0; j < lis.getLength(); j++) {
                        Node li = lis.item(j);
                        if ("LI".equals(li.getNodeName())) {
                            Node a = li.getChildNodes().item(0);
                            String content = a.getTextContent().trim();
                            String[] contents = content.split(" ");
                            String word = content;
                            if (contents.length == 2) {
                                word = contents[0].trim();
                            }
                            else if (contents.length > 2){
                                word = contents[1].trim();
                            }
                            if (vocabulary.equals(word)) {
                                String url = "http://www.merriam-webster.com" + a.getAttributes().getNamedItem("href").getTextContent();
                                DOMParser anotherParser = new DOMParser();
                                anotherParser.parse(url);
                                Document document = anotherParser.getDocument();
                                meanings.addAll(parseMeaning(document));
                            }
                        }
                    }
                    return meanings;
                }
            }
            if (!find) {
                return parseMeaning(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<VocabularyMeaning>();
    }

    private static List<VocabularyMeaning> parseMeaning(Document document) {
        NodeList spans = document.getElementsByTagName("SPAN");
        List<VocabularyMeaning> meanings = new ArrayList<VocabularyMeaning>();
        String attr = null;
        for (int i = 0; i < spans.getLength(); i++) {
            Node span = spans.item(i);
            Node classNode = span.getAttributes().getNamedItem("class");
            if (classNode != null && classNode.getTextContent().equals("main-fl")) {
                attr = span.getChildNodes().item(1).getTextContent();
            }
        }
        for (int i = 0; i < spans.getLength(); i++) {
            Node span = spans.item(i);
            Node classNode = span.getAttributes().getNamedItem("class");
            if (classNode != null && classNode.getTextContent().equals("ssens")) {
                String content = "";
                NodeList spanChildren = span.getChildNodes();
                for (int j = 0; j < spanChildren.getLength(); j++) {
                    Node child = spanChildren.item(j);
                    String text = child.getTextContent().trim();
                    if (text.length() > 1) {
                        content = content + text + " ";
                    }
                }
                if (content.length() > 0) {
                    VocabularyMeaning vocabularyMeaning = new VocabularyMeaning();
                    vocabularyMeaning.setAttribute(attr);
                    vocabularyMeaning.setMeaning(content);
                    meanings.add(vocabularyMeaning);
                }

            }
        }
        return meanings;
    }

    public static void main(String[] args) throws IOException {
        List<String> vocabularyList = Utils.getGRERemainingVocabularyList();
        boolean go = true;
        BufferedWriter bw = new BufferedWriter(new FileWriter("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/gre/target/vocabulary-mw-meaning", true));
        for (String vocabulary : vocabularyList) {
            if (go) {
                System.out.println(vocabulary);
                List<VocabularyMeaning> meanings = parseTraslate(vocabulary);
                if (meanings != null) {
                    Gson gson = new Gson();
                    bw.append(vocabulary).append("\t").append(gson.toJson(meanings));
                    bw.append("\n");
                }
                else {
                    bw.append(vocabulary).append("\n");
                }
            }
        }
        bw.close();
    }
}
