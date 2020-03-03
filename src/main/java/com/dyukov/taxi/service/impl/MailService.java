package com.dyukov.taxi.service.impl;

import com.dyukov.taxi.dao.OrderDetailsDao;
import com.dyukov.taxi.entity.TpUser;
import com.dyukov.taxi.entity.UserMailSettings;
import com.dyukov.taxi.exception.UserMailSettingsNotFoundException;
import com.dyukov.taxi.repository.IUserDetailsRepository;
import com.dyukov.taxi.repository.IUserMailSettingsRepository;
import com.dyukov.taxi.service.IMailService;
import com.dyukov.taxi.service.context.ContextAction;
import com.dyukov.taxi.utils.IMailBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Collection;

@Service
@PropertySource("classpath:message.properties")
public class MailService implements IMailService {

    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private IMailBuilder mailBuilder;

    @Autowired
    private IUserMailSettingsRepository mailSettingsRepository;

    @Autowired
    private IUserDetailsRepository userDetailsRepository;

    @Value("${mailServer.address}")
    private String mailAddress;

    @Value("${mail.confirm.body}")
    private String registerConfirmBody;

    @Value("Hi, you have requested a new password. To proceed follow this link %s")
    private String confirmNewPasswordGenerationBody;

    @Value("Hi, you requested a new password. Your new password is %s")
    private String newPasswordBody;

    @Override
    public void prepareAndSend(String recipient, String message) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(recipient);
            Context context = new Context();
            context.setVariable("message", "test message");
            context.setVariable("headerTitle", "Header Title");
            String body = templateEngine.process("orderDetailsTemplate", context);
            messageHelper.setSubject("Sample mail subject");
            messageHelper.setText(body, true);
        };
        try {
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void sendRegistrationConfirmationEmail(String recipient, String confirmToken) {
        String link = mailAddress + "/?confirm=" + confirmToken;
        String body = String.format(registerConfirmBody, link);
        MimeMessagePreparator messagePreparator = mailBuilder.getMimeMessagePreparator(recipient, body,
                "Registration confirmation");
        try {
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void sendOrderUpdateNotification(Collection<String> recipients, OrderDetailsDao order, ContextAction action) {
        Context context = action.getContext(order);

        recipients.forEach(recipient -> {
            MimeMessagePreparator messagePreparator = mailBuilder.getMimeMessagePreparator(recipient, context,
                    "orderDetailsTemplate", action.getSubject(), true);
            try {
                TpUser user = userDetailsRepository.findUserAccount(recipient);
                try {
                    UserMailSettings settings = mailSettingsRepository.getSettingsByUser(user);
                    if (action.sendUpdate(settings)) {
                        mailSender.send(messagePreparator);
                    }
                } catch (UserMailSettingsNotFoundException e) {
                    // Do nothing as no mail settings for user setup.
                }
            } catch (MailException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        });
    }

    @Override
    public void sendNewPassword(String recipient, String newPassword) {
        String body = String.format(newPasswordBody, newPassword);
        MimeMessagePreparator messagePreparator = mailBuilder.getMimeMessagePreparator(recipient, body,
                "New password generated");
        try {
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

    }

    @Override
    public void sendNewPasswordRequestTokenMail(String recipient, String confirmToken) {
        String link = mailAddress + "/?generateNewPassword=" + confirmToken;
        String body = String.format(confirmNewPasswordGenerationBody, link);
        MimeMessagePreparator messagePreparator = mailBuilder.getMimeMessagePreparator(recipient, body,
                "Confirm new password generation");
        try {
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

    }
}
