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
import javax.persistence.OneToMany;

@Entity
public class Author {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userName;

    private String fullName;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Article> articles;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;


    //default constructor needed by JPA and Hibernate
    public Author() {
    }

    public Author(String userName) {
        this.userName = userName;
        articles = new ArrayList<>();
        comments = new ArrayList<>();
    }

    public String getUserName() {
        return userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void addArticle(Article article) {
        articles.add(article);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public List<Article> getArticles() {
        return new ArrayList<>(articles);
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
