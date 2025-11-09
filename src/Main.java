// Main.java
import Controller.LoginController;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginController(new View.LoginView());
        });
    }
}