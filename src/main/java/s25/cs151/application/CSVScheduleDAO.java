package s25.cs151.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;

public class CSVScheduleDAO implements ScheduleDAO {
    private static final String FILE_NAME = "schedule.csv";

    @Override
    public void save(Schedule schedule) throws IOException {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(FILE_NAME, true)))) {
            // Split timeSlot
            String[] timeParts = schedule.getTimeSlot().split("-", 2);
            String startTime = timeParts.length > 0 ? timeParts[0].trim() : "";
            String endTime = timeParts.length > 1 ? timeParts[1].trim() : "";

            // Split course
            String[] courseParts = schedule.getCourse().split(" ", 2);
            String coursePrefix = courseParts.length > 0 ? courseParts[0].trim() : "";
            String courseName = courseParts.length > 1 ? courseParts[1].trim() : "";

            pw.println(schedule.getStudentFullName() + "," +
                    schedule.getScheduleDate() + "," +
                    startTime + "," +
                    endTime + "," +
                    coursePrefix + "," +
                    courseName + "," +
                    schedule.getReason() + "," +
                    schedule.getComment());
        }
    }

    @Override
    public ObservableList<Schedule> load() throws IOException {
        ObservableList<Schedule> schedules = FXCollections.observableArrayList();
        File file = new File(FILE_NAME);

        if (!file.exists()) return schedules;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 6) {
                    schedules.add(new Schedule(
                            parts[0].trim(), parts[1].trim(), parts[2].trim(),
                            parts[3].trim(), parts[4].trim(), parts[5].trim()));
                }
            }
        }
        return schedules;
    }

    @Override
    public boolean scheduleExists(String studentName, String date) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 2 &&
                        parts[0].trim().equals(studentName) &&
                        parts[1].trim().equals(date)) {
                    return true;
                }
            }
        }
        return false;
    }
}
