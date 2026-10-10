package ch.heigvd.process;

import java.io.Reader;
import java.util.ArrayList;

public class CsvDataExtractor {

    ArrayList<ArrayList<String>> data;

    public CsvDataExtractor() {


        data = new ArrayList<>();


    }

    public void printData() {

        for(ArrayList<String> line : data){

            for (String element : line){
                System.out.print(element + " ; ");
            }

            System.out.println();
        }
    }

    public void extraction(Reader in) {

        try {
            int character;
            int lineNumber = 0;
            int elementNumber = 0;

            data.add(new ArrayList<>());
            data.get(lineNumber).add("");

            while ((character = in.read()) != -1) {

                if ((char) character == '\r')
                    continue;

                if ((char) character == '\n') {
                    lineNumber++;
                    data.add(new ArrayList<>());
                    data.get(lineNumber).add("");
                    elementNumber = 0;
                    continue;
                }

                if (character == ';') {
                    elementNumber++;
                    data.get(lineNumber).add("");
                    continue;
                }

                String element = data.get(lineNumber).get(elementNumber) + (char)character;
                data.get(lineNumber).set(elementNumber, element);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }
}