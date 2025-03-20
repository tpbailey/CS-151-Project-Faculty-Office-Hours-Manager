package s25.cs151.application;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

import java.io.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;


public class officeHoursController {

//    @FXML
//    private TableView table = new TableView();

    @FXML
    private ComboBox<String> semesterDropdown;

    @FXML
    private TextField yearInput;

    @FXML
    private CheckBox mondayCheckbox;

    @FXML
    private CheckBox tuesdayCheckbox;

    @FXML
    private CheckBox wednesdayCheckbox;

    @FXML
    private CheckBox thursdayCheckbox;

    @FXML
    private CheckBox fridayCheckbox;

    @FXML
    private String year;

    @FXML
    private String semester;

    @FXML
    private String days;

    @FXML
    private String time;

    @FXML
    public void initialize() {
        semesterDropdown.getItems().addAll("Winter", "Spring", "Summer", "Fall");

        yearInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                yearInput.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML
    protected void onSubmitButtonClick() {
        if (semesterDropdown.getValue() == null || yearInput.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Missing Required Fields");
            alert.setContentText("Please select a semester and enter a year.");
            alert.showAndWait();
            return;
        }

        year = yearInput.getText();
        semester = semesterDropdown.getSelectionModel().getSelectedItem();

        StringBuilder selectedDays = new StringBuilder();
        boolean weekdaySelected = false;

        if (mondayCheckbox.isSelected()) {
            selectedDays.append("Monday, ");
            weekdaySelected = true;
        }
        if (tuesdayCheckbox.isSelected()) {
            selectedDays.append("Tuesday, ");
            weekdaySelected = true;
        }
        if (wednesdayCheckbox.isSelected()) {
            selectedDays.append("Wednesday, ");
            weekdaySelected = true;
        }
        if (thursdayCheckbox.isSelected()) {
            selectedDays.append("Thursday, ");
            weekdaySelected = true;
        }
        if (fridayCheckbox.isSelected()) {
            selectedDays.append("Friday, ");
            weekdaySelected = true;
        }

        if (weekdaySelected) {
            selectedDays.setLength(selectedDays.length() - 2);
        } else {
            selectedDays.append("None");
        }

        days = selectedDays.toString();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Office Hours Set");
        alert.setHeaderText("Receipt of Submission");
        alert.setContentText("Year: " + yearInput.getText() + "\nSemester: " + semesterDropdown.getValue() +
                "\n" + selectedDays.toString());
        alert.showAndWait();
        try {
            writeToCSVFile();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        //Read CSV data
        ObservableList<ObservableList<String>> data = readCSVFile();

        //Create the TableView
        TableView<ObservableList<String>> table = createTableView(data);

        Group group = new Group();

        Stage stage2 = new Stage();
        Scene scene2 = new Scene(group, 700, 400);

        ((Group) scene2.getRoot()).getChildren().add(table);

        stage2.setTitle("Faculty office hours");
        stage2.setScene(scene2);
        stage2.show();

    }

    public void writeToCSVFile() throws FileNotFoundException {
        File csvFile = new File("officeHours.csv");
        try (FileWriter fw = new FileWriter(csvFile, true); // Open file in append mode
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            out.println(year + "," + semester + "," + days); // Write data as a new line
        } catch (IOException e) {
            e.printStackTrace(); // Handle potential exceptions
        }
//        PrintWriter out = new PrintWriter(csvFile);
//        out.println(year + "," + semester + "," + days);
//        out.close();
    }

    public ObservableList<ObservableList<String>> readCSVFile() {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new FileReader("officeHours.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                ObservableList<String> row = FXCollections.observableArrayList();
                String[] values = line.split(","); // Split the line into individual values
                row.addAll(values); // Add all values of the row
                data.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static TableView<ObservableList<String>> createTableView(ObservableList<ObservableList<String>> data) {
        TableView<ObservableList<String>> table = new TableView<>();
        String[] columnTitles = {"Year", "Semester", "Selected Days"};
        if (!data.isEmpty()) {
            int columnCount = data.get(0).size(); // Assume all rows have the same number of columns
            for (int i = 0; i < columnCount; i++) {
                final int colIndex = i;
                TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnTitles[i]);
                column.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get(colIndex)));
                table.getColumns().add(column);
            }
        }

        table.setItems(data);
        return table;
    }
}


