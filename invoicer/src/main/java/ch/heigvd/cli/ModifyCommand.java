package ch.heigvd.cli;

import ch.heigvd.process.ModifyProcess;
import ch.heigvd.utils.IOhelper;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command (
    name = "modify",
    mixinStandardHelpOptions = true,
    version = "Invoicer 1.0",
    description = "Modify a CSV file and create a new modified CSV file.",
    sortOptions = false,
    sortSynopsis = false    
)

public class ModifyCommand implements Runnable {

    @Option(   
        names = {"-i", "--input"},
        description = "Input CSV file path",
        required = true)
    private String inputFilePath;

    @Option(
        names = {"-o", "--output"},
        description = "Output modified CSV file path",
        required = true)
    private String outputFilePath;

    @Option (
        names = {"-r", "--replace"},
        description = "Replace specific values in the CSV file",
        required = false,
        paramLabel = "lettreCol,numRow,newVal",
        arity = "1..*")
    private String replace;

    @Option (
        names = {"-dr", "--delete-rows"},
        description = "Delete specific rows from the CSV file",
        required = false,
        paramLabel = "numRow or numRow-numRow",
        arity = "1..*")
    private String deleteRows;

    @Option (
        names = {"-dc", "--delete-columns"},
        description = "Delete specific columns from the CSV file",
        required = false,
        paramLabel = "lettreCol or lettreCol-lettreCol",
        arity = "1..*")
    private String deleteColumns;

    @Option (
        names = {"-a", "--add"},
        description = "Add new row to the CSV file",
        required = false,
        paramLabel = "newLine",
        arity = "1")
    private String add;

    @Option (
        names = { "-s", "--sort" },
        description = "Sort the CSV file by specific columns or lines",
        required = false,
        paramLabel = "firstElemCol",
        arity = "1")
    private String sort;

    
    @Override
    public void run() {
        // Implement the logic to modify the CSV file here
        try {
            // Validate input file
            if (!IOhelper.csvExtension(inputFilePath)) {
                throw new IllegalArgumentException("Input must be a .csv file: " + inputFilePath);
            }
            IOhelper.inputFileCheck(inputFilePath);

            // Validate output file
            if (!IOhelper.csvExtension(outputFilePath)) {
                throw new IllegalArgumentException("Output must be a .csv file: " + outputFilePath);
            }
            IOhelper.outputFileCheck(outputFilePath);
            
            // Check if at least one modification option is provided
            if (replace == null && deleteRows == null && deleteColumns == null && add == null && sort == null) {
                throw new IllegalArgumentException("At least one modification option must be provided.");
            }

            System.out.println("Modifying CSV file: " + inputFilePath);
            System.out.println("Output will be saved to: " + outputFilePath);

            /********************************************************************************************
            // TODO: gérer les conflits entre les options (ex: deleteRows et add)
            ********************************************************************************************/

            // Perform modify operation
            if (replace != null) {
                checkReplace(replace);
                System.out.println("Replacing values: " + replace);
                ModifyProcess.replace(inputFilePath, outputFilePath, replace);
            }
            if (deleteRows != null) {
                checkRows(deleteRows);
                System.out.println("Deleting rows: " + deleteRows);
                ModifyProcess.deleteRows(inputFilePath, outputFilePath, deleteRows);
            }
            if (deleteColumns != null) {
                checkColumns(deleteColumns);
                System.out.println("Deleting columns: " + deleteColumns);
                ModifyProcess.deleteColumns(inputFilePath, outputFilePath, deleteColumns);
            }
            if (add != null) {
                checkAdd(add);
                System.out.println("Adding rows/columns: " + add);
                ModifyProcess.add(inputFilePath, outputFilePath, add);
            }
            if (sort != null) {
                checkSort(sort);
                System.out.println("Sorting by: " + sort);
                ModifyProcess.sort(inputFilePath, outputFilePath, sort);
            }
            
            System.out.println("Success. Created invoice file: " + outputFilePath + " from input CSV file: " + inputFilePath);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    // Checks "colLetter,rowNumber,newVal"
    private void checkReplace(String value) {
        String[] parts = value.split(",", 3);
        if (parts.length != 3) {
            throw new IllegalArgumentException("--replace: expected format <colLetter,rowNumber,newVal> (got: " + value + ")");
        }
        if (!parts[0].matches("[A-Za-z]+")) {
            throw new IllegalArgumentException("--replace: column must be one or more letters (got: " + parts[0] + ")");
        }
        if (!parts[1].matches("[0-9]+") || Integer.parseInt(parts[1]) < 1) {
            throw new IllegalArgumentException("--replace: row must be a number >= 1 (got: " + parts[1] + ")");
        }
    }

    // Checks "numRow" or "numRow-numRow"
    private void checkRows(String value) {
        if (!value.matches("[0-9]+(-[0-9]+)?")) {
            throw new IllegalArgumentException("--delete-rows: expected format <rowNumber> or <rowNumber-rowNumber> (got: " + value + ")");
        }
        String[] parts = value.split("-");
        int from = Integer.parseInt(parts[0]);
        int to = Integer.parseInt(parts[parts.length - 1]);
        if (from < 1 || from > to) {
            throw new IllegalArgumentException("--delete-rows: invalid range (got: " + value + ")");
        }
    }

    // Checks "numCol" or "numCol-numCol"
    private void checkColumns(String value) {
        if (!value.matches("[A-Za-z]+(-[A-Za-z]+)?")) {
            throw new IllegalArgumentException("--delete-columns: expected format <colLetter> or <colLetter-colLetter> (got: " + value + ")");
        }
    }

    // Checks value not empty
    private void checkAdd(String value) {
        if (value.isBlank()) {
            throw new IllegalArgumentException("--add: the new line cannot be empty");
        }
    }

    // Checks "firstElemCol"
    private void checkSort(String value) {
        if (!value.matches("[A-Za-z]+")) {
            throw new IllegalArgumentException("--sort: a column (letters) is expected (got: " + value + ")");
        }
    }
    
}
