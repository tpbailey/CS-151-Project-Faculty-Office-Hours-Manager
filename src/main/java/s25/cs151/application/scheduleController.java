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
    private DatePicker scheduleDatePicker = new DatePicker();
    private String formattedDate;
    private ComboBox<String> timeDropdown;
    private ComboBox<String> courseDropdown;
    private TextField reason;
    private TextField comment;
    private TableView<Schedule> scheduleTable;

    @Override
    public void start(Stage stage) throws Exception, FileNotFoundException {

        Label header = new Label("Schedule Office Hour");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; ");
        header.setAlignment(Pos.CENTER);

        studentFullName = new TextField();
        Label studentFullNameLabel = new Label("Enter student's full name");
        studentFullName.setPromptText("Enter full name");
        Label scheduleDateLabel = new Label("Select schedule date");
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
        formattedDate = scheduleDatePicker.getValue().format(formatter);

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

        String name = studentFullName.getText();
        String date = scheduleDatePicker.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        String time = timeDropdown.getValue();
        String course = courseDropdown.getValue();
        String reas = reason.getText();
        String comm = comment.getText();

        Schedule schedule = new Schedule(name, date, time, course, reas, comm);
        try {
            writeScheduleCSV(schedule);
            displayScheduleTb();
        }catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void writeScheduleCSV(Schedule schedule) throws FileNotFoundException {
        File file = new File("schedule.csv");
        try (FileWriter fw = new FileWriter(file, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw);){

            pw.println(String.join(",",
                    schedule.getStudentFullName(),
                    schedule.getScheduleDate(),
                    schedule.getTimeSlot(),
                    schedule.getCourse(),
                    schedule.getReason().isEmpty() ? "N/A" : schedule.getReason(),
                    schedule.getComment().isEmpty() ? "N/A" : schedule.getComment()
            ));


//             pw.println(schedule.getStudentFullName() + ", " + schedule.getScheduleDate() + ", " + schedule.getTimeSlot() +
//                     ", " + schedule.getCourse() + ", " + schedule.getReason() + ", " + schedule.getComment());
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void displayScheduleTb() {
        scheduleTable = createScheduleTableView();
        scheduleTable.setItems(readScheduleCSV());


        ObservableList<Schedule> schedules = readScheduleCSV();

// DEBUG: Print list size and contents
//        System.out.println("Loaded schedules: " + schedules.size());
//        for (Schedule s : schedules) {
//            System.out.println("Schedule entry -> " + s.getStudentFullName() + ", " + s.getScheduleDate() + ", " + s.getTimeSlot() + ", " + s.getCourse() + ", " + s.getReason() + ", " + s.getComment());
//        }

        scheduleTable = createScheduleTableView();
        scheduleTable.setItems(schedules);







        VBox container = new VBox(10, new Label("Schedule"), scheduleTable);
        container.setPadding(new javafx.geometry.Insets(15));
        Scene tableScene = new Scene(container, 700, 400);

        Stage tableStage = new Stage();
        tableStage.setTitle("Schedule Table");
        tableStage.setScene(tableScene);
        tableStage.show();
    }

    private TableView<Schedule> createScheduleTableView() {
        TableView<Schedule> table = new TableView<>();

        TableColumn<Schedule, String> nameCol = new TableColumn<>("Student's name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("studentFullName"));

        TableColumn<Schedule, String> dateCol = new TableColumn<>("Schedule date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("scheduleDate"));

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

    private ObservableList<Schedule> readScheduleCSV() {
        ObservableList<Schedule> list = FXCollections.observableArrayList();
        File file = new File("schedule.csv");
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {

                String[] parts = line.split(",", -1);  // Simple split on comma

                if (parts.length >= 8) {
                    String name = parts[0].trim();
                    String date = parts[1].trim();
                    String timeSlot = parts[2].trim();
                    String courseName = parts[3];
                    String code = parts[4];
                    String section = parts[5];
                    String course = courseName+ " " + code + " " + section;
                    String reason = parts[6].trim().isEmpty() ? "N/A" : parts[6].trim();
                    String comment = parts[7].trim().isEmpty() ? "N/A" : parts[7].trim();





                // Split with limit -1 to preserve trailing empty values
//                String[] parts = line.split(",\\s*", -1);
//
//                if (parts.length >= 6) {  // Minimum required columns
//                    String name = parts[0];
//                    String date = parts[1];
//                    String timeSlot = parts[2];
//                    String code = parts[3];
//                    String course = parts[4];
//
//                    // Only the LAST TWO entries get "na" conversion
//                    String reason = (parts.length > 5 && parts[5].isEmpty()) ? "N/A" : parts[5];
//                    String comment = (parts.length > 6 && parts[6].isEmpty()) ? "N/A" : parts[6];

                    list.add(new Schedule(name, date, timeSlot, course, reason, comment));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void showAlert(Alert.AlertType alertType, String s) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Error");
        alert.setHeaderText(s);
        alert.showAndWait();
    }
}

