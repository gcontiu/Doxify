package com.helloworld.data.dto;

public class UserDashboardDTO {

    private Double coinsGained;
    private int totalNumberOfArticles;
    private Long articleId;
    private String articleName;
    private String userFullName;
    private int averageTimeSpentOnArticle;
    private int numberOfLinesPerArticle;
    private Double coinsGainedPerArticle;

    public Double getCoinsGained() {
        return coinsGained;
    }

    public void setCoinsGained(Double coinsGained) {
        this.coinsGained = coinsGained;
    }

    public int getTotalNumberOfArticles() {
        return totalNumberOfArticles;
    }

    public void setTotalNumberOfArticles(int totalNumberOfArticles) {
        this.totalNumberOfArticles = totalNumberOfArticles;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public int getAverageTimeSpentOnArticle() {
        return averageTimeSpentOnArticle;
    }

    public void setAverageTimeSpentOnArticle(int averageTimeSpentOnArticle) {
        this.averageTimeSpentOnArticle = averageTimeSpentOnArticle;
    }

    public int getNumberOfLinesPerArticle() {
        return numberOfLinesPerArticle;
    }

    public void setNumberOfLinesPerArticle(int numberOfLinesPerArticle) {
        this.numberOfLinesPerArticle = numberOfLinesPerArticle;
    }

    public Double getCoinsGainedPerArticle() {
        return coinsGainedPerArticle;
    }

    public void setCoinsGainedPerArticle(Double coinsGainedPerArticle) {
        this.coinsGainedPerArticle = coinsGainedPerArticle;
    }
}
