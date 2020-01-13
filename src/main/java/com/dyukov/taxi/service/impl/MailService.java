package com.dyukov.taxi.service.impl;

import com.dyukov.taxi.dao.OrderDetailsDao;
import com.dyukov.taxi.service.IMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailService implements IMailService {

    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender mailSender;



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
    public void sendNewOrderNotification(String recipient, OrderDetailsDao order) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(recipient);
            Context context = new Context();
            context.setVariable("addressFrom", order.getOrder().getAddressFrom());
            context.setVariable("addressTo", order.getOrder().getAddressTo());
            context.setVariable("appointmentTime", order.getOrder().getAppointmentDate());
            context.setVariable("clientName", order.getOrder().getClient().getFirstName() + " " + order.getOrder().getClient().getLastName());
            context.setVariable("headerTitle", "New Order");
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
}
