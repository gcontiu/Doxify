package com.helloworld.data;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class Comment {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @PrimaryKeyJoinColumn
    private Author author;

    @ManyToOne
    @PrimaryKeyJoinColumn
    private Article article;


    //default constructor needed by JPA and Hibernate
    public Comment() {
    }

    public Comment(Author author, Article article) {
        this.author = author;
        this.article = article;
    }
}
