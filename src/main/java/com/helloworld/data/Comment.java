package com.helloworld.data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

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

    public Comment(Author author, Article article, String contentHash) {
        this.author = author;
        this.article = article;
        this.contentHash = contentHash;
        commentReadActions = new ArrayList<>();
    }

    public void addCommentReadAction(CommentReadAction commentReadAction) {
        commentReadActions.add(commentReadAction);
    }

    public List<CommentReadAction> getCommentReadActions() {
        return new ArrayList<>(commentReadActions);
    }
}
