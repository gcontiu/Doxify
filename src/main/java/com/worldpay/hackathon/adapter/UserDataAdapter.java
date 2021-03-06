package com.worldpay.hackathon.adapter;

import com.worldpay.hackathon.data.ArticleReadAction;
import com.worldpay.hackathon.data.Author;
import com.worldpay.hackathon.data.dto.UserDashboardDTO;
import com.worldpay.hackathon.repository.ArticleRepository;
import com.worldpay.hackathon.repository.AuthorRepository;
import com.worldpay.hackathon.service.CoinCalculator;
import com.worldpay.hackathon.data.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Adapter class which maps the Author's data from Database to an {@code UserDashboardDTO}, in order to display it's data into User's
 * Statistics Page.
 */
@Service
public class UserDataAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDataAdapter.class);
    private final AuthorRepository authorRepository;
    private final ArticleRepository articleRepository;
    private final CoinCalculator coinCalculator;

    @Autowired
    public UserDataAdapter(AuthorRepository authorRepository, ArticleRepository articleRepository, CoinCalculator coinCalculator) {
        this.authorRepository = authorRepository;
        this.articleRepository = articleRepository;
        this.coinCalculator = coinCalculator;
    }

    public List<UserDashboardDTO> getAllArticles(String username) {
        List<UserDashboardDTO> userDto = new ArrayList<>();

        Author author = authorRepository.findByUserName(username);
        List<Article> articles = articleRepository.findAllByAuthor(author);

        for (Article article : articles) {
            userDto.add(toEntity(article));
        }

        return userDto;
    }

    /**
     * Map all data needed for an Author's statistics page
     *
     * @param article
     * @return
     */
    private UserDashboardDTO toEntity(Article article) {
        UserDashboardDTO userDto = new UserDashboardDTO();
        userDto.userFullName = article.getAuthor().getFullName();
        userDto.articleId = article.getId();
        userDto.articleTitle = article.getTitle();

        List<ArticleReadAction> readActions = article.getArticleReadActions();
        userDto.coinsGainedPerArticle = computeSumOfCoinsPerArticle(readActions);
        userDto.coinsGained = coinCalculator.round(computeSumOfCoins(article.getAuthor()));
        userDto.totalNumberOfArticles = article.getAuthor().getArticles().size();

        return userDto;
    }

    /**
     * Compute the sum of coins per Article
     *
     * @param readActions
     * @return
     */
    private Double computeSumOfCoinsPerArticle(List<ArticleReadAction> readActions) {
        Double sum = readActions.stream()
                .mapToDouble(ArticleReadAction::getNrOfCoins)
                .sum();
        LOGGER.info("Coins gained: {}", sum);
        return sum;
    }

    /**
     * Compute the total number of coins gained by an Author
     *
     * @param author contributor for which the coins are computed
     * @return total number of coins gained by an Author
     */
    private Double computeSumOfCoins(Author author) {
        List<Article> articles = author.getArticles()
                .stream()
                .filter(article -> !article.isBlackListed())
                .collect(Collectors.toList());
        Double sum = 0d;

        if (!articles.isEmpty()) {
            for (Article article : articles) {
                List<ArticleReadAction> readActions = article.getArticleReadActions();

                for (ArticleReadAction action : readActions) {
                    Double noOfCoins = action.getNrOfCoins();
                    sum += noOfCoins;
                }
            }
            LOGGER.info("Author '{}' gained a total of '{}' coins.", author.getFullName(), sum);
        }
        return sum;
    }

}
