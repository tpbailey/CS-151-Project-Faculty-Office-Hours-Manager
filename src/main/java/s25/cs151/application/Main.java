package s25.cs151.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(s25.cs151.application.Main.class.getResource("home-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 400);
        stage.setTitle("Office Hours Scheduler");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) throws FileNotFoundException {
        launch();
    }
}