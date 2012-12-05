package org.seanxiaoxiao.vocabularysishu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.StringMap;
import com.google.gson.stream.MalformedJsonException;

public class Utils {

    public static List<String> getVocabularyList() {
        try {
            List<String> vocabularyList = new ArrayList<String>();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/vocabularylist")));
            for (String line = null; (line = br.readLine()) != null; ) {
                vocabularyList.add(line.split(",")[0].trim());
            }
            return vocabularyList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static List<String> getCET4VocabularyList() {
        try {
            List<String> vocabularyList = new ArrayList<String>();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/cet4/source/四级大纲")));
            for (String line = null; (line = br.readLine()) != null; ) {
                vocabularyList.add(line.split(" ")[0].trim());
            }
            br.close();
            br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/cet4/source/四级-精精选")));
            for (String line = null; (line = br.readLine()) != null; ) {
                vocabularyList.add(line.trim());
                br.readLine();
            }
            br.close();
            return vocabularyList;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    

    public static List<String> getCET4OtherVocabulary() {
        try {
            List<String> vocabularyList = new ArrayList<String>();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/cet4/source/四级乱序")));
            for (String line = null; (line = br.readLine()) != null; ) {
                if (line.startsWith("Word List")) {
                    line = br.readLine();
                }
                vocabularyList.add(line.trim());
                br.readLine();
            }
            return vocabularyList;
        }
        catch (Exception e) {
            return null;
        }
    }

    public static List<String> getTOEFLVocabularyList() {
        try {
            List<String> vocabularyList = new ArrayList<String>();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/vocabularylist")));
            for (String line = null; (line = br.readLine()) != null; ) {
                String[] info = line.split(",");
                if ((Integer.valueOf(info[1]) & 2) != 0) {
                    vocabularyList.add(line.split(",")[0]);
                }
            }
            return vocabularyList;
        } catch (Exception e) {
            return null;
        }
    }
    
    public static List<String> getGMATVocabularyList() {
        try {
            List<String> vocabularyList = new ArrayList<String>();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/vocabularylist")));
            for (String line = null; (line = br.readLine()) != null; ) {
                String[] info = line.split(",");
                if ((Integer.valueOf(info[1]) & 4) != 0) {
                    vocabularyList.add(line.split(",")[0]);
                }
            }
            return vocabularyList;
        } catch (Exception e) {
            return null;
        }
    }
    
    public static List<String> getIELTSVocabularyList() {
        try {
            List<String> vocabularyList = new ArrayList<String>();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/vocabularylist")));
            for (String line = null; (line = br.readLine()) != null; ) {
                String[] info = line.split(",");
                if ((Integer.valueOf(info[1]) & 8) != 0) {
                    vocabularyList.add(line.split(",")[0]);
                }
            }
            return vocabularyList;
        } catch (Exception e) {
            return null;
        }
    }
    

    public static List<String> getGREVocabularyList() {
        try {
            List<String> vocabularyList = new ArrayList<String>();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/vocabularylist")));
            for (String line = null; (line = br.readLine()) != null; ) {
                String[] info = line.split(",");
                if ((Integer.valueOf(info[1]) & 1) != 0) {
                    vocabularyList.add(line.split(",")[0]);
                }
            }
            return vocabularyList;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Vocabulary> getAllVocabularies() {
        try {
            List<Vocabulary> vocabularyList = new ArrayList<Vocabulary>();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/vocabularylist")));
            for (String line = null; (line = br.readLine()) != null; ) {
                Vocabulary vocabulary = new Vocabulary();
                String[] info = line.split(",");
                vocabulary.setSpell(info[0]);
                vocabulary.setType(Integer.valueOf(info[1]));
                vocabularyList.add(vocabulary);
            }
            return vocabularyList;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Vocabulary> getGREVocabularies() {
        try {
            List<Vocabulary> vocabularyList = new ArrayList<Vocabulary>();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/vocabularylist")));
            for (String line = null; (line = br.readLine()) != null; ) {
                Vocabulary vocabulary = new Vocabulary();
                String[] info = line.split(",");
                if ((Integer.valueOf(info[1]) & 1) != 0) {
                    vocabulary.setSpell(info[0]);
                    vocabulary.setType(Integer.valueOf(info[1]));
                    vocabularyList.add(vocabulary);
                }
            }
            return vocabularyList;
        } catch (Exception e) {
            return null;
        }
    }
    
    public static Map<String, List<VocabularyMeaning>> getMeanings() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/vocabulary-meaning")));
        String line = null;
        Gson gson = new Gson();
        Map<String, List<VocabularyMeaning>> result = new HashMap<String, List<VocabularyMeaning>>();
        while ((line = br.readLine()) != null) {
            String[] infos = line.split("::");
            String word = infos[0];
            String meaning = infos[1];
            List<StringMap> list = gson.fromJson(meaning, List.class);
            List<VocabularyMeaning> vocabularyMeanings = new ArrayList<VocabularyMeaning>();
            int tongCount = 0;
            for (StringMap map : list) {
                VocabularyMeaning vocabularyMeaning = new VocabularyMeaning();
                vocabularyMeaning.setAttribute((String)map.get("attribute"));
                vocabularyMeaning.setMeaning((String)map.get("meaning"));
                String meaningContent = (String)map.get("meaning");
                String attrContent = (String)map.get("attribute");
                if (meaningContent.startsWith("同 ")) {
                    System.out.println(word);
                    tongCount++;
                }
                Float specialFloat = Float.valueOf(map.get("special").toString());
                vocabularyMeaning.setSpecial(specialFloat.intValue());
                vocabularyMeanings.add(vocabularyMeaning);
            }
            result.put(word, vocabularyMeanings);
        }
        br.close();
        return result;
    }
    
    public static List<String> wordsNoImgs() {
        List<String> wordList = Utils.getVocabularyList();
        File rootDir = new File("/Users/xiaoxiao/workspace/resource");
        File[] subDirs = rootDir.listFiles();
        for (File subdir : subDirs) {
            if (subdir.isDirectory()) {
            File[] imgFiles = subdir.listFiles();
                for (File file : imgFiles) {
                    if (!file.getName().startsWith(".")) {
                        String name = file.getName().split("\\.")[0];
                        wordList.remove(name);
                    }
                }
            }
        }
        return wordList;
    }
    
    private static void rename() {
        File rootDir = new File("/Users/xiaoxiao/workspace/resource");
        File[] subDirs = rootDir.listFiles();
        for (File subdir : subDirs) {
            if (subdir.isDirectory()) {
            File[] imgFiles = subdir.listFiles();
                for (File file : imgFiles) {
                    String name = file.getAbsolutePath().replace("+", " ");
                    String[] infos = name.split("-");
                    if (infos.length == 2) {
                        if (!(infos[1].charAt(0) <= '9')) {
                            continue;
                        }
                        File renamedFile = new File(infos[0] + ".jpg");
                        file.renameTo(renamedFile);
                    }
                    if (infos.length == 3) {
                        File renamedFile = new File(infos[0] + "-" + infos[1] + ".jpg");
                        file.renameTo(renamedFile);
                    }
                    if (name.endsWith(".jpg.jpg")) {
                        infos = name.split(".jpg.jpg");
                        File renamedFile = new File(infos[0] + ".jpg");
                        file.renameTo(renamedFile);
                    }
                    if (name.contains(" 2.jpg")) {
                        file.delete();
                    }
                }
            }
        }
    }
    
    public static int imgCountFromGoogle() {
        File rootDir = new File("/Users/xiaoxiao/workspace/resource-google");
        File[] subDirs = rootDir.listFiles();
        int count = 0;
        for (File subdir : subDirs) {
            if (subdir.isDirectory()) {
                File[] imgDirs = subdir.listFiles();
                for (File file : imgDirs) {
                    if (file.isDirectory()) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
    
    public static void meaningNotExist() throws IOException {
        List<String> wordList = Utils.getVocabularyList();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/vocabulary-meaning")));
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            String word = line.split("::")[0];
            if (!wordList.contains(word)) {
                System.out.println(word);
            }
        }
    }
    
    public static void wmMeaningNotExist() throws IOException {
        List<String> wordList = Utils.getVocabularyList();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/vocabulary-mw-meaning")));
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            String word = line.split("\t")[0];
            if (!wordList.contains(word)) {
                System.out.println(word);
            }
        }
    }
    
    public static void deleteExistedImgDir() {
        List<String> imgWords = new ArrayList<String>();
        File rootDir = new File("/Users/xiaoxiao/workspace/resource");
        File[] subDirs = rootDir.listFiles();
        for (File subDir : subDirs) {
            if (subDir.isDirectory()) {
                File[] images = subDir.listFiles();
                for (File file : images) {
                    if (file.getName().endsWith(".jpg")) {
                        imgWords.add(file.getName().split(".jpg")[0]);
                    }
                }
            }
        }
        System.out.println(imgWords);
        File googleRootDir = new File("/Users/xiaoxiao/workspace/resource-google");
        File []alphabeticDirs = googleRootDir.listFiles();
        for (File dir : alphabeticDirs) {
            if (dir.isDirectory()) {
                File[] imageDirs = dir.listFiles();
                for (File file : imageDirs) {
                    if (file.isDirectory()) {
                        String name = file.getName().replace("+", " ");
                        if (imgWords.contains(name)) {
                            deleteDir(file);
                        }
                    }
                }
            }
        }
    }
    
    public static void flawedMeanings() throws IOException {
        BufferedReader br3 = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/summary")));
        String summary = null;
        Pattern p = Pattern.compile("^p\\.");
        
        while ((summary = br3.readLine()) != null) {
            String[] infos = summary.split("\t");
            String spell = infos[0];
            String content = infos[1];
            if (content.contains("") || content.contains("2")) {
                System.out.println(summary);
            }
        }
    }
    
    private static void deleteDir(File dir) {
        File[] files = dir.listFiles();
        for (File file : files) {
            file.delete();
        }
        dir.delete();
    }
    
    public static void longestSummary() throws IOException {
        String line = "";
        String meaning = "";
        BufferedReader br3 = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/summary")));
        String summary = null;
        Pattern p = Pattern.compile("^p\\.");
        
        while ((summary = br3.readLine()) != null) {
            String[] infos = summary.split("\t");
            String spell = infos[0];
            String content = infos[1];
            if (content.length() > meaning.length()) {
                line = spell;
                meaning = content;
            }
        }
        System.out.println(line);
    }
    
    public static void meaningCheck() throws IOException {
        BufferedReader br3 = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/vocabulary-meaning")));
        String line = "";
        Gson gson = new Gson();
        while ((line = br3.readLine()) != null) {
            try {
            List list = gson.fromJson(line.split("::")[1], List.class);
            for (Object object : list) {
                    Map meaning = gson.fromJson(object.toString(), Map.class);
                    if (meaning.get("meaning").toString().endsWith("：")) {
                        System.out.println(line.split("::")[0] + " " + meaning.get("attribute").toString());
                    }
                
            }
            }
            catch (JsonSyntaxException e) {
                System.err.println(line.split("::")[0]);
                System.err.println(e.getMessage());
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        //longestSummary();
        //flawedMeanings();
        //meaningCheck();
//        rename();
//        deleteExistedImgDir();
        List<String> cet4 = getCET4VocabularyList();
        List<String> cet4Other = getCET4OtherVocabulary();
        for (String word : cet4Other) {
            if (!cet4.contains(word)) {
                System.out.println(word);
            }
        }
    }
}
