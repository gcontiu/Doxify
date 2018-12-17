package com.worldpay.hackathon.service;

import java.time.LocalDateTime;
import java.util.List;

import com.worldpay.hackathon.repository.AuthorRepository;
import com.worldpay.hackathon.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.worldpay.hackathon.data.Article;
import com.worldpay.hackathon.data.ArticleReadAction;
import com.worldpay.hackathon.data.Author;
import com.worldpay.hackathon.data.Comment;
import com.worldpay.hackathon.data.CommentReadAction;
import com.worldpay.hackathon.data.dto.ArticleDTO;
import com.worldpay.hackathon.data.dto.CommentDTO;
import com.worldpay.hackathon.repository.ArticleRepository;

@Service
public class DataProcessorService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DataProcessorService.class);

    private final AuthorRepository authorRepository;
    private final ArticleRepository articleRepository;
    private final CoinCalculator coinCalculator;
    private final CommentRepository commentRepository;

    @Autowired
    public DataProcessorService(AuthorRepository authorRepository, ArticleRepository articleRepository, CoinCalculator coinCalculator,
                                CommentRepository commentRepository) {
        this.authorRepository = authorRepository;
        this.articleRepository = articleRepository;
        this.coinCalculator = coinCalculator;
        this.commentRepository = commentRepository;
    }

    public long countAllAuthors() {
        return authorRepository.count();
    }

    public long countAllArticles() {
        return articleRepository.count();
    }

    public void processArticleDetails(ArticleDTO articleDTO) {

        processArticle(articleDTO, Boolean.FALSE);
    }

    public void processBlackListedArticle(ArticleDTO articleDTO) {

        processArticle(articleDTO, Boolean.TRUE);
    }

    private void processArticle(ArticleDTO articleDTO, Boolean isBlackListed) {
        Author author = persistAuthor(articleDTO.author.username, articleDTO.author.fullName);

        Article article = persistArticle(articleDTO.title, articleDTO.url, author, isBlackListed, articleDTO.category, articleDTO.nrOfLines);

        persistComments(articleDTO.commentList, articleDTO.timeSpentInSeconds, article);

        persistArticleReadAction(articleDTO.nrOfLines, articleDTO.timeSpentInSeconds, article, isBlackListed);
    }

    private void persistComments(List<CommentDTO> comments, Double spentTime, Article article) {
        Double commentCoinValue = coinCalculator.calculateNrOfCoinsForComment();
        for (CommentDTO commentDTO : comments) {
            Comment comment = commentRepository.findByContentHash(commentDTO.hash);
            if (comment == null) {
                Author commentAuthor = persistAuthor(commentDTO.user, commentDTO.author);
                comment = new Comment(commentAuthor, article, commentDTO.hash);
                commentAuthor.addComment(comment);
                if (!article.isBlackListed()) {
                    CommentReadAction commentReadAction = new CommentReadAction(comment, LocalDateTime.now());
                    commentReadAction.setNrOfCoins(commentCoinValue);
                    commentReadAction.setSecondsSpent(spentTime);
                    comment.addCommentReadAction(commentReadAction);
                }
                authorRepository.save(commentAuthor);
            }
        }
    }

    private void persistArticleReadAction(Integer nrOfLines, Double spentTime, Article article, boolean isBlacklisted) {
        ArticleReadAction articleReadAction = new ArticleReadAction(article, LocalDateTime.now());

        Double nrOfCoins;
        if(isBlacklisted) {
            nrOfCoins = 0.0;
        } else {
            nrOfCoins = coinCalculator.calculateNrOfCoinsForArticle(nrOfLines, spentTime);
        }

        articleReadAction.setNrOfCoins(nrOfCoins);
        articleReadAction.setSecondsSpent(spentTime);
        article.addReadAction(articleReadAction);
        articleRepository.save(article);
    }

    private Article persistArticle(String articleTitle, String url, Author author, boolean isBlackListed, String category, int nrOfLines) {
        Article article = articleRepository.findByTitle(articleTitle);

        if (article == null) {
            article = new Article(articleTitle, url, author, isBlackListed, category, nrOfLines);
            articleRepository.save(article);
        }
        return article;
    }

    private Author persistAuthor(String username, String fullName) {
        Author author = authorRepository.findByUserName(username);

        if (author == null) {
            author = new Author(username);
            author.setFullName(fullName);
            authorRepository.save(author);
        }
        return author;
    }
}
