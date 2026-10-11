package ch.heigvd;

import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import ch.heigvd.process.CsvDataExtractor;
import ch.heigvd.process.ModifyProcess;

public class App 
{
    public static void main( String[] args )
    {
        ModifyProcess.deleteColumns(
                "./src/main/java/ch/heigvd/io/data_file.csv",
                "./src/main/java/ch/heigvd/io/new_data_file.csv",
                "C E");
    }
}
