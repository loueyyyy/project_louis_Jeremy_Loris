package ch.heigvd.process;

import ch.heigvd.utils.IOhelper;

public class InvoiceProcess {

    public static void invoiceProcess(String inputFilePath, String outputFilePath) {
        IOhelper.inputFileCheck(inputFilePath);
        IOhelper.outputFileCheck(outputFilePath);
        // TODO: Implement invoice processing logic
    }
}