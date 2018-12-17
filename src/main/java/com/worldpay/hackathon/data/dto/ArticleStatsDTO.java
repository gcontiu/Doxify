package com.worldpay.hackathon.data.dto;

import java.time.LocalDateTime;

public class ArticleStatsDTO implements Comparable {
    public String articleTitle;
    public String articleUrl;
    public int rank;
    public int nrOfLines;
    public int timesRead;
    public Double averageTimeSpent;
    public Double totalCoins;
    public LocalDateTime timestamp;

    @Override
    public int compareTo(Object o) {
        if (o instanceof ArticleStatsDTO) {
            int compare = -Double.compare(this.totalCoins, ((ArticleStatsDTO) o).totalCoins);
            if (compare == 0) {
                return this.timestamp.compareTo(((ArticleStatsDTO) o).timestamp);
            } else {
                return compare;
            }
        } else {
            return 0;
        }
    }
}
