package ua.university.domain;

public class Student extends Person{
    private String studentId;
    private int course;
    private String group;
    private int yearEnroll;
    private String studyForm;
    private String status;

    public Student(int id, String firstName, String middleName, String lastName, String birthDate, String email,
                   String phoneNumber, String studentId, int course, String group, int yearEnroll, String studyForm, String status) {
        super(id, firstName, middleName, lastName, birthDate, email, phoneNumber);
        this.studentId = studentId;
        this.course = course;
        this.group = group;
        this.yearEnroll = yearEnroll;
        this.studyForm = studyForm;
        this.status = status;
    }

    public String getStudentId() {
        return studentId;
    }

    public int getCourse() {
        return course;
    }

    public String getGroup() {
        return group;
    }

    public int getYearEnroll() {
        return yearEnroll;
    }

    public String getStudyForm() {
        return studyForm;
    }

    public String getStatus() {
        return status;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setYearEnroll(int yearEnroll) {
        this.yearEnroll = yearEnroll;
    }

    public void setStudyForm(String studyForm) {
        this.studyForm = studyForm;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", course=" + course +
                ", group='" + group + '\'' +
                ", yearEnroll=" + yearEnroll +
                ", studyForm='" + studyForm + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
