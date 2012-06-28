package org.seanxiaoxiao.vocabularysishu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VocabularyGrap {

    private static final String GRE_VOCABULARY_PAGE = "http://dict.yqie.com/GRE_glossary.htm";

    private static final String TOFEL_VOCABULARY_PAGE = "http://dict.yqie.com/TOFEL_glossary.htm";

    private static final String GMAT_VOCABULARY_PAGE = "http://dict.yqie.com/GMAT_glossary.htm";

    private static final String IELTS_VOCABULARY_PAGE = "http://dict.yqie.com/IELTS_glossary.htm";

    private static final int GRETYPE = 1;

    private static final int TOFELTYPE = 2;

    private static final int GMATTYPE = 4;

    private static final int IELTSTYPE = 8;

    private static final Map<String, Vocabulary> maps = new HashMap<String, Vocabulary>();

    private static void parseGREVocabulary() {
        DOMParser parser = new DOMParser();
        try {
            parser.parse(GRE_VOCABULARY_PAGE);
            Document doc = parser.getDocument();
            NodeList wordList = doc.getElementsByTagName("A");
            System.out.println("xxxx");
            for (int i = 0; i < wordList.getLength(); i++) {
                Node node = wordList.item(i);
                addVocabulary(node.getTextContent(), GRETYPE);
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
            System.out.println("yyyy");
            for (int i = 0; i < wordList.getLength(); i++) {
                Node node = wordList.item(i);
                addVocabulary(node.getTextContent(), TOFELTYPE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseGMATVocabulary() {
        DOMParser parser = new DOMParser();
        try {
            parser.parse(GMAT_VOCABULARY_PAGE);
            Document doc = parser.getDocument();
            NodeList wordList = doc.getElementsByTagName("A");
            System.out.println("zzzz");
            for (int i = 0; i < wordList.getLength(); i++) {
                Node node = wordList.item(i);
                addVocabulary(node.getTextContent(), GMATTYPE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseIELTSVocabulary() {
        DOMParser parser = new DOMParser();
        try {
            parser.parse(IELTS_VOCABULARY_PAGE);
            Document doc = parser.getDocument();
            NodeList wordList = doc.getElementsByTagName("A");
            for (int i = 0; i < wordList.getLength(); i++) {
                Node node = wordList.item(i);
                addVocabulary(node.getTextContent(), IELTSTYPE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addVocabulary(String vocabulary, int type) {
        System.out.println("Add " + vocabulary + " with type " + type);
        Vocabulary existed = maps.get(vocabulary);
        if (existed == null) {
            Vocabulary newVocabulary = new Vocabulary();
            newVocabulary.setSpell(vocabulary);
            newVocabulary.setType(type);
            maps.put(vocabulary, newVocabulary);
        }
        else {
            existed.mergeType(type);
        }
    }
    
    public static void parseGRELost() throws IOException {
        File file = new File("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/grelost");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            addVocabulary(line, GRETYPE);
        }
    }

    public static void parseAnotherGRELost() throws IOException {
        File file = new File("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/greanotherlost");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            addVocabulary(line, GRETYPE);
        }
    }

    public static void parseTOEFLLost() throws IOException {
        File file = new File("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/toefllost");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            addVocabulary(line, TOFELTYPE);
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("start parsing gre");
        parseGREVocabulary();
        parseTOFELVocabulary();
        parseGMATVocabulary();
        parseIELTSVocabulary();
        parseGRELost();
        parseTOEFLLost();
        parseAnotherGRELost();
        List<String> vocabularyList = new ArrayList<String>();
        for (String vocabulary : maps.keySet()) {
            vocabularyList.add(vocabulary);
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/vocabularylist")));
        Collections.sort(vocabularyList, String.CASE_INSENSITIVE_ORDER);
        for (String vocabulary : vocabularyList) {
            bw.append(vocabulary);
            bw.append(",");
            bw.append(maps.get(vocabulary).getType() + "");
            bw.append("\n");
        }
        bw.close();
    }
}
