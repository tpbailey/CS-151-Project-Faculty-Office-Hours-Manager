package s25.cs151.application;

import javafx.collections.ObservableList;

public interface CourseDAO {
    void save(Course course);
    ObservableList<Course> getAll();
}
