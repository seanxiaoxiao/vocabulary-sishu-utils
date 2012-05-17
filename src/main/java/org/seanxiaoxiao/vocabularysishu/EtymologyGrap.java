package org.seanxiaoxiao.vocabularysishu;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EtymologyGrap {

    private static String pageFormat = "http://www.etymonline.com/index.php?allowed_in_frame=0&search=%s&searchmode=none";

    private static void parseEtymology(String vocabulary) {
        String url = String.format(pageFormat, vocabulary);
        DOMParser parser = new DOMParser();
        try {
            parser.parse(url);
            Document doc = parser.getDocument();
            NodeList contentList = doc.getElementsByTagName("dd");
            Node node = contentList.item(0);
            System.out.println(node.getTextContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        parseEtymology("abacus");
        parseEtymology("cat");
        parseEtymology("zone");
    }
}
