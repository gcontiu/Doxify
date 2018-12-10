package com.helloworld.adapter;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.helloworld.data.Article;
import com.helloworld.data.ArticleReadAction;
import com.helloworld.data.Author;
import com.helloworld.data.Comment;
import com.helloworld.data.CommentReadAction;
import com.helloworld.data.dto.AuthorStatsDTO;
import com.helloworld.repository.ArticleReadActionRepository;
import com.helloworld.repository.AuthorRepository;
import com.helloworld.service.CoinCalculator;

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
        final int[] rank = { 1 };

        LOGGER.info("Getting AuthorStats from database and calculating rankings...");
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

        String userName = author.getUserName();
        String fullName = author.getFullName();

        List<Article> articles = author.getArticles()
                .stream()
                .filter(article -> !article.isBlackListed())
                .collect(Collectors.toList());

        int nrOfArticles = articles.size();

        if (nrOfArticles > 0) {
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

            double totalCoins = coinCalculator.round(Double.sum(totalArticleReadCoins, totalArticleCommentsCoins));

            Article mostReadArticle = totalCoinsPerArticleReadMap.entrySet()
                    .stream()
                    .sorted(Comparator.comparingDouble((ToDoubleFunction<Map.Entry<Article, Double>>) Map.Entry::getValue)
                            .reversed())
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(new Article());

            String mostReadArticleTitle = mostReadArticle.getTitle();
            String mostReadArticleUrl = mostReadArticle.getUrl();

            return new AuthorStatsDTO(userName, fullName, nrOfArticles, totalCoins, mostReadArticleTitle, mostReadArticleUrl);
        }
        return new AuthorStatsDTO(userName, fullName, nrOfArticles, 0, null, null);
    }

    public Double getAverageTimeSpentOnArticles() {
        double average = articleReadActionRepository.findAll()
                .stream()
                .mapToDouble(ArticleReadAction::getSecondsSpent)
                .average()
                .orElse(0);

        return coinCalculator.round(average);
    }

    public Double getTopAchievedCoinForArticle() {
        double coins = articleReadActionRepository.findAll()
                .stream()
                .max(Comparator.comparing(ArticleReadAction::getNrOfCoins))
                .map(ArticleReadAction::getNrOfCoins)
                .orElse(0.0);

        return coinCalculator.round(coins);
    }
}
