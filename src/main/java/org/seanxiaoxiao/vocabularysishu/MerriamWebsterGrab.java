package org.seanxiaoxiao.vocabularysishu;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MerriamWebsterGrab {

    private static List<String> parseTraslate(String vocabulary) {
        List<String> meanings = new ArrayList<String>();
        DOMParser parser = new DOMParser();
        String page = "http://www.merriam-webster.com/dictionary/" + vocabulary.replaceAll(" ", "%20");
        try {
            parser.parse(page);
            Document doc = parser.getDocument();
            NodeList divs = doc.getElementsByTagName("DIV");
            for (int i = 0; i < divs.getLength(); i++) {
                Node div = divs.item(i);
                Node classNode = div.getAttributes().getNamedItem("class");
                if (classNode != null && classNode.getTextContent().equals("sblk")) {
                    System.out.println(div.getTextContent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return meanings;
    }

    public static void main(String[] args) throws IOException {
        List<String> vocabularyList = Utils.getVocabularyList();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/vocabulary-mw-meaning")));
        for (String vocabulary : vocabularyList) {
            System.out.println(vocabulary);
            List<String> meanings = parseTraslate(vocabulary);
            bw.append(vocabulary).append("\t");
            for (String meaning : meanings) {
                System.out.println(meaning);
                bw.append(meaning.replaceAll("\n", " ") + "\t");
            }
            bw.append("\n");
        }
    }
}
