package org.seanxiaoxiao.vocabularysishu;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<String> getVocabularyList() {
        try {
            List<String> vocabularyList = new ArrayList<String>();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/vocabularylist")));
            for (String line = null; (line = br.readLine()) != null; ) {
                vocabularyList.add(line.split(",")[0]);
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
    
    public static void main(String[] args) {
        
    }
}
