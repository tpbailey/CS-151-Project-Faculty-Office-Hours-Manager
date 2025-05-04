package s25.cs151.application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Schedule;
import model.dao.CSVScheduleDAO;
import model.dao.ScheduleDAO;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class searchOfficeHoursController extends Application {
    private ObservableList<Schedule> namesList;
    @FXML
    private TextField studentName;
    @FXML
    private TableView<Schedule> scheduleTable;

    public void start(Stage stage) throws Exception {
        namesList = readScheduleCSV();

        Label header = new Label("Search Office Hours Schedule");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");
        header.setAlignment(javafx.geometry.Pos.CENTER);

        studentName = new TextField();
        studentName.setPromptText("Enter name");

        studentName.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.trim().isEmpty()) {
                scheduleTable.setItems(namesList);
                scheduleTable.sort();
            }
        });

        HBox searchBarContainer = new HBox(studentName);
        searchBarContainer.setAlignment(javafx.geometry.Pos.CENTER);

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> onSearchButtonClick());

        Button deleteButton = new Button("Delete Selected");
        deleteButton.setOnAction(e -> onDeleteButtonClick());

        Button editButton = new Button("Edit Selected");
        editButton.setOnAction(e -> onEditButtonClick());

        scheduleTable = createScheduleTableView();
        scheduleTable.setItems(namesList);

        TableColumn<Schedule, ?> dateCol = scheduleTable.getColumns().get(1);
        TableColumn<Schedule, ?> timeCol = scheduleTable.getColumns().get(2);
        dateCol.setSortType(TableColumn.SortType.DESCENDING);
        timeCol.setSortType(TableColumn.SortType.DESCENDING);
        scheduleTable.getSortOrder().setAll(dateCol, timeCol);
        scheduleTable.sort();

        HBox buttonBox = new HBox(10, deleteButton, editButton);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);

        VBox vb = new VBox(10, header, searchBarContainer, searchButton, scheduleTable, buttonBox);
        studentName.prefWidthProperty().bind(vb.widthProperty().divide(2));
        vb.setSpacing(10);
        vb.setPadding(new Insets(10, 20, 10, 20));
        vb.setStyle("-fx-alignment: center;-fx-background-color: radial-gradient(center 50% 50%, radius 60%, #fceabb, #f8b500);");

        Scene scene = new Scene(vb, 800, 500);
        stage.setScene(scene);
        stage.setTitle("Search Office Hours Schedule");
        stage.show();
        searchButton.requestFocus();
    }

    private TableView<Schedule> createScheduleTableView() {
        TableView<Schedule> table = new TableView<>();

        TableColumn<Schedule, String> nameCol = new TableColumn<>("Student's name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("studentFullName"));

        TableColumn<Schedule, LocalDate> dateCol = new TableColumn<>("Schedule date");
        dateCol.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(LocalDate.parse(
                        cell.getValue().getScheduleDate(),
                        DateTimeFormatter.ofPattern("MM/dd/yyyy")
                ))
        );
        dateCol.setComparator(LocalDate::compareTo);

        TableColumn<Schedule, String> timeSlotCol = new TableColumn<>("Time slot");
        timeSlotCol.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));

        TableColumn<Schedule, String> courseCol = new TableColumn<>("Course");
        courseCol.setCellValueFactory(new PropertyValueFactory<>("course"));

        TableColumn<Schedule, String> reasonCol = new TableColumn<>("Reason");
        reasonCol.setCellValueFactory(new PropertyValueFactory<>("reason"));

        TableColumn<Schedule, String> commentCol = new TableColumn<>("Comment");
        commentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));

        table.getColumns().addAll(nameCol, dateCol, timeSlotCol, courseCol, reasonCol, commentCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }

    private ObservableList<Schedule> readScheduleCSV() throws IOException {
        ObservableList<Schedule> schedules = FXCollections.observableArrayList();
        Path path = Paths.get("schedule.csv");
        if (!Files.exists(path)) return schedules;

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                // split on every comma, keep empty tokens
                String[] toks = line.split(",", -1);
                if (toks.length < 6) continue;   // at minimum: name,date,start,end,reason,comment

                String student = toks[0].trim();
                String date    = toks[1].trim();
                String start   = toks[2].trim();
                String end     = toks[3].trim();

                // last two tokens are reason & comment
                String reason  = toks[toks.length - 2].trim();
                String comment = toks[toks.length - 1].trim();

                // everything in between [4 .. length-3] is the course field, re-joined with commas
                String course =
                        java.util.stream.IntStream.range(4, toks.length - 2)
                                .mapToObj(i -> toks[i].trim())
                                .collect(Collectors.joining(", "));

                // now build your Schedule exactly as before
                schedules.add(new Schedule(
                        student,
                        date,
                        start + "-" + end,
                        course,
                        reason,
                        comment
                ));
            }
        }

        return schedules;
    }


    private List<Schedule> searchList(String searchWords, List<Schedule> listOfSchedules) {
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));
        return listOfSchedules.stream().filter(schedule ->
                searchWordsArray.stream().allMatch(word ->
                        schedule.getStudentFullName().toLowerCase().contains(word.toLowerCase())
                )
        ).collect(Collectors.toList());
    }

    private void onSearchButtonClick() {
        String searchText = studentName.getText().trim();
        if (searchText.isEmpty()) {
            scheduleTable.setItems(namesList);
            scheduleTable.sort();
            return;
        }
        ObservableList<Schedule> filteredSchedules = FXCollections.observableArrayList(
                searchList(searchText, namesList)
        );
        scheduleTable.setItems(filteredSchedules);
        scheduleTable.sort();
    }

    private void onDeleteButtonClick() {
        Schedule selected = scheduleTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            namesList.remove(selected);
            saveAllSchedules(namesList);
            scheduleTable.setItems(namesList);
            scheduleTable.refresh();
        } else {
            showAlert(Alert.AlertType.ERROR, "Please select a schedule to delete.");
        }
    }

    private void onEditButtonClick() {
        Schedule selected = scheduleTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "Please select a schedule to edit.");
            return;
        }

        Stage editStage = new Stage();
        editStage.setTitle("Edit Office Hours Schedule");

        String[] timeParts = selected.getTimeSlot().split("-", 2);
        String startTime = timeParts.length > 0 ? timeParts[0].trim() : "";
        String endTime = timeParts.length > 1 ? timeParts[1].trim() : "";

        String[] courseParts = selected.getCourse().split(" ", 2);
        String coursePrefix = courseParts.length > 0 ? courseParts[0].trim() : "";
        String courseName = courseParts.length > 1 ? courseParts[1].trim() : "";

        TextField nameField = new TextField(selected.getStudentFullName());
        TextField dateField = new TextField(selected.getScheduleDate());
        TextField startTimeField = new TextField(startTime);
        TextField endTimeField = new TextField(endTime);
        TextField coursePrefixField = new TextField(coursePrefix);
        TextField courseNameField = new TextField(courseName);
        TextField reasonField = new TextField(selected.getReason());
        TextField commentField = new TextField(selected.getComment());

        Button saveButton = new Button("Save Changes");
        saveButton.setOnAction(e -> {
            selected.setStudentFullName(nameField.getText().trim());
            selected.setScheduleDate(dateField.getText().trim());
            selected.setTimeSlot(startTimeField.getText().trim() + "-" + endTimeField.getText().trim());
            selected.setCourse(coursePrefixField.getText().trim() + " " + courseNameField.getText().trim());
            selected.setReason(reasonField.getText().trim());
            selected.setComment(commentField.getText().trim());

            scheduleTable.refresh();

            ScheduleDAO dao = new CSVScheduleDAO();
            try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("schedule.csv", false)))) {
                for (Schedule s : namesList) {
                    dao.save(s);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error saving changes.");
            }

            editStage.close();
        });

        VBox form = new VBox(10,
                new Label("Student Name:"), nameField,
                new Label("Date (MM/dd/yyyy):"), dateField,
                new Label("Start Time:"), startTimeField,
                new Label("End Time:"), endTimeField,
                new Label("Course Prefix:"), coursePrefixField,
                new Label("Course Name:"), courseNameField,
                new Label("Reason:"), reasonField,
                new Label("Comment:"), commentField,
                saveButton);
        form.setPadding(new Insets(15));
        Scene scene = new Scene(form, 400, 500);
        editStage.setScene(scene);
        editStage.show();
    }

    private void saveAllSchedules(ObservableList<Schedule> schedules) {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("schedule.csv", false)))) {
            for (Schedule schedule : schedules) {
                String[] times = schedule.getTimeSlot().split("-", -1);
                String startTime = times.length > 0 ? times[0].trim() : "";
                String endTime = times.length > 1 ? times[1].trim() : "";

                String[] courseParts = schedule.getCourse().split(" ", 2);
                String coursePrefix = courseParts.length > 0 ? courseParts[0].trim() : "";
                String courseName = courseParts.length > 1 ? courseParts[1].trim() : "";

                pw.println(schedule.getStudentFullName() + "," +
                        schedule.getScheduleDate() + "," +
                        startTime + "," + endTime + "," +
                        coursePrefix + "," + courseName + "," +
                        schedule.getReason() + "," + schedule.getComment());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}