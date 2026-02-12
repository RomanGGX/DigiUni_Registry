package ua.university.repository;

import ua.university.domain.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeacherRepository {
    private Teacher[] teachers;

    public TeacherRepository() {
        teachers = new Teacher[0];
    }

    public void setInitialData(Teacher[] initialTeachers) {
        this.teachers = initialTeachers;
    }

    public Teacher[] getTeachers() {
        return teachers.clone();
    }

    public Optional<Teacher> findById(int id) {
        for (Teacher teacher : teachers) {
            if (teacher.getId() == id) {
                return Optional.of(teacher);
            }
        }
        return Optional.empty();
    }

    public Optional<Teacher> findByFullName(String[] names) {
        for (Teacher teacher : teachers) {
            if (teacher.getFirstName().equalsIgnoreCase(names[0]) &&
                    teacher.getMiddleName().equalsIgnoreCase(names[1]) &&
                    teacher.getLastName().equalsIgnoreCase(names[2])) {
                return Optional.of(teacher);
            }
        }
        return Optional.empty();
    }

    public Optional<Teacher> findByEmail(String email) {
        for (Teacher teacher : teachers) {
            if (email.equalsIgnoreCase(teacher.getEmail())) {
                return Optional.of(teacher);
            }
        }
        return Optional.empty();
    }

    public Teacher[] findByFaculty(Faculty faculty) {
        List<Teacher> result = new ArrayList<>();
        for (Teacher teacher : teachers) {
            if (teacher.getDepartment() != null &&
                    teacher.getDepartment().getFaculty() != null &&
                    teacher.getDepartment().getFaculty().getCode() == faculty.getCode()) {
                result.add(teacher);
            }
        }
        return result.toArray(new Teacher[0]);
    }

    public Teacher[] findByDepartment(Department department) {
        List<Teacher> result = new ArrayList<>();
        for (Teacher teacher : teachers) {
            if (teacher.getDepartment() != null &&
                    teacher.getDepartment().getCode() == department.getCode()) {
                result.add(teacher);
            }
        }
        return result.toArray(new Teacher[0]);
    }

    public Teacher[] findByPosition(String position) {
        List<Teacher> result = new ArrayList<>();
        for (Teacher teacher : teachers) {
            if (teacher.getPosition().equalsIgnoreCase(position)) {
                result.add(teacher);
            }
        }
        return result.toArray(new Teacher[0]);
    }

    public void addTeacher(Teacher teacher) {
        Teacher[] newArray = new Teacher[teachers.length + 1];
        System.arraycopy(teachers, 0, newArray, 0, teachers.length);
        newArray[teachers.length] = teacher;
        teachers = newArray;
    }

    public boolean deleteTeacherById(int id) {
        boolean found = false;
        for (Teacher teacher : teachers) {
            if (teacher.getId() == id) {
                found = true;
                break;
            }
        }
        if (!found) {
            return false;
        }

        Teacher[] newArray = new Teacher[teachers.length - 1];
        int newIndex = 0;
        for (Teacher teacher : teachers) {
            if (teacher.getId() != id) {
                newArray[newIndex++] = teacher;
            }
        }
        teachers = newArray;
        return true;
    }

    public boolean updateTeacher(int id, Teacher updatedTeacher) {
        for (int i = 0; i < teachers.length; i++) {
            if (teachers[i].getId() == id) {
                teachers[i] = updatedTeacher;
                return true;
            }
        }
        return false;
    }

    public int getNextId() {
        int maxId = 0;
        for (Teacher teacher : teachers) {
            if (teacher.getId() > maxId) {
                maxId = teacher.getId();
            }
        }
        return maxId + 1;
    }
}