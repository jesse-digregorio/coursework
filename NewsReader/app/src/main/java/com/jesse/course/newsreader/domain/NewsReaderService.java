package com.jesse.course.newsreader.domain;

import com.jesse.course.newsreader.model.Article;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NewsReaderService {

    @GET("v0/topstories.json?print=pretty")
    List<Integer> getRecentArticlesList();

    @GET("v0/item/{articleId}.json?print=pretty")
    Article getArticle(@Path("articleId") int articleId);

}
