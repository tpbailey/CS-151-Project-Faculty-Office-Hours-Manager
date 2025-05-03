package s25.cs151.application;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
<<<<<<< HEAD
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
=======
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
>>>>>>> origin/master
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class searchOfficeHoursController extends Application {
    private ObservableList<Schedule> namesList;
    @FXML
    private TextField studentName;
<<<<<<< HEAD
    @FXML TableView<Schedule> scheduleTable;

    private List<Schedule> searchList(String searchWords, List<Schedule> listOfSchedules){
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));

        return listOfSchedules.stream().filter(schedule -> {
            return searchWordsArray.stream().allMatch((word ->
                    schedule.getStudentFullName().toLowerCase().contains(word.toLowerCase())));
        }).collect(Collectors.toList());
    }


    public void start(Stage stage) throws Exception, FileNotFoundException {
=======
    @FXML
    private TableView<Schedule> scheduleTable;

    @Override
    public void start(Stage stage) throws Exception {
>>>>>>> origin/master
        namesList = readScheduleCSV();

        Label header = new Label("Search Office Hours Schedule");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; ");
<<<<<<< HEAD
        header.setAlignment(Pos.CENTER);
=======
        header.setAlignment(javafx.geometry.Pos.CENTER);
>>>>>>> origin/master

        studentName = new TextField();
        studentName.setPromptText("Enter name");
        HBox searchBarContainer = new HBox(studentName);
<<<<<<< HEAD
        searchBarContainer.setAlignment(Pos.CENTER);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 200));
=======
        searchBarContainer.setAlignment(javafx.geometry.Pos.CENTER);
>>>>>>> origin/master

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> onSearchButtonClick());

<<<<<<< HEAD
        // Initialize scheduleTable
        scheduleTable = createScheduleTableView();
        scheduleTable.setItems(namesList); // Populate with initial data
=======
        Button deleteButton = new Button("Delete Selected");
        deleteButton.setOnAction(e -> onDeleteButtonClick());

        scheduleTable = createScheduleTableView();
        scheduleTable.setItems(namesList);
>>>>>>> origin/master

        // Configure sorting AFTER setting items
        TableColumn<Schedule, ?> dateCol = scheduleTable.getColumns().get(1);
        TableColumn<Schedule, ?> timeCol = scheduleTable.getColumns().get(2);
        dateCol.setSortType(TableColumn.SortType.DESCENDING);
        timeCol.setSortType(TableColumn.SortType.DESCENDING);
        scheduleTable.getSortOrder().setAll(dateCol, timeCol);
        scheduleTable.sort();

<<<<<<< HEAD
        VBox vb = new VBox(10, header, grid, searchBarContainer, searchButton, scheduleTable);
        studentName.prefWidthProperty().bind(vb.widthProperty().divide(2));

        // vb.getChildren().add(listView);

        vb.setSpacing(10);
        vb.setPadding(new Insets(10, 20, 10, 20));
        vb.setStyle("-fx-alignment: center;" + "-fx-background-color: radial-gradient(center 50% 50%, radius 60%,  #fceabb, #f8b500);");

        //namesList = readScheduleCSV();
=======
        VBox vb = new VBox(10, header, searchBarContainer, searchButton, scheduleTable, deleteButton);
        studentName.prefWidthProperty().bind(vb.widthProperty().divide(2));
        vb.setSpacing(10);
        vb.setPadding(new Insets(10, 20, 10, 20));
        vb.setStyle("-fx-alignment: center;" +
                "-fx-background-color: radial-gradient(center 50% 50%, radius 60%,  #fceabb, #f8b500);");
>>>>>>> origin/master

        Scene scene = new Scene(vb, 800, 500);
        stage.setScene(scene);
        stage.setTitle("Search Office Hours Schedule");
        stage.show();
<<<<<<< HEAD
        //so "enter name" appears
=======
>>>>>>> origin/master
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

        TableColumn<Schedule, LocalTime> timeSlotCol = new TableColumn<>("Time slot");
<<<<<<< HEAD
        //timeSlotCol.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));

        timeSlotCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(
                LocalTime.parse(cell.getValue().getTimeSlot().split("-")[0], DateTimeFormatter.ofPattern("H:mm"))
        ));

=======
        timeSlotCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(
                LocalTime.parse(cell.getValue().getTimeSlot().split("-")[0], DateTimeFormatter.ofPattern("H:mm"))
        ));
>>>>>>> origin/master
        timeSlotCol.setComparator(LocalTime::compareTo);

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

    private ObservableList<Schedule> readScheduleCSV() {
        namesList = FXCollections.observableArrayList();
        File file = new File("schedule.csv");
        if (!file.exists()) return namesList;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
<<<<<<< HEAD

                String[] parts = line.split(",", -1);  // Simple split on comma

=======
                String[] parts = line.split(",", -1);
>>>>>>> origin/master
                if (parts.length >= 8) {
                    String nameInCSV = parts[0].trim();
                    String date = parts[1].trim();
                    String timeSlot = parts[2].trim();
                    String courseName = parts[3];
                    String code = parts[4];
                    String section = parts[5];
<<<<<<< HEAD
                    String course = courseName+ " " + code + " " + section;
=======
                    String course = courseName + " " + code + " " + section;
>>>>>>> origin/master
                    String reason = parts[6].trim().isEmpty() ? "N/A" : parts[6].trim();
                    String comment = parts[7].trim().isEmpty() ? "N/A" : parts[7].trim();

                    namesList.add(new Schedule(nameInCSV, date, timeSlot, course, reason, comment));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return namesList;
    }

<<<<<<< HEAD
    private void onSearchButtonClick() {
        if(studentName.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Student's name is required.");
        }

        // Filter schedules based on search input
        ObservableList<Schedule> filteredSchedules = FXCollections.observableArrayList(
                searchList(studentName.getText(), namesList)
        );

        // Update the TableView with filtered results
        scheduleTable.setItems(filteredSchedules);
    }

    private void showAlert(Alert.AlertType alertType, String s) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Error");
        alert.setHeaderText(s);
        alert.showAndWait();
    }
}
=======
    private List<Schedule> searchList(String searchWords, List<Schedule> listOfSchedules) {
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));
        return listOfSchedules.stream().filter(schedule ->
                searchWordsArray.stream().allMatch(word ->
                        schedule.getStudentFullName().toLowerCase().contains(word.toLowerCase())
                )
        ).collect(Collectors.toList());
    }

    private void onSearchButtonClick() {
        if (studentName.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Student's name is required.");
            return;
        }
        ObservableList<Schedule> filteredSchedules = FXCollections.observableArrayList(
                searchList(studentName.getText(), namesList)
        );
        scheduleTable.setItems(filteredSchedules);
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

    private void saveAllSchedules(ObservableList<Schedule> schedules) {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("schedule.csv", false)))) {
            for (Schedule schedule : schedules) {
                pw.println(schedule.getStudentFullName() + "," +
                        schedule.getScheduleDate() + "," +
                        schedule.getTimeSlot() + "," +
                        schedule.getCourse() + "," +
                        schedule.getReason() + "," +
                        schedule.getComment());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String s) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }
}
>>>>>>> origin/master
