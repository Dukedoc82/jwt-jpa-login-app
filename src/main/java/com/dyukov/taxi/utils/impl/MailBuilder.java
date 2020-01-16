package com.dyukov.taxi.utils.impl;

import com.dyukov.taxi.dao.OrderDetailsDao;
import com.dyukov.taxi.utils.IMailBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class MailBuilder implements IMailBuilder {

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public MimeMessagePreparator getMimeMessagePreparator(String recipient, Context context, String templateName,
                                                          String subject, boolean isHtml) {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(recipient);
            String body = templateEngine.process(templateName, context);
            messageHelper.setSubject(subject);
            messageHelper.setText(body, isHtml);
        };
    }

    @Override
    public MimeMessagePreparator getMimeMessagePreparator(String recipient, String body, String subject) {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(recipient);
            messageHelper.setSubject(subject);
            messageHelper.setText(body, false);
        };
    }

    @Override
    public Context getNewOrderContext(OrderDetailsDao order) {
        return getOrderUpdateContext(order, "New Order");
    }

    @Override
    public Context getCancelledOrderContext(OrderDetailsDao order) {
        return getOrderUpdateContext(order, "Order cancelled");
    }



    private Context getOrderUpdateContext(OrderDetailsDao order, String title) {
        Context context = new Context();
        fillOrderDetails(order, context);
        context.setVariable("headerTitle", title);
        return context;
    }

    private void fillOrderDetails(OrderDetailsDao order, Context context) {
        context.setVariable("addressFrom", order.getOrder().getAddressFrom());
        context.setVariable("addressTo", order.getOrder().getAddressTo());
        context.setVariable("appointmentTime", order.getOrder().getAppointmentDate());
        context.setVariable("clientName", order.getOrder().getClient().getFirstName() + " " + order.getOrder().getClient().getLastName());
    }
}
