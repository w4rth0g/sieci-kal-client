package main.client.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.client.UserInfo;
import main.client.communication.CommAddEvent;
import main.client.communication.CommDeleteEvent;
import main.client.controllers.CalendarController;
import main.client.model.Event;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DayBox extends VBox {
    private final Label dayLabel;
    private final Button addButton;
    private final HBox eventDotsContainer;
    private final LocalDate date;
    private final List<Event> eventsForDay;
    private Stage modalStage;
    private CalendarController calendarController;

    public DayBox(CalendarController controller, int day, int eventCount, LocalDate date, List<Event> eventList) {
        super(5);
        this.calendarController = controller;
        this.dayLabel = new Label(Integer.toString(day));
        this.getStylesheets().add("/style.css");
        this.dayLabel.getStyleClass().add("day-label");

        this.date = date;
        this.eventsForDay = eventList;

        this.setPrefHeight(100);
        this.setMaxHeight(100);

        this.eventDotsContainer = new HBox();
        this.eventDotsContainer.setSpacing(1);
        eventDotsContainer.setAlignment(Pos.CENTER);

        for (int i = 0; i < Math.min(eventCount, 2); i++) {
            Label dot = new Label("•");
            dot.getStyleClass().add("event-dot");
            eventDotsContainer.getChildren().add(dot);
        }

        if (eventCount > 2) {
            Label moreEvents = new Label("+" + (eventCount - 2));
            moreEvents.getStyleClass().add("more-events");
            eventDotsContainer.getChildren().add(moreEvents);
        }

        this.addButton = new Button("Add");
        this.addButton.getStyleClass().add("add-button");

        this.getChildren().addAll(dayLabel, eventDotsContainer);

        this.setOnMouseEntered(e -> {
            this.setCursor(Cursor.HAND);
            this.getStyleClass().add("day-box-entered");
            this.dayLabel.getStyleClass().add("text-white");
            eventDotsContainer.getChildren().forEach(ch -> ch.getStyleClass().add("text-white"));
        });
        this.setOnMouseExited(e -> {
            this.setCursor(Cursor.DEFAULT);
            this.getStyleClass().remove("day-box-entered");
            this.dayLabel.getStyleClass().remove("text-white");
            eventDotsContainer.getChildren().forEach(ch -> ch.getStyleClass().remove("text-white"));
        });

        this.getStyleClass().add("day-box");

        this.setOnMouseClicked(e -> {
            showModal();
        });
    }

    private void showModal() {
        modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Szczegóły " + date);

        this.addButton.setOnAction(e -> {
            VBox eventForm = createEventForm();
            Scene eventFormScene = new Scene(eventForm, 300, 400);
            modalStage.setScene(eventFormScene);
        });
        modalStage.setScene(createEventList());
        modalStage.showAndWait();
    }

    private Scene createEventList() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(20));
        scrollPane.getStylesheets().add("/style.css");

        BorderPane modalLayout = new BorderPane();
        scrollPane.setContent(modalLayout);

        Label dateLabel = new Label(date.toString());
        dateLabel.setAlignment(Pos.CENTER);
        modalLayout.setTop(dateLabel);

        VBox eventBoxes = new VBox(10);
        eventBoxes.setPadding(new Insets(20, 0, 20, 0));
        for (Event evt : this.eventsForDay) {
            VBox evtBox = new VBox(3);
            evtBox.setPadding(new Insets(10));
            evtBox.getStyleClass().add("evt-box");
            Label author = new Label("User z id: " + evt.getUserId());
            Label title = new Label(evt.getTitle());
            Label desc = new Label(evt.getDescription());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            Label timeL = new Label(
                    evt.getStartTime().toLocalDateTime().toLocalTime().format(formatter) + " - "
                            + evt.getEndTime().toLocalDateTime().toLocalTime().format(formatter)
            );

            author.getStyleClass().add("text-white");
            title.getStyleClass().add("text-white");
            desc.getStyleClass().add("text-white");
            timeL.getStyleClass().add("text-white");

            evtBox.getChildren().addAll(author, timeL, title, desc);

            if (evt.getUserId() == UserInfo.getUserId()) {
                Button delButton = new Button("Usuń");

                delButton.setOnAction(e -> {
                    CommDeleteEvent commDeleteEvent = new CommDeleteEvent(evt.getEventId());
                    commDeleteEvent.sendAndGetResp();
                    try {
                        commDeleteEvent.parseResponse();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }

                    refreshEventsDisplay();
                });

                evtBox.getChildren().add(delButton);
            }

            eventBoxes.getChildren().add(evtBox);
        }

        HBox bottomSection = new HBox();

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> modalStage.close());
        bottomSection.getChildren().addAll(closeButton, spacer, this.addButton);

        modalLayout.setBottom(bottomSection);
        modalLayout.setCenter(eventBoxes);

        Scene modalScene = new Scene(scrollPane, 300, 400);
        return modalScene;
    }

    private VBox createEventForm() {
        Label infoLbl = new Label("Dodaj wydarzenie");
        infoLbl.setAlignment(Pos.CENTER);
        infoLbl.setFont(Font.font(20));

        TextField titleInput = new TextField();
        titleInput.setPromptText("Tytuł wydarzenia");

        TextField descriptionInput = new TextField();
        descriptionInput.setPromptText("Opis wydarzenia");

        Spinner<Integer> startHourSpinner = new Spinner<>(0, 23, 12);
        Spinner<Integer> startMinuteSpinner = new Spinner<>(0, 59, 0);
        startHourSpinner.setEditable(true);
        startMinuteSpinner.setEditable(true);
        HBox startTimeBox = new HBox(5, startHourSpinner, new Label(":"), startMinuteSpinner);

        Spinner<Integer> endHourSpinner = new Spinner<>(0, 23, 13);
        Spinner<Integer> endMinuteSpinner = new Spinner<>(0, 59, 0);
        endHourSpinner.setEditable(true);
        endMinuteSpinner.setEditable(true);
        HBox endTimeBox = new HBox(5, endHourSpinner, new Label(":"), endMinuteSpinner);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> handleSubmit(titleInput.getText(), descriptionInput.getText(),
                startHourSpinner.getValue(), startMinuteSpinner.getValue(),
                endHourSpinner.getValue(), endMinuteSpinner.getValue()));

        VBox formLayout = new VBox(10, infoLbl, titleInput, descriptionInput, startTimeBox, endTimeBox, submitButton);
        formLayout.setPadding(new Insets(20));
        formLayout.setAlignment(Pos.CENTER);

        return formLayout;
    }

    private void handleSubmit(String title, String description, int startHour, int startMinute, int endHour, int endMinute) {
        try {
            String formattedTitle = title.replace(" ", "_");
            String formattedDescription = description.replace(" ", "_");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            String formattedStartTime = date.atTime(startHour, startMinute).format(formatter);
            String formattedEndTime = date.atTime(endHour, endMinute).format(formatter);

            CommAddEvent commAddEvent = new CommAddEvent(formattedTitle, formattedDescription,
                    formattedStartTime,
                    formattedEndTime);

            commAddEvent.sendAndGetResp();
            commAddEvent.parseResponse();
            refreshEventsDisplay();

        } catch (Exception ex) {
            System.err.println("Error submitting event: " + ex.getMessage());
        }
    }

    private void refreshEventsDisplay() {
        calendarController.updateCalendar();
        modalStage.close();
    }
}
