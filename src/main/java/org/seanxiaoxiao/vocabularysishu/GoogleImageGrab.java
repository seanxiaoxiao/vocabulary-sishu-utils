package org.seanxiaoxiao.vocabularysishu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.SAXException;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

public class GoogleImageGrab {
    
    private static Pattern linkElementPattern = Pattern.compile("<a [^>]*>");
    private static Pattern srcPattern = Pattern.compile(";imgurl=([^&]*)&");
    
    private String word = null;
    
    public static void main(String[] args) throws SAXException, IOException {
        List<String> wordList = Utils.getVocabularyList();

        for (String word : wordList) {
            System.out.println(word);
            word = word.replace(" ", "+");
            File dir = new File("/Users/xiaoxiao/workspace/resource-google/" + word.toLowerCase().charAt(0) + "/" + word);
            if (!exsited(word) && !dir.exists()) {
                Selenium selenium = null;
                try {
                    selenium = new DefaultSelenium("localhost", 4444, "*firefox", "http://www.google.com.hk/");
                    selenium.start();
                    goForWord(selenium, word);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if (selenium != null) {
                        selenium.stop();
                    }
                }
            }
        }
    }

    private static void goForWord(Selenium selenium, String word) {
        String googlePage = "http://www.google.com.hk/search?hl=zh-CN&newwindow=1&safe=strict&biw=1439&bih=705&tbs=isz%3Am&tbm=isch&sa=1&q=" + word + "&oq=" + word + "&gs_l=img.3...0.0.0.4149.0.0.0.0.0.0.0.0..0.0...0.0.FvVwQ4CxTkA";
        System.out.println(googlePage);
        try {
            selenium.open(googlePage);
            selenium.waitForPageToLoad("60000");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        String htmlSource = selenium.getHtmlSource();
        Matcher linkElementMatcher = linkElementPattern.matcher(htmlSource);
        int count = 0;
        try {
            
            while (linkElementMatcher.find()) {
            try {
                String imageString = linkElementMatcher.group();
                if (count >= 9) {
                    break;
                }
                if (imageString.contains("class=\"rg_l\"")) {
                    Matcher srcMatcher = srcPattern.matcher(imageString);
                    if (srcMatcher.find()) {
                        String src = srcMatcher.group(1);
                        File jpgFile = new File(getDir(word) + "/" + word + "-" + count + ".jpg");
                        
                        URL url = new URL(src);
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
                        if (jpgFile.length() > 1024 * 120) {
                            jpgFile.delete();
                            continue;
                        }
                        count++;
                        is.close();
                        fos.close();
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            selenium.close();
            selenium.stop();
        }
    }
    
    private static String getDir(String word) {
        File dir = new File("/Users/xiaoxiao/workspace/resource-google/" + word.toLowerCase().charAt(0) + "/" + word);
        dir.mkdirs();
        return "/Users/xiaoxiao/workspace/resource-google/" + word.toLowerCase().charAt(0) + "/" + word;
    }
    
    private static boolean exsited(String word) {
        File file = new File("/Users/xiaoxiao/workspace/resource/" + word.toLowerCase().charAt(0) + "/" + word + ".jpg");
        return file.exists();
    }
}
