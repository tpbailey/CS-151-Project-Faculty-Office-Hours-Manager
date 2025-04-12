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
        timeDropdown.setValue(timeDropdown.getItems().getFirst());

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
        courseDropdown.setValue(courseDropdown.getItems().getFirst());

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
        String name = studentFullName.getText();
        String reas = reason.getText();
        String comm = comment.getText();

        if(studentFullName.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Student's full name is required.");
        }
        if(scheduleDatePicker.getValue() == null){
            showAlert(Alert.AlertType.ERROR, "Schedule date is required.");
        }
        if(timeDropdown.getValue() == null || timeDropdown.getValue().toString().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Time slot is required.");
        }
        if(courseDropdown.getValue() == null || courseDropdown.getValue().toString().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Course is required.");
        }

        Schedule schedule = new Schedule(name, reas, comm);
        //try {
//            writescheduleCSV(schedule);
//            displayscheduleTb();
//        }catch (FileNotFoundException ex) {
//            throw new RuntimeException(ex);
//        }


    }

    private void showAlert(Alert.AlertType alertType, String s) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Error");
        alert.setHeaderText(s);
        alert.showAndWait();
    }
}

