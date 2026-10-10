package ch.heigvd.process;

import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.io.Writer;
import java.io.FileWriter;

import ch.heigvd.process.CsvDataExtractor;
import ch.heigvd.process.LatexFileReader;

public class InvoiceProcess {

    public static void invoiceProcess(String inputFilePath, String outputFilePath) {

        try {
            Reader csvReader = new FileReader(inputFilePath, StandardCharsets.UTF_8);
            CsvDataExtractor csvExtractor = new CsvDataExtractor();

            Reader invoiceModelReader = new FileReader("./src/main/java/ch/heigvd/model/invoice_model.tex", StandardCharsets.UTF_8);
            LatexFileReader invoiceModelExtractor = new LatexFileReader();

            Writer invoiceModelWriter = new FileWriter("./src/main/java/ch/heigvd/io/new_invoice.tex", StandardCharsets.UTF_8);

            csvExtractor.extraction(csvReader);
            invoiceModelExtractor.read(invoiceModelReader);

            csvReader.close();
            invoiceModelReader.close();

            int[] coordinates = invoiceModelExtractor.search("HEADERS");

//            -- TODO : this line doesn't work if CsvDataExtractor doesn't have the method linesNumber
            int newInvoiceMaxLineIndex = invoiceModelExtractor.linesNumber() + csvExtractor.linesNumber() - 1;

//            -- TODO : these lines doesn't work if CsvDataExtractor doesn't have the method columnsNumber and linesNumber
            int csvMaxLinesIndex = csvExtractor.linesNumber() - 1;
            int csvMaxColumnsIndex = csvExtractor.columnsNumber() - 1;

            for (int lineNumber = 0; lineNumber < newInvoiceMaxLineIndex; lineNumber++)
            {
                if (lineNumber == coordinates[0])
                {
                    for (int csvLineNumber = 0; csvLineNumber < csvMaxLinesIndex; csvLineNumber++)
                    {
                        for (int columnNumber = 0; columnNumber < csvMaxColumnsIndex; columnNumber++)
                        {
                            String csvData = csvExtractor.getData(csvLineNumber, columnNumber);
                            invoiceModelExtractor.appendData(lineNumber, columnNumber, csvData);
                        }
                        lineNumber++;
                    }
                }
            }

            invoiceModelWriter.close();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}