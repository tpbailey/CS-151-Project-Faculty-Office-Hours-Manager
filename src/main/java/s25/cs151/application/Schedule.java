package s25.cs151.application;

public class Schedule {
    private String studentFullName;
    private String scheduleDate;
<<<<<<< HEAD
    private String timeSlot;
    private String course;
=======
    private String timeSlot; // <-- ADD THIS
    private String course;   // <-- ADD THIS
>>>>>>> origin/master
    private String reason;
    private String comment;

    public Schedule(String studentFullName, String scheduleDate, String timeSlot, String course, String reason, String comment) {
        this.studentFullName = studentFullName;
        this.scheduleDate = scheduleDate;
        this.timeSlot = timeSlot;
        this.course = course;
<<<<<<< HEAD
=======
        this.reason = reason;
        this.comment = comment;
    }
    public Schedule(String studentFullName, String scheduleDate, String reason, String comment) {
        this.studentFullName = studentFullName;
        this.scheduleDate = scheduleDate;
        this.timeSlot = "N/A";  // Default if missing
        this.course = "N/A";    // Default if missing
>>>>>>> origin/master
        this.reason = reason;
        this.comment = comment;
    }

<<<<<<< HEAD
    // Getters (ensure all 6 fields have getters)
    public String getStudentFullName() { return studentFullName; }
    public String getScheduleDate() { return scheduleDate; }
    public String getTimeSlot() { return timeSlot; }
    public String getCourse() { return course; }
    public String getReason() { return reason; }
    public String getComment() { return comment; }
=======
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
>>>>>>> origin/master
}
