package ua.university.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public class Student extends Person{
    private String studentId;
    private int course;
    private String group;
    private int yearEnroll;
    private String studyForm;
    private String status;
    private Department department;

    public Student() {}

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

    @JsonIgnore
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
        String facultyName = (getFaculty() != null) ? getFaculty().getShortName() : "не вказано";
        String deptName = (getDepartment() != null) ? getDepartment().getName() : "не вказано";

        return String.format(
                "[ID: %d] %s | Квиток: %s, Курс: %d, Група: %s | %s, %s | Вступ: %d, Форма: %s, Статус: %s | Дн: %s, Email: %s, Тел: %s",
                getId(),
                getFullName(),
                studentId,
                course,
                group,
                facultyName,
                deptName,
                yearEnroll,
                studyForm,
                status,
                getBirthDate(),
                getEmail(),
                getPhoneNumber()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student other = (Student) o;
        return this.getId() == other.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
