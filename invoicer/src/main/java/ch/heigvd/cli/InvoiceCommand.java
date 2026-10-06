package ch.heigvd.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "invoice",
    mixinStandardHelpOptions = true,
    version = "Invoicer 1.0",
    description = "Create PDF invoices from a CSV file."
)
public class InvoiceCommand implements Runnable {

    @Option(names = {"-i", "--input"}, description = "Input CSV file path", required = true)
    private String inputFilePath;

    @Option(names = {"-o", "--output"}, description = "Output PDF invoice file path", required = true)
    private String outputFilePath;

    @Override
    public void run() {
        // Implement the logic to create invoices from the CSV file here
        System.out.println("Creating invoices from CSV file: " + inputFilePath);
        System.out.println("Output will be saved to: " + outputFilePath);
    }
}

