package com.helloworld.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CoinCalculator {

    private static final int DECIMAL_PLACE = 2;

    @Value("${coins.baseValue}")
    private Double baseValue;

    @Value("${coins.numberOfLinesPercentage}")
    private Double numberOfLinesPercentage;

    @Value("${coins.timeSpentPercentage}")
    private Double timeSpentPercentage;

    @Value("${coins.commentPercentage}")
    private Double commentPercentage;

    public Double calculateNrOfCoinsForArticle(Integer numberOfLines, Double timeSpent) {
        return round(numberOfLines * numberOfLinesPercentage + timeSpent * timeSpentPercentage + baseValue);
    }

    public Double calculateNrOfCoinsForComment() {
        return round(commentPercentage * baseValue);
    }

    public double round(double number) {
        BigDecimal bd = BigDecimal.valueOf(number);
        bd = bd.setScale(DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }
}
