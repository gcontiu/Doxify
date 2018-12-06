package com.helloworld.data;

import static javax.persistence.GenerationType.IDENTITY;

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

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ArticleReadAction> articleReadActions;


    //default constructor needed by JPA and Hibernate
    public Article() {
    }

    public Article(String title, String url, Author author, boolean isBlackListed) {
        this.title = title;
        this.url = url;
        this.author = author;
        this.isBlackListed = isBlackListed;
        comments = new ArrayList<>();
        articleReadActions = new ArrayList<>();
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

    public Long getId() {
        return id;
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
