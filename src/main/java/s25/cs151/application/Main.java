package s25.cs151.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;



import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends Application {

    /**
     *
     * @param primarystage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException
     */
    @Override
    public void start(Stage primarystage) throws IOException {
        facultyHomeController hp = new facultyHomeController();
        hp.display(primarystage);
    }

    /**
     * @
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        launch();
    }
}