package com.helloworld.data;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class Article {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @PrimaryKeyJoinColumn
    private Author author;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(unique = true, nullable = false)
    private String url;

    @Column(nullable = false)
    private boolean isBlackListed;

    private LocalDateTime timeStamp;

    private String category;

    private int nrOfLines;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ArticleReadAction> articleReadActions;

    // default constructor needed by JPA and Hibernate
    public Article() {
        timeStamp = LocalDateTime.now();
    }

    public Article(String title, String url, Author author, boolean isBlackListed, String category, int nrOfLines) {
        this.title = title;
        this.url = url;
        this.author = author;
        this.isBlackListed = isBlackListed;
        this.category = category;
        this.nrOfLines = nrOfLines;
        comments = new ArrayList<>();
        articleReadActions = new ArrayList<>();
        timeStamp = LocalDateTime.now();
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void addReadAction(ArticleReadAction articleReadAction) {
        articleReadActions.add(articleReadAction);
    }

    public void setBlackListed(boolean blackListed) {
        isBlackListed = blackListed;
    }

    public boolean isBlackListed() {
        return isBlackListed;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public String getCategory() {
        return category;
    }

    public int getNrOfLines() {
        return nrOfLines;
    }

    public Author getAuthor() {
        return author;
    }

    public List<Comment> getComments() {
        return new ArrayList<>(comments);
    }

    public List<ArticleReadAction> getArticleReadActions() {
        return new ArrayList<>(articleReadActions);
    }
}
