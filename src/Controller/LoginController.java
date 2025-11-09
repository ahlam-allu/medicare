// Controller/LoginController.java
package Controller;

import View.LoginView;
import View.DashboardFrame;
import javax.swing.*;

public class LoginController {
    private final LoginView view;

    public LoginController(LoginView view) {
        this.view = view;
        this.view.addLoginListener(e -> attemptLogin());
        this.view.setVisible(true);
    }

    private void attemptLogin() {
        String password = view.getPassword();

        if ("1234".equals(password)) {
            view.dispose();
            SwingUtilities.invokeLater(() -> new DashboardFrame());
        } else {
            view.showError("Incorrect password. Try '1234'");
            view.clearError();
            SwingUtilities.invokeLater(() -> view.txtPassword.requestFocus());
        }
    }
}