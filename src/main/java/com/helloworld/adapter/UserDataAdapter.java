package com.helloworld.adapter;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helloworld.data.Article;
import com.helloworld.data.ArticleReadAction;
import com.helloworld.data.Author;
import com.helloworld.data.dto.UserDashboardDTO;
import com.helloworld.repository.ArticleRepository;
import com.helloworld.repository.AuthorRepository;

/**
 * Adapter class which maps the Author's data from Database to an {@code UserDashboardDTO}, in order to display it's data into User's
 * Statistics Page.
 */
@Service
public class UserDataAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDataAdapter.class);
    private final AuthorRepository authorRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public UserDataAdapter(AuthorRepository authorRepository, ArticleRepository articleRepository) {
        this.authorRepository = authorRepository;
        this.articleRepository = articleRepository;
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
        userDto.setUserFullName(article.getAuthor()
                .getFullName());
        userDto.setArticleId(article.getId());
        userDto.setArticleName(article.getTitle());

        List<ArticleReadAction> readActions = article.getArticleReadActions();
        userDto.setCoinsGainedPerArticle(computeSumOfCoinsPerArticle(readActions));
        userDto.setCoinsGained(computeSumOfCoins());
        // TODO compute/get all needed values for displaying the Author's statistics data

        return userDto;
    }

    /**
     * Compute the cum of coins per Article
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
     * 
     * Compute the total number of coins gained by an Author
     * 
     * @return
     */
    private Double computeSumOfCoins() {
        // TODO add logic to compute the total amount of coins per Author
        return 0d;
    }

}
