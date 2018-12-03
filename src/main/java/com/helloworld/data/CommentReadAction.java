package com.helloworld.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class CommentReadAction {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private Comment comment;

    @Column(nullable = false)
    private LocalDateTime timeStamp;

    @Column(nullable = false)
    private Float secondsSpent;

    @Column(nullable = false)
    private Float nrOfCoins;

    public CommentReadAction(){

    }

    public CommentReadAction(Comment comment, LocalDateTime timeStamp) {
        this.comment = comment;
        this.timeStamp = timeStamp;
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
}
