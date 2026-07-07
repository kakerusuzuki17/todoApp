package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.Dialog;
import javafx.geometry.Insets;

import java.io.File;
import java.time.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class MainController {

    @FXML
    private TextField titleField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private ComboBox<Integer> startHourComboBox;
    @FXML
    private ComboBox<Integer> startMinuteComboBox;
    @FXML
    private ComboBox<Integer> endHourComboBox;
    @FXML
    private ComboBox<Integer> endMinuteComboBox;
    @FXML
    private TextArea memoTextArea;
    @FXML
    private Slider prioritySlider;
    @FXML
    private Label priorityLabel;
    @FXML
    private GridPane calendarGridPane;
    @FXML
    private ListView<Todo> todoListView;
    @FXML
    private ImageView iconImageView;
    @FXML
    private Label yearMonthLabel;
    @FXML
    private Pane achievePane;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button achieveButton;
    private String selectedIcon;
    private StackPane selectedCell;
    private StackPane todayCell;

    private final ObservableList<Todo> todos =
            FXCollections.observableArrayList();

    private final Random random = new Random();

    YearMonth yearMonth = YearMonth.now();

    @FXML
    public void initialize() {
        //各要素に値をセット
        startDatePicker.setValue(LocalDate.now());

        // 時
        for (int i = 0; i < 24; i++) {
            startHourComboBox.getItems().add(i);
            endHourComboBox.getItems().add(i);
        }
        startHourComboBox.setValue(12);
        endHourComboBox.setValue(13);

        // 分
        for (int i = 0; i < 60; i = i + 15) {
            startMinuteComboBox.getItems().add(i);
            endMinuteComboBox.getItems().add(i);
        }
        startMinuteComboBox.setValue(0);
        endMinuteComboBox.setValue(0);

        // 優先度
        prioritySlider.setValue(50);
        priorityLevelSet((int) prioritySlider.getValue());

        // アイコン
        iconImageView.setImage(new Image("/icons/hospital1.png"));
        selectedIcon = "hospital1.png";

        todoListView.getSelectionModel().selectedItemProperty().addListener( // 予定選択時のみボタンを有効に
                (obs, oldTodo, newTodo) -> {
                    editButton.setDisable(newTodo == null);
                    deleteButton.setDisable(newTodo == null);
                    achieveButton.setDisable(newTodo == null);
                }
        );

        makeCalendar(); // カレンダー日付作成
        makeTodoList(); // 予定リスト作成
        setAchievePane(); // 達成済みアイコンをランダム表示
    }

    private void makeCalendar() { // カレンダー日付作成
        calendarGridPane.getChildren().clear();
        yearMonthLabel.setText(yearMonth.toString());
        int lastDay = yearMonth.lengthOfMonth(); // 月末
        int startCol = yearMonth.atDay(1).getDayOfWeek().getValue() % 7;
        String[] weekDay = {"日", "月", "火", "水", "木", "金", "土"};

        for (int col = 0; col < 7; col++) {
            Label weekDayLabel = new Label(weekDay[col]);
            weekDayLabel.setPrefWidth(120);
            weekDayLabel.setAlignment(Pos.CENTER);

            if (col == 0) { //日曜
                weekDayLabel.setStyle("""
                    -fx-font-size: 16;
                    -fx-font-weight: bold;
                    -fx-text-fill: red;
                """);
            }
            else if (col == 6) { //土曜
                weekDayLabel.setStyle("""
                    -fx-font-size: 16;
                    -fx-font-weight: bold;
                    -fx-text-fill: blue;
                """);
            }
            else { //平日
                weekDayLabel.setStyle("""
                -fx-font-size: 16;
                -fx-font-weight: bold;
            """);
            }
            calendarGridPane.add(weekDayLabel, col, 0);
        }

        for (int day = 1; day <= lastDay; day++) {
            int index = startCol + day - 1;
            int col = index % 7;
            int row = index / 7 + 1;

            StackPane cell = new StackPane();
            LocalDate finalDay = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), day);
            List<Todo> list = TodoDAO.selectByDate(finalDay);
            HBox iconBox = new HBox(2);
            StackPane.setAlignment(iconBox, Pos.TOP_RIGHT);
            iconBox.setPadding(new Insets(5, 5, 0, 30));
            cell.getChildren().add(iconBox);
            for (Todo todo : list) {
                ImageView icon = new ImageView(
                        new Image(getClass().getResourceAsStream("/icons/" + todo.getIcon()))
                );
                icon.setFitWidth(16);
                icon.setFitHeight(16);

                iconBox.getChildren().add(icon);
                if (iconBox.getChildren().size() >= 3) { // アイコン表示は3つまで
                    break;
                }
            }

            cell.setPrefSize(120, 100);
            if (yearMonth.equals(YearMonth.now()) && LocalDate.now().getDayOfMonth() == day) { // 今日の日付のセルの色変更
                cell.setStyle("""
                    -fx-background-color: lightgray;
                    -fx-border-color: lightgray;
                """);
                todayCell = cell;
            }
            else {
                cell.setStyle("""
                    -fx-background-color: white;
                    -fx-border-color: lightgray;
                """);
            }

            Label dayLabel = new Label(String.valueOf(day));
            StackPane.setAlignment(dayLabel, Pos.TOP_LEFT);
            dayLabel.setPadding(new Insets(5));
            dayLabel.setPrefSize(80, 80);
            if (col == 0) { //日曜
                dayLabel.setStyle("""
                    -fx-font-size: 16;
                    -fx-font-weight: bold;
                    -fx-text-fill: red;
                """);
            }
            else if (col == 6) { //土曜
                dayLabel.setStyle("""
                    -fx-font-size: 16;
                    -fx-font-weight: bold;
                    -fx-text-fill: blue;
                """);
            }
            else { //平日
                dayLabel.setStyle("""
                -fx-font-size: 16;
                -fx-font-weight: bold;
            """);
            }
            cell.getChildren().add(dayLabel);

            cell.setOnMouseClicked(e -> { // 日付がクリックされたら
                // 前回選択セルの色を元に戻す
                if (selectedCell != null) {
                    if (selectedCell == todayCell) { // 今日の日付
                        selectedCell.setStyle("""
                            -fx-background-color: lightgray;
                            -fx-border-color: lightgray;
                        """);
                    } else { // その他の日付
                        selectedCell.setStyle("""
                            -fx-background-color: white;
                            -fx-border-color: lightgray;
                        """);
                    }
                }
                selectedCell = cell;
                // 選択されたセルの色変更
                selectedCell.setStyle("""
                    -fx-background-color: #CDE8FF;
                    -fx-border-color: dodgerblue;
                    -fx-border-width: 2;
                """);

                showTodo(finalDay);
            });

            calendarGridPane.add(cell, col, row);
        }
    }

    private void makeTodoList() { // 予定リスト作成
        todoListView.getItems().clear();
        todoListView.setItems(todos);

        todoListView.setOnMouseClicked(e -> { // 予定がクリックされたら各値を入力フィールドに表示
            Todo todoSelected = todoListView.getSelectionModel().getSelectedItem();
            if (todoSelected != null) {
//                editButton.setDisable(true); // 編集ボタンを有効に
                titleField.setText(todoSelected.getTitle());
                startDatePicker.setValue(todoSelected.start.toLocalDate());
                startHourComboBox.setValue(todoSelected.start.getHour());
                startMinuteComboBox.setValue(todoSelected.start.getMinute());
                endHourComboBox.setValue(todoSelected.end.getHour());
                endMinuteComboBox.setValue(todoSelected.end.getMinute());
                prioritySlider.setValue(todoSelected.getPriority());
                priorityLevelSet((int) prioritySlider.getValue());
                iconImageView.setImage(new Image(getClass().getResourceAsStream("/icons/" + todoSelected.getIcon())));
                memoTextArea.setText(todoSelected.getMemo());
            }
        });

        todoListView.setCellFactory(list -> new TodoCell());
        todos.clear();
        todos.addAll(TodoDAO.selectMonth(yearMonth));
    }

    private void setAchievePane() { // 達成アイコン表示
        achievePane.getChildren().clear();
        List<Todo> AchieveIcons = TodoDAO.selectAchieve();
        for (Todo icon : AchieveIcons) {
            ImageView image = new ImageView(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/" + icon.getIcon()))));
            image.setFitWidth(12 + (icon.getPriority() * 0.2));
            image.setFitHeight(12 + (icon.getPriority() * 0.2));
            // 左下付近にランダム配置
            image.setLayoutX(random.nextInt(200));
            image.setLayoutY(random.nextInt(60));
            // 回転
            image.setRotate(random.nextInt(61) - 30); // -30°～30°
            achievePane.getChildren().add(image);
        }
    }
    private void showTodo(LocalDate date) { // 日付をクリックしたとき

        todoListView.getItems().clear();
        todoListView.getItems().addAll(
                TodoDAO.selectByDate(date)
        );
        startDatePicker.setValue(date);
    }

    @FXML
    private void onPriorityChanged() { // 優先度スライダーを触っている時

        priorityLevelSet((int) prioritySlider.getValue());
    }

    private void priorityLevelSet(int priority) { //優先度を数値から段階表示
        if (0 == priority) {
            priorityLabel.setText("★");
        }
        else if (0 < priority && priority < 33) {
            priorityLabel.setText("★★");
        }
        else if (33 <= priority && priority < 67) {
            priorityLabel.setText("★★★");
        }
        else if (67 <= priority && priority < 100) {
            priorityLabel.setText("★★★★");
        }
        else if (priority == 100) {
            priorityLabel.setText("★★★★★");
        }
    }

    @FXML
    private void onSelectIcon() { //アイコン選択画面押下

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("アイコン選択");
        TilePane tilePane = new TilePane();
        tilePane.setHgap(10);
        tilePane.setVgap(10);
        tilePane.setPrefColumns(7); // 1行に7個並べる

        ScrollPane scrollPane = new ScrollPane(tilePane);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportWidth(350);
        scrollPane.setPrefViewportHeight(250);

        File folder = new File("src/main/resources/icons"); // アイコンフォルダ内のファイル名取得
        String[] icons = folder.list();

        for (String icon : icons) {
            Image image = new Image(
                    getClass().getResourceAsStream("/icons/" + icon));

            ImageView imageView = new ImageView(image);

            imageView.setFitWidth(40);
            imageView.setFitHeight(40);

            imageView.setOnMouseClicked(e -> {
                dialog.setResult(icon);
                dialog.close();
            });
            tilePane.getChildren().add(imageView);
        }
        dialog.getDialogPane().setContent(tilePane);
        dialog.getDialogPane().setContent(scrollPane);

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(icon -> {
            selectedIcon = icon;
            iconImageView.setImage(
                    new Image(getClass().getResourceAsStream("/icons/" + icon)
                    )
            );
        });
    }

    @FXML
    private void onPrevMonthButtonClick() { //前の月ボタン押下
        yearMonth = yearMonth.minusMonths(1);
        makeCalendar();
        makeTodoList();
    }

    @FXML
    private void onNextMonthButtonClick() { //次の月ボタン押下
        yearMonth = yearMonth.plusMonths(1);
        makeCalendar();
        makeTodoList();
    }

    @FXML
    private void onAddButtonClick() { //追加ボタン押下

        String title = titleField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = startDatePicker.getValue();
        Integer startHour = startHourComboBox.getValue();
        Integer startMinute = startMinuteComboBox.getValue();
        Integer endHour = endHourComboBox.getValue();
        Integer endMinute = endMinuteComboBox.getValue();
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(startHour, startMinute));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(endHour, endMinute));
        String memo = memoTextArea.getText();
        Integer priority = (int) prioritySlider.getValue();

        if (checkData(title, start, end, memo)) {

            TodoDAO.insert(title, start, end, priority, memo, selectedIcon);
            titleField.clear();
            todos.clear();
            memoTextArea.clear();
            todos.addAll(TodoDAO.selectMonth(yearMonth));
            makeCalendar();
        }
    }

    @FXML
    private void onDeleteButtonClick() { //削除ボタン押下

        Todo todo = todoListView.getSelectionModel().getSelectedItem();

        if (todo != null) {
            TodoDAO.delete(todo.getId());
            todos.clear();
            todos.addAll(TodoDAO.selectMonth(yearMonth));
            makeCalendar();
        }
    }

    @FXML
    private void onEditButtonClick() { //編集ボタン押下

        String title = titleField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = startDatePicker.getValue();
        Integer startHour = startHourComboBox.getValue();
        Integer startMinute = startMinuteComboBox.getValue();
        Integer endHour = endHourComboBox.getValue();
        Integer endMinute = endMinuteComboBox.getValue();
        LocalTime startTime = LocalTime.of(startHour, startMinute);
        LocalDateTime start = LocalDateTime.of(startDate, startTime);
        LocalTime endTime = LocalTime.of(endHour, endMinute);
        LocalDateTime end = LocalDateTime.of(endDate, endTime);
        String memo = memoTextArea.getText();
        Integer priority = (int) prioritySlider.getValue();
        int id = todoListView.getSelectionModel().getSelectedItem().getId();

        if (checkData(title, start, end, memo)) {

            TodoDAO.update(title, start, end, priority, memo, selectedIcon, id);
            titleField.clear();
            todos.clear();
            memoTextArea.clear();
            todos.addAll(TodoDAO.selectMonth(yearMonth));
            makeCalendar();
        }
    }

    @FXML
    private void onAchieveButtonClick() { //達成ボタン押下

        Todo todo = todoListView.getSelectionModel().getSelectedItem();

        if (todo != null) {
            TodoDAO.achieve(todo.getId());
            todos.clear();
            todos.addAll(TodoDAO.selectMonth(yearMonth));
            makeCalendar();
            setAchievePane();
        }
    }

    private boolean checkData(String title, LocalDateTime start, LocalDateTime end, String memo) {
        if (title.isBlank()) { // タイトルが入力されていない場合
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("入力エラー");
            alert.setHeaderText("タイトルが入力されていません");
            alert.setContentText("タイトルを入力してください。");
            alert.showAndWait();
            return false;
        }

        if (title.length() > 50) { // タイトルが50文字より長い場合
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("入力エラー");
            alert.setHeaderText("タイトルが長すぎます");
            alert.setContentText("タイトルは50字以内で入力してください。");
            alert.showAndWait();
            return false;
        }

        if (!start.isBefore(end)) { // 開始・終了時刻
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("入力エラー");
            alert.setHeaderText("時刻が正しくありません");
            alert.setContentText("開始時刻は終了時刻より前にしてください。");
            alert.showAndWait();
            return false;
        }

        if (memo.length() > 500) { // メモが500文字より長い場合
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("入力エラー");
            alert.setHeaderText("メモが長すぎます");
            alert.setContentText("メモは500字以内で入力してください。");
            alert.showAndWait();
            return false;
        }

        return true;
    }
}