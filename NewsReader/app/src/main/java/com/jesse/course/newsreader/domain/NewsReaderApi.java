package com.jesse.course.newsreader.domain;

public class NewsReaderApi extends BaseService<NewsReaderService> {

    public NewsReaderApi(String baseUrl) {
        super(NewsReaderService.class, baseUrl);
    }
}
