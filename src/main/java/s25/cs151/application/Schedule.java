package s25.cs151.application;

public class Schedule {
    private String studentFullName;
    private String scheduleDate;
    private String timeSlot;
    private String course;
    private String reason;
    private String comment;

    public Schedule(String studentFullName, String scheduleDate, String timeSlot, String course, String reason, String comment) {
        this.studentFullName = studentFullName;
        this.scheduleDate = scheduleDate;
        this.timeSlot = timeSlot;
        this.course = course;
        this.reason = reason;
        this.comment = comment;
    }

    // Getters (ensure all 6 fields have getters)
    public String getStudentFullName() { return studentFullName; }
    public String getScheduleDate() { return scheduleDate; }
    public String getTimeSlot() { return timeSlot; }
    public String getCourse() { return course; }
    public String getReason() { return reason; }
    public String getComment() { return comment; }
}
