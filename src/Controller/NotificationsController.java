package Controller;

import View.NotificationsView;
import dao.NotificationDAO;
import java.awt.event.ActionEvent;

public class NotificationsController {
    private final NotificationsView view;
    private final NotificationDAO dao;

    public NotificationsController(NotificationsView view) {
        this.view = view;
        this.dao = new NotificationDAO();
        init();
    }

    private void init() {
        view.addRefreshListener(this::load);
        load(null);
    }

    private void load(ActionEvent e) {
        view.setNotifications(dao.getNotifications());
    }
}