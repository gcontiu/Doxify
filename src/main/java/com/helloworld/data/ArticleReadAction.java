package com.helloworld.data;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class ArticleReadAction {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Article article;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Float secondsSpent;

    @Column(nullable = false)
    private Float nrOfCoins;

    public ArticleReadAction() {
    }

    public ArticleReadAction(Article article, LocalDateTime timestamp) {
        this.article = article;
        this.timestamp = timestamp;
    }

    public void setSecondsSpent(Float secondsSpent) {
        this.secondsSpent = secondsSpent;
    }

    public void setNrOfCoins(Float nrOfCoins) {
        this.nrOfCoins = nrOfCoins;
    }

    public Float getNrOfCoins() {
        return nrOfCoins;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Float getSecondsSpent() {
        return secondsSpent;
    }
}
