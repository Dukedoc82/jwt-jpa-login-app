package com.dyukov.taxi.utils;

import org.springframework.mail.javamail.MimeMessagePreparator;
import org.thymeleaf.context.Context;

public interface IMailBuilder {

    MimeMessagePreparator getMimeMessagePreparator(String recipient, Context context, String templateName,
                                                          String subject, boolean isHtml);

    MimeMessagePreparator getMimeMessagePreparator(String recipient, String body, String subject);
}
