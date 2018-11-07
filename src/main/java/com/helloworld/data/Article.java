package com.helloworld.data;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
    private String name;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ReadAction> readActions;


    //default constructor needed by JPA and Hibernate
    public Article() {
    }

    public Article(String name, Author author) {
        this.name = name;
        this.author = author;
        comments = new ArrayList<>();
        readActions = new ArrayList<>();
    }


    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void addReadAction(ReadAction readAction) {
        readActions.add(readAction);
    }
}
