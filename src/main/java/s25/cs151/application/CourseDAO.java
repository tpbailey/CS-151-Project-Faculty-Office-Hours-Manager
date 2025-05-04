package s25.cs151.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Course;

import java.io.*;

public class CourseDAO {
    private static final String FILE_PATH = "course.csv";

    public void save(Course course) {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH, true)))) {
            pw.println(course.getCourseCode() + "," + course.getCourseName() + "," + course.getSectionNumber());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Course> getAll() {
        ObservableList<Course> list = FXCollections.observableArrayList();
        File file = new File(FILE_PATH);
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    list.add(new Course(parts[0].trim(), parts[1].trim(), parts[2].trim()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        list.sort((c1, c2) -> c2.getCourseCode().compareToIgnoreCase(c1.getCourseCode()));
        return list;
    }
}
