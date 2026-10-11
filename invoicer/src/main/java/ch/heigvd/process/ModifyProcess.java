package ch.heigvd.process;

import java.io.Reader;
import java.io.Writer;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.util.Arrays;

public class ModifyProcess {

    public static void replace(String inputFilePath, String outputFilePath, String replace)
    {
        try {
            Reader csvReader = new FileReader(inputFilePath, StandardCharsets.UTF_8);
            CsvDataExtractor csvExtractor = new CsvDataExtractor();

            Writer csvWriter = new FileWriter(outputFilePath, StandardCharsets.UTF_8);

            int dataColumnToReplace = 0;
            int dataLineToReplace = 0;
            String newData = "";
            String dataToWrite;
            int charIndex = 0;

            csvExtractor.extraction(csvReader);

            while (!Character.isDigit(replace.charAt(charIndex)))
                dataColumnToReplace = (dataColumnToReplace * 26) + replace.charAt(charIndex++) - 'A' + 1;

            --dataColumnToReplace;

            while (replace.charAt(charIndex) != ' ')
                dataLineToReplace = (dataLineToReplace * 10) + replace.charAt(charIndex++) - '0';

            --dataLineToReplace;

            ++charIndex;

            while (charIndex < replace.length())
                newData += replace.charAt(charIndex++);


            for (int csvLine = 0; csvLine < csvExtractor.linesNumber(); csvLine++)
            {
                for (int csvColumn = 0; csvColumn < csvExtractor.columnsNumber(); csvColumn++)
                {
                    if (csvLine == dataLineToReplace && csvColumn == dataColumnToReplace)
                        dataToWrite = newData;
                    else
                        dataToWrite = csvExtractor.getData(csvLine, csvColumn);

                    if (csvColumn != csvExtractor.columnsNumber() - 1)
                        dataToWrite += ";";
                    else
                        dataToWrite += System.lineSeparator();

                    csvWriter.write(dataToWrite);
                }
            }

            csvReader.close();
            csvWriter.close();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void deleteRows(String inputFilePath, String outputFilePath, String deleteRows)
    {
        try {
            Reader csvReader = new FileReader(inputFilePath, StandardCharsets.UTF_8);
            CsvDataExtractor csvExtractor = new CsvDataExtractor();

            Writer csvWriter = new FileWriter(outputFilePath, StandardCharsets.UTF_8);

            int rowToDeleteStart = 0;
            int rowToDeleteEnd = 0;
            int charIndex = 0;
            String dataToWrite;

            csvExtractor.extraction(csvReader);

            while (deleteRows.charAt(charIndex) != ' ')
                rowToDeleteStart = (rowToDeleteStart * 10) + deleteRows.charAt(charIndex++) - '0';

            --rowToDeleteStart;

            ++charIndex;

            while (charIndex < deleteRows.length())
                rowToDeleteEnd = (rowToDeleteEnd * 10) + deleteRows.charAt(charIndex++) - '0';

            --rowToDeleteEnd;

            for (int csvLine = 0; csvLine < csvExtractor.linesNumber(); csvLine++)
            {
                if (csvLine >= rowToDeleteStart && csvLine <= rowToDeleteEnd)
                    continue;

                for (int csvColumn = 0; csvColumn < csvExtractor.columnsNumber(); csvColumn++)
                {
                    dataToWrite = csvExtractor.getData(csvLine, csvColumn);

                    if (csvColumn != csvExtractor.columnsNumber() - 1)
                        dataToWrite += ";";
                    else
                        dataToWrite += System.lineSeparator();

                    csvWriter.write(dataToWrite);
                }
            }

            csvReader.close();
            csvWriter.close();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void deleteColumns(String inputFilePath, String outputFilePath, String deleteColumns)
    {
        try {
            Reader csvReader = new FileReader(inputFilePath, StandardCharsets.UTF_8);
            CsvDataExtractor csvExtractor = new CsvDataExtractor();

            Writer csvWriter = new FileWriter(outputFilePath, StandardCharsets.UTF_8);

            int columnToDeleteStart = 0;
            int columnToDeleteEnd = 0;
            int charIndex = 0;
            String dataToWrite;

            csvExtractor.extraction(csvReader);

            while (deleteColumns.charAt(charIndex) != ' ')
                columnToDeleteStart = (columnToDeleteStart * 26) + deleteColumns.charAt(charIndex++) - 'A' + 1;

            --columnToDeleteStart;

            System.out.println(columnToDeleteStart);

            ++charIndex;

            while (charIndex < deleteColumns.length())
                columnToDeleteEnd = (columnToDeleteEnd * 26) + deleteColumns.charAt(charIndex++) - 'A' + 1;

            --columnToDeleteEnd;

            System.out.println(columnToDeleteEnd);

            for (int csvLine = 0; csvLine < csvExtractor.linesNumber(); csvLine++)
            {
                for (int csvColumn = 0; csvColumn < csvExtractor.columnsNumber(); csvColumn++)
                {
                    if (csvColumn >= columnToDeleteStart && csvColumn <= columnToDeleteEnd)
                        continue;

                    dataToWrite = csvExtractor.getData(csvLine, csvColumn);

                    if (csvColumn != csvExtractor.columnsNumber() - 1)
                        dataToWrite += ";";
                    else
                        dataToWrite += System.lineSeparator();

                    csvWriter.write(dataToWrite);
                }
            }

            csvReader.close();
            csvWriter.close();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void add(String inputFilePath, String outputFilePath, String add)
    {
        try {
            Reader csvReader = new FileReader(inputFilePath, StandardCharsets.UTF_8);
            CsvDataExtractor csvExtractor = new CsvDataExtractor();

            Writer csvWriter = new FileWriter(outputFilePath, StandardCharsets.UTF_8);

            String dataToWrite;
            String[] newData;

            csvExtractor.extraction(csvReader);

            for (int csvLine = 0; csvLine < csvExtractor.linesNumber(); csvLine++)
            {
                for (int csvColumn = 0; csvColumn < csvExtractor.columnsNumber(); csvColumn++)
                {
                    dataToWrite = csvExtractor.getData(csvLine, csvColumn);

                    if (csvColumn != csvExtractor.columnsNumber() - 1)
                        dataToWrite += ";";
                    else
                        dataToWrite += System.lineSeparator();

                    csvWriter.write(dataToWrite);
                }
            }

            if (add.contains(";"))
                newData = add.split(";");
            else
                newData = add.split(" ");

            for (int csvColumn = 0; csvColumn < csvExtractor.columnsNumber(); csvColumn++)
            {
                dataToWrite = newData[csvColumn];

                if (csvColumn != csvExtractor.columnsNumber() - 1)
                    dataToWrite += ";";
                else
                    dataToWrite += System.lineSeparator();

                csvWriter.write(dataToWrite);
            }

            csvReader.close();
            csvWriter.close();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void sort(String inputFilePath, String outputFilePath, String sort)
    {
        try {
            Reader csvReader = new FileReader(inputFilePath, StandardCharsets.UTF_8);
            CsvDataExtractor csvExtractor = new CsvDataExtractor();

            Writer csvWriter = new FileWriter(outputFilePath, StandardCharsets.UTF_8);

            csvExtractor.extraction(csvReader);

            String dataToWrite;
            int columnSortingReference = 0;
            String[] dataToSort = new String[csvExtractor.linesNumber() - 1];
            int[] dataSortingOrder = new int[csvExtractor.linesNumber() - 1];

            for (int csvColumn = 0; csvColumn < csvExtractor.columnsNumber(); csvColumn++)
            {
                if (csvExtractor.getData(0, csvColumn).toLowerCase().equals(sort))
                {
                    columnSortingReference = csvColumn;
                    break;
                }
            }

            for (int csvLine = 0; csvLine < csvExtractor.linesNumber() - 1; csvLine++) {
                dataToSort[csvLine] = csvExtractor.getData(csvLine + 1, columnSortingReference);
                dataToSort[csvLine] += "!" + csvLine;
            }

            Arrays.sort(dataToSort, String.CASE_INSENSITIVE_ORDER);


            for (int csvLine = 0; csvLine < csvExtractor.linesNumber() - 1; csvLine++) {

                int dataToSortColumnIndex = dataToSort[csvLine].length() - 1;

                while (dataToSort[csvLine].charAt( dataToSortColumnIndex ) != '!')
                {
                    dataSortingOrder[csvLine] += (dataToSort[csvLine].charAt( dataToSortColumnIndex ) - '0') * ((dataToSort[csvLine].length() - dataToSortColumnIndex) * 10);
                    --dataToSortColumnIndex;
                }
                dataSortingOrder[csvLine] /= 10;
                ++dataSortingOrder[csvLine];
            }

            for (int sortedIndex :  dataSortingOrder)
            {
                for (int csvColumn = 0; csvColumn < csvExtractor.columnsNumber(); csvColumn++)
                {
                    dataToWrite = csvExtractor.getData(sortedIndex, csvColumn);

                    if (csvColumn != csvExtractor.columnsNumber() - 1)
                        dataToWrite += ";";
                    else
                        dataToWrite += System.lineSeparator();

                    csvWriter.write(dataToWrite);
                }
            }

            csvReader.close();
            csvWriter.close();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}