package com.jesse.course.guessthecelebrity;

/**
 * Created by jesse on 7/11/2017.
 */

public class Celebrity {

    private String name;
    private String url;
    private int rank;

    public Celebrity(String name, String url, int rank) {
        this.name = name;
        this.url = url;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
