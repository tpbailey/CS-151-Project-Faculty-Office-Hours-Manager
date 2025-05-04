package s25.cs151.application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;


import java.io.*;

public class scheduleController extends Application{
    private TextField studentFullName;
    DatePicker scheduleDatePicker = new DatePicker();
    private ComboBox<String> timeDropdown;
    private ComboBox<String> courseDropdown;
    private TextField reason;
    private TextField comment;

    @Override
    public void start(Stage stage) throws Exception, FileNotFoundException {

        Label header = new Label("Schedule Office Hour");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; ");
        header.setAlignment(Pos.CENTER);

        studentFullName = new TextField();
        Label studentFullNameLabel = new Label("Enter student's full name");
        studentFullName.setPromptText("Enter full name");
        Label scheduleDateLabel = new Label("Enter schedule date");
        Label timeSlotLabel = new Label("Select time slot");
        Label courseLabel = new Label("Select course");
        reason = new TextField();
        Label reasonLabel = new Label("Enter reason (optional)");
        reason.setPromptText("Enter reason");
        comment = new TextField();
        Label commentLabel = new Label("Enter comment (optional)");
        comment.setPromptText("Enter comment");
        VBox root = new VBox(10, header);
        root.setPadding(new Insets(10, 20, 10, 20));
        root.setStyle("-fx-alignment: center;-fx-background-color: radial-gradient(center 50% 50%, radius 60%,  #fceabb, #f8b500);");

        scheduleDatePicker.setValue(LocalDate.now());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedDate = scheduleDatePicker.getValue().format(formatter);
        //System.out.println("Selected Date: " + formattedDate);

        timeDropdown = new ComboBox<>();
        // Extract time slots from CSV
        try (BufferedReader br = new BufferedReader(new FileReader("timeslots.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                timeDropdown.getItems().add(line.trim()); // Add each line as a time slot
            }
        } catch (IOException e) {
            System.err.println("Error reading time slots CSV file: " + e.getMessage());
        }
        if (!timeDropdown.getItems().isEmpty()) {
            timeDropdown.setValue(timeDropdown.getItems().get(0)); // Use index 0 to get the first item
        }


        courseDropdown = new ComboBox<>();
        // Extract course from CSV
        try (BufferedReader br = new BufferedReader(new FileReader("course.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                courseDropdown.getItems().add(line.trim()); // Add each line as a time slot
            }
        } catch (IOException e) {
            System.err.println("Error reading course CSV file: " + e.getMessage());
        }
        if (!courseDropdown.getItems().isEmpty()) {
            courseDropdown.setValue(courseDropdown.getItems().get(0)); // Get the first item using index 0
        }


        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> onSubmitButtonClick());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 200));
        grid.setHgap(30);
        grid.setVgap(20);
        grid.add(studentFullNameLabel, 0, 0);
        grid.add(studentFullName, 1, 0);
        grid.add(scheduleDateLabel, 0, 1);
        grid.add(scheduleDatePicker, 1, 1);
        grid.add(timeSlotLabel, 0, 2);
        grid.add(timeDropdown, 1, 2);
        grid.add(courseLabel, 0, 3);
        grid.add(courseDropdown, 1, 3);
        grid.add(reasonLabel, 0, 4);
        grid.add(reason, 1, 4);
        grid.add(commentLabel, 0, 5);
        grid.add(comment, 1, 5);
        grid.add(submitButton, 1, 6);

        VBox vb = new VBox(header, grid);
        //vb.setAlignment(Pos.CENTER);
        vb.setSpacing(10);
        vb.setStyle("-fx-alignment: center;" + "-fx-background-color: radial-gradient(center 50% 50%, radius 60%,  #fceabb, #f8b500);");

