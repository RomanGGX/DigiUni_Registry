package ua.university.network;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ua.university.exceptions.ServerSaveException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Launch {

    private static final Path mainPath = Path.of("src", "main", "resources", "data", "stable");

    /** Launches server */
    public static void main(String[] args) {
        boolean active = true;

        while (active) {
            System.out.println("Server is waiting for request");

            try (ServerSocket server = new ServerSocket(5432);
                 Socket socket = server.accept();
                 BufferedReader in = new BufferedReader(
                         new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                 BufferedWriter out = new BufferedWriter(
                         new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {
                socket.setSoTimeout(5_000);
                String line = in.readLine();
                switch (line) {
                    case "DOWNLOAD":
                        sendDatabase(out);
                        System.out.println("Server sent database");
                        break;
                    case "UPDATE":
                        updateDatabase(in, out);
                        System.out.println("Server saved changes");
                        break;
                    case "END":
                        active = false;
                        System.out.println("Server stopped");
                        break;
                    default:
                        out.write("Wrong request");
                }

                out.flush();
            } catch (IOException ex) {
                // Log level error
            }
        }
    }

    /** Sends database to user */
    private static void sendDatabase(BufferedWriter out) throws IOException {
        Map<String, String> data = new HashMap<>();
        data.put("students", Files.readString(mainPath.resolve("Students.json"), StandardCharsets.UTF_8));
        data.put("faculties", Files.readString(mainPath.resolve("Faculties.json"), StandardCharsets.UTF_8));
        data.put("departments", Files.readString(mainPath.resolve("Departments.json"), StandardCharsets.UTF_8));
        data.put("teachers", Files.readString(mainPath.resolve("Teachers.json"), StandardCharsets.UTF_8));

        ObjectMapper mapper = new ObjectMapper();
        String stringFiles = mapper.writeValueAsString(data);

        out.write(stringFiles);
    }

    /** Updates database from user request */
    private static void updateDatabase(BufferedReader in, BufferedWriter out) throws IOException {
        StringBuilder response = new StringBuilder();
        String line;

        while (!(line = in.readLine()).equals("#END#")) {
            response.append(line).append("\n");
        }

        if (!response.isEmpty())
            response.setLength(response.length()-1);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = mapper.readValue(response.toString(), new TypeReference<>() {});

        Thread saveStudents = new Thread(() -> {
            try {
                Files.writeString(mainPath.resolve("Students.json"), data.get("students"), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new ServerSaveException("Failed to save students to the server", e);
            }
        });
        Thread saveFaculties = new Thread(() -> {
            try {
                Files.writeString(mainPath.resolve("Faculties.json"), data.get("faculties"), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new ServerSaveException("Failed to save faculties to the server", e);
            }
        });
        Thread saveDepartments = new Thread(() -> {
            try {
                Files.writeString(mainPath.resolve("Departments.json"), data.get("departments"), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new ServerSaveException("Failed to save departments to the server", e);
            }
        });
        Thread saveTeachers = new Thread(() -> {
            try {
                Files.writeString(mainPath.resolve("Teachers.json"), data.get("teachers"), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new ServerSaveException("Failed to save teachers to the server", e);
            }
        });

        saveStudents.start();
        saveFaculties.start();
        saveDepartments.start();
        saveTeachers.start();

        try {
            saveStudents.join();
            saveFaculties.join();
            saveDepartments.join();
            saveTeachers.join();
        } catch (InterruptedException e) {
            throw new IOException("Thread interrupted on writing", e);
        }

        out.write("SUCCESS\n");
        out.flush();
    }
}