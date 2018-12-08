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
    private Double secondsSpent;

    @Column(nullable = false)
    private Double nrOfCoins;

    public CommentReadAction() {

    }

    public CommentReadAction(Comment comment, LocalDateTime timeStamp) {
        this.comment = comment;
        this.timeStamp = timeStamp;
    }

    public void setSecondsSpent(Double secondsSpent) {
        this.secondsSpent = secondsSpent;
    }

    public void setNrOfCoins(Double nrOfCoins) {
        this.nrOfCoins = nrOfCoins;
    }

    public Double getNrOfCoins() {
        return nrOfCoins;
    }
}