        stage.setScene(new Scene(vb, 700,500));
        stage.setTitle("Schedule office hour");
        stage.show();


    }

    private void onSubmitButtonClick() {
        // Validate fields
        if (studentFullName.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Student's full name is required.");
            return;
        }
        if (scheduleDatePicker.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Schedule date is required.");
            return;
        }
        if (timeDropdown.getValue() == null || timeDropdown.getValue().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Time slot is required.");
            return;
        }
        if (courseDropdown.getValue() == null || courseDropdown.getValue().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Course is required.");
            return;
        }

        try {
            // Check for duplicate schedules before saving
            try (BufferedReader br = new BufferedReader(new FileReader("schedule.csv"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 6 &&
                            parts[0].equals(studentFullName.getText()) &&
                            parts[1].equals(scheduleDatePicker.getValue().toString())) {
                        showAlert(Alert.AlertType.ERROR, "Duplicate Schedule", "This schedule already exists.");
                        return; // Exit if duplicate found
                    }
                }
            }

            // Create a schedule object
            Schedule schedule = new Schedule(
                    studentFullName.getText(),
                    scheduleDatePicker.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                    timeDropdown.getValue(),
                    courseDropdown.getValue(),
                    reason.getText(),
                    comment.getText()
            );

            //ScheduleDAO dao = new CSVScheduleDAO();
            //dao.save(schedule);  //uses the polymorphic method

            writescheduleCSV(schedule);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Schedule saved successfully!");

            // Save the schedule and display the table
            //displayscheduleTb();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while processing the schedule.");
        }
    }

    private void writescheduleCSV(Schedule schedule) {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("schedule.csv", true)))) {
            // split the three-part course string on commas (and trim all parts)
            String[] courseParts = schedule.getCourse().split("\\s*,\\s*");
            String prefix     = courseParts.length > 0 ? courseParts[0] : "";
            String courseName = courseParts.length > 1 ? courseParts[1] : "";
            String section    = courseParts.length > 2 ? courseParts[2] : "";

            // now write exactly 8 columns: student, date, start-end, prefix, name, section, reason, comment
            pw.printf("%s,%s,%s,%s,%s,%s,%s,%s%n",
                    schedule.getStudentFullName(),
                    schedule.getScheduleDate(),
                    schedule.getTimeSlot(),
                    prefix,
                    courseName,
                    section,
                    schedule.getReason(),
                    schedule.getComment()
            );
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save schedule.");
        }
    }




    /*private void displayscheduleTb() {
        TableView<Schedule> table = new TableView<>();
        ObservableList<Schedule> schedules = FXCollections.observableArrayList();

        try (BufferedReader br = new BufferedReader(new FileReader("schedule.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    schedules.add(new Schedule(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        TableColumn<Schedule, String> nameCol = new TableColumn<>("Student Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("studentFullName"));

        TableColumn<Schedule, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("scheduleDate"));

        TableColumn<Schedule, String> timeSlotCol = new TableColumn<>("Time Slot");
        timeSlotCol.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));

        TableColumn<Schedule, String> courseCol = new TableColumn<>("Course");
        courseCol.setCellValueFactory(new PropertyValueFactory<>("course"));

        TableColumn<Schedule, String> reasonCol = new TableColumn<>("Reason");
        reasonCol.setCellValueFactory(new PropertyValueFactory<>("reason"));

        TableColumn<Schedule, String> commentCol = new TableColumn<>("Comment");
        commentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));

        table.getColumns().addAll(nameCol, dateCol, timeSlotCol, courseCol, reasonCol, commentCol);
        table.setItems(schedules);

        VBox container = new VBox(10, new Label("Scheduled Office Hours:"), table);
        container.setPadding(new Insets(15));
        Stage tableStage = new Stage();
        tableStage.setTitle("Schedule Table");
        tableStage.setScene(new Scene(container, 800, 500)); // Wider for 6 columns
        tableStage.show();
    }*/




    private void showAlert(Alert.AlertType alertType, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Notification");
        alert.setHeaderText(header); // Add the header argument
        alert.setContentText(content); // Add the content argument
        alert.showAndWait();
    }

}