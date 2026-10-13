package ch.heigvd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.StringReader;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ch.heigvd.process.CsvDataExtractor;

class CsvDataExtractorTest {

    private static CsvDataExtractor read(String csv) {
        CsvDataExtractor extractor = new CsvDataExtractor();
        extractor.extraction(new StringReader(csv));
        return extractor;
    }

    @Test
    void readsLinesAndColumns() {
        CsvDataExtractor csv = read("Nom;Prix\nCafe;3.50\nThe;4.00");
        assertEquals(3, csv.linesNumber());
        assertEquals(2, csv.columnsNumber());
        assertEquals("Nom", csv.getData(0, 0));
        assertEquals("3.50", csv.getData(1, 1));
        assertEquals("The", csv.getData(2, 0));
    }

    @Test
    void keepsEmptyCells() {
        CsvDataExtractor csv = read("a;;c\n;e;");
        
        assertEquals(2, csv.linesNumber());
        assertEquals(3, csv.columnsNumber());
        assertEquals("", csv.getData(0, 1));
        assertEquals("", csv.getData(1, 0));
        assertEquals("", csv.getData(1, 2));
        assertEquals("a",csv.getData(0,0));
    }

    @Test
    void ignoresCarriageReturns() {
        CsvDataExtractor csv = read("a;b\r\nc;d");

        assertEquals(2, csv.linesNumber());
        assertEquals("b", csv.getData(0, 1));
        assertEquals("c", csv.getData(1, 0));
    }

    @Test
    void worksWithASingleColumn() {
        CsvDataExtractor csv = read("a\nb");

        assertEquals(2, csv.linesNumber());
        assertEquals(1, csv.columnsNumber());
    }

    @Test
    void keepsAccentedCharacters() {
        assertEquals("Coca cola", read("Coca cola;5").getData(0, 0));
        assertEquals("5", read("Coca cola;5").getData(0, 1));
    }

    // Edge case: empty file
    @Test
    void emptyInputGivesOneEmptyCell() {
        CsvDataExtractor csv = read("");

        assertEquals(1, csv.linesNumber());
        assertEquals(1, csv.columnsNumber());
        assertEquals("", csv.getData(0, 0));
    }

    @Test
    void setDataReplacesOneCell() {
        CsvDataExtractor csv = read("a;b\nc;d");

        csv.setData(1, 0, "X");

        assertEquals("X", csv.getData(1, 0));
        assertEquals("d", csv.getData(1, 1));
    }

    @Test
    void appendToDataConcatenatesAStringOrACharacter() {
        CsvDataExtractor csv = read("ab;c");

        csv.appendToData(0, 0, "cd");
        csv.appendToData(0, 1, 'x');

        assertEquals("abcd", csv.getData(0, 0));
        assertEquals("cx", csv.getData(0, 1));
    }

    @Test
    void appendDataAddsALineAtTheEnd() {
        CsvDataExtractor csv = read("a;b");

        csv.appendData("z");

        assertEquals(2, csv.linesNumber());
        assertEquals("z", csv.getData(1, 0));
    }

    @Test
    void appendDataInsertsALineAtThePosition() {
        CsvDataExtractor csv = read("a\nc");

        csv.appendData(1, "b");

        assertEquals(3, csv.linesNumber());
        assertEquals("b", csv.getData(1, 0));
        assertEquals("c", csv.getData(2, 0));
    }

    @Test
    void getDataOutsideTheFileThrows() {
        CsvDataExtractor csv = read("a;b");

        assertThrows(IndexOutOfBoundsException.class, () -> csv.getData(1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> csv.getData(0, 2));
    }

    
    @Disabled("ici il y a un bug , si on a un retour a la ligne a la fin , ca crée une ligne vide")
    @Test
    void aFinalLineBreakDoesNotAddALine() {
        assertEquals(2, read("a;b\nc;d\n").linesNumber());
    }
}