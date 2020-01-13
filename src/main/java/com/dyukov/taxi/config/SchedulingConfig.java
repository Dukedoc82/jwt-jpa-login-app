package com.dyukov.taxi.config;

import com.dyukov.taxi.service.IMailService;
import com.dyukov.taxi.service.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulingConfig {
    private final long ONE_DAY_INTERVAL = 1000 * 60 * 60 * 24;

    @Autowired
    private ITokenService tokenService;

    /*@Autowired
    private JavaMailSender javaMailSender;*/

    @Autowired
    private IMailService mailService;

    @Scheduled(initialDelay = ONE_DAY_INTERVAL, fixedRate = ONE_DAY_INTERVAL)
    public void clearOutdatedBlacklistTokens() {
        tokenService.clearOutdatedTokensFromBlacklist();
    }

    @Scheduled(initialDelay = 1000, fixedRate = ONE_DAY_INTERVAL)
    public void sendEmail() {
        /*SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("dmitry.dyukov@gmail.com");
        message.setSubject("Test email");
        message.setText("This is a test text");
        javaMailSender.send(message);*/
        mailService.prepareAndSend("dmitry.dyukov@gmail.com", "Some Test Message");
    }
}
