package com.helloworld.service;


import com.helloworld.data.Article;
import com.helloworld.data.ArticleReadAction;
import com.helloworld.data.Author;
import com.helloworld.data.Comment;
import com.helloworld.data.CommentReadAction;
import com.helloworld.data.dto.CommentDTO;
import com.helloworld.data.dto.ArticleDTO;
import com.helloworld.data.dto.AuthorStatsDTO;
import com.helloworld.repository.ArticleRepository;
import com.helloworld.repository.AuthorRepository;
import com.helloworld.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Cacheable(cacheNames = "authorStats")
    public List<AuthorStatsDTO> getAllAuthorStats() {
        LOGGER.info("Getting AuthorStats from database...");
        List<AuthorStatsDTO> authorStatsDTOs = new ArrayList<>();

        authorRepository.findAll().forEach(author -> {
            String userName = author.getUserName();
            String fullName = author.getFullName();
            List<Article> articles = author.getArticles();
            int nrOfArticles = articles.size();
            double nrOfCoins = articles.stream()
                    .mapToDouble(article -> {
                        double sum = article.getArticleReadActions().stream()
                                .mapToDouble(ArticleReadAction::getNrOfCoins)
                                .sum();
                        sum += article.getComments().stream()
                                .map(Comment::getCommentReadActions)
                                .mapToDouble(commentReadActionList -> commentReadActionList.stream()
                                        .mapToDouble(CommentReadAction::getNrOfCoins)
                                        .sum())
                                .sum();
                        return sum;
                    }).sum();
            authorStatsDTOs.add(
                    new AuthorStatsDTO(userName, fullName, nrOfArticles, (new BigDecimal(nrOfCoins)).floatValue()));
        });

        LOGGER.info("Calculating Author rankings...");
        authorStatsDTOs.sort(AuthorStatsDTO::compareTo);

        int rank = authorStatsDTOs.size();
        for (AuthorStatsDTO authorStatsDTO : authorStatsDTOs) {
            authorStatsDTO.rank = rank--;
        }
        return authorStatsDTOs;
    }

    private void processArticle(ArticleDTO articleDTO, Boolean isBlackListed) {
        Author author = persistAuthor(articleDTO.author.username, articleDTO.author.fullName);

        Article article = persistArticle(articleDTO.title, author, isBlackListed);

        persistComments(articleDTO.commentList, articleDTO.timeSpentInSeconds, article);

        persistArticleReadAction(articleDTO.nrOfLines, articleDTO.timeSpentInSeconds, article);
    }

    private void persistComments(List<CommentDTO> comments, Float spentTime, Article article) {
        Float commentCoinValue = coinCalculator.calculateNrOfCoinsForComment();
        for (CommentDTO commentDTO : comments) {
            Comment comment = commentRepository.findByContentHash(commentDTO.hash);
            if (comment == null) {
                Author commentAuthor = persistAuthor(commentDTO.user, commentDTO.author);
                comment = new Comment(commentAuthor, article, commentDTO.hash);
                commentAuthor.addComment(comment);
                CommentReadAction commentReadAction = new CommentReadAction(comment, LocalDateTime.now());
                commentReadAction.setNrOfCoins(commentCoinValue);
                commentReadAction.setSecondsSpent(spentTime);
                comment.addCommentReadAction(commentReadAction);
                authorRepository.save(commentAuthor);
            }
        }
    }

    private void persistArticleReadAction(Integer nrOfLines, Float spentTime, Article article) {
        ArticleReadAction articleReadAction = new ArticleReadAction(article, LocalDateTime.now());

        Float nrOfCoins = coinCalculator.calculateNrOfCoinsForArticle(nrOfLines, spentTime);

        articleReadAction.setNrOfCoins(nrOfCoins);
        articleReadAction.setSecondsSpent(spentTime);
        article.addReadAction(articleReadAction);
        articleRepository.save(article);
    }

    private Article persistArticle(String articleTitle, Author author, boolean isBlackListed) {
        Article article = articleRepository.findByTitle(articleTitle);

        if (article == null) {
            article = new Article(articleTitle, author);
            article.setBlackListed(isBlackListed);
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
