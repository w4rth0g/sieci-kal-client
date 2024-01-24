package main.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import main.client.CalendarApp;
import main.client.UserInfo;
import main.client.communication.CommGetEvents;
import main.client.communication.CommLogout;
import main.client.components.DayBox;
import main.client.model.Event;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
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

    @FXML
    private Label userLabel;

    private LocalDate currentDate;

    @FXML
    public void initialize() {
        currentDate = LocalDate.now();
        userLabel.setText(UserInfo.getUsername());

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

    public void updateCalendar() {
        calendarGrid.getChildren().clear();
        calendarGrid.getColumnConstraints().clear();
        calendarGrid.getRowConstraints().clear();
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
            columnConstraints.setMinWidth(45);
            columnConstraints.setPrefWidth(70);
            columnConstraints.setMaxWidth(70);
            calendarGrid.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < 7; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setMinHeight(70);
            rowConstraints.setPrefHeight(70);
            rowConstraints.setMaxHeight(70);
            calendarGrid.getRowConstraints().add(rowConstraints);
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

            int evtCount = 0;
            List<Event> eventsForDay = new ArrayList<>();
            for (Event e : commGetEvents.getEventList()) {
                if (date.equals(e.getStartTime().toLocalDateTime().toLocalDate())) {
                    evtCount++;
                    eventsForDay.add(e);
                }
            }
            DayBox dayBox = new DayBox(this, dayOfMonth, evtCount, date, eventsForDay);
            int column = (dayOfMonth + offset - 1) % 7;
            int row = (dayOfMonth + offset - 1) / 7 + 1;
            calendarGrid.add(dayBox, column, row);
        }
    }

}
