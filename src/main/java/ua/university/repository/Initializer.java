package ua.university.repository;

import ua.university.domain.*;

public class Initializer {
    public static void initializeAll(StudentRepository studentRepository, FacultyRepository facultyRepository, DepartmentRepository departmentRepository, TeacherRepository teacherRepository) {

        //kma faculties
        Faculty fi = new Faculty(1, "Факультет інформатики", "ФІ", null, "fi@ukma.edu.ua");
        Faculty fen = new Faculty(2, "Факультет економічних наук", "ФЕН", null, "fen@ukma.edu.ua");
        Faculty fcnct = new Faculty(3, "Факультет соціальних наук та соціальних технологій", "ФСНСТ", null, "fcnct@ukma.edu.ua");

        Faculty[] faculties = {fi, fen, fcnct};
        facultyRepository.setFaculties(faculties);

        //department examples
        Department dm = new Department(1, "Кафедра математики", fi, null, "1-100");
        Department di = new Department(2, "Кафедра інформатики", fi, null, "1-201");
        Department df = new Department(3, "Кафедра фінансів", fen, null, "3-205");
        Department det = new Department(4, "Кафедра економічної теорії", fen, null, "2-102");
        Department po = new Department(5, "Кафедра політології", fcnct, null, "10-110");
        Department so = new Department(6, "Кафедра соціології", fcnct, null, "6-201");

        Department[] departments = {dm, di, df, det, po, so};
        departmentRepository.setDepartments(departments);

        //Student examples
        Student st1 = new Student(1, "Іван", "Іванович", "Шевченко", "02.03.2007",
                "ivan@gmail.com", "+380534386432", "3F34AB", 1, "42B",
                2025, "бюджет", "навчається", dm);

        Student st2 = new Student(2, "Марія", "Ігорівна", "Мельник", "18.11.2006",
                "maria@gmail.com", "+380839301980", "1K91AB", 3, "10B",
                2023, "контракт", "навчається", dm);

        Student st3 = new Student(3, "Андрій", "Сергійович", "Бондаренко", "21.01.2007",
                "andrii@gmail.com", "+380671234567", "9Q77EF", 1, "12C",
                2025, "бюджет", "навчається", di);

        Student st4 = new Student(4, "Катерина", "Володимирівна", "Романюк", "30.05.2006",
                "kateryna@gmail.com", "+380931112244", "2T10GH", 3, "15B",
                2023, "контракт", "навчається", di);

        Student st5 = new Student(5, "Дмитро", "Олександрович", "Гриценко", "09.12.2006",
                "dmytro@gmail.com", "+380991234111", "4A88JK", 2, "20D",
                2024, "бюджет", "навчається", dm);

        Student st6 = new Student(6, "Софія", "Андріївна", "Ткаченко", "17.04.2007",
                "sofia@gmail.com", "+380631234222", "6B12LM", 1, "21A",
                2025, "контракт", "навчається", df);

        Student st7 = new Student(7, "Максим", "Ігорович", "Литвин", "03.02.2006",
                "maksym@gmail.com", "+380731234333", "8C55NP", 4, "30B",
                2022, "контракт", "навчається", det);

        Student st8 = new Student(8, "Наталія", "Миколаївна", "Савчук", "26.09.2007",
                "nataliia@gmail.com", "+380501234444", "5D19RS", 1, "31C",
                2025, "бюджет", "навчається", det);

        Student st9 = new Student(9, "Марія", "Ігорівна", "Мельник", "18.11.2006",
                "maria@gmail.com", "+380839301980", "1K91AB", 3, "10B",
                2023, "контракт", "навчається", po);

        Student st10 = new Student(10, "Богдан", "Віталійович", "Сидоренко", "11.06.2007",
                "bohdan@gmail.com", "+380681234555", "3E73TU", 1, "11A",
                2025, "бюджет", "навчається", po);

        Student st11 = new Student(11, "Юлія", "Романівна", "Олійник", "07.07.2006",
                "yuliia@gmail.com", "+380951234666", "7F20VW", 2, "50A",
                2024, "контракт", "навчається", so);

        Student st12 = new Student(12, "Тарас", "Михайлович", "Кравченко", "28.10.2007",
                "taras@gmail.com", "+380661234777", "9G14XY", 1, "51B",
                2025, "бюджет", "навчається", so);

        Student[] students = {st1, st2, st3, st4, st5, st6, st7, st8, st9, st10, st11, st12};
        studentRepository.setStudents(students);

        //Teacher examples
        Teacher teacher1 = new Teacher(1, "Олександр", "Петрович", "Коваленко", "15.03.1975",
                "kovalenko@ukma.edu.ua", "+380501234567", "професор",
                dm, "д.т.н.", "професор", "07.11.2015", 250.0);

        Teacher teacher2 = new Teacher(2, "Ірина", "Михайлівна", "Соловей", "09.10.1982",
                "solovei@ukma.edu.ua", "+380503456789", "доцент",
                dm, "к.ф.-м.н.", "доцент", "14.09.2011", 220.0);

        Teacher teacher3 = new Teacher(3, "Артем", "Валерійович", "Паламарчук", "28.01.1978",
                "palamarchuk@ukma.edu.ua", "+380504567890", "професор",
                di, "д.т.н.", "професор", "03.03.2013", 300.0);

        Teacher teacher4 = new Teacher(4, "Оксана", "Сергіївна", "Гнатюк", "16.05.1985",
                "hnatiuk@ukma.edu.ua", "+380505678901", "старший викладач",
                di, "к.т.н.", "старший викладач", "01.09.2016", 190.0);

        Teacher teacher5 = new Teacher(5, "Віктор", "Олегович", "Руденко", "02.12.1974",
                "rudenko@ukma.edu.ua", "+380506789012", "професор",
                df, "д.е.н.", "професор", "12.02.2010", 280.0);

        Teacher teacher6 = new Teacher(6, "Тетяна", "Юріївна", "Марченко", "19.04.1981",
                "marchenko@ukma.edu.ua", "+380507890123", "доцент",
                df, "к.е.н.", "доцент", "05.09.2012", 210.0);

        Teacher teacher7 = new Teacher(7, "Сергій", "Анатолійович", "Клименко", "11.06.1977",
                "klymenko@ukma.edu.ua", "+380508901234", "професор",
                det, "д.е.н.", "професор", "20.10.2008", 260.0);

        Teacher teacher8 = new Teacher(8, "Людмила", "Павлівна", "Яценко", "24.08.1986",
                "yatsenko@ukma.edu.ua", "+380509012345", "доцент",
                det, "к.е.н.", "доцент", "15.01.2017", 200.0);

        Teacher teacher9 = new Teacher(9, "Наталія", "Іванівна", "Шевченко", "22.07.1980",
                "shevchenko@ukma.edu.ua", "+380502345678", "доцент",
                po, "к.т.н.", "доцент", "11.02.2009", 430.0);

        Teacher teacher10 = new Teacher(10, "Ігор", "Степанович", "Левченко", "13.09.1979",
                "levchenko@ukma.edu.ua", "+380501119988", "старший викладач",
                po, "к.політ.н.", "старший викладач", "01.09.2014", 240.0);

        Teacher teacher11 = new Teacher(11, "Марина", "Олександрівна", "Кузьменко", "05.01.1983",
                "kuzmenko@ukma.edu.ua", "+380501223344", "доцент",
                so, "к.соц.н.", "доцент", "10.03.2012", 205.0);

        Teacher teacher12 = new Teacher(12, "Павло", "Іванович", "Данилюк", "27.11.1976",
                "danilyuk@ukma.edu.ua", "+380501334455", "професор",
                so, "д.соц.н.", "професор", "18.09.2007", 255.0);

        Teacher[] teachers = {
                teacher1, teacher2, teacher3, teacher4, teacher5, teacher6,
                teacher7, teacher8, teacher9, teacher10, teacher11, teacher12
        };
        teacherRepository.setInitialData(teachers);

        fi.setDean(teacher1);
        fen.setDean(teacher2);

        facultyRepository.updateFaculty(fi.getCode(), fi);
        facultyRepository.updateFaculty(fen.getCode(), fen);

        po.setHead(teacher3);
        so.setHead(teacher4);
        det.setHead(teacher5);
        dm.setHead(teacher6);
        df.setHead(teacher7);
        di.setHead(teacher8);

        departmentRepository.updateDepartment(po.getCode(), po);
        departmentRepository.updateDepartment(so.getCode(), so);
        departmentRepository.updateDepartment(det.getCode(), det);
        departmentRepository.updateDepartment(dm.getCode(), dm);
        departmentRepository.updateDepartment(df.getCode(), df);
        departmentRepository.updateDepartment(di.getCode(), di);
    }
}