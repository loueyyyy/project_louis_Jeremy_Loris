package ch.heigvd.process;

import ch.heigvd.utils.IOhelper;

public class ModifyProcess {

    public static void modifyProcess(String inputFilePath, String outputFilePath) {
        IOhelper.inputFileCheck(inputFilePath);
        IOhelper.outputFileCheck(outputFilePath);
        // TODO: Implement invoice processing logic
    }

    public static void replace(String inputFilePath, String outputFilePath, String replace) {
        IOhelper.inputFileCheck(inputFilePath);
        IOhelper.outputFileCheck(outputFilePath);
    }
    
    public static void deleteRows(String inputFilePath, String outputFilePath, String deleteRows) {
        IOhelper.inputFileCheck(inputFilePath);
        IOhelper.outputFileCheck(outputFilePath);
    }

    public static void deleteColumns(String inputFilePath, String outputFilePath, String deleteColumns) {
        IOhelper.inputFileCheck(inputFilePath);
        IOhelper.outputFileCheck(outputFilePath);
    }

    public static void add(String inputFilePath, String outputFilePath, String add) {
        IOhelper.inputFileCheck(inputFilePath);
        IOhelper.outputFileCheck(outputFilePath);
    }

    public static void sort(String inputFilePath, String outputFilePath, String sort) {
        IOhelper.inputFileCheck(inputFilePath);
        IOhelper.outputFileCheck(outputFilePath);
    }

    
}