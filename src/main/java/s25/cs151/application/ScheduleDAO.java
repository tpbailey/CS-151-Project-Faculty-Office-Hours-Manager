package s25.cs151.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.List;

public class ScheduleDAO {

    private static final String FILE_NAME = "schedule.csv";

    // Method to save a single schedule to the file
    public static void save(Schedule schedule) throws IOException {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(FILE_NAME, true)))) {
            pw.println(schedule.getStudentFullName() + "," +
                    schedule.getScheduleDate() + "," +
                    schedule.getReason() + "," +
                    schedule.getComment());
        }
    }

    // Method to load all schedules from the file
    public static ObservableList<Schedule> load() throws IOException {
        ObservableList<Schedule> schedules = FXCollections.observableArrayList();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return schedules; // Return an empty list if file doesn't exist
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) { // Ensure valid file format
                    schedules.add(new Schedule(
                            parts[0],                  // Student Name
                            parts[1],                  // Schedule Date
                            parts[2],                  // Time Slot
                            parts[3],                  // Course
                            parts.length > 4 ? parts[4] : "N/A", // Reason (default to "N/A" if missing)
                            parts.length > 5 ? parts[5] : "N/A"  // Comment (default to "N/A" if missing)
                    ));
                } else {
                    System.out.println("Skipping line due to insufficient columns: " + line);
                }
            }
        }


        return schedules;
    }

    // Optional: Method to check if a schedule already exists
    public static boolean scheduleExists(String studentName, String date) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4 &&
                        parts[0].equals(studentName) &&
                        parts[1].equals(date)) {
                    return true;
                }
            }
        }
        return false;
    }
}
