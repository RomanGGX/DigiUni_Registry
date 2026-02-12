package ua.university.repository;

import ua.university.domain.University;

public class UniversityRepository {
    private University university;

    public UniversityRepository() {
        KMAUniversity();
    }

    private void KMAUniversity() {
        university = new University("Національний університет «Києво-Могилянська академія»",
                "КМА", "Київ", "вул. Григорія Сковороди, 2");
    }

    public University getUniversity() {
        return university;
    }

    public void updateUniversity(University updatedUniversity) {
        this.university = updatedUniversity;
    }
}
