package com.helloworld.data.dto;

public class AuthorStatsDTO implements Comparable {
    public String username;
    public String fullName;
    public int nrOfArticles;
    public float totalCoins;
    public int rank;

    public AuthorStatsDTO(String username, String fullName, int nrOfArticles, float totalCoins) {
        this.username = username;
        this.fullName = fullName;
        this.nrOfArticles = nrOfArticles;
        this.totalCoins = totalCoins;
    }


    @Override
    public int compareTo(Object o) {
        if (o instanceof AuthorStatsDTO) {
            return Float.compare(this.totalCoins, ((AuthorStatsDTO) o).totalCoins);
        } else {
            return 0;
        }
    }
}