package com.helloworld.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.helloworld.data.Article;
import com.helloworld.data.ArticleReadAction;
import com.helloworld.data.dto.ArticleStatsDTO;
import com.helloworld.repository.ArticleRepository;
import com.helloworld.repository.AuthorRepository;

@Service
public class ArticleStatsAdapter {

    private final ArticleRepository articleRepository;
    private final AuthorRepository authorRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleStatsAdapter.class);

    @Autowired
    public ArticleStatsAdapter(ArticleRepository articleRepository, AuthorRepository authorRepository) {
        this.articleRepository = articleRepository;
        this.authorRepository = authorRepository;
    }

    @Cacheable(cacheNames = "articleStats")
    public List<ArticleStatsDTO> getAllArticleStatsByUsername(String username) {
        LOGGER.info("Getting all ArticleStats from database for user '{}' and calculating rankings...", username);

        final int[] rank = { 1 };
        return authorRepository.findByUserName(username).getArticles().stream()
                .map(this::getStatsForArticle)
                .sorted(ArticleStatsDTO::compareTo)
                .limit(10)
                .peek(authorStatsDTO -> authorStatsDTO.rank = rank[0]++)
                .collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "articleStats")
    public List<ArticleStatsDTO> getAllArticleStatsByCategory(String category) {
        LOGGER.info("Getting all ArticleStats from database for category '{}' and calculating rankings...", category);

        final int[] rank = { 1 };
        return articleRepository.findAllByCategory(category).stream()
                .map(this::getStatsForArticle)
                .sorted(ArticleStatsDTO::compareTo)
                .limit(5)
                .peek(authorStatsDTO -> authorStatsDTO.rank = rank[0]++)
                .collect(Collectors.toList());
    }


    @Cacheable(cacheNames = "articleStats")
    public ArticleStatsDTO getStatsForArticle(Article article) {
        LOGGER.info("Calculating ArticleStats for '{}'...", article.getTitle());

        ArticleStatsDTO articleStatsDTO = new ArticleStatsDTO();

        articleStatsDTO.articleTitle = article.getTitle();
        articleStatsDTO.articleUrl = article.getUrl();
        //articleStatsDTO.nrOfLines = article.getNrOfLines();
        articleStatsDTO.timesRead = article.getArticleReadActions().size();
        articleStatsDTO.averageTimeSpent = article.getArticleReadActions().stream()
                .mapToDouble(ArticleReadAction::getSecondsSpent)
                .average()
                .orElse(0);
        articleStatsDTO.totalCoins = article.getArticleReadActions().stream()
                .mapToDouble(ArticleReadAction::getNrOfCoins)
                .sum();
        articleStatsDTO.timestamp = article.getTimeStamp();

        return articleStatsDTO;
    }
}
