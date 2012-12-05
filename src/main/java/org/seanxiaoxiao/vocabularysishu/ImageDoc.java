package org.seanxiaoxiao.vocabularysishu;


import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.IOException;  
import java.io.InputStream;  

import org.apache.poi.hwpf.extractor.WordExtractor;  

public class ImageDoc {
    public static void main(String[] args) {
        readDocPOI("/Users/xiaoxiao/Downloads/grepic.doc");
    }

    public static void readDocPOI(String docPath) {
        try {
            InputStream inputStrem = new FileInputStream(docPath);
            WordExtractor extractor = new WordExtractor(inputStrem);
            inputStrem.close();
            System.out.println(extractor.getText());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
