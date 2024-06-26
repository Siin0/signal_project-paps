package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class to define what file to output to via a string pointing to the directory of said file.
 * Can output information regarding a patient with the output() method
 */
public class FileOutputStrategy implements OutputStrategy {

    // Changed BaseDirectory to baseDirectory
    private String baseDirectory;

    // Changed file_map to fileMap
    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    /**
     * Declares what file to output to
     *
     * @param baseDirectory string of the directory to output
     */
    public FileOutputStrategy(String baseDirectory) {
        this.baseDirectory = baseDirectory; // Removed extra line
    }

    /**
     * Outputs information of a patient
     *
     * @param patientId The ID of the patient
     * @param timestamp The time of registry
     * @param label Category of input
     * @param data Data corresponding to the method of registry
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        // Spread over 2 lines to avoid exceeding column limit
        String FilePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label
                + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(FilePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + FilePath + ": " + e.getMessage());
        }
    }
}