package com.helloworld.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CoinCalculator {

    private static final int decimalPlace = 2;

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

    public Float calculateNrOfCoinsForComment(){
        return round(commentPercentage * baseValue);
    }

    private float round(float number) {
        BigDecimal bd = new BigDecimal(number);
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
