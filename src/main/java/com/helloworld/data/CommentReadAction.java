package com.helloworld.data;

import javax.persistence.*;
import java.util.Date;

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
    private Date timeStamp;

    @Column(nullable = false)
    private Integer secondsSpent;

    @Column(nullable = false)
    private Float nrOfCoins;
}
