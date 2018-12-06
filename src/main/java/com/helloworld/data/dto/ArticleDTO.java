package com.helloworld.data.dto;

import java.util.List;

public class ArticleDTO {
    public Float timeSpentInSeconds;
    public Integer nrOfLines;
    public AuthorDTO author;
    public String title;
    public List<CommentDTO> commentList;

    public Float getTimeSpentInSeconds() {
        return timeSpentInSeconds;
    }

    public void setTimeSpentInSeconds(Float timeSpentInSeconds) {
        this.timeSpentInSeconds = timeSpentInSeconds;
    }

    public Integer getNrOfLines() {
        return nrOfLines;
    }

    public void setNrOfLines(Integer nrOfLines) {
        this.nrOfLines = nrOfLines;
    }

    public AuthorDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDTO author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CommentDTO> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentDTO> commentList) {
        this.commentList = commentList;
    }

}
