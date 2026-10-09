package ch.heigvd.cli;

import ch.heigvd.process.InvoiceProcess;
import ch.heigvd.utils.IOhelper;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;


@Command(
    name = "invoice",
    mixinStandardHelpOptions = true,
    version = "Invoicer 1.0",
    description = "Create PDF invoices from a CSV file."
)
public class InvoiceCommand implements Runnable {

    @Option(names = {"-i", "--input"},
        description = "Input CSV file path", required = true)
    private String inputFilePath;

    @Option(names = {"-o", "--output"}, description = "Output PDF invoice file path", required = true)
    private String outputFilePath;

    @Override
    public void run() {

        try {
            // Validate input file
            if (!IOhelper.csvExtension(inputFilePath)) {
                throw new IllegalArgumentException("Input must be a .csv file: " + inputFilePath);
            }
            IOhelper.inputFileCheck(inputFilePath, "input");

            // Validate output file
            if (!IOhelper.pdfExtension(outputFilePath)) {
                throw new IllegalArgumentException("Output must be a .pdf file: " + outputFilePath);
            }
            IOhelper.outputFileCheck(outputFilePath, "output");

            System.out.println("Creating invoices from CSV file: " + inputFilePath);
            System.out.println("Output will be saved to: " + outputFilePath);

            // Perform invoice operation
            InvoiceProcess.invoiceProcess(inputFilePath, outputFilePath);

            System.out.println("Success. Created invoice PDF file: " + outputFilePath + " from input CSV file: " + inputFilePath);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}


