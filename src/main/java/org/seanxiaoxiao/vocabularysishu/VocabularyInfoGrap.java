package org.seanxiaoxiao.vocabularysishu;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import java_cup.runtime.lr_parser;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VocabularyInfoGrap {

    private static final String PHONETIC_PATTERN = "http://www.iciba.com/%s";

    private static final String ETYMON_PATTERN = "http://www.etymonline.com/index.php?allowed_in_frame=0&search=%s&searchmode=none";

    private static String getPhonetic(String vocabulary) {
        String url = String.format(PHONETIC_PATTERN, vocabulary);
        DOMParser parser = new DOMParser();
        try {
            parser.parse(url);
            Document doc = parser.getDocument();
            NodeList contentList = doc.getElementsByTagName("span");
            for (int i = 0; i < contentList.getLength(); i++) {
                Node spanNode = contentList.item(i);
                Node classAttribute = spanNode.getAttributes().getNamedItem("class");
                if (classAttribute != null) {
                    if ("fl".equals(classAttribute.getTextContent())) {
                        String content = spanNode.getTextContent();
                        int startIndex = content.lastIndexOf("[");
                        int endIndex = content.lastIndexOf("]");
                        return content.substring(startIndex + 1, endIndex).split(",")[0];
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String parseEtymology(String vocabulary) {
        String url = String.format(ETYMON_PATTERN, vocabulary);
        DOMParser parser = new DOMParser();
        try {
            parser.parse(url);
            Document doc = parser.getDocument();
            NodeList contentList = doc.getElementsByTagName("dd");
            Node node = contentList.item(0);
            return node.getTextContent();
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/cet6/target/vocabulary-info")));
        List<String> vocabularyList = Utils.getCET6VocabularyList();
        Map<String, String> rawVocabularyInfo = Utils.getRawInfo();
        for (String vocabulary : vocabularyList) {
            if (vocabulary.indexOf(" ") < 0) {
                if (rawVocabularyInfo.containsKey(vocabulary)) {
                    System.out.println("Has key: " + vocabulary);
                    bw.append(rawVocabularyInfo.get(vocabulary));
                }
                else {
                    System.out.println(vocabulary);
                    String phonetic = getPhonetic(vocabulary);
                    if (phonetic == null) {
                        phonetic = " ";
                    }
//                    String etymology = parseEtymology(vocabulary);
//                    if (etymology == null) {
//                        etymology = " ";
//                    }
//                    etymology = etymology.replaceAll("\n", " ");
                    bw.append(vocabulary).append("\t").append(phonetic).append("\t");
                }
            }
            else {
                bw.append(vocabulary);
            }
            bw.append("\n");
        }
        bw.close();
    }
}
