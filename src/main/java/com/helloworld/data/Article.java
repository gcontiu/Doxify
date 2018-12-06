package com.helloworld.data;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

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

    @Column(nullable = false)
    private boolean isBlackListed;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ArticleReadAction> articleReadActions;

    // default constructor needed by JPA and Hibernate
    public Article() {
    }

    public Article(String title, Author author) {
        this.title = title;
        this.author = author;
        comments = new ArrayList<>();
        articleReadActions = new ArrayList<>();
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

    public String getTitle() {
        return title;
    }

    public List<ArticleReadAction> getArticleReadActions() {
        return articleReadActions;
    }

}
