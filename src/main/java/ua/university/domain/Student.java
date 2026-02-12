package ua.university.domain;

public class Student extends Person{
    private String studentId;
    private int course;
    private String group;
    private int yearEnroll;
    private String studyForm;
    private String status;
    private Department department;

    public Student(int id, String firstName, String middleName, String lastName, String birthDate, String email,
                   String phoneNumber, String studentId, int course, String group, int yearEnroll, String studyForm, String status, Department department) {
        super(id, firstName, middleName, lastName, birthDate, email, phoneNumber);
        this.studentId = studentId;
        this.course = course;
        this.group = group;
        this.yearEnroll = yearEnroll;
        this.studyForm = studyForm;
        this.status = status;
        this.department = department;
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

    public Department getDepartment() {
        return department;
    }

    public Faculty getFaculty() {
        if (department != null) {
            return department.getFaculty();
        }
        return null;
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

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + getId() +
                ", fullName='" + getFullName() + '\'' +
                ", faculty='" + (department != null && department.getFaculty() != null
                ? department.getFaculty().getShortName()
                : "не вказано") + '\'' +
                ", department='" + (department != null ? department.getName() : "не вказано") + '\'' +
                ", birthDate='" + getBirthDate() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", studentId='" + studentId + '\'' +
                ", course=" + course +
                ", group='" + group + '\'' +
                ", yearEnroll=" + yearEnroll +
                ", studyForm='" + studyForm + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
