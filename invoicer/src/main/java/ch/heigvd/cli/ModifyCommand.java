package ch.heigvd.cli;

import ch.heigvd.process.ModifyProcess;
import ch.heigvd.utils.IOhelper;
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





    @Option (names = {"-r", "--replace"}, description = "Replace specific values in the CSV file", required = false)
    private String replace;

    @Option (names = {"-dr", "--delete-rows"}, description = "Delete specific rows from the CSV file", required = false)
    private String deleteRows;

    @Option (names = {"-dc", "--delete-columns"}, description = "Delete specific columns from the CSV file", required = false)
    private String deleteColumns;

    @Option (names = {"-a", "--add"}, description = "Add new rows and/or columns to the CSV file", required = false)
    private String add;

    @Option (names = { "-s", "--sort" }, description = "Sort the CSV file by specific columns or lines", required = false)
    private String sort;

    
    @Override
    public void run() {
        // Implement the logic to modify the CSV file here
        try {
            // Validate input file
            if (!IOhelper.csvExtension(inputFilePath)) {
                throw new IllegalArgumentException("Input must be a .csv file: " + inputFilePath);
            }
            IOhelper.inputFileCheck(inputFilePath, "input");

            // Validate output file
            if (!IOhelper.csvExtension(outputFilePath)) {
                throw new IllegalArgumentException("Output must be a .csv file: " + outputFilePath);
            }
            IOhelper.outputFileCheck(outputFilePath, "output");
            
            // Check if at least one modification option is provided
            if (replace == null && deleteRows == null && deleteColumns == null && add == null && sort == null) {
                throw new IllegalArgumentException("At least one modification option must be provided.");
            }

            System.out.println("Modifying CSV file: " + inputFilePath);
            System.out.println("Output will be saved to: " + outputFilePath);

            // Perform modify operation
            if (replace != null) {
                System.out.println("Replacing values: " + replace);
                ModifyProcess.replace(inputFilePath, outputFilePath, replace);
            }
            if (deleteRows != null) {
                System.out.println("Deleting rows: " + deleteRows);
                ModifyProcess.deleteRows(inputFilePath, outputFilePath, deleteRows);
            }
            if (deleteColumns != null) {
                System.out.println("Deleting columns: " + deleteColumns);
                ModifyProcess.deleteColumns(inputFilePath, outputFilePath, deleteColumns);
            }
            if (add != null) {
                System.out.println("Adding rows/columns: " + add);
                ModifyProcess.add(inputFilePath, outputFilePath, add);
            }
            if (sort != null) {
                System.out.println("Sorting by: " + sort);
                ModifyProcess.sort(inputFilePath, outputFilePath, sort);
            }
            
            System.out.println("Success. Created invoice file: " + outputFilePath + " from input CSV file: " + inputFilePath);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
    
}
