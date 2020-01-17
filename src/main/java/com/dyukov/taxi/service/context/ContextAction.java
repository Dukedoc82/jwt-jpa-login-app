package com.dyukov.taxi.service.context;

import com.dyukov.taxi.dao.OrderDetailsDao;
import com.dyukov.taxi.entity.UserMailSettings;
import org.thymeleaf.context.Context;

public enum ContextAction {
    ASSIGN {
        @Override
        public Context getContext(OrderDetailsDao order) {
            return getOrderUpdateContext(order,"Order Assigned");
        }

        @Override
        public String getSubjectMessage() {
            return "Order Assigned";
        }

        @Override
        public boolean sendUpdate(UserMailSettings mailSettings) {
            return mailSettings.getAssignOrder();
        }
    },
    CANCEL {
        @Override
        public Context getContext(OrderDetailsDao order) {
            return getOrderUpdateContext(order, "Order Cancelled");
        }

        @Override
        public String getSubjectMessage() {
            return "Order Cancelled";
        }

        @Override
        public boolean sendUpdate(UserMailSettings mailSettings) {
            return mailSettings.getCancelOrder();
        }
    },
    COMPLETE {
        @Override
        public Context getContext(OrderDetailsDao order) {
            return getOrderUpdateContext(order, "Order Completed");
        }

        @Override
        public String getSubjectMessage() {
            return "Order Completed";
        }

        @Override
        public boolean sendUpdate(UserMailSettings mailSettings) {
            return mailSettings.getCompleteOrder();
        }
    },
    NEW {
        @Override
        public Context getContext(OrderDetailsDao order) {
            return getOrderUpdateContext(order, "New Order");
        }

        @Override
        public String getSubjectMessage() {
            return "New Order Created";
        }

        @Override
        public boolean sendUpdate(UserMailSettings mailSettings) {
            return mailSettings.getNewOrder();
        }
    },
    REFUSE {
        @Override
        public Context getContext(OrderDetailsDao order) {
            return getOrderUpdateContext(order, "Order Refused");
        }

        @Override
        public String getSubjectMessage() {
            return "Order Refused";
        }

        @Override
        public boolean sendUpdate(UserMailSettings mailSettings) {
            return mailSettings.getRefuseOrder();
        }
    };

    protected static final String SUBJECT_FORMAT = "[EPAM Taxi] %s";

    public abstract Context getContext(OrderDetailsDao order);

    public abstract boolean sendUpdate(UserMailSettings mailSettings);

    protected abstract String getSubjectMessage();

    public final String getSubject() {
        return String.format(SUBJECT_FORMAT, getSubjectMessage());
    }

    protected Context getOrderUpdateContext(OrderDetailsDao order, String title) {
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
