package main.client.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
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

    public DayBox(int day, int eventCount, LocalDate date, List<Event> eventList) {
        super(5);
        this.dayLabel = new Label(Integer.toString(day));
        this.getStylesheets().add("/style.css");
        this.dayLabel.getStyleClass().add("day-label");

        this.date = date;
        this.eventsForDay = eventList;

        this.setPrefHeight(100);
        this.setMaxHeight(100);

        // Initialize the container for event dots and the "Add" button
        this.eventDotsContainer = new HBox();
        this.eventDotsContainer.setSpacing(1);
        eventDotsContainer.setAlignment(Pos.CENTER); // Align content to the center

        // Create and add dots to the eventDotsContainer
        for (int i = 0; i < Math.min(eventCount, 4); i++) {
            Label dot = new Label("•");
            dot.getStyleClass().add("event-dot");
            eventDotsContainer.getChildren().add(dot);
        }

        if (eventCount > 4) {
            Label moreEvents = new Label("+" + (eventCount - 4));
            moreEvents.getStyleClass().add("more-events");
            eventDotsContainer.getChildren().add(moreEvents);
        }

        // Initialize the "Add" button
        this.addButton = new Button("Add");
        this.addButton.getStyleClass().add("add-button");

        // Add the day label and the container to the VBox
        this.getChildren().addAll(dayLabel, eventDotsContainer);

        // Handle mouse hover events
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
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Szczegóły " + date);

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
        modalStage.setScene(modalScene);
        modalStage.showAndWait();
    }

}
