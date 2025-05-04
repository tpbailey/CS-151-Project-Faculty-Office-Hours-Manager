package controller;

import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import s25.cs151.application.searchOfficeHoursController;

public class facultyHomeController {
    public void display(Stage primaryStage) throws IOException {


        Label header = new Label("Welcome to Faculty Home");
        header.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");

        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);
        hb.setSpacing(20);
        Button btn1 = new Button("Define Semester's Office Hours");
        Button btn2 = new Button("Time Slot");
        Button btn3 = new Button("Course");
        Button btn4 = new Button("Schedule Office Hours");
        Button btn5 = new Button("Search Office Hours Schedule");
        hb.getChildren().addAll(btn1, btn2, btn3, btn4, btn5);
        VBox vb = new VBox(header, hb);
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(20);
        vb.setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 60%,  #fceabb, #f8b500);");

        btn1.setOnAction(e -> {
            OfficeHoursController officeHoursApp = new OfficeHoursController();
            try {
                Stage newStage = new Stage();
                officeHoursApp.start(newStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        btn2.setOnAction(e -> {
            TimeSlotController timeslotApp = new TimeSlotController();
            try {
                timeslotApp.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btn3.setOnAction(e->{
            CourseController courseApp = new CourseController();
            try {
                Stage newStage = new Stage();
                courseApp.start(newStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btn4.setOnAction(e->{
            scheduleController scheduleApp = new scheduleController();
            try {
                Stage newStage = new Stage();
                scheduleApp.start(newStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btn5.setOnAction(e->{
            searchOfficeHoursController searchApp = new searchOfficeHoursController();
            try {
                Stage newStage = new Stage();
                searchApp.start(newStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        primaryStage.setScene(new Scene(vb, 700, 500));
        primaryStage.setTitle("Faculty Home");
        primaryStage.show();
    }

}