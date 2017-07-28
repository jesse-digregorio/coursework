package com.jesse.course.newsreader.domain;

public class NewsReaderApi extends BaseService<NewsReaderService> {

    public static final String baseUrl = "https://hacker-news.firebaseio.com/";

    public NewsReaderApi() {
        super(NewsReaderService.class, baseUrl);
    }
}
