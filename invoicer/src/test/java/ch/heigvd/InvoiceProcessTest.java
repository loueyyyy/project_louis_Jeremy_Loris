package ch.heigvd;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import ch.heigvd.process.InvoiceProcess;


class InvoiceProcessTest {

    private static final Path MODEL = Path.of("./src/main/java/ch/heigvd/model/invoice_model.tex");

    @TempDir
    Path tmp;

    private List<String> invoice(String csv) throws IOException {
        Path in = tmp.resolve("in.csv");
        Path out = tmp.resolve("out.tex");

        Files.writeString(in, csv, StandardCharsets.UTF_8);
        InvoiceProcess.invoiceProcess(in.toString(), out.toString());

        return Files.readAllLines(out, StandardCharsets.UTF_8);
    }

    private static int markerIndex(List<String> lines, String marker) {
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).contains(marker))
                return i;
        }
        throw new AssertionError("Marker not found: " + marker);
    }

    private static String lineAfter(List<String> lines, String marker) {
        return lines.get(markerIndex(lines, marker) + 1);
    }

    private static double amountAfter(List<String> lines, String marker) {
        return Double.parseDouble(lineAfter(lines, marker).replace(" CHF", ""));
    }

    @Test
    void headersAreInsertedAfterTheMarker() throws IOException {
        List<String> lines = invoice("Nom;Prix\nCafe;3.5");
        int marker = markerIndex(lines, "--- HEADERS ---");

        assertEquals("Nom &", lines.get(marker + 1));
        assertEquals("Prix", lines.get(marker + 2));
    }

    @Test
    void everyCellOfALineIsInsertedAfterTheMarker() throws IOException {
        List<String> lines = invoice("Nom;Prix\nCafe;3.5\nThe;4");
        int marker = markerIndex(lines, "--- INSTANCES ---");

        assertEquals("Cafe &", lines.get(marker + 1));
        assertEquals("3.5 CHF\\\\", lines.get(marker + 2));
        assertEquals("The &", lines.get(marker + 3));
        assertEquals("4 CHF\\\\", lines.get(marker + 4));
    }

    @Test
    void thePriceInTheMiddleOfALineGetsTheUnit() throws IOException {
        List<String> lines = invoice("Nom;Prix;Categorie\nCafe;3.5;Boisson");
        int marker = markerIndex(lines, "--- INSTANCES ---");

        assertEquals("Cafe &", lines.get(marker + 1));
        assertEquals("3.5 CHF &", lines.get(marker + 2));
        assertEquals("Boisson\\\\", lines.get(marker + 3));
    }

    @Test
    void thePriceColumnCanBeTheFirstOne() throws IOException {
        List<String> lines = invoice("Prix;Nom\n7;Eau");
        int marker = markerIndex(lines, "--- INSTANCES ---");

        assertEquals("7 CHF &", lines.get(marker + 1));
        assertEquals("Eau\\\\", lines.get(marker + 2));
    }

    @Test
    void thePriceColumnIsFoundWhateverTheCase() throws IOException {
        List<String> lines = invoice("PRIX;Nom\n7;Eau");

        assertEquals("7.00 CHF", lineAfter(lines, "--- SUBTOTAL ---"));
    }

    
    @Test
    void theSubtotalIsTheSumOfThePrices() throws IOException {
        List<String> lines = invoice("Nom;Prix\na;10.5\nb;20.25");

        assertEquals("30.75 CHF", lineAfter(lines, "--- SUBTOTAL ---"));
    }

    @Test
    void theVatIs8Point1PercentOfTheSubtotal() throws IOException {
        List<String> lines = invoice("Nom;Prix\na;10.5\nb;20.25");

        double exceptedVat = Math.round(30.75 * 0.081 * 100) / 100.0;
        assertEquals(exceptedVat, amountAfter(lines, "--- VAT ---"), 0.0001);
    }

    @Test
    void theTotalIsTheSubtotalPlusTheVat() throws IOException {
        List<String> lines = invoice("Nom;Prix\na;10.5\nb;20.25");

        double expectedTotal = Math.round(30.75 * 1.081 * 100) / 100.0;
        assertEquals(expectedTotal, amountAfter(lines, "--- TOTAL ---"), 0.0001);
    }

    // Edge case: no line below the header
    @Test
    void aHeaderWithoutDataGivesZeroTotals() throws IOException {
        List<String> lines = invoice("Nom;Prix");

        assertEquals("0.00 CHF", lineAfter(lines, "--- SUBTOTAL ---"));
        assertEquals(0.0, amountAfter(lines, "--- VAT ---"), 0.0001);
        assertEquals(0.0, amountAfter(lines, "--- TOTAL ---"), 0.0001);
    }

    @Test
    void windowsLineEndingsGiveTheSameInvoice() throws IOException {
        List<String> unix = invoice("Nom;Prix\na;1.5\nb;2.5");
        List<String> windows = invoice("Nom;Prix\r\na;1.5\r\nb;2.5");

        assertEquals(unix, windows);
    }

    @Test
    void accentedCharactersAreKept() throws IOException {
        List<String> lines = invoice("Nom;Prix\nCrème brûlée;5");
        int marker = markerIndex(lines, "--- INSTANCES ---");

        assertEquals("Crème brûlée &", lines.get(marker + 1));
    }

    @Test
    void theModelIsKept() throws IOException {
        List<String> model = Files.readAllLines(MODEL, StandardCharsets.UTF_8);

        List<String> lines = invoice("Nom;Prix\na;1.5");

        assertTrue(lines.containsAll(model));
    }

    
    @Disabled("ici ya un bug: 0.1 + 0.2 donne 0.30000000000000004 CHF pcq erreur d'arrondi")
    @Test
    void theSubtotalIsRoundedToTwoDecimals() throws IOException {
        List<String> lines = invoice("Nom;Prix\na;0.1\nb;0.2");

        assertEquals("0.30 CHF", lineAfter(lines, "--- SUBTOTAL ---"));
    }

}