package model.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Schedule;

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
                if (parts.length >= 8) {
                    String studentName = parts[0].trim();
                    String date = parts[1].trim();
                    String startTime = parts[2].trim();
                    String endTime = parts[3].trim();
                    String coursePrefix = parts[4].trim();
                    String courseName = parts[5].trim();
                    String reason = parts[6].trim();
                    String comment = parts[7].trim();

                    String timeSlot = startTime + "-" + endTime;
                    String course = coursePrefix.isEmpty() ? courseName : coursePrefix + " " + courseName;

                    if (reason.isEmpty()) reason = "N/A";
                    if (comment.isEmpty()) comment = "N/A";

                    schedules.add(new Schedule(studentName, date, timeSlot, course, reason, comment));
                } else {
                    System.err.println("Skipping malformed line: " + line);
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
