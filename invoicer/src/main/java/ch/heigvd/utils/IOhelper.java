package ch.heigvd.utils;

import java.nio.file.Files;
import java.nio.file.Paths;


public final class IOhelper {
    
    public static int inputFileCheck(String inputFilePath) {
        // Implement the logic to check if the input file exists and is readable
        System.out.println("Checking input file: " + inputFilePath);
        if (inputFilePath == null || inputFilePath.isEmpty()) {
            System.err.println("Error: Input file path is null or empty.");
            return 1; // Return an error code
        }
        if (!Files.exists(Paths.get(inputFilePath))) {
            System.err.println("Error: Input file does not exist.");
            return 1; // Return an error code
        }
        if (!Files.isReadable(Paths.get(inputFilePath))) {
            System.err.println("Error: Input file is not readable.");
            return 1; // Return an error code
        }
        return 0; // Return 0 if the check passes, or an error code if it fails
    }

    public static int outputFileCheck(String outputFilePath) {
        // Implement the logic to check if the output file can be created or is writable
        System.out.println("Checking output file: " + outputFilePath);
        if (Files.notExists(Paths.get(outputFilePath))) {
            System.err.println("Error: Output file path is null or empty.");
            try {
                Files.createDirectories(Paths.get(outputFilePath).getParent());
                Files.createFile(Paths.get(outputFilePath));
            } catch (Exception e) {
                System.err.println("Error: Unable to create or write to the output file.");
                return 1; // Return an error code
            }
        }
        if (!Files.isWritable(Paths.get(outputFilePath))) {
            System.err.println("Error: Output file is not writable.");
            return 1; // Return an error code
        }
        return 0; // Return 0 if the check passes, or an error code if it fails
    }
}
