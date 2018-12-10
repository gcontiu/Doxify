package com.helloworld.data.dto;

public class AuthorStatsDTO implements Comparable {
    public String username;
    public String fullName;
    public int nrOfArticles;
    public double totalCoins;
    public int rank;
    public String mostReadArticleName;
    public String mostReadArticleURL;

    @Override
    public int compareTo(Object o) {
        if (o instanceof AuthorStatsDTO) {
            return -Double.compare(this.totalCoins, ((AuthorStatsDTO) o).totalCoins);
        } else {
            return 0;
        }
    }
}
