package ch.heigvd.process;

import java.io.Reader;
import java.util.ArrayList;

public class LatexFileReader
{
    ArrayList<ArrayList<String>> data;

    public LatexFileReader() {

        data = new ArrayList<>();

    }

    public void printData() {

        for(ArrayList<String> line : data){

            for (String element : line){
                System.out.print(element );
            }

            System.out.println();
        }
    }

    public void read(Reader in) {

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

                if (character == ' ') {
                    elementNumber++;
                    data.get(lineNumber).add("");
                    continue;
                }

                appendToData(lineNumber, elementNumber, (char)character);
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
        data.get(lineNumber).add(columnNumber, value);
    }

    public void appendData(int columnNumber, String value)
    {
        int lineNumber = data.size() - 1;
        data.get(lineNumber).add(columnNumber, value);
    }

    public void appendData(String value)
    {
        int lineNumber = data.size() - 1;
        int columnNumber = data.get(0).size() - 1;
        data.get(lineNumber).add(columnNumber, value);
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

    public int[] search(String value)
    {
        int lineNumber = 0;
        int columnNumber = 0;
        boolean found = false;
        int[] coordinates = new int[2];

        for (ArrayList<String> line : data) {
            for (String element : line) {
                if (element.equals(value)) {
                    found = true;
                    break;
                }
                columnNumber++;
            }
            if (found)
                break;
            columnNumber = 0;
            lineNumber++;
        }

        coordinates[0] = lineNumber;
        coordinates[1] = columnNumber;

        System.out.println(coordinates);

        return coordinates;
    }
}