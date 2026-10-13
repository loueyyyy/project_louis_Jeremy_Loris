package ch.heigvd.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ModifyProcess {

    public static void replace(String inputFilePath, String outputFilePath, String replace)
    {
        try (BufferedReader csvReader = new BufferedReader(new FileReader(inputFilePath, StandardCharsets.UTF_8));
            BufferedWriter csvWriter = new BufferedWriter(new FileWriter(outputFilePath, StandardCharsets.UTF_8))) {
            CsvDataExtractor csvExtractor = new CsvDataExtractor();


            int dataColumnToReplace = 0;
            int dataLineToReplace = 0;
            String newData = "";
            String dataToWrite;
            int charIndex = 0;

            csvExtractor.extraction(csvReader);

            while (!Character.isDigit(replace.charAt(charIndex)))
                dataColumnToReplace = (dataColumnToReplace * 26) + replace.toUpperCase().charAt(charIndex++) - 'A' + 1;

            --dataColumnToReplace;

            while (replace.charAt(charIndex) != ':')
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

        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void deleteRows(String inputFilePath, String outputFilePath, String deleteRows)
    {
        try (BufferedReader csvReader = new BufferedReader(new FileReader(inputFilePath, StandardCharsets.UTF_8));
            BufferedWriter csvWriter = new BufferedWriter(new FileWriter(outputFilePath, StandardCharsets.UTF_8))) {
            CsvDataExtractor csvExtractor = new CsvDataExtractor();


            int rowToDeleteStart = 0;
            int rowToDeleteEnd = 0;
            int charIndex = 0;
            String dataToWrite;

            csvExtractor.extraction(csvReader);

            while (deleteRows.charAt(charIndex) != '-')
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

        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void deleteColumns(String inputFilePath, String outputFilePath, String deleteColumns)
    {
        try (BufferedReader csvReader = new BufferedReader(new FileReader(inputFilePath, StandardCharsets.UTF_8));
            BufferedWriter csvWriter = new BufferedWriter(new FileWriter(outputFilePath, StandardCharsets.UTF_8))) {
            CsvDataExtractor csvExtractor = new CsvDataExtractor();


            int columnToDeleteStart = 0;
            int columnToDeleteEnd = 0;
            int charIndex = 0;
            String dataToWrite;

            csvExtractor.extraction(csvReader);

            while (deleteColumns.charAt(charIndex) != '-')
                columnToDeleteStart = (columnToDeleteStart * 26) + deleteColumns.charAt(charIndex++) - 'A' + 1;

            --columnToDeleteStart;

            ++charIndex;

            while (charIndex < deleteColumns.length())
                columnToDeleteEnd = (columnToDeleteEnd * 26) + deleteColumns.charAt(charIndex++) - 'A' + 1;

            --columnToDeleteEnd;

            for (int csvLine = 0; csvLine < csvExtractor.linesNumber(); csvLine++)
            {
                for (int csvColumn = 0; csvColumn < csvExtractor.columnsNumber(); csvColumn++)
                {
                    if (csvColumn >= columnToDeleteStart && csvColumn <= columnToDeleteEnd)
                    {
                        if (csvColumn == csvExtractor.columnsNumber() - 1 - (columnToDeleteEnd - columnToDeleteStart + 1))
                        {
                            System.out.println(csvColumn);
                            csvWriter.write(System.lineSeparator());
                        }

                        continue;
                    }

                    dataToWrite = csvExtractor.getData(csvLine, csvColumn);

                    System.out.println(csvExtractor.columnsNumber());
                    System.out.println(columnToDeleteEnd - columnToDeleteStart);

                    if (csvColumn != csvExtractor.columnsNumber() - 1 - (columnToDeleteEnd - columnToDeleteStart + 1))
                        dataToWrite += ";";
                    else
                        dataToWrite += System.lineSeparator();

                    csvWriter.write(dataToWrite);
                }
            }

        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void add(String inputFilePath, String outputFilePath, String add)
    {
        try (BufferedReader csvReader = new BufferedReader(new FileReader(inputFilePath, StandardCharsets.UTF_8));
            BufferedWriter csvWriter = new BufferedWriter(new FileWriter(outputFilePath, StandardCharsets.UTF_8))) {
            CsvDataExtractor csvExtractor = new CsvDataExtractor();


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
            
            newData = add.split(";");

            for (int csvColumn = 0; csvColumn < csvExtractor.columnsNumber(); csvColumn++)
            {
                dataToWrite = newData[csvColumn];

                if (csvColumn != csvExtractor.columnsNumber() - 1)
                    dataToWrite += ";";
                else
                    dataToWrite += System.lineSeparator();

                csvWriter.write(dataToWrite);
            }

        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void sort(String inputFilePath, String outputFilePath, String sort)
    {
        try (BufferedReader csvReader = new BufferedReader(new FileReader(inputFilePath, StandardCharsets.UTF_8));
            BufferedWriter csvWriter = new BufferedWriter(new FileWriter(outputFilePath, StandardCharsets.UTF_8))) {
            CsvDataExtractor csvExtractor = new CsvDataExtractor();

            csvExtractor.extraction(csvReader);

            String dataToWrite;
            int columnSortingReference = 0;
            String[] dataToSort = new String[csvExtractor.linesNumber() - 1];
            int[] dataSortingOrder = new int[csvExtractor.linesNumber() - 1];
            //find the column

            for (int csvColumn = 0; csvColumn < csvExtractor.columnsNumber(); csvColumn++)
            {
                dataToWrite = csvExtractor.getData(0, csvColumn);

                if (csvColumn != csvExtractor.columnsNumber() - 1)
                    dataToWrite += ";";
                else
                    dataToWrite += System.lineSeparator();

                csvWriter.write(dataToWrite);
            }

            for (int csvColumn = 0; csvColumn < csvExtractor.columnsNumber(); csvColumn++)
            {
                //if (csvExtractor.getData(0, csvColumn).toLowerCase().equals(sort))
                if (csvExtractor.getData(0, csvColumn).equalsIgnoreCase(sort))
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

            for (int csvLine = 0; csvLine < dataToSort.length; csvLine++) {
                int separatorIndex = dataToSort[csvLine].lastIndexOf("!");
                dataSortingOrder[csvLine] = Integer.parseInt(dataToSort[csvLine].substring(separatorIndex + 1));
            }

            for (int sortedIndex :  dataSortingOrder)
            {
                for (int csvColumn = 0; csvColumn < csvExtractor.columnsNumber(); csvColumn++)
                {
                    dataToWrite = csvExtractor.getData(sortedIndex +1 , csvColumn);

                    if (csvColumn != csvExtractor.columnsNumber() - 1)
                        dataToWrite += ";";
                    else
                        dataToWrite += System.lineSeparator();

                    csvWriter.write(dataToWrite);
                }
            }

        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}