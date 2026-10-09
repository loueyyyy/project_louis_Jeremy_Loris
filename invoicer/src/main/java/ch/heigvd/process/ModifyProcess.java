package ch.heigvd.process;

import ch.heigvd.utils.IOhelper;

public class ModifyProcess {

    public static void modifyProcess(String inputFilePath, String outputFilePath) {
        IOhelper.inputFileCheck(inputFilePath);
        IOhelper.outputFileCheck(outputFilePath);
        // TODO: Implement invoice processing logic
    }
}