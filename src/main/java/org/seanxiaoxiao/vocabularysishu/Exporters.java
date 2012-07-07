package org.seanxiaoxiao.vocabularysishu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

public class Exporters {

    private static void exportRepo() {
        List<String> repoList = new ArrayList<String>();
        repoList.add("GRE顺序");
        repoList.add("GRE逆序");
        repoList.add("GRE乱序");
        repoList.add("GRE分类");
        repoList.add("TOEFL顺序");
        repoList.add("TOEFL逆序");
        repoList.add("TOEFL乱序");
        repoList.add("TOEFL分类");
        repoList.add("GMAT顺序");
        repoList.add("GMAT逆序");
        repoList.add("GMAT乱序");
        repoList.add("IELTS顺序");
        repoList.add("IELTS逆序");
        repoList.add("IELTS乱序");
        Gson gson = new Gson();
        System.out.println(gson.toJson(repoList));
    }
    
    private static void exportGRE顺序() {
        List<String> greVocabularies = Utils.getGREVocabularyList();
        List<VSList> result = new ArrayList<VSList>();
        int j = 0;
        for (int i = 1; i <= 50; i++) {
            VSList list = new VSList();
            list.setRepoName("GRE顺序");
            list.setOrder(i);
            list.setName("GRE顺序List" + i);
            for (; j < greVocabularies.size() && j < i * greVocabularies.size() / 50; j++) {
                list.addVocabulary(greVocabularies.get(j));
            }
            result.add(list);
        }
        Gson gson = new Gson();
        System.out.println(gson.toJson(result));
    }
    
    private static void exportGRE逆序() {
        List<String> greVocabularies = Utils.getGREVocabularyList();
        Collections.reverse(greVocabularies);
        List<VSList> result = new ArrayList<VSList>();
        int j = 0;
        for (int i = 1; i <= 50; i++) {
            VSList list = new VSList();
            list.setRepoName("GRE逆序");
            list.setOrder(i);
            list.setName("GRE逆序List" + i);
            for (; j < greVocabularies.size() && j < i * greVocabularies.size() / 50; j++) {
                list.addVocabulary(greVocabularies.get(j));
            }
            result.add(list);
        }
        Gson gson = new Gson();
        System.out.println(gson.toJson(result));
    }
    
