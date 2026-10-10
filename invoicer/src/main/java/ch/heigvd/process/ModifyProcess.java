package ch.heigvd.process;

import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ModifyProcess {

    Reader in;

    static void fileOpening(String inputFilePath) {

        Reader in = new FileReader(inputFilePath, StandardCharsets.UTF_8);
        CsvDataExtractor extractor = new CsvDataExtractor();

    }

    public static void replace(String inputFilePath, String outputFilePath, String replace)
    {

    }

//    public static void deleteRows(String inputFilePath, String outputFilePath, String deleteRows)
//    {
//
//    }

//    public static void deleteColumns(String inputFilePath, String outputFilePath, String deleteColumns)
//    {
//
//    }

//    public static void add(String inputFilePath, String outputFilePath, String add)
//    {
//
//    }

//    public static void sort(String inputFilePath, String outputFilePath, String sort)
//    {
//
//    }
}