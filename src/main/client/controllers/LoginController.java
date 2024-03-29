package main.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.client.CalendarApp;
import main.client.UserInfo;
import main.client.communication.CommLogin;

// kontroller odpowiedzialny za widok logowania uzytkownika
// layout z pliku FXML
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    private void handleSubmit() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        CommLogin commLogin = new CommLogin(username, password);
        String resp = commLogin.sendAndGetResp();


        // wysylanie zadania o zalogowanie uzytkownika
        if (resp != null) {
            try {
                commLogin.parseResponse();
                UserInfo.setToken(commLogin.getLoginToken());
                UserInfo.setUserId(commLogin.getUserId());
                UserInfo.setUsername(commLogin.getUsername());
                CalendarApp.showCalendarScene();
            } catch (Exception e) {
                messageLabel.setText(e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            messageLabel.setText("Wystąpił nieznany błąd aplikacji.");
        }
    }
}

