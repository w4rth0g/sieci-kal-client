package main.client;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DayBox extends VBox {
    private final Label dayLabel;
    private Button addButton;
    private Label eventLabel;

    public DayBox(int day, boolean hasEvent, String eventTitle) {
        super(5); // 5 is the spacing between children in VBox
        this.dayLabel = new Label(Integer.toString(day));
        this.dayLabel.getStyleClass().add("day-label");

        if (hasEvent) {
            this.eventLabel = new Label(eventTitle);
            this.eventLabel.getStyleClass().add("event-label");
            this.getChildren().addAll(dayLabel, eventLabel);
        } else {
            this.addButton = new Button("Add");
            this.addButton.getStyleClass().add("add-button");
            this.getChildren().addAll(dayLabel, addButton);
        }

        this.getStyleClass().add("day-box");
    }

}
