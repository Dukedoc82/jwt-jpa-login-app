package com.dyukov.taxi.config;

import com.dyukov.taxi.service.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulingConfig {
    private final long ONE_DAY_INTERVAL = 1000 * 60 * 60 * 24;

    @Autowired
    private ITokenService tokenService;

    @Scheduled(initialDelay = ONE_DAY_INTERVAL, fixedRate = ONE_DAY_INTERVAL)
    public void clearOutdatedBlacklistTokens() {
        tokenService.clearOutdatedTokensFromBlacklist();
    }
}
