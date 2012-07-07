package org.seanxiaoxiao.vocabularysishu;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrabSummary {

    private static List<String> wordList = Utils.getVocabularyList();
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/summary-gre")));
        String line = null;
        Map<String, String> summaryMap = new HashMap<String, String>();
        while ((line = br.readLine()) != null) {
            String[] infos = line.split("\t");
            summaryMap.put(infos[0].trim(), infos[1].trim());
        }
        br.close();
        br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/summary-gmat")));
        while ((line = br.readLine()) != null) {
            String[] infos = line.split(" ");
            if (!summaryMap.containsKey(infos[0].trim())) {
                summaryMap.put(infos[0].trim(), infos[1].trim());
            }
        }
        br.close();
        br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/summary-gre2")));
        while ((line = br.readLine()) != null) {
            String[] infos = line.split("\t");
            if (!summaryMap.containsKey(infos[0].trim())) {
                summaryMap.put(infos[0].trim(), infos[1].trim());
            }
        }
        br.close();
        br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/summary-toefl")));
        while ((line = br.readLine()) != null) {
            String[] infos = line.split(" ");
            String summary = br.readLine();
            if (!summaryMap.containsKey(infos[0].trim())) {
                summaryMap.put(infos[0].trim(), summary.trim());
            }
        }
        br.close();
        br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/xiaoxiao/workspace/vocabulary-sishu-utils/src/main/resources/summary-ielts")));
        while ((line = br.readLine()) != null) {
            String[] infos = line.split(" ");
            String summary = br.readLine();
            if (!summaryMap.containsKey(infos[0])) {
                summaryMap.put(infos[0].trim(), summary.trim());
            }
        }
        br.close();
        int count = 0;
        for (String word : wordList) {
            String summary = summaryMap.get(word);
            if (summary != null && summary.length() > 12) {
            //    System.out.println(word + " " + summary);
            }
        }
        Map<String, List<VocabularyMeaning>> allMeanings = Utils.getMeanings();
        for (String word : wordList) {
            if (!summaryMap.containsKey(word)) {
                count++;
                String meaning = "";
                for (VocabularyMeaning vocabularyMeaning : allMeanings.get(word)) {
                    meaning = meaning + " " + vocabularyMeaning.getAttribute() + vocabularyMeaning.getMeaning();
                }
                System.out.println(word + "," + meaning);
            }
        }
        System.out.println(count);
    }
}
