package org.seanxiaoxiao.vocabularysishu;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;

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
    
    public static void main(String[] args) throws IOException {
        getMeanings();
    }
}
