package com.helloworld.data.dto;


import java.util.List;

public class ArticleDTO {

    public Float timeSpentInSeconds;
    public Integer nrOfLines;
    public AuthorDTO author;
    public String title;
    public String url;
    public List<CommentDTO> commentList;
}
