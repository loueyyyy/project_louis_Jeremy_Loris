package ch.heigvd.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command (
    name = "modify",
    mixinStandardHelpOptions = true,
    version = "Invoicer 1.0",
    description = "Modify a CSV file and create a new modified CSV file."
)
public class ModifyCommand implements Runnable {

    @Option(names = {"-i", "--input"}, description = "Input CSV file path", required = true)
    private String inputFilePath;

    @Option(names = {"-o", "--output"}, description = "Output modified CSV file path", required = true)
    private String outputFilePath;

    @Override
    public void run() {
        // Implement the logic to modify the CSV file here
        System.out.println("Modifying CSV file: " + inputFilePath);
        System.out.println("Output will be saved to: " + outputFilePath);
    }
    
}
