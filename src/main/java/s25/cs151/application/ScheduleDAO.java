package s25.cs151.application;

import javafx.collections.ObservableList;
import java.io.IOException;

public interface ScheduleDAO {
    void save(Schedule schedule) throws IOException;
    ObservableList<Schedule> load() throws IOException;
    boolean scheduleExists(String studentName, String date) throws IOException;
}