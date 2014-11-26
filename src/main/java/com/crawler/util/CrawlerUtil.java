package com.crawler.util;

public class CrawlerUtil {

    public static void sleep(Long mills){
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
