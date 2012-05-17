package org.seanxiaoxiao.vocabularysishu;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TOFELVocabularyGrap {
    private static final String TOFEL_VOCABULARY_PAGE = "http://dict.yqie.com/TOFEL_glossary.htm";

    private static void parseVocabulary() {
        DOMParser parser = new DOMParser();
        try {
            parser.parse(TOFEL_VOCABULARY_PAGE);
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

    public static void main(String[] args) {
        parseVocabulary();
    }
}
