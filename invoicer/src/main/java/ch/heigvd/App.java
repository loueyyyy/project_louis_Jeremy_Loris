package ch.heigvd;

//import java.io.Reader;
//import java.io.FileReader;
//import java.nio.charset.StandardCharsets;
//
//import ch.heigvd.io.CsvDataExtractor;

import java.io.IOException;
import  ch.heigvd.process.InvoiceProcess;

public class App 
{
    public static void main( String[] args )
    {

        InvoiceProcess.invoiceProcess(
                "./src/main/java/ch/heigvd/io/data_file.csv",
                "./src/main/java/ch/heigvd/io/invoice.tex"
        );
    }
}
