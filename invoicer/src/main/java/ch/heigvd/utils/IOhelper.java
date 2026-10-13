package ch.heigvd.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Provides file validation utilities for input/output paths and CSV or LaTeX extensions.
 */
public final class IOhelper {

    public static int inputFileCheck(String inputFilePath) {
        // Implement the logic to check if the input file exists and is readable
        System.out.println("Checking input file: " + inputFilePath);
        if (inputFilePath == null || inputFilePath.isBlank()) {
            System.err.println("Error: Input file path is null or empty.");
            return 1; // Return an error code
        }

        Path path = Paths.get(inputFilePath);
        if (!Files.exists(path)) {
            System.err.println("Error: Input file does not exist.");
            return 1; // Return an error code
        }
        if (!Files.isReadable(path)) {
            System.err.println("Error: Input file is not readable.");
            return 1; // Return an error code
        }
        if (!Files.isRegularFile(path)) {
            System.err.println("Error: Input file is not a file.");
            return 1; // Return an error code
        }
        return 0; // Return 0 if the check passes, or an error code if it fails
    }

    public static int outputFileCheck(String outputFilePath) {
        // Implement the logic to check if the output file can be created or is writable
        System.out.println("Checking output file: " + outputFilePath);
        if (outputFilePath == null || outputFilePath.isBlank()) {
            System.err.println("Error: Output file path is null or empty.");
            return 1;
        }
        Path path = Paths.get(outputFilePath);

        try {
            Path parent = path.toAbsolutePath().getParent();

            //manage the case where the parent doesn't exist and creates it
            if (parent != null) {
                Files.createDirectories(parent);
            }
            //same thing but for the path itself
            if (Files.notExists(path)) {
                Files.createFile(path);
            }

            // Check that the output is a regular writable file
            if (!Files.isRegularFile(path) || !Files.isWritable(path)) {
                System.err.println("Error: Output file is not writable.");
                return 1;
            }

            return 0;

        } catch (IOException | SecurityException e) {
            System.err.println("Error: Unable to create or write to the output file: " + e.getMessage());
            return 1;
        }
    }

    public static boolean csvExtension(String filePath) {
        return filePath != null && filePath.toLowerCase().endsWith(".csv");
    }

    public static boolean texExtension(String filePath) {
        return filePath != null && filePath.toLowerCase().endsWith(".tex");
    }

    
}