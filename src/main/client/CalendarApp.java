package main.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.client.communication.CommLogout;

import java.util.Objects;

// klasa glowna aplikacji
public class CalendarApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        showLoginScene();
        primaryStage.show();
    }

    // inicjalizajca scen login i kalendarz
    public static void showLoginScene() throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(CalendarApp.class.getResource("/login_layout.fxml")));
        primaryStage.setMinWidth(300.0);
        primaryStage.setMinHeight(500.0);
        primaryStage.setWidth(700.0);
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Login");
    }

    public static void showCalendarScene() throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(CalendarApp.class.getResource("/calendar_layout.fxml")));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Online Calendar");

        // wylogowanie uzytkownika przy zamknieciu okna
        primaryStage.setOnCloseRequest(e -> {
            CommLogout commLogout = new CommLogout();
            String resp = commLogout.sendAndGetResp();

            if (resp != null) {
                try {
                    commLogout.parseResponse();
                    UserInfo.setToken(null);
                    CalendarApp.showLoginScene();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
