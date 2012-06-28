package org.seanxiaoxiao.vocabularysishu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class ImageGrab {

    public static void main(String[] args) throws IOException {
        List<String> wordList = Utils.getVocabularyList();
        int count = 0;
        boolean go = false;
        for (String word : wordList) {
            if (word.equals("disinfectant")) {
                go = true;
            }
            if (go) {
                try {
                    System.out.println(word);
                    URL url = new URL(getUrl(word));
                    URLConnection urlConnection = url.openConnection();
                    urlConnection.getInputStream();
                    File dir = new File(getDir(word));
                    dir.mkdirs();
                    InputStream is = urlConnection.getInputStream();
                    File jpgFile = new File(getDir(word) + "/" + word + ".jpg");
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
        }
        System.out.println(count);
    }
    
    private static String getUrl(String word) {
        word = word.toLowerCase();
        return String.format("http://photographicdictionary.com/sites/photographicdictionary.com/files/photos/%s/%s.jpg", word.charAt(0), word);
    }

    private static String getDir(String word) {
        return "/Users/xiaoxiao/workspace/resource/" + word.toLowerCase().charAt(0);
    }
}
