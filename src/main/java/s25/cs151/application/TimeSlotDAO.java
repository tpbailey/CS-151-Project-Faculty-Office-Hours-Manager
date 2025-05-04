package s25.cs151.application;

import model.TimeSlot;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;
import java.util.List;

public class TimeSlotDAO {
    public static void save(List<TimeSlot> timeSlotList, String filename) throws Exception {
        File file = new File(filename);
        try (FileWriter fw = new FileWriter(file, false);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw)) {
            for (TimeSlot ts : timeSlotList) {
                pw.println(ts.getFrom() + "," + ts.getTo());
            }
        }
    }
}
