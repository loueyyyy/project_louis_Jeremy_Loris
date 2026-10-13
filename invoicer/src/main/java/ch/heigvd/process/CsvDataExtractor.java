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
            int buffer;
            int lineNumber = 0;
            int elementNumber = 0;

            data.add(new ArrayList<>());
            data.get(lineNumber).add("");

            if ((character = in.read()) != -1) {
                buffer = character;
                while ((character = in.read()) != -1) {

                    if ((char) buffer == '\r') {
                        buffer = character;
                        continue;
                    }

                    if ((char) buffer == '\n') {
                        lineNumber++;
                        data.add(new ArrayList<>());
                        data.get(lineNumber).add("");
                        elementNumber = 0;
                        buffer = character;
                        continue;
                    }

                    if ((char) buffer == ';') {
                        elementNumber++;
                        data.get(lineNumber).add("");
                        buffer = character;
                        continue;
                    }

                    appendToData(lineNumber, elementNumber, (char) buffer);
                    buffer = character;
                }
                if ((char) buffer != ';') {
                    if ((char) buffer != '\r'
                    && (char) buffer != '\n') {
                        appendToData(lineNumber, elementNumber, (char) buffer);
                    }
                }
                else
                data.get(lineNumber).add("");

                while (data.get(lineNumber).size() != data.get(0).size()) {
                    data.remove(lineNumber);
                    --lineNumber;
                }
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public String getData(int lineNumber, int columnNumber) {
        return data.get(lineNumber).get(columnNumber);
    }

    public void setData(int lineNumber, int columnNumber, String value)
    {
        data.get(lineNumber).set(columnNumber, value);
    }

    public void appendData(int lineNumber, int columnNumber, String value)
    {
        if (lineNumber >= data.size())
        {
            appendData(value);
            return;
        }

        if (columnNumber >= data.get(lineNumber).size())
        {
            data.get(lineNumber).add(value);
            return;
        }

        data.get(lineNumber).add(columnNumber, value);
    }

    public void appendData(int lineNumber, String value)
    {
        if (lineNumber >= data.size())
        {
            appendData(value);
            return;
        }

        data.get(lineNumber).add(value);
    }

    public void appendData(String value)
    {
        int lineNumber = data.size();
        data.add(new ArrayList<>());
        data.get(lineNumber).add(value);
    }

    public void appendToData(int lineNumber, int columnNumber, String value)
    {
        String element = data.get(lineNumber).get(columnNumber) + value;
        data.get(lineNumber).set(columnNumber, element);
    }

    public void appendToData(int lineNumber, int columnNumber, char value)
    {
        String element = data.get(lineNumber).get(columnNumber) + value;
        data.get(lineNumber).set(columnNumber, element);
    }

    public int linesNumber()
    {
        return data.size();
    }

    public int columnsNumber()
    {
        return data.get(0).size();
    }
}