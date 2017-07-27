package com.jesse.course.newsreader.domain;

import com.jesse.course.newsreader.model.Article;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NewsReaderService {

    @GET("v0/topstories.json?print=pretty")
    Flowable<List<Integer>> getRecentArticlesList();

    @GET("v0/item/{articleId}.json?print=pretty")
    Flowable<Article> getArticle(@Path("articleId") int articleId);

}
