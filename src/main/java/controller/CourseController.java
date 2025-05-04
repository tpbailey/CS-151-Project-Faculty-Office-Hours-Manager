package controller;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Course;
import s25.cs151.application.CourseDAO;

public class CourseController extends Application {
    private TextField courseCodeField;
    private TextField courseNameField;
    private TextField sectionNumberField;
    private CourseDAO courseDAO = new CourseDAO();

    @Override
    public void start(Stage stage) {
        Label header = new Label("Select the Course");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");
        header.setAlignment(Pos.CENTER);

        courseCodeField = new TextField();
        courseNameField = new TextField();
        sectionNumberField = new TextField();

        Button submit = new Button("Submit");
        submit.setOnAction(e -> handleSubmit());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 200));
        grid.setHgap(30);
        grid.setVgap(20);

        grid.add(new Label("Enter Course Code"), 0, 0);
        grid.add(courseCodeField, 1, 0);
        grid.add(new Label("Enter Course Name"), 0, 1);
        grid.add(courseNameField, 1, 1);
        grid.add(new Label("Enter Section Number"), 0, 2);
        grid.add(sectionNumberField, 1, 2);
        grid.add(submit, 1, 3);

        VBox root = new VBox(10, header, grid);
        root.setStyle("-fx-alignment: center;" +
                "-fx-background-color: radial-gradient(center 50% 50%, radius 60%,  #fceabb, #f8b500);");

        stage.setScene(new Scene(root, 700, 500));
        stage.setTitle("Course Entry");
        stage.show();
    }

    private void handleSubmit() {
        String code = courseCodeField.getText().trim();
        String name = courseNameField.getText().trim();
        String section = sectionNumberField.getText().trim();

        if (code.isEmpty() || name.isEmpty() || section.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "All fields are required.");
            return;
        }

        ObservableList<Course> existingCourses = courseDAO.getAll();
        for (Course c : existingCourses) {
            if (c.getCourseCode().equalsIgnoreCase(code) &&
                    c.getCourseName().equalsIgnoreCase(name) &&
                    c.getSectionNumber().equalsIgnoreCase(section)) {
                showAlert(Alert.AlertType.ERROR, "This course entry already exists.");
                return;
            }
        }

        Course course = new Course(code, name, section);
        courseDAO.save(course);
        displayTable();
    }

    private void displayTable() {
        TableView<Course> table = new TableView<>();
        ObservableList<Course> courses = courseDAO.getAll();

        TableColumn<Course, String> codeCol = new TableColumn<>("Course Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        // Force descending order and disable further sorting
        codeCol.setSortType(TableColumn.SortType.DESCENDING);
        codeCol.setSortable(false);

        TableColumn<Course, String> nameCol = new TableColumn<>("Course Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));

        TableColumn<Course, String> sectionCol = new TableColumn<>("Section Number");
        sectionCol.setCellValueFactory(new PropertyValueFactory<>("sectionNumber"));

        table.getColumns().addAll(codeCol, nameCol, sectionCol);
        table.setItems(courses);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Optionally, add the column to the sort order so it appears sorted by default
        table.getSortOrder().add(codeCol);

        VBox layout = new VBox(10, new Label("All Courses"), table);
        layout.setPadding(new Insets(15));

        Stage tableStage = new Stage();
        tableStage.setTitle("Courses Table");
        tableStage.setScene(new Scene(layout, 700, 400));
        tableStage.show();
    }


    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Input Error");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
