package main.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import main.client.CalendarApp;
import main.client.UserInfo;
import main.client.communication.CommGetEvents;
import main.client.communication.CommLogout;
import main.client.components.DayBox;
import main.client.model.Event;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

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
        calendarGrid.getColumnConstraints().clear();
        YearMonth yearMonth = YearMonth.from(currentDate);
        monthLabel.setText(yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + yearMonth.getYear());

        String[] dayNames = {"Poniedziałek", "Wtorek", "Środa", "Czwartek", "Piątek", "Sobota", "Niedziela"};
        CommGetEvents commGetEvents = new CommGetEvents();

        try {
            commGetEvents.sendAndGetResp();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < 7; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setMinWidth(30);
            columnConstraints.setPrefWidth(70);
            columnConstraints.setMaxWidth(70);
            calendarGrid.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < 7; i++) {
            Label dayNameLabel = new Label(dayNames[i]);
            dayNameLabel.getStyleClass().add("day-name-label");
            calendarGrid.add(dayNameLabel, i, 0);
        }

        LocalDate firstOfMonth = currentDate.withDayOfMonth(1);
        int dayOfWeekOfFirstOfMonth = firstOfMonth.getDayOfWeek().getValue();

        int offset = (dayOfWeekOfFirstOfMonth - 1 + 7) % 7;

        for (int dayOfMonth = 1; dayOfMonth <= yearMonth.lengthOfMonth(); dayOfMonth++) {
            LocalDate date = yearMonth.atDay(dayOfMonth);
            DayBox dayBox = new DayBox(dayOfMonth, false, "");
            for (Event e : commGetEvents.getEventList()) {
                if (date.equals(e.getStartTime().toLocalDateTime().toLocalDate())) {
                    dayBox = new DayBox(dayOfMonth, true, e.getTitle());
                }
            }
            int column = (dayOfMonth + offset - 1) % 7;
            int row = (dayOfMonth + offset - 1) / 7 + 1;
            calendarGrid.add(dayBox, column, row);
        }
    }

}
