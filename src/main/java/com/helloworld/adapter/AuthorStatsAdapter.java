package com.helloworld.adapter;

import com.helloworld.data.Article;
import com.helloworld.data.ArticleReadAction;
import com.helloworld.data.Comment;
import com.helloworld.data.CommentReadAction;
import com.helloworld.data.dto.AuthorStatsDTO;
import com.helloworld.repository.AuthorRepository;
import com.helloworld.service.CoinCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorStatsAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorStatsAdapter.class);
    private final AuthorRepository authorRepository;
    private final CoinCalculator coinCalculator;

    @Autowired
    public AuthorStatsAdapter(AuthorRepository authorRepository, CoinCalculator coinCalculator) {
        this.authorRepository = authorRepository;
        this.coinCalculator = coinCalculator;
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
            nrOfCoins = coinCalculator.round(nrOfCoins);

            Optional<Article> mostReadArticle = articles.stream()
                    .reduce((article1, article2) -> {
                        if (article1.getArticleReadActions().size() > article2.getArticleReadActions().size()) {
                            return article1;
                        } else {
                            return article2;
                        }
                    });
            String mostReadArticleTitle = null;
            String mostReadArticleUrl = null;
            if (mostReadArticle.isPresent()) {
                mostReadArticleTitle = mostReadArticle.get().getTitle();
                mostReadArticleUrl = mostReadArticle.get().getUrl();
            }

            authorStatsDTOs.add(new AuthorStatsDTO(userName, fullName, nrOfArticles, nrOfCoins, mostReadArticleTitle,
                    mostReadArticleUrl));
        });

        LOGGER.info("Calculating Author rankings...");
        authorStatsDTOs.sort(AuthorStatsDTO::compareTo);

        int rank = 1;
        for (AuthorStatsDTO authorStatsDTO : authorStatsDTOs) {
            authorStatsDTO.rank = rank++;
        }
        return authorStatsDTOs;
    }
}
