package ua.university.network;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ua.university.exceptions.LocalFileSaveException;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Client {

    private final Path local;

    /** Initializes client and local path */
    public Client(Path local) throws IOException {
        if (Files.notExists(local.resolve("Faculties.json"))) Files.createFile(local.resolve("Faculties.json"));
        if (Files.notExists(local.resolve("Departments.json"))) Files.createFile(local.resolve("Departments.json"));
        if (Files.notExists(local.resolve("Students.json"))) Files.createFile(local.resolve("Students.json"));
        if (Files.notExists(local.resolve("Teachers.json"))) Files.createFile(local.resolve("Teachers.json"));

        this.local = local;
    }

    /** OVERLOADING. Initializes client and local path */
    public Client() throws IOException {
        this(Path.of("src", "main", "resources", "data", "running"));
    }

    /** Download data from the server */
    public void downloadData() throws IOException {
        try (Socket socket = new Socket("localhost", 5432);
             BufferedWriter out = new BufferedWriter(
                     new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {
            out.write("DOWNLOAD\n");
            out.flush();

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line).append("\n");
            }

            if (!response.isEmpty())
                response.setLength(response.length()-1);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> data = mapper.readValue(response.toString(), new TypeReference<>() {});

            Thread saveLocalStudents = new Thread(() -> {
                try {
                    Files.writeString(local.resolve("Students.json"), data.get("students"), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    throw new LocalFileSaveException("Failed to save students to local file", e);
                }
            });
            Thread saveLocalFaculties = new Thread(() -> {
                try {
                    Files.writeString(local.resolve("Faculties.json"), data.get("faculties"), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    throw new LocalFileSaveException("Failed to save faculties to local file", e);
                }
            });
            Thread saveLocalDepartments = new Thread(() -> {
                try {
                    Files.writeString(local.resolve("Departments.json"), data.get("departments"), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    throw new LocalFileSaveException("Failed to save departments to local file", e);
                }
            });
            Thread saveLocalTeachers = new Thread(() -> {
                try {
                    Files.writeString(local.resolve("Teachers.json"), data.get("teachers"), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    throw new LocalFileSaveException("Failed to save teachers to local file", e);
                }
            });

            saveLocalStudents.start();
            saveLocalFaculties.start();
            saveLocalDepartments.start();
            saveLocalTeachers.start();

            try {
                saveLocalStudents.join();
                saveLocalFaculties.join();
                saveLocalDepartments.join();
                saveLocalTeachers.join();
            } catch (InterruptedException e) {
                throw new IOException("Thread interrupted on writing", e);
            }
        } catch (UnknownHostException ex) {
            throw new UnknownHostException("Server is off");
        }
    }

    /** Saves changes to the server version */
    public void saveChanges() throws IOException {
        try (Socket socket = new Socket("localhost", 5432);
             BufferedWriter out = new BufferedWriter(
                     new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {
            out.write("UPDATE\n");
            out.flush();

            Map<String, String> data = new HashMap<>();
            data.put("students", Files.readString(local.resolve("Students.json"), StandardCharsets.UTF_8));
            data.put("faculties", Files.readString(local.resolve("Faculties.json"), StandardCharsets.UTF_8));
            data.put("departments", Files.readString(local.resolve("Departments.json"), StandardCharsets.UTF_8));
            data.put("teachers", Files.readString(local.resolve("Teachers.json"), StandardCharsets.UTF_8));

            ObjectMapper mapper = new ObjectMapper();
            String stringFiles = mapper.writeValueAsString(data);

            out.write(stringFiles);
            out.write("\n#END#\n");
            out.flush();

            if (!in.readLine().equals("SUCCESS")) throw new IOException("Failed to upload changes to the server");
        } catch (UnknownHostException ex) {
            throw new UnknownHostException("Server is off");
        }
    }

    /** Stops the server */
    public void stopServer() throws IOException {
        try (Socket socket = new Socket("localhost", 5432);
             BufferedWriter out = new BufferedWriter(
                     new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {
            out.write("END\n");
        } catch (UnknownHostException ex) {
            throw new UnknownHostException("Server is off");
        }
    }
}