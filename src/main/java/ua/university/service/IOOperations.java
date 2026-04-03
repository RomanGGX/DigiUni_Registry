package ua.university.service;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class IOOperations {

    private final Path running;
    private final Path stable;

    /**
     * Initializes running and stable paths
     * @param running Path that changes during work
     * @param stable Path that changes on save
     * @throws IOException Throws if initialization fails
     */
    public IOOperations(Path running, Path stable) throws IOException {
        running = running.resolve("RunningConfiguration.txt");
        stable = stable.resolve("StableConfiguration.txt");

        if (Files.notExists(running)) Files.createFile(running);
        if (Files.notExists(stable)) {
            Files.createFile(stable);
            initializeStable(stable);
        }

        this.running = running;
        this.stable = stable;
    }

    public Path getRunning() {return running;}

    public Path getStable() {return stable;}

    /**
     * Transfers all information from src to dst
     * @param src Source path
     * @param dst Destination path
     * @throws IOException Throws if path not found
     */
    public void transferFiles(Path src, Path dst) throws IOException {
        try (FileChannel in = FileChannel.open(src, StandardOpenOption.READ);
             FileChannel out = FileChannel.open(dst, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
            long size = in.size();
            long transfered = 0;
            while (transfered < size) {
                transfered += in.transferTo(transfered, size - transfered, out);
            }
        } catch (IOException ex) {
            throw new IOException("Failed to transfer from one file to another", ex);
        }
    }

    /**
     * Copies stable configuration to running configuration
     * @throws IOException Throws if path not found
     */
    public void copyStableToRunning() throws IOException {
        transferFiles(stable, running);
    }

    /**
     * Initializes stable configuration if there is none
     * @param stable Stable configuration path
     * @throws IOException Throws if writing fails
     */
    private void initializeStable(Path stable) throws IOException {
        initializeFaculties(stable);
        initializeDepartments(stable);
        initializeStudents(stable);
        initializeTeachers(stable);

        Files.writeString(stable, ".End", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    /**
     * Initializes faculties
     * @param stable Stable configuration path
     * @throws IOException Throws if writing fails
     */
    private void initializeFaculties(Path stable) throws IOException {
        try {
            Files.writeString(stable, ".Faculties\n", StandardCharsets.UTF_8);

            Files.writeString(stable, "#1#Факультет інформатики#ФІ#kovalenko@ukma.edu.ua#fi@ukma.edu.ua\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#2#Факультет економічних наук#ФЕН#solovei@ukma.edu.ua#fen@ukma.edu.ua\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#3#Факультет соціальних наук та соціальних технологій#ФСНСТ#null#fcnct@ukma.edu.ua\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            throw new IOException("Failed to write faculties to stable", ex);
        }
    }

    /**
     * Initializes departments
     * @param stable Stable configuration path
     * @throws IOException Throws if writing fails
     */
    private void initializeDepartments(Path stable) throws IOException {
        try {
            Files.writeString(stable, ".Departments\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

            Files.writeString(stable, "#1#Кафедра математики#ФІ#marchenko@ukma.edu.ua#1-100\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#2#Кафедра інформатики#ФІ#yatsenko@ukma.edu.ua#1-201\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#3#Кафедра фінансів#ФЕН#klymenko@ukma.edu.ua#3-205\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#4#Кафедра економічної теорії#ФЕН#rudenko@ukma.edu.ua#2-102\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#5#Кафедра політології#ФСНСТ#palamarchuk@ukma.edu.ua#10-110\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#6#Кафедра соціології#ФСНСТ#hnatiuk@ukma.edu.ua#6-201\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            throw new IOException("Failed to write departments to stable", ex);
        }
    }

    /**
     * Initializes students
     * @param stable Stable configuration path
     * @throws IOException Throws if writing fails
     */
    private void initializeStudents(Path stable) throws IOException {
        try {
            Files.writeString(stable, ".Students\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

            Files.writeString(stable, "#1#Іван#Іванович#Шевченко#02.03.2007#ivan@gmail.com#+380534386432#3F34AB#1#42B#2025#бюджет#навчається#1-100\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#2#Марія#Ігорівна#Мельник#18.11.2006#maria@gmail.com#+380839301980#1K91AB#3#10B#2023#контракт#навчається#1-100\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#3#Андрій#Сергійович#Бондаренко#21.01.2007#andrii@gmail.com#380671234567#9Q77EF#1#12C#2025#бюджет#навчається#1-201\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#4#Катерина#Володимирівна#Романюк#30.05.2006#kateryna@gmail.com#+380931112244#2T10GH#3#15B#2023#контракт#навчається#1-201\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#5#Дмитро#Олександрович#Гриценко#09.12.2006#dmytro@gmail.com#+380991234111#4A88JK#2#20D#2024#бюджет#навчається#1-100\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#6#Софія#Андріївна#Ткаченко#17.04.2007#sofia@gmail.com#+380631234222#6B12LM#1#21A#2025#контракт#навчається#3-205\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#7#Максим#Ігорович#Литвин#03.02.2006#maksym@gmail.com#+380731234333#8C55NP#4#30B#2022#контракт#навчається#2-102\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#8#Наталія#Миколаївна#Савчук#26.09.2007#nataliia@gmail.com#+380501234444#5D19RS#1#31C#2025#бюджет#навчається#2-102\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#9#Марія#Ігорівна#Мельник#18.11.2006#maria2@gmail.com#+380839301980#1K91AB#3#10B#2023#контракт#навчається#10-110\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#10#Богдан#Віталійович#Сидоренко#11.06.2007#bohdan@gmail.com#+380681234555#3E73TU#1#11A#2025#бюджет#навчається#10-110\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#11#Юлія#Романівна#Олійник#07.07.2006#yuliia@gmail.com#+380951234666#7F20VW#2#50A#2024#контракт#навчається#6-201\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#12#Тарас#Михайлович#Кравченко#28.10.2007#taras@gmail.com#+380661234777#9G14XY#1#51B#2025#бюджет#навчається#6-201\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            throw new IOException("Failed to write students to stable", ex);
        }
    }

    /**
     * Initializes teachers
     * @param stable Stable configuration path
     * @throws IOException Throws if writing fails
     */
    private void initializeTeachers(Path stable) throws IOException {
        try {
            Files.writeString(stable, ".Teachers\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

            Files.writeString(stable, "#1#Олександр#Петрович#Коваленко#15.03.1975#kovalenko@ukma.edu.ua#+380501234567#професор#1-100#д.т.н.#професор#07.11.2015#250.0\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#2#Ірина#Михайлівна#Соловей#09.10.1982#solovei@ukma.edu.ua#+380503456789#доцент#1-100#к.ф.-м.н.#доцент#14.09.2011#220.0\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#3#Артем#Валерійович#Паламарчук#28.01.1978#palamarchuk@ukma.edu.ua#+380504567890#професор#1-201#д.т.н.#професор#03.03.2013#300.0\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#4#Оксана#Сергіївна#Гнатюк#16.05.1985#hnatiuk@ukma.edu.ua#+380505678901#старший викладач#1-201#к.т.н.#старший викладач#01.09.2016#190.0\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#5#Віктор#Олегович#Руденко#02.12.1974#rudenko@ukma.edu.ua#+380506789012#професор#3-205#д.е.н.#професор#12.02.2010#280.0\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#6#Тетяна#Юріївна#Марченко#19.04.1981#marchenko@ukma.edu.ua#+380507890123#доцент#3-205#к.е.н.#доцент#05.09.2012#210.0\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#7#Сергій#Анатолійович#Клименко#11.06.1977#klymenko@ukma.edu.ua#+380508901234#професор#2-102#д.е.н.#професор#20.10.2008#260.0\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#8#Людмила#Павлівна#Яценко#24.08.1986#yatsenko@ukma.edu.ua#+380509012345#доцент#2-102#к.е.н.#доцент#15.01.2017#200.0\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#9#Наталія#Іванівна#Шевченко#22.07.1980#shevchenko@ukma.edu.ua#+380502345678#доцент#10-110#к.т.н.#доцент#11.02.2009#430.0\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#10#Ігор#Степанович#Левченко#13.09.1979#levchenko@ukma.edu.ua#+380501119988#старший викладач#10-110#к.політ.н.#старший викладач#01.09.2014#240.0\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#11#Марина#Олександрівна#Кузьменко#05.01.1983#kuzmenko@ukma.edu.ua#+380501223344#доцент#6-201#к.соц.н.#доцент#10.03.2012#205.0\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.writeString(stable, "#12#Павло#Іванович#Данилюк#27.11.1976#danilyuk@ukma.edu.ua#+380501334455#професор#6-201#д.соц.н.#професор#18.09.2007#255.0\n", StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            throw new IOException("Failed to write teachers to stable", ex);
        }
    }
}
