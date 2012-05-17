package org.seanxiaoxiao.vocabularysishu;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VocabularyGrap {

    private static final String GRE_VOCABULARY_PAGE = "http://dict.yqie.com/GRE_glossary.htm";

    private static final String TOFEL_VOCABULARY_PAGE = "http://dict.yqie.com/TOFEL_glossary.htm";

    private static final Set<String> vocabularySet = new HashSet<String>();

    private static void parseGREVocabulary() {
        DOMParser parser = new DOMParser();
        try {
            parser.parse(GRE_VOCABULARY_PAGE);
            Document doc = parser.getDocument();
            NodeList wordList = doc.getElementsByTagName("A");
            for (int i = 0; i < wordList.getLength(); i++) {
                Node node = wordList.item(i);
                vocabularySet.add(node.getTextContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseTOFELVocabulary() {
        DOMParser parser = new DOMParser();
        try {
            parser.parse(TOFEL_VOCABULARY_PAGE);
            Document doc = parser.getDocument();
            NodeList wordList = doc.getElementsByTagName("A");
            for (int i = 0; i < wordList.getLength(); i++) {
                Node node = wordList.item(i);
                vocabularySet.add(node.getTextContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        parseGREVocabulary();
        parseTOFELVocabulary();
        List<String> vocabularyList = new ArrayList<String>();
        for (String vocabulary : vocabularySet) {
            vocabularyList.add(vocabulary);
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/vocabularylist")));
        Collections.sort(vocabularyList);
        for (String vocabulary : vocabularyList) {
            bw.append(vocabulary);
            bw.append("\n");
        }
        bw.close();
    }
}
