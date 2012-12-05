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

public class AudioGrap {

    private static void grapOnline() throws IOException {
        List<String> allVocabularyies = Utils.getVocabularyList();
        
        for (String vocabulary : allVocabularyies) {
            String url = "http://www.iciba.com/" + vocabulary;
            File dir = new File("/Users/xiaoxiao/workspace/resource-mp3/" + vocabulary.toLowerCase().charAt(0));
            dir.mkdirs();
            File mp3File = new File(dir + "/" + vocabulary + ".mp3");
            if (mp3File.exists()) {
                continue;
            }
            DOMParser parser = new DOMParser();
            try {
                parser.parse(url);
                Document doc = parser.getDocument();
                
                NodeList aList = doc.getElementsByTagName("A");
                String downloadLink = null;
                for (int i = 0; i < aList.getLength(); i++) {
                    Node node = aList.item(i);
                    Node attribute = node.getAttributes().getNamedItem("title");
                    if (attribute != null) {
                        String title = node.getAttributes().getNamedItem("title").getNodeValue();
                        Node parent = node.getParentNode();
                        if (title.contains("发音") && parent != null && parent.getNodeName().equals("SPAN")) {
                            String rawLink = node.getAttributes().getNamedItem("onclick").getNodeValue();
                            int start = rawLink.indexOf("('");
                            int end = rawLink.indexOf("')", start);
                            downloadLink = rawLink.substring(start + 2, end);
                            URL mp3Link = new URL(downloadLink);
                            URLConnection urlConnection = mp3Link.openConnection();
                            urlConnection.getInputStream();
                            InputStream is = urlConnection.getInputStream();
                            mp3File.createNewFile();
                            byte[] data = new byte[1024 * 8];
                            int length;
                            FileOutputStream fos = new FileOutputStream(mp3File);
                            while ((length = is.read(data)) != -1) {
                                fos.write(data, 0, length);
                            }
                            is.close();
                            fos.close();
                        }
                    }
                }
                System.out.println(downloadLink);
                System.out.println(vocabulary);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws ClassNotFoundException, IOException {
//        System.out.println("xxxx");
        grapOnline();
    }
}