    public static void exportGRE乱序() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/GRE Random")));
        String line = null;
        List<VSList> result = new ArrayList<VSList>();
        VSList vsList = null;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("Word List")) {
                vsList = new VSList();
                result.add(vsList);
                int order = Integer.valueOf(line.split(" ")[2]);
                vsList.setOrder(order);
                vsList.setRepoName("GRE乱序");
                vsList.setName("GRE乱序List" + order);
            }
            else {
                if (line != null) {
                    vsList.addVocabulary(line.trim());
                }
            }
        }
        Gson gson = new Gson();
        System.out.println(gson.toJson(result));
    }
    
    public static void exportGRE分类() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/gre-category")));
        String line = null;
        List<String> words = Utils.getGREVocabularyList();
        List<VSList> result = new ArrayList<VSList>();
        VSList vsList = null;
        int count = 1;
        while ((line = br.readLine()) != null) {
            String[] infos = line.split(" ");
            if (infos.length == 1) {
                vsList = new VSList();
                result.add(vsList);
                int order = Integer.valueOf(count++);
                vsList.setOrder(order);
                vsList.setRepoName("GRE分类");
                vsList.setName(line.trim());
            }
            else {
                if (line != null) {
                    if (!words.contains(infos[0].trim())) {
                        //System.out.println(line);
                        //System.err.println(infos[0].trim());
                    }
                    else {
                        vsList.addVocabulary(infos[0].trim());
                    }
                }
            }
        }
        Gson gson = new Gson();
        System.out.println(gson.toJson(result));
    }
    
    public static void exportTOEFL顺序() {
        List<String> TOEFLVocabularies = Utils.getTOEFLVocabularyList();
        List<VSList> result = new ArrayList<VSList>();
        int j = 0;
        for (int i = 1; i <= 50; i++) {
            VSList list = new VSList();
            list.setRepoName("TOEFL顺序");
            list.setOrder(i);
            list.setName("TOEFL顺序List" + i);
            for (; j < TOEFLVocabularies.size() && j < i * TOEFLVocabularies.size() / 40; j++) {
                list.addVocabulary(TOEFLVocabularies.get(j));
            }
            result.add(list);
        }
        Gson gson = new Gson();
        System.out.println(gson.toJson(result));
    }
    
    public static void exportTOEFL逆序() {
        List<String> TOEFLVocabularies = Utils.getTOEFLVocabularyList();
        Collections.reverse(TOEFLVocabularies);
        List<VSList> result = new ArrayList<VSList>();
        int j = 0;
        for (int i = 1; i <= 50; i++) {
            VSList list = new VSList();
            list.setRepoName("TOEFL逆序");
            list.setOrder(i);
            list.setName("TOEFL逆序List" + i);
            for (; j < TOEFLVocabularies.size() && j < i * TOEFLVocabularies.size() / 40; j++) {
                list.addVocabulary(TOEFLVocabularies.get(j));
            }
            result.add(list);
        }
        Gson gson = new Gson();
        System.out.println(gson.toJson(result));
    }
    
    public static void exportTOEFL乱序() {
        List<String> TOEFLVocabularies = Utils.getTOEFLVocabularyList();
        Collections.shuffle(TOEFLVocabularies);
        List<VSList> result = new ArrayList<VSList>();
        int j = 0;
        for (int i = 1; i <= 50; i++) {
            VSList list = new VSList();
            list.setRepoName("TOEFL乱序");
            list.setOrder(i);
            list.setName("TOEFL乱序List" + i);
            for (; j < TOEFLVocabularies.size() && j < i * TOEFLVocabularies.size() / 40; j++) {
                list.addVocabulary(TOEFLVocabularies.get(j));
            }
            result.add(list);
        }
        Gson gson = new Gson();
        System.out.println(gson.toJson(result));
    }
    
    public static void exportTOEFL分类() throws IOException {
        BufferedReader bReader = new BufferedReader(new FileReader("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/TOEFL-category"));
        List<VSList> result = new ArrayList<VSList>();
        String line = null;
        VSList list = null;
        int order = 0;
        while ((line = bReader.readLine()) != null) {
            Pattern pattern = Pattern.compile("[\u4E00-\u9FA5]*");
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                order++;
                list = new VSList();
                list.setName("TOEFL分类-" + line);
                list.setRepoName("TOEFL分类");
                list.setOrder(order);
                result.add(list);
            }
            else {
                list.addVocabulary(line);
            }
        }
        Gson gson = new Gson();
        System.out.println(gson.toJson(result));
    }

    public static void exportGMAT顺序() {
        List<String> gmatVocabularyList = Utils.getGMATVocabularyList();
        List<VSList> result = new ArrayList<VSList>();
        int j = 0;
        for (int i = 1; i <= 50; i++) {
            VSList list = new VSList();
            list.setRepoName("GMAT顺序");
            list.setOrder(i);
            list.setName("GMAT顺序List" + i);
            for (; j < gmatVocabularyList.size() && j < i * gmatVocabularyList.size() / 40; j++) {
                list.addVocabulary(gmatVocabularyList.get(j));
            }
            result.add(list);
        }
        Gson gson = new Gson();
        System.out.println(gson.toJson(result));
    }
    
    public static void exportGMAT逆序() {
        List<String> gmatVocabularyList = Utils.getGMATVocabularyList();
        Collections.reverse(gmatVocabularyList);
        List<VSList> result = new ArrayList<VSList>();
        int j = 0;
        for (int i = 1; i <= 50; i++) {
            VSList list = new VSList();
            list.setRepoName("GMAT逆序");
            list.setOrder(i);
            list.setName("GMAT逆序List" + i);
            for (; j < gmatVocabularyList.size() && j < i * gmatVocabularyList.size() / 40; j++) {
                list.addVocabulary(gmatVocabularyList.get(j));
            }
            result.add(list);
        }
        Gson gson = new Gson();
        System.out.println(gson.toJson(result));
    }
    
    public static void exportGMAT乱序() {
        List<String> gmatVocabularyList = Utils.getGMATVocabularyList();
        Collections.shuffle(gmatVocabularyList);
        List<VSList> result = new ArrayList<VSList>();
        int j = 0;
        for (int i = 1; i <= 50; i++) {
            VSList list = new VSList();
            list.setRepoName("GMAT乱序");
            list.setOrder(i);
            list.setName("GMAT乱序List" + i);
            for (; j < gmatVocabularyList.size() && j < i * gmatVocabularyList.size() / 40; j++) {
                list.addVocabulary(gmatVocabularyList.get(j));
            }
            result.add(list);
        }
        Gson gson = new Gson();
        System.out.println(gson.toJson(result));
    }
    
    public static void exportIELTS顺序() {
        List<String> ieltsVocabularyList = Utils.getIELTSVocabularyList();
        List<VSList> result = new ArrayList<VSList>();
        int j = 0;
        for (int i = 1; i <= 50; i++) {
            VSList list = new VSList();
            list.setRepoName("IELTS顺序");
            list.setOrder(i);
            list.setName("IELTS顺序List" + i);
            for (; j < ieltsVocabularyList.size() && j < i * ieltsVocabularyList.size() / 40; j++) {
                list.addVocabulary(ieltsVocabularyList.get(j));
            }
            result.add(list);
        }
        Gson gson = new Gson();
        System.out.println(gson.toJson(result));
    }
    
    public static void exportIELTS逆序() {
        List<String> ieltsVocabularyList = Utils.getIELTSVocabularyList();
        Collections.reverse(ieltsVocabularyList);
        List<VSList> result = new ArrayList<VSList>();
        int j = 0;
        for (int i = 1; i <= 50; i++) {
            VSList list = new VSList();
            list.setRepoName("IELTS逆序");
            list.setOrder(i);
            list.setName("IELTS逆序List" + i);
            for (; j < ieltsVocabularyList.size() && j < i * ieltsVocabularyList.size() / 40; j++) {
                list.addVocabulary(ieltsVocabularyList.get(j));
            }
            result.add(list);
        }
        Gson gson = new Gson();
        System.out.println(gson.toJson(result));
    }
    
    public static void exportIELTS乱序() {
        List<String> ieltsVocabularyList = Utils.getIELTSVocabularyList();
        Collections.shuffle(ieltsVocabularyList);
        List<VSList> result = new ArrayList<VSList>();
        int j = 0;
        for (int i = 1; i <= 50; i++) {
            VSList list = new VSList();
            list.setRepoName("IELTS乱序");
            list.setOrder(i);
            list.setName("IELTS乱序List" + i);
            for (; j < ieltsVocabularyList.size() && j < i * ieltsVocabularyList.size() / 40; j++) {
                list.addVocabulary(ieltsVocabularyList.get(j));
            }
            result.add(list);
        }
        Gson gson = new Gson();
        System.out.println(gson.toJson(result));
    }
    
    private static void exportVocabularyInfo() throws IOException {
        String info = null;
        Map<String, Vocabulary> vocabularyMap = new HashMap<String, Vocabulary>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/vocabulary-info")));
        while ((info = br.readLine()) != null) {
            if (info.trim().equals("")) {
                continue;
            }
            String[] infos = info.split("\t");
            Vocabulary vocabulary = new Vocabulary();
            if (infos.length == 3) {
                vocabulary.setSpell(infos[0].trim());
                vocabulary.setPhonetic(infos[1].trim());
                vocabulary.setEtymology(infos[2].trim());
            }
            else {
                vocabulary.setSpell(infos[0]);
                vocabulary.setPhonetic("");
                vocabulary.setEtymology("");
            }
            vocabularyMap.put(vocabulary.getSpell(), vocabulary);
        }

        BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/vocabularylist")));
        String line = null;
        List<Vocabulary> vocabularies = new ArrayList<Vocabulary>();
        while ((line = br1.readLine()) != null) {
            String[] infos = line.split(",");
            String spell = infos[0];
            int type = Integer.parseInt(infos[1]);
            Vocabulary vocabulary = vocabularyMap.get(spell);
            if (vocabulary != null) {
                vocabulary.setType(type);
                vocabularies.add(vocabulary);
            }
            else {
                vocabulary = new Vocabulary();
                vocabulary.setSpell(spell);
                vocabulary.setType(type);
                vocabularyMap.put(spell, vocabulary);
                vocabularies.add(vocabulary);
            }
        }
        BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/vocabulary-audio")));
        String audioString = null;
        while ((audioString = br2.readLine()) != null) {
            String[] infos = audioString.split(",");
            String spell = infos[0];
            String link = infos[1];
            if (!link.equals("null")) {
                Vocabulary vocabulary = vocabularyMap.get(spell);
                if (vocabulary == null) {
                    System.out.println(spell);
                }
                vocabulary.setAudioLink(link);
            }
        }
        br1.close();
        br.close();
        br2.close();
        Gson gson = new Gson();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/Vocabularies.txt")));
        for (Vocabulary vocabulary : vocabularies) {
            bw.append(gson.toJson(vocabulary)).append("\n");
        }
        bw.close();
    }

    private static void exportVocabularyMeaning() throws IOException {
        Map<String, List<VocabularyMeaning>> vocabulariesMeaningMap = new HashMap<String, List<VocabularyMeaning>>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/vocabulary-meaning")));
        for (int i = 0; i < 500; i++) {
            String info = br.readLine();
            String[] infos = info.split("\t");
            List<VocabularyMeaning> meanings = new ArrayList<VocabularyMeaning>();
            for (int j = 1; j < infos.length; j++) {
                VocabularyMeaning meaning = new VocabularyMeaning();
                String[] meaningInfo = infos[j].split(":: ");
                meaning.setAttribute(meaningInfo[0]);
                meaning.setMeaning(meaningInfo[1]);
                meanings.add(meaning);
            }
            vocabulariesMeaningMap.put(infos[0], meanings);
        }
        br.close();
        Gson gson = new Gson();
        System.out.println(gson.toJson(vocabulariesMeaningMap));
    }
    
    public static void main(String[] args) throws IOException {
        //exportRepo();
        //exportGRE顺序();
        //exportIELTS乱序();
        //exportGRE乱序();
        exportGRE分类();
        //exportVocabularyInfo();
        //exportVocabularyMeaning();
    }
}
