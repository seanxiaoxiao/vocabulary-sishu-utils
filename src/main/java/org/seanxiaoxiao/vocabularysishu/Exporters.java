package org.seanxiaoxiao.vocabularysishu;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        repoList.add("TOFEL顺序");
        repoList.add("TOFEL逆序");
        repoList.add("TOEFL乱序");
        repoList.add("TOFEL分类");
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
    
    public static void exportTOFEL顺序() {
        List<String> tofelVocabularies = Utils.getTOEFLVocabularyList();
        List<VSList> result = new ArrayList<VSList>();
        int j = 0;
        for (int i = 1; i <= 50; i++) {
            VSList list = new VSList();
            list.setRepoName("TOFEL顺序");
            list.setOrder(i);
            list.setName("TOFEL顺序List" + i);
            for (; j < tofelVocabularies.size() && j < i * tofelVocabularies.size() / 40; j++) {
                list.addVocabulary(tofelVocabularies.get(j));
            }
            result.add(list);
        }
        Gson gson = new Gson();
        System.out.println(gson.toJson(result));
    }
    
    public static void exportTOFEL逆序() {
        List<String> tofelVocabularies = Utils.getTOEFLVocabularyList();
        Collections.reverse(tofelVocabularies);
        List<VSList> result = new ArrayList<VSList>();
        int j = 0;
        for (int i = 1; i <= 50; i++) {
            VSList list = new VSList();
            list.setRepoName("TOFEL逆序");
            list.setOrder(i);
            list.setName("TOFEL逆序List" + i);
            for (; j < tofelVocabularies.size() && j < i * tofelVocabularies.size() / 40; j++) {
                list.addVocabulary(tofelVocabularies.get(j));
            }
            result.add(list);
        }
        Gson gson = new Gson();
        System.out.println(gson.toJson(result));
    }
    
    public static void exportTOFEL乱序() {
        List<String> tofelVocabularies = Utils.getTOEFLVocabularyList();
        Collections.shuffle(tofelVocabularies);
        List<VSList> result = new ArrayList<VSList>();
        int j = 0;
        for (int i = 1; i <= 50; i++) {
            VSList list = new VSList();
            list.setRepoName("TOFEL乱序");
            list.setOrder(i);
            list.setName("TOFEL乱序List" + i);
            for (; j < tofelVocabularies.size() && j < i * tofelVocabularies.size() / 40; j++) {
                list.addVocabulary(tofelVocabularies.get(j));
            }
            result.add(list);
        }
        Gson gson = new Gson();
        System.out.println(gson.toJson(result));
    }
    
    public static void exportTOFEL分类() throws IOException {
        BufferedReader bReader = new BufferedReader(new FileReader("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/tofel-category"));
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
                list.setName("TOFEL分类-" + line);
                list.setRepoName("TOFEL分类");
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
        List<Vocabulary> vocabularies = new ArrayList<Vocabulary>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/vocabulary-info")));
        for (int i = 0; i < 500; i++) {
            String info = br.readLine();
            String[] infos = info.split("\t");
            Vocabulary vocabulary = new Vocabulary();
            if (infos.length == 3) {
                vocabulary.setSpell(infos[0]);
                vocabulary.setPhonetic(infos[1]);
                vocabulary.setEtymology(infos[2]);
            }
            else {
                vocabulary.setSpell(infos[0]);
                vocabulary.setPhonetic(infos[1]);
            }
            vocabularies.add(vocabulary);
        }
        br.close();
        Gson gson = new Gson();
        System.out.println(gson.toJson(vocabularies));
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
        exportGRE逆序();
        exportIELTS乱序();
        exportGRE乱序();
        exportTOFEL分类();
        //exportVocabularyInfo();
        //exportVocabularyMeaning();
    }
}
