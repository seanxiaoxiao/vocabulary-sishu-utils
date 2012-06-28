package org.seanxiaoxiao.vocabularysishu;

import java.util.ArrayList;
import java.util.List;

public class VSList {

    private String name;

    private int order;

    private String repoName;

    private List<String> vocabularyList = new ArrayList<String>();
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getRepoName() {
        return repoName;
    }
    
    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }
    
    public int getOrder() {
        return order;
    }
    
    public void setOrder(int order) {
        this.order = order;
    }
    
    public List<String> getVocabularyList() {
        return vocabularyList;
    }
    
    public void addVocabulary(String vocabulary) {
        vocabularyList.add(vocabulary);
    }
    
}
