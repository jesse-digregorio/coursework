package com.jesse.course.newsreader.domain;

public class NewsReaderApi extends BaseService<NewsReaderService> {

    public static final String BASE_URL = "https://hacker-news.firebaseio.com/";

    public NewsReaderApi() {
        super(NewsReaderService.class, BASE_URL);
    }
}
