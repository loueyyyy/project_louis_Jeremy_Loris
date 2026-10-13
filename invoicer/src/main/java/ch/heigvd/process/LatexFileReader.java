package ch.heigvd.process;

import java.io.Reader;
import java.util.ArrayList;

public class LatexFileReader
{
    ArrayList<String> data;

    public LatexFileReader() {

        data = new ArrayList<>();

    }

    public void printData() {

            for (String line : data){
                System.out.println(line);
            }
    }

    public void read(Reader in) {

        try {
            int character;
            int lineNumber = 0;

            data.add("");

            while ((character = in.read()) != -1) {

                if ((char) character == '\r')
                    continue;

                if ((char) character == '\n') {
                    lineNumber++;
                    data.add("");
                    continue;
                }

                appendData(lineNumber, (char)character);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

    public String getData(int lineNumber)
    {
        return data.get(lineNumber);
    }

    public void setData(int lineNumber, String value)
    {
        data.set(lineNumber, value);
    }

    public void appendNewLine(int lineNumber, String value)
    {
        if (lineNumber < data.size())
            data.add(lineNumber, value);
        else
            data.add(value);
    }

    public void appendData(int lineNumber, char value)
    {
        String line = data.get(lineNumber) + value;
        data.set(lineNumber, line);
    }

    public void appendData(char value)
    {
        appendData(data.size() - 1, value);
    }

    public int linesNumber()
    {
        return data.size();
    }

    public int search(String value)
    {
        int lineNumber = 0;

            for (String line : data) {
                if (line.contains(value)) {
                    break;
                }
                ++lineNumber;
            }

        return lineNumber;
    }
}