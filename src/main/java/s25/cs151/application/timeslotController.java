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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
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

        //  creating the table view!
        tableView = new TableView<>();
        timeSlotList = FXCollections.observableArrayList();
        tableView.setItems(timeSlotList);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<TimeSlot, String> fromCol = new TableColumn<>("From Hour");
        fromCol.setCellValueFactory(new PropertyValueFactory<>("from"));
        TableColumn<TimeSlot, String> toCol = new TableColumn<>("To Hour");
        toCol.setCellValueFactory(new PropertyValueFactory<>("to"));
        tableView.getColumns().addAll(fromCol, toCol);

        // this is to upload everything into the timeslots.csv
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
        //stage.setScene(new Scene(vb, 700, 500));
        stage.show();
    }

    /**
     * creating times from 00:00 to 23:59
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
     * adding new time slots to table validation
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
        // Optionally clear selections after adding
        startTime.getSelectionModel().clearSelection();
        endTime.getSelectionModel().clearSelection();
    }

    /**
     * is the start time before the end time?
     */
    private boolean isValidTimeSlot(String from, String to) {
        return convertToMinutes(from) < convertToMinutes(to);
    }

    private int convertToMinutes(String time) {
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        return hour * 60 + minute;
    }

    /**
     * saving list of times into the timeslots.csv
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

            // Write header
            pw.println("From,To");
            // Write each time slot
            for (TimeSlot ts : timeSlotList) {
                pw.println(ts.getFrom() + "," + ts.getTo());
            }
            showAlert(Alert.AlertType.INFORMATION, "Success", "Time slots saved successfully.");
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save time slots: " + ex.getMessage());
        }
    }

    /**
     * error warning
     */
    private void showAlert(Alert.AlertType type, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle("Time Slots");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
