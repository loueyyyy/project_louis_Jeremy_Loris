package ch.heigvd.process;

import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import ch.heigvd.io.CsvDataExtractor;

public class InvoiceProcess {

    public static void invoiceProcess(String inputFilePath, String outputFilePath) {

        Reader reader = new FileReader(inputFilePath, StandardCharsets.UTF_8);
        CsvDataExtractor extractor = new CsvDataExtractor();

        reader.close();
    }
}