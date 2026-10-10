package ch.heigvd;

//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import ch.heigvd.io.CsvDataExtractor;

public class App 
{
    public static void main( String[] args )
    {
        try {
            // BufferedReader reader = new BufferedReader(
            //          new InputStreamReader(new FileInputStream("./src/main/java/ch/heigvd/model/data_file.csv"), StandardCharsets.UTF_8));

            Reader reader = new FileReader("./src/main/java/ch/heigvd/model/data_file.csv", StandardCharsets.UTF_8);

            CsvDataExtractor extractor = new CsvDataExtractor();

            extractor.extraction(reader);

            extractor.printData();

            reader.close();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
