package ch.heigvd.process;

import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.io.Writer;
import java.io.FileWriter;

import java.util.ArrayList;

import ch.heigvd.process.CsvDataExtractor;
import ch.heigvd.process.LatexFileReader;

public class InvoiceProcess {

    public static void invoiceProcess(String inputFilePath, String outputFilePath) {

        try {
            Reader csvReader = new FileReader(inputFilePath, StandardCharsets.UTF_8);
            CsvDataExtractor csvExtractor = new CsvDataExtractor();

            Reader invoiceModelReader = new FileReader("./src/main/java/ch/heigvd/model/invoice_model.tex", StandardCharsets.UTF_8);
            LatexFileReader invoiceModelExtractor = new LatexFileReader();

            Writer invoiceModelWriter = new FileWriter(outputFilePath, StandardCharsets.UTF_8);

            csvExtractor.extraction(csvReader);
            invoiceModelExtractor.read(invoiceModelReader);

            csvReader.close();
            invoiceModelReader.close();

            int referenceWordLine = invoiceModelExtractor.search("--- HEADERS ---");
            int referenceWordLineNumber = referenceWordLine + 1;

            int prixFieldIndex = -1;

            for (int csvColumn = 0; csvColumn < csvExtractor.columnsNumber(); csvColumn++)
            {
                String csvData = csvExtractor.getData(0, csvColumn);

                if (csvData.toLowerCase().equals("prix"))
                {
                    prixFieldIndex = csvColumn;
                }

                if (csvColumn < csvExtractor.columnsNumber() - 1)
                    csvData += " &";

                invoiceModelExtractor.appendNewLine(referenceWordLineNumber++, csvData);
            }

            referenceWordLine = invoiceModelExtractor.search("--- INSTANCES ---");
            referenceWordLineNumber = referenceWordLine + 1;

            double subTotalPrice = 0;

            for (int csvLine = 1; csvLine < csvExtractor.linesNumber(); csvLine++)
            {
                for (int csvColumn = 0; csvColumn < csvExtractor.columnsNumber(); csvColumn++)
                {
                    String csvData = csvExtractor.getData(csvLine, csvColumn);

                    if (csvColumn == prixFieldIndex)
                    {
                        subTotalPrice += Double.parseDouble(csvData);
                        csvData += " CHF";
                    }

                    if (csvColumn < csvExtractor.columnsNumber() - 1)
                        csvData += " &";
                    else
                        csvData += "\\\\";

                    invoiceModelExtractor.appendNewLine(referenceWordLineNumber++, csvData);
                }
            }

            referenceWordLine = invoiceModelExtractor.search("--- SUBTOTAL ---");
            referenceWordLineNumber = referenceWordLine + 1;

            String subTotalPriceString = "" + subTotalPrice + " CHF";

            invoiceModelExtractor.appendNewLine(referenceWordLineNumber, subTotalPriceString);

            referenceWordLine = invoiceModelExtractor.search("--- VAT ---");
            referenceWordLineNumber = referenceWordLine + 1;

            double vat = subTotalPrice * 8.1 / 100.0;
            String vatString = "" + vat + " CHF";

            invoiceModelExtractor.appendNewLine(referenceWordLineNumber, vatString);

            referenceWordLine = invoiceModelExtractor.search("--- TOTAL ---");
            referenceWordLineNumber = referenceWordLine + 1;

            double totalPrice = subTotalPrice + vat;
            String totalPriceString = "" + totalPrice + " CHF";

            invoiceModelExtractor.appendNewLine(referenceWordLineNumber, totalPriceString);

            for (int invoiceLine = 0; invoiceLine < invoiceModelExtractor.linesNumber(); invoiceLine++)
            {
                String line = invoiceModelExtractor.getData(invoiceLine);

                invoiceModelWriter.write(line);
                invoiceModelWriter.write(System.lineSeparator());
            }

            invoiceModelWriter.close();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}