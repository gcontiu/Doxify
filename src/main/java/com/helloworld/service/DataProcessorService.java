package com.helloworld.service;


import com.helloworld.data.Article;
import com.helloworld.data.ArticleReadAction;
import com.helloworld.data.Author;
import com.helloworld.data.dto.DocumentationDetailsDTO;
import com.helloworld.repository.ArticleReadActionRepository;
import com.helloworld.repository.ArticleRepository;
import com.helloworld.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DataProcessorService {

    private final AuthorRepository authorRepository;
    private final ArticleRepository articleRepository;
    private final CoinCalculator coinCalculator;
    private final ArticleReadActionRepository articleReadActionRepository;

    @Autowired
    public DataProcessorService(AuthorRepository authorRepository, ArticleRepository articleRepository, CoinCalculator coinCalculator,
                                ArticleReadActionRepository articleReadActionRepository) {
        this.authorRepository = authorRepository;
        this.articleRepository = articleRepository;
        this.coinCalculator = coinCalculator;
        this.articleReadActionRepository = articleReadActionRepository;
    }

    public void processDocumentDetails(DocumentationDetailsDTO documentationDetailsDTO) {

        Author author = persistAuthor(documentationDetailsDTO);

        Article article = persistArticle(documentationDetailsDTO, author);

        persistReadAction(documentationDetailsDTO, article);

    }

    private void persistReadAction(DocumentationDetailsDTO documentationDetailsDTO, Article article) {
        ArticleReadAction articleReadAction = new ArticleReadAction(article, LocalDateTime.now());

        Float nrOfCoins = coinCalculator.calculateNrOfCoinsForArticle(documentationDetailsDTO.nrOfLines, documentationDetailsDTO.spentTimeInSeconds);

        articleReadAction.setNrOfCoins(nrOfCoins);
        articleReadAction.setSecondsSpent(documentationDetailsDTO.spentTimeInSeconds);
        articleReadActionRepository.save(articleReadAction);
    }

    private Article persistArticle(DocumentationDetailsDTO documentationDetailsDTO, Author author) {
        Article article = articleRepository.findByTitle(documentationDetailsDTO.articleTitle);

        if (article == null) {
            article = new Article(documentationDetailsDTO.articleTitle, author);
            articleRepository.save(article);
        }
        return article;
    }

    private Author persistAuthor(DocumentationDetailsDTO documentationDetailsDTO) {
        Author author = authorRepository.findByUserName(documentationDetailsDTO.author);

        if (author == null) {
            author = new Author(documentationDetailsDTO.author);
            author.setFullName(documentationDetailsDTO.authorName);
            authorRepository.save(author);
        }
        return author;
    }
}
