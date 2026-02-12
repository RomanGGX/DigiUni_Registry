package ua.university.repository;

import ua.university.domain.Faculty;
import ua.university.domain.University;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FacultyRepository {
    private Faculty[] faculties;

    public FacultyRepository() {
        faculties = new Faculty[0];
    }

    public Faculty[] getFaculties() {
        return faculties.clone();
    }

    public void setFaculties(Faculty[] initialFaculties) {
        this.faculties = initialFaculties;
    }

    public Optional<Faculty> findByCode(int code) {
        for (Faculty faculty : faculties) {
            if (faculty.getCode() == code) {
                return Optional.of(faculty);
            }
        }
        return Optional.empty();
    }

    public void addFaculty(Faculty faculty) {
        Faculty[] newArray = new Faculty[faculties.length + 1];
        System.arraycopy(faculties, 0, newArray, 0, faculties.length);
        newArray[faculties.length] = faculty;
        faculties = newArray;
    }

    public boolean deleteFacultyByCode(int code) {
        boolean found = false;
        for (Faculty faculty : faculties) {
            if (faculty.getCode() == code) {
                found = true;
                break;
            }
        }
        if (!found) return false;

        Faculty[] newArray = new Faculty[faculties.length - 1];
        int newIndex = 0;
        for (Faculty faculty : faculties) {
            if (faculty.getCode() != code) {
                newArray[newIndex++] = faculty;
            }
        }
        faculties = newArray;
        return true;
    }

    public boolean updateFaculty(int code, Faculty updatedFaculty) {
        for (int i = 0; i < faculties.length; i++) {
            if (faculties[i].getCode() == code) {
                faculties[i] = updatedFaculty;
                return true;
            }
        }
        return false;
    }

    public int getNextCode() {
        int maxCode = 0;
        for (Faculty faculty : faculties) {
            if (faculty.getCode() > maxCode) {
                maxCode = faculty.getCode();
            }
        }
        return maxCode + 1;
    }
}
