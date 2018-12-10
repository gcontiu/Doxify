package com.helloworld.data.dto;

import java.util.List;

public class ArticleDTO {
    public Double timeSpentInSeconds;
    public Integer nrOfLines;
    public AuthorDTO author;
    public String title;
    public String url;
    public String category;
    public List<CommentDTO> commentList;
}
