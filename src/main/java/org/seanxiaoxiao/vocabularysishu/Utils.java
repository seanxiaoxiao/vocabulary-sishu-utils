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
                vocabularyList.add(line);
            }
            return vocabularyList;
        } catch (Exception e) {
            return null;
        }
    }
}
