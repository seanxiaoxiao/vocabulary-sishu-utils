package org.seanxiaoxiao.vocabularysishu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AudioGrap {

    private static void grapOnline() {
        List<String> allVocabularyies = Utils.getVocabularyList();
        for (String vocabulary : allVocabularyies) {
            String url = "http://www.iciba.com/" + vocabulary;
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
                        if ("真人发音".equals(node.getAttributes().getNamedItem("title").getNodeValue())) {
                            String rawLink = node.getAttributes().getNamedItem("onclick").getNodeValue();
                            int start = rawLink.indexOf("('");
                            int end = rawLink.indexOf("')", start);
                            downloadLink = rawLink.substring(start + 2, end);
                        }
                    }
                }
                System.out.println(downloadLink);
                System.out.println(vocabulary);
                
                URL downloadUrl = new URL(downloadLink);
                URLConnection connection = downloadUrl.openConnection();
                InputStream inputStream = connection.getInputStream();
                byte[] buffer = new byte[8 * 1024];
                File mp3 = new File("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/audio/" + vocabulary + ".mp3");
                mp3.createNewFile();
                OutputStream outputStream = new FileOutputStream(mp3);
                while(inputStream.read(buffer) > 0) {
                    outputStream.write(buffer);
                }
                inputStream.close();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void grapFromDB() throws ClassNotFoundException, SQLException, IOException {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:/Users/xiaoxiao/Desktop/voices.db");
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("select * from voices;");
        while (rs.next()) {
            String name = rs.getString("name");
            byte[] buffer = rs.getBytes("content");
            File mp3 = new File("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/audio-db/" + name);
            mp3.createNewFile();
            OutputStream outputStream = new FileOutputStream(mp3);
            outputStream.write(buffer);
            outputStream.close();
        }
        rs.close();
        conn.close();
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        System.out.println("xxxx");
        grapFromDB();
    }
}
