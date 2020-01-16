package com.dyukov.taxi.utils;

import com.dyukov.taxi.dao.OrderDetailsDao;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.thymeleaf.context.Context;

public interface IMailBuilder {

    MimeMessagePreparator getMimeMessagePreparator(String recipient, Context context, String templateName,
                                                          String subject, boolean isHtml);

    Context getNewOrderContext(OrderDetailsDao order);

    Context getCancelledOrderContext(OrderDetailsDao order);

    MimeMessagePreparator getMimeMessagePreparator(String recipient, String body, String subject);

}
