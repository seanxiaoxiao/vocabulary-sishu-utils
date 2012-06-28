package org.seanxiaoxiao.vocabularysishu;

import javax.swing.text.html.HTMLEditorKit.Parser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Vocabmadeeasy {

    public static void main(String[] args) {
        
    }
    
    private static void parseURL(Document document) {
        NodeList images = document.getElementsByTagName("IMG");
        for (int i = 0; i < images.getLength(); i++) {
            Node imageNode = images.item(i);
            Node title = imageNode.getAttributes().getNamedItem("title");
            if (title == null) {
                continue;
            }
            String titleContent = title.getTextContent();
            Node src = imageNode.getAttributes().getNamedItem("src");
            if (src == null) {
                continue;
            }
            String srcContent = src.getTextContent();
        }
    }
}
