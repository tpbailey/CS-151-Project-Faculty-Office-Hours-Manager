package s25.cs151.application;

public class Schedule{
    private String studentFullName;
    private String scheduleDate;
    private String timeSlot;
    private String course;
    private String reason;
    private String comment;

    public Schedule(String studentFullName, String scheduleDate, String timeSlot,
                    String course, String reason, String comment){
        this.studentFullName = studentFullName;
        this.scheduleDate = scheduleDate;
        this.timeSlot = timeSlot;
        this.course = course;
        this.reason = reason;
        this.comment = comment;
    }

    public String getStudentFullName(){
        return studentFullName;
    }
    public void setStudentFullName(String studentFullName){
        this.studentFullName = studentFullName;
    }
    public String getScheduleDate(){
        return scheduleDate;
    }
    public void setScheduleDate(String scheduleDate){
        this.scheduleDate = scheduleDate;
    }
    public String getTimeSlot(){
        return timeSlot;
    }
    public void setTimeSlot(String timeSlot){
        this.timeSlot = timeSlot;
    }
    public String getCourse(){
        return course;
    }
    public void setCourse(String course){
        this.course = course;
    }
    public String getReason(){
        return reason;
    }
    public void setReason(String reason){
        this.reason = reason;
    }
    public String getComment(){
        return comment;
    }
    public void setComment(String comment){
        this.comment = comment;
    }

}
