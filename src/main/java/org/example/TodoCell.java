package org.example;

import javafx.scene.control.ListCell;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;

public class TodoCell extends ListCell<Todo> {

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("HH:mm");
    String[] weekDay = {"日", "月", "火", "水", "木", "金", "土"};

    @Override
    protected void updateItem(Todo todo, boolean empty) {
        super.updateItem(todo, empty);

        if (empty || todo == null) {
            setGraphic(null);
            return;
        }

        ImageView icon = new ImageView(
                new Image(getClass().getResourceAsStream("/icons/" + todo.getIcon()))
        );
        icon.setFitWidth(24);
        icon.setFitHeight(24);

        // 優先度を表示
        String priorityLevel = "";
        int priority = todo.getPriority();
        if (0 == priority) {
            priorityLevel = "★";
        }
        else if (0 < priority && priority < 33) {
            priorityLevel = "★★";
        }
        else if (33 <= priority && priority < 67) {
            priorityLevel = "★★★";
        }
        else if (67 <= priority && priority < 100) {
            priorityLevel = "★★★★";
        }
        else if (priority == 100) {
            priorityLevel = "★★★★★";
        }

        Label todoLabel = new Label(
                todo.getStart().getMonthValue() + "月 "
                    + todo.getStart().getDayOfMonth() + "日 ("
                    + weekDay[todo.getStart().getDayOfWeek().getValue()] + ") "
                    + todo.getStart().format(formatter) + " ～ "
                    + todo.getEnd().format(formatter) + "  "
                    + priorityLevel + "\n"
                    + todo.getTitle() + "\n"
                    + todo.getMemo()
        );
        todoLabel.setStyle("""
                    -fx-font-size: 16;
                """);

        VBox text = new VBox(todoLabel);
        HBox root = new HBox(10, icon, text);

        setGraphic(root);
    }
}