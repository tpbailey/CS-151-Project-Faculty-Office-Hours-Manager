package model;

public class Schedule {
    private String studentFullName;
    private String scheduleDate;

    private String timeSlot; // <-- ADD THIS
    private String course;   // <-- ADD THIS

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
    public Schedule(String studentFullName, String scheduleDate, String reason, String comment) {
        this.studentFullName = studentFullName;
        this.scheduleDate = scheduleDate;
        this.timeSlot = "N/A";  // Default if missing
        this.course = "N/A";    // Default if missing

        this.reason = reason;
        this.comment = comment;
    }


    public String getStudentFullName() {
        return studentFullName;
    }

    public void setStudentFullName(String studentFullName) {
        this.studentFullName = studentFullName;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getTimeSlot() {    // <-- ADD THIS
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {  // <-- ADD THIS
        this.timeSlot = timeSlot;
    }

    public String getCourse() {    // <-- ADD THIS
        return course;
    }

    public void setCourse(String course) {  // <-- ADD THIS
        this.course = course;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
