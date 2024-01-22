package main.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import main.client.CalendarApp;
import main.client.UserInfo;
import main.client.communication.CommLogout;
import main.client.components.DayBox;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Locale;
import java.time.format.TextStyle;

public class CalendarController {

    @FXML
    private Label monthLabel;

    @FXML
    private GridPane calendarGrid;

    @FXML
    private Button previousMonthButton;

    @FXML
    private Button nextMonthButton;

    @FXML
    private Button logoutButton;

    private LocalDate currentDate;

    @FXML
    public void initialize() {
        currentDate = LocalDate.now();
        updateCalendar();

        previousMonthButton.setOnAction(e -> {
            currentDate = currentDate.minusMonths(1);
            updateCalendar();
        });

        nextMonthButton.setOnAction(e -> {
            currentDate = currentDate.plusMonths(1);
            updateCalendar();
        });

        logoutButton.setOnAction(e -> {
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

    private void updateCalendar() {
        calendarGrid.getChildren().clear();
        YearMonth yearMonth = YearMonth.from(currentDate);
        monthLabel.setText(yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + yearMonth.getYear());

        for (int i = 1; i <= yearMonth.lengthOfMonth(); i++) {
            LocalDate date = yearMonth.atDay(i);
            DayBox dayBox = new DayBox(i, false, "");
            calendarGrid.add(dayBox, (date.getDayOfWeek().getValue() - 1) % 7, (i - 1) / 7);
        }
    }
}
