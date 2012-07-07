package org.seanxiaoxiao.vocabularysishu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Vocabmadeeasy {

    public static void main(String[] args) throws SAXException, IOException {
        DOMParser parser = new DOMParser();
        parser.parse("http://vocabmadeeasy.com/2009/01/a-list-1-10/");
        Document doc = parser.getDocument();
        parseURL(doc);
    }
    
    private static void parseURL(Document document) throws SAXException, IOException {
        NodeList images = document.getElementsByTagName("IMG");
        for (int i = 0; i < images.getLength(); i++) {
            try {
                Node imageNode = images.item(i);
                Node title = imageNode.getAttributes().getNamedItem("title");
                if (title == null) {
                    continue;
                }
                String titleContent = title.getTextContent().trim().toLowerCase();
                Node src = imageNode.getAttributes().getNamedItem("src");
                if (src == null) {
                    continue;
                }
                String srcContent = src.getTextContent();
                File jpgFile = new File(getDir(titleContent) + "/" + titleContent + ".jpg");
                if (jpgFile.exists()) {
                    continue;
                }
                List<String> wordList = Utils.getVocabularyList();
                if (!wordList.contains(titleContent)) {
                    System.err.println(titleContent);
                    continue;
                }
                URL url = new URL(srcContent);
                URLConnection urlConnection = url.openConnection();
                urlConnection.getInputStream();
                File dir = new File(getDir(titleContent));
                dir.mkdirs();
                InputStream is = urlConnection.getInputStream();
                jpgFile.createNewFile();
                byte[] data = new byte[1024];
                int length;
                FileOutputStream fos = new FileOutputStream(jpgFile);
                while ((length = is.read(data)) != -1) {
                    fos.write(data, 0, length);
                }
                is.close();
                fos.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        NodeList aNodes = document.getElementsByTagName("A");
        String next = null;
        for (int i = 0; i < aNodes.getLength(); i++) {
            Node aNode = aNodes.item(i);
            if (aNode.getTextContent().startsWith("NEXT")) {
                next = aNode.getAttributes().getNamedItem("href").getTextContent();
                System.out.println(next);
            }
        }
        if (next != null) {
            DOMParser parser = new DOMParser();
            parser.parse(next);
            Document doc = parser.getDocument();
            parseURL(doc);
        }
    }

    private static String getDir(String word) {
        return "/Users/xiaoxiao/workspace/resource/" + word.toLowerCase().charAt(0);
    }
}
