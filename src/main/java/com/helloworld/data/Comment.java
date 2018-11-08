package com.helloworld.data;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.*;
import java.util.List;

@Entity
public class Comment {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Article article;

    @Column(unique = true, nullable = false)
    private String contentHash;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommentReadAction> commentReadActions;


    //default constructor needed by JPA and Hibernate
    public Comment() {
    }

    public Comment(Author author, Article article) {
        this.author = author;
        this.article = article;
    }
}
