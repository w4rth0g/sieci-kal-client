package main.client.components;

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
            this.dayLabel.getStyleClass().add("day-box-entered");
            eventDotsContainer.getChildren().forEach(ch -> ch.getStyleClass().add("day-box-entered"));
        });
        this.setOnMouseExited(e -> {
            this.setCursor(Cursor.DEFAULT);
            this.getStyleClass().remove("day-box-entered");
            this.dayLabel.getStyleClass().remove("day-box-entered");
            eventDotsContainer.getChildren().forEach(ch -> ch.getStyleClass().remove("day-box-entered"));
        });

        this.getStyleClass().add("day-box");

        this.setOnMouseClicked(e -> {
            showModal();
        });
    }

    private void showModal() {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL); // Set the window as a modal dialog
        modalStage.setTitle("Szczegóły " + date);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        // Layout for modal content
        BorderPane modalLayout = new BorderPane();
        scrollPane.setContent(modalLayout);

        // Date label
        Label dateLabel = new Label(date.toString());
        modalLayout.setTop(dateLabel);

        // Event boxes container
        VBox eventBoxes = new VBox(5);
        // ... Add your event boxes here

        // Hours on the right side
        VBox hoursList = new VBox(5);
        for (int hour = 0; hour < 24; hour++) {
            Label hourLabel = new Label(String.format("%02d:00", hour));
            hoursList.getChildren().add(hourLabel);
        }

        // Close button
        HBox bottomSection = new HBox();

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> modalStage.close());
        bottomSection.getChildren().addAll(closeButton, spacer, this.addButton);
        modalLayout.setBottom(bottomSection);

        // Set the main content and hours list to the modal layout
        modalLayout.setCenter(eventBoxes);
        modalLayout.setRight(hoursList);

        // Create a scene with the layout
        Scene modalScene = new Scene(scrollPane, 300, 400); // Adjust the size as needed
        modalStage.setScene(modalScene);
        modalStage.showAndWait(); // Show the modal and wait for it to be closed
    }

    // Additional methods for updating the DayBox, handling events, etc.
}
