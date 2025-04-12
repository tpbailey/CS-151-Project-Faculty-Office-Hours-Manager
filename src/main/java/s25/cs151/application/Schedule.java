package s25.cs151.application;

public class Schedule{
    private String studentFullName;
    private String scheduleDate;
    private String reason;
    private String comment;

    public Schedule(String studentFullName, String reason, String comment){
        this.studentFullName = studentFullName;
        this.scheduleDate = scheduleDate;
        this.reason = reason;
        this.comment = comment;
    }

    public String getStudentFullName(String studentFullName){
        return studentFullName;
    }
    public void setStudentFullName(){
        this.studentFullName = studentFullName;
    }
    public String getScheduleDate(String scheduleDate){
        return scheduleDate;
    }
    public void setScheduleDate(){
        this.scheduleDate = scheduleDate;
    }
    public String getReason(String reason){
        return reason;
    }
    public void setReason(){
        this.reason = reason;
    }
    public String getComment(String comment){
        return comment;
    }
    public void setComment(){
        this.comment = comment;
    }

}
