package s25.cs151.application;

import controller.facultyHomeController;
import javafx.application.Application;
import javafx.stage.Stage;



import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primarystage) throws IOException {
        facultyHomeController hp = new facultyHomeController();
        hp.display(primarystage);
    }

    public static void main(String[] args) throws FileNotFoundException {
        launch();
    }
}