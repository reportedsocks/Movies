package com.reportedsocks.movies.model;

public class Movie {

    private String titleText;
    private String yearText;
    private String posterURL;
    private String imdbId;

    public Movie(String titleText, String yearText, String posterURL, String imdbId) {
        this.titleText = titleText;
        this.yearText = yearText;
        this.posterURL = posterURL;
        this.imdbId = imdbId;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getYearText() {
        return yearText;
    }

    public void setYearText(String yearText) {
        this.yearText = yearText;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }
}
