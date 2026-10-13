package ch.heigvd;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ch.heigvd.process.LatexFileReader;

class LatexFileReaderTest {

    private static LatexFileReader read(String text) {
        LatexFileReader latex = new LatexFileReader();
        latex.read(new StringReader(text));
        return latex;
    }

    @Test
    void readsLines() {
        LatexFileReader latex = read("\\begin{document}\n% --- HEADERS ---\n\\end{document}");

        assertEquals(3, latex.linesNumber());
        assertEquals("\\begin{document}", latex.getData(0));
        assertEquals("% --- HEADERS ---", latex.getData(1));
    }

    @Test
    void ignoresCarriageReturns() {
        LatexFileReader latex = read("a\r\nb");

        assertEquals(2, latex.linesNumber());
        assertEquals("a", latex.getData(0));
        assertEquals("b", latex.getData(1));
    }

    // Edge case: empty model
    @Test
    void emptyInputGivesOneEmptyLine() {
        LatexFileReader latex = read("");

        assertEquals(1, latex.linesNumber());
        assertEquals("", latex.getData(0));
    }

    @Test
    void keepsEmptyLinesInTheMiddle() {
        LatexFileReader latex = read("a\n\nb");

        assertEquals(3, latex.linesNumber());
        assertEquals("", latex.getData(1));
    }

    @Test
    void searchGivesTheFirstMatchingLine() {
        LatexFileReader latex = read("a\n% --- TOTAL ---\nb\n% --- TOTAL ---");

        assertEquals(1, latex.search("--- TOTAL ---"));
    }

    @Test
    void searchMatchesAPartOfALine() {
        LatexFileReader latex = read("a\n    % --- VAT ---   \nb");

        assertEquals(1, latex.search("--- VAT ---"));
    }

    @Test
    void searchGivesTheNumberOfLinesWhenTheValueIsMissing() {
        LatexFileReader latex = read("a\nb");

        assertEquals(2, latex.search("--- VAT ---"));
    }

    @Test
    void appendNewLineInsertsAtThePosition() {
        LatexFileReader latex = read("a\nc");

        latex.appendNewLine(1, "b");

        assertEquals(3, latex.linesNumber());
        assertEquals("a", latex.getData(0));
        assertEquals("b", latex.getData(1));
        assertEquals("c", latex.getData(2));
    }

    @Test
    void appendNewLineBeyondTheEndAppends() {
        LatexFileReader latex = read("a");

        latex.appendNewLine(5, "z");

        assertEquals(2, latex.linesNumber());
        assertEquals("z", latex.getData(1));
    }

    @Test
    void insertionsAfterAMarkerKeepTheirOrder() {
        LatexFileReader latex = read("start\n% --- HEADERS ---\nend");

        int line = latex.search("--- HEADERS ---") + 1;
        latex.appendNewLine(line++, "Nom &");
        latex.appendNewLine(line++, "Prix");

        assertEquals("% --- HEADERS ---", latex.getData(1));
        assertEquals("Nom &", latex.getData(2));
        assertEquals("Prix", latex.getData(3));
        assertEquals("end", latex.getData(4));
    }

    @Test
    void setDataReplacesALine() {
        LatexFileReader latex = read("a\nb");

        latex.setData(1, "X");

        assertEquals("X", latex.getData(1));
    }

    @Test
    void appendDataWithALineNumberAddsACharacterToThatLine() {
        LatexFileReader latex = read("ab\ncd");

        latex.appendData(1, 'e');

        assertEquals("cde", latex.getData(1));
    }

    //@Disabled("ici ya un bug ou appenddata/getData (jsp encore lequel) utilise le nombre de ligne comme index , ce qui crée une exception")
    @Test
    void appendDataWithoutLineNumberAddsACharacterToTheLastLine() {
        LatexFileReader latex = read("ab");

        latex.appendData('c');

        assertEquals("abc", latex.getData(0));
    }
}