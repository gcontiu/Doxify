package com.helloworld.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CoinCalculator {

    private static final int DECIMAL_PLACE = 2;

    @Value("${coins.baseValue}")
    private Float baseValue;

    @Value("${coins.numberOfLinesPercentage}")
    private Float numberOfLinesPercentage;

    @Value("${coins.timeSpentPercentage}")
    private Float timeSpentPercentage;

    @Value("${coins.commentPercentage}")
    private Float commentPercentage;

    public Float calculateNrOfCoinsForArticle(Integer numberOfLines, Float timeSpent) {
        return round(numberOfLines * numberOfLinesPercentage + timeSpent * timeSpentPercentage + baseValue);
    }

    public Float calculateNrOfCoinsForComment() {
        return round(commentPercentage * baseValue);
    }

    private float round(float number) {
        BigDecimal bd = BigDecimal.valueOf(number);
        bd = bd.setScale(DECIMAL_PLACE, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
