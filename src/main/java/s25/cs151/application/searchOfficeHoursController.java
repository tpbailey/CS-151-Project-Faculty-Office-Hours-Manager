package s25.cs151.application;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
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
    @FXML TableView<Schedule> scheduleTable;

    private List<Schedule> searchList(String searchWords, List<Schedule> listOfSchedules){
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));

        return listOfSchedules.stream().filter(schedule -> {
            return searchWordsArray.stream().allMatch((word ->
                    schedule.getStudentFullName().toLowerCase().contains(word.toLowerCase())));
        }).collect(Collectors.toList());
    }


    public void start(Stage stage) throws Exception, FileNotFoundException {
        namesList = readScheduleCSV();

        Label header = new Label("Search Office Hours Schedule");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; ");
        header.setAlignment(Pos.CENTER);

        studentName = new TextField();
        studentName.setPromptText("Enter name");
        HBox searchBarContainer = new HBox(studentName);
        searchBarContainer.setAlignment(Pos.CENTER);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 200));

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> onSearchButtonClick());

        // Initialize scheduleTable
        scheduleTable = createScheduleTableView();
        scheduleTable.setItems(namesList); // Populate with initial data

        // Configure sorting AFTER setting items
        TableColumn<Schedule, ?> dateCol = scheduleTable.getColumns().get(1);
        TableColumn<Schedule, ?> timeCol = scheduleTable.getColumns().get(2);
        dateCol.setSortType(TableColumn.SortType.DESCENDING);
        timeCol.setSortType(TableColumn.SortType.DESCENDING);
        scheduleTable.getSortOrder().setAll(dateCol, timeCol);
        scheduleTable.sort();

        VBox vb = new VBox(10, header, grid, searchBarContainer, searchButton, scheduleTable);
        studentName.prefWidthProperty().bind(vb.widthProperty().divide(2));

        // vb.getChildren().add(listView);

        vb.setSpacing(10);
        vb.setPadding(new Insets(10, 20, 10, 20));
        vb.setStyle("-fx-alignment: center;" + "-fx-background-color: radial-gradient(center 50% 50%, radius 60%,  #fceabb, #f8b500);");

        //namesList = readScheduleCSV();

        Scene scene = new Scene(vb, 800, 500);
        stage.setScene(scene);
        stage.setTitle("Search Office Hours Schedule");
        stage.show();
        //so "enter name" appears
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
        //timeSlotCol.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));

        timeSlotCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(
                LocalTime.parse(cell.getValue().getTimeSlot().split("-")[0], DateTimeFormatter.ofPattern("H:mm"))
        ));

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

                String[] parts = line.split(",", -1);  // Simple split on comma

                if (parts.length >= 8) {
                    String nameInCSV = parts[0].trim();
                    String date = parts[1].trim();
                    String timeSlot = parts[2].trim();
                    String courseName = parts[3];
                    String code = parts[4];
                    String section = parts[5];
                    String course = courseName+ " " + code + " " + section;
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
