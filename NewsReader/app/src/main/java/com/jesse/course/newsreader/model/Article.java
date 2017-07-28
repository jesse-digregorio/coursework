package com.jesse.course.newsreader.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Article {

    @SerializedName("by") // Ask me why I did this! I'd love to explain it further :)
    public String author;
    public int descendants;
    public int id;
    public List<Integer> kids;
    public int score;
    public long time;
    public String title;
    public String type;
    public String url;

}
