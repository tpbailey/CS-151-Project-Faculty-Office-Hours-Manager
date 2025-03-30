package s25.cs151.application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class timeslotController extends Application {
    private ComboBox<String> startTime;
    private ComboBox<String> endTime;
    private ObservableList<TimeSlot> timeSlotList;
    private TableView<TimeSlot> tableView;

    @Override
    public void start(Stage stage) {
        Label header = new Label("Define Semester's Time Slots");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");

        // creating time selectors
        startTime = new ComboBox<>();
        endTime = new ComboBox<>();
        ObservableList<String> times = generateTimes();
        startTime.setItems(times);
        endTime.setItems(times);
        startTime.setPromptText("From Hour");
        endTime.setPromptText("To Hour");

        // button to add a new time slot
        Button addButton = new Button("Add Time Slot");
        addButton.setOnAction(e -> addTimeSlot());

        HBox timeInputBox = new HBox(10, new Label("From:"), startTime, new Label("To:"), endTime, addButton);
        timeInputBox.setAlignment(Pos.CENTER);

        // creating the table view and observable list
        tableView = new TableView<>();
        timeSlotList = FXCollections.observableArrayList();
        tableView.setItems(timeSlotList);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<TimeSlot, String> fromCol = new TableColumn<>("From Hour");
        fromCol.setCellValueFactory(new PropertyValueFactory<>("from"));
        TableColumn<TimeSlot, String> toCol = new TableColumn<>("To Hour");
        toCol.setCellValueFactory(new PropertyValueFactory<>("to"));
        tableView.getColumns().addAll(fromCol, toCol);

        // load previously saved time slots from the CSV file
        loadTimeSlotsFromFile();

        // button to save all time slots into timeslots.csv
        Button saveButton = new Button("Save Time Slots");
        saveButton.setOnAction(e -> saveTimeSlots());

        HBox buttonBox = new HBox(saveButton);
        buttonBox.setAlignment(Pos.CENTER);

        // setting styling to be the same as other pages
        VBox root = new VBox(20, header, timeInputBox, tableView, buttonBox);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-alignment: center;-fx-background-color: radial-gradient(center 50% 50%, radius 60%,  #fceabb, #f8b500);");

        Scene scene = new Scene(root, 700, 500);
        stage.setTitle("Time Slots");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Creates time strings from 00:00 to 23:59 in 15-minute increments.
     */
    private ObservableList<String> generateTimes() {
        List<String> times = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                times.add(String.format("%d:%02d", hour, minute));
            }
        }
        return FXCollections.observableArrayList(times);
    }

    /**
     * Adds a new time slot to the table after validation and sorts the list by "From Hour" (ascending).
     */
    private void addTimeSlot() {
        String from = startTime.getValue();
        String to = endTime.getValue();

        if (from == null || to == null) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please select both From Hour and To Hour.");
            return;
        }

        if (!isValidTimeSlot(from, to)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "From Hour must be earlier than To Hour.");
            return;
        }

        TimeSlot slot = new TimeSlot(from, to);
        timeSlotList.add(slot);

        // Sort the list by "From Hour" in ascending order
        timeSlotList.sort((ts1, ts2) -> Integer.compare(convertToMinutes(ts1.getFrom()), convertToMinutes(ts2.getFrom())));

        // Optionally clear selections after adding
        startTime.getSelectionModel().clearSelection();
        endTime.getSelectionModel().clearSelection();
    }

    /**
     * Checks if the start time is before the end time.
     */
    private boolean isValidTimeSlot(String from, String to) {
        return convertToMinutes(from) < convertToMinutes(to);
    }

    /**
     * Converts a time string (e.g., "3:30") to minutes since midnight.
     */
    private int convertToMinutes(String time) {
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        return hour * 60 + minute;
    }

    /**
     * Loads time slot records from the timeslots.csv file into the observable list.
     */
    private void loadTimeSlotsFromFile() {
        File file = new File("timeslots.csv");
        if (!file.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            // Each line should be in the format "from,to"
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String from = parts[0].trim();
                    String to = parts[1].trim();
                    TimeSlot ts = new TimeSlot(from, to);
                    timeSlotList.add(ts);
                }
            }
            // Sort the list after loading
            timeSlotList.sort((ts1, ts2) -> Integer.compare(convertToMinutes(ts1.getFrom()), convertToMinutes(ts2.getFrom())));
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load time slots: " + ex.getMessage());
        }
    }

    /**
     * Saves the list of time slots to the timeslots.csv file.
     */
    private void saveTimeSlots() {
        if (timeSlotList.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Information", "No time slots to save.");
            return;
        }
        File file = new File("timeslots.csv");
        try (FileWriter fw = new FileWriter(file, false);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw)) {

            // Write each time slot to the file in "from,to" format
            for (TimeSlot ts : timeSlotList) {
                pw.println(ts.getFrom() + "," + ts.getTo());
            }
            showAlert(Alert.AlertType.INFORMATION, "Success", "Time slots saved successfully.");
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save time slots: " + ex.getMessage());
        }
    }

    /**
     * Displays an alert dialog.
     */
    private void showAlert(Alert.AlertType type, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle("Time Slots");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
