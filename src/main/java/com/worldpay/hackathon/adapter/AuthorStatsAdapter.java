package com.worldpay.hackathon.adapter;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.worldpay.hackathon.data.ArticleReadAction;
import com.worldpay.hackathon.data.Author;
import com.worldpay.hackathon.data.Comment;
import com.worldpay.hackathon.data.CommentReadAction;
import com.worldpay.hackathon.data.dto.AuthorStatsDTO;
import com.worldpay.hackathon.repository.ArticleReadActionRepository;
import com.worldpay.hackathon.repository.AuthorRepository;
import com.worldpay.hackathon.service.CoinCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.worldpay.hackathon.data.Article;

@Service
public class AuthorStatsAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorStatsAdapter.class);
    private final AuthorRepository authorRepository;
    private final CoinCalculator coinCalculator;
    private final ArticleReadActionRepository articleReadActionRepository;

    @Autowired
    public AuthorStatsAdapter(AuthorRepository authorRepository, CoinCalculator coinCalculator, ArticleReadActionRepository articleReadActionRepository) {
        this.authorRepository = authorRepository;
        this.coinCalculator = coinCalculator;
        this.articleReadActionRepository = articleReadActionRepository;
    }

    @Cacheable(cacheNames = "authorStats")
    public List<AuthorStatsDTO> getAllAuthorStats() {
        LOGGER.info("Getting all AuthorStats from database and calculating rankings...");

        final int[] rank = { 1 };
        return StreamSupport.stream(authorRepository.findAll().spliterator(), false)
                .map(this::getStatsForAuthor)
                .filter(authorStatsDTO -> authorStatsDTO.totalCoins > 0)
                .sorted(AuthorStatsDTO::compareTo)
                .limit(5)
                .peek(authorStatsDTO -> authorStatsDTO.rank = rank[0]++)
                .collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "authorStats")
    public AuthorStatsDTO getStatsForAuthor(Author author) {
        LOGGER.info("Calculating AuthorStats for '{}'...", author.getUserName());

        AuthorStatsDTO authorStatsDTO = new AuthorStatsDTO();

        authorStatsDTO.username = author.getUserName();
        authorStatsDTO.fullName = author.getFullName();

        List<Article> articles = author.getArticles()
                .stream()
                .filter(article -> !article.isBlackListed())
                .collect(Collectors.toList());

        authorStatsDTO.nrOfArticles = articles.size();

        if (authorStatsDTO.nrOfArticles > 0) {
            Map<Article, Double> totalCoinsPerArticleReadMap = articles.stream()
                    .collect(Collectors.toMap(article -> article, article -> article.getArticleReadActions()
                            .stream()
                            .mapToDouble(ArticleReadAction::getNrOfCoins)
                            .sum()));

            Map<Article, Double> totalCoinsPerArticleCommentMap = articles.stream()
                    .collect(Collectors.toMap(article -> article, article -> article.getComments()
                            .stream()
                            .map(Comment::getCommentReadActions)
                            .mapToDouble(commentReadActions -> commentReadActions.stream()
                                    .mapToDouble(CommentReadAction::getNrOfCoins)
                                    .sum())
                            .sum()));

            double totalArticleReadCoins = totalCoinsPerArticleReadMap.values()
                    .stream()
                    .mapToDouble(Double::doubleValue)
                    .sum();

            double totalArticleCommentsCoins = totalCoinsPerArticleCommentMap.values()
                    .stream()
                    .mapToDouble(Double::doubleValue)
                    .sum();

            authorStatsDTO.totalCoins = coinCalculator.round(Double.sum(totalArticleReadCoins, totalArticleCommentsCoins));

            Article mostReadArticle = totalCoinsPerArticleReadMap.entrySet()
                    .stream()
                    .sorted(Comparator.comparingDouble((ToDoubleFunction<Map.Entry<Article, Double>>) Map.Entry::getValue)
                            .reversed())
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(new Article());

            authorStatsDTO.mostReadArticleName = mostReadArticle.getTitle();
            authorStatsDTO.mostReadArticleURL = mostReadArticle.getUrl();
        }

        return authorStatsDTO;
    }

    @Cacheable(cacheNames = "averageTimeOnArticle")
    public Double getAverageTimeSpentOnArticles() {
        LOGGER.info("Calculating the average time spent on articles...");

        double average = articleReadActionRepository.findAll()
                .stream()
                .mapToDouble(ArticleReadAction::getSecondsSpent)
                .average()
                .orElse(0);

        return coinCalculator.round(average);
    }

    @Cacheable(cacheNames = "topAchievedCoinsForArticle")
    public Double getTopAchievedCoinForArticle() {
        LOGGER.info("Calculating the max coins achieved for an article...");

        double coins = articleReadActionRepository.findAll()
                .stream()
                .max(Comparator.comparing(ArticleReadAction::getNrOfCoins))
                .map(ArticleReadAction::getNrOfCoins)
                .orElse(0.0);

        return coinCalculator.round(coins);
    }
}
