package ua.university.service;

import ua.university.domain.*;

public class CRUDOperations {
    /*
    Displays universities list
     */
    public void showUniversities(University[] universities){
        if(universities == null || universities.length == 0) {
            System.out.println("Information about universities was not found");
            return;
        }
        System.out.println("---Universities List---");
        for (University university : universities){
            System.out.println(university.toString());
        }
    }
    /*
    Displays faculties list
     */
    public void showFaculties(Faculty[] faculties) {
        if(faculties == null || faculties.length == 0){
            System.out.println("Information about faculties was not found");
            return;
        }
        System.out.println("---Faculties list---");
        for (Faculty faculty : faculties){
            System.out.println(faculty.toString());
        }
    }

    /*
    Displays departments list
     */
    public void showDepartments(Department[] departments) {
        if(departments == null || departments.length == 0){
            System.out.println("Information about departments was not found");
            return;
        }
        System.out.println("---Departments list---");
        for (Department department : departments){
            System.out.println(department.toString());
        }
    }

    /*
     Displays persons list
     */
    public void showPersons(Person[] persons) {
        if(persons == null || persons.length == 0){
            System.out.println("Information about persons was not found");
            return;
        }
        System.out.println("---Persons list---");
        for (Person person: persons){
            System.out.println(person.toString());
        }
    }
    /*
     Displays teachers list
     */
    public void showTeachers(Teacher[] teachers) {
        if(teachers == null || teachers.length == 0){
            System.out.println("Information about teachers was not found");
            return;
        }
        System.out.println("---Teachers list---");
        for (Teacher teacher : teachers){
            System.out.println(teacher.toString());
        }
    }
    /*
     Displays students list
     */
    public void showStudents(Student[] students) {
        if(students == null || students.length == 0){
            System.out.println("Information about students was not found");
            return;
        }
        System.out.println("---Students list---");
        for (Student student : students){
            System.out.println(student.toString());
        }
    }

}
