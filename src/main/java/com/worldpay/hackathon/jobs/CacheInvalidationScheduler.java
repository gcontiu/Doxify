package com.worldpay.hackathon.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CacheInvalidationScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheInvalidationScheduler.class);

    @CacheEvict(cacheNames = "authorStats", allEntries = true)
    //@Scheduled(fixedRate = 60000)
    public void invalidateAuthorStatsCache() {
        LOGGER.info("Invalidated AuthorStatsCache.");
    }
}
