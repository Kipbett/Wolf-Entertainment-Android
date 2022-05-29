package com.wolf.wolfentertainment.model;

public class MovieModel {
    String poster, title, year, type;

    public MovieModel(String poster, String title,  String year, String type) {
        this.poster = poster;
        this.title = title;
        this.year = year;
        this.type = type;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String language) {
        this.type = type;
    }
}
