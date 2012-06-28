package org.seanxiaoxiao.vocabularysishu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class DictInfo {

    public static void main(String[] args) throws IOException {
        List<String> wordList = Utils.getVocabularyList();
        int count = 0;
        for (String word : wordList) {
            try {
                System.out.println(word);
                File jpgFile = new File(getDir(word) + "/" + word + ".jpg");
                if (jpgFile.exists()) {
                    continue;
                }
                
                URL url = new URL(getUrl(word));
                URLConnection urlConnection = url.openConnection();
                urlConnection.getInputStream();
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
                count++;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(count);
    }
    
    private static String getUrl(String word) {
        return String.format("http://www.dicts.info/img/ud/%s.jpg", word.charAt(0), word);
    }

    private static String getDir(String word) {
        return "/Users/xiaoxiao/workspace/resource/" + word.toLowerCase().charAt(0);
    }
}
