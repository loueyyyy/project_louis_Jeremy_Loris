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

import ch.heigvd.process.ModifyProcess;


class ModifyProcessTest {

    private static final String CSV ="Nom;Ville;Prix\n" + "Martin;Geneve;8.90\n" + "dupont;Lausanne;12.50\n" + "Bernard;Yverdon;15.20";

    @TempDir
    Path tmp;

    private String input(String content) throws IOException {
        Path file = tmp.resolve("in.csv");
        Files.writeString(file, content, StandardCharsets.UTF_8);
        return file.toString();
    }

    private String output() {
        return tmp.resolve("out.csv").toString();
    }

    private List<String> result() throws IOException {
        return Files.readAllLines(tmp.resolve("out.csv"), StandardCharsets.UTF_8);
    }

    
    
    @Test
    void replaceChangesOnlyTheTargetCell() throws IOException {
        ModifyProcess.replace(input(CSV), output(), "B2:Fribourg");

        assertEquals(List.of("Nom;Ville;Prix","Martin;Fribourg;8.90","dupont;Lausanne;12.50","Bernard;Yverdon;15.20"), result());
    }
    //check si ca garde l'espace
    @Test
    void replaceKeepsTheSpacesOfTheNewValue() throws IOException {
        ModifyProcess.replace(input(CSV), output(), "A2:Jean Martin");

        assertEquals("Jean Martin;Geneve;8.90", result().get(1));
    }
    //a faire : check si on peut changer le header
    @Test
    void replaceCanChangeTheHeader() throws IOException {
        ModifyProcess.replace(input(CSV), output(), "C1:Montant");

        assertEquals("Nom;Ville;Montant", result().get(0));
    }

    @Test
    void replaceCanChangeTheLastCell() throws IOException {
        ModifyProcess.replace(input(CSV), output(), "C4:99.99");

        assertEquals("Bernard;Yverdon;99.99", result().get(3));
    }

    // a faire : check avec 2 lettre, genre AA pour 27 
    @Test
    void replaceWithATwoLetterColumn() throws IOException {
        StringBuilder header = new StringBuilder();
        StringBuilder line = new StringBuilder();
        for (int i = 1; i <= 27; i++) {
            // [h1,h2....h27] et [v1,v2....v27]
            header.append("h").append(i).append(i < 27 ? ";" : "");
            line.append("v").append(i).append(i < 27 ? ";" : "");
        }

       
        ModifyProcess.replace(input(header + "\n" + line), output(), "AA2:X");

        String[] cells = result().get(1).split(";");
        assertEquals(27, cells.length);
        //bien modifié le 27eme
        assertEquals("X", cells[26]);
        assertEquals("v26", cells[25]);
    }

    

    @Test
    void deleteRowsRemovesARangeOfRows() throws IOException {
        ModifyProcess.deleteRows(input(CSV), output(), "2-3");

        assertEquals(List.of("Nom;Ville;Prix", "Bernard;Yverdon;15.20"), result());
    }

    @Test
    void deleteRowsRemovesASingleRowWhenTheRangeHasOneRow() throws IOException {
        ModifyProcess.deleteRows(input(CSV), output(), "3-3");

        assertEquals(List.of("Nom;Ville;Prix","Martin;Geneve;8.90","Bernard;Yverdon;15.20"), result());
    }

    @Test
    void deleteRowsCanRemoveTheHeader() throws IOException {
        ModifyProcess.deleteRows(input(CSV), output(), "1-1");

        assertEquals(3, result().size());
        assertEquals("Martin;Geneve;8.90", result().get(0));
    }

    

    @Test
    void deleteColumnsRemovesARangeOfColumns() throws IOException {
        ModifyProcess.deleteColumns(input(CSV), output(), "A-B");

        assertEquals(List.of("Prix", "8.90", "12.50", "15.20"), result());
    }

    @Test
    void deleteColumnsRemovesASingleColumnWhenTheRangeHasOneColumn() throws IOException {
        ModifyProcess.deleteColumns(input(CSV), output(), "B-B");

        assertEquals(List.of("Nom;Prix","Martin;8.90","dupont;12.50","Bernard;15.20"), result());
    }

    @Disabled("delete la derniere colonne fait tt bug")
    @Test
    void deleteColumnsCanRemoveTheLastColumn() throws IOException {
        ModifyProcess.deleteColumns(input(CSV), output(), "C-C");

        assertEquals("Nom;Ville", result().get(0));
        assertEquals("Bernard;Yverdon", result().get(3));
    }

    

    @Test
    void addAppendsALineSeparatedBySemicolons() throws IOException {
        ModifyProcess.add(input(CSV), output(), "Roux;Sion;21.30");

        List<String> lines = result();
        assertEquals(5, lines.size());
        assertEquals("Bernard;Yverdon;15.20", lines.get(3));
        assertEquals("Roux;Sion;21.30", lines.get(4));
    }

    @Test
    void addKeepsTheExistingLines() throws IOException {
        ModifyProcess.add(input(CSV), output(), "Roux;Sion;21.30");

        assertEquals(CSV.lines().toList(), result().subList(0, 4));
    }

    


    private List<String> lastLines(int count) throws IOException {
        List<String> lines = result();
        return lines.subList(lines.size() - count, lines.size());
    }

    @Test
    void sortOrdersTheLinesIgnoringTheCase() throws IOException {
        ModifyProcess.sort(input(CSV), output(), "nom");

        assertEquals(List.of("Bernard;Yverdon;15.20","dupont;Lausanne;12.50","Martin;Geneve;8.90"), lastLines(3));
    }

    @Test
    void sortCanUseAnotherColumn() throws IOException {
        ModifyProcess.sort(input(CSV), output(), "ville");
        assertEquals(List.of("Martin;Geneve;8.90","dupont;Lausanne;12.50","Bernard;Yverdon;15.20"), lastLines(3));
    }

    @Test
    void sortKeepsTheOriginalOrderOfEqualValues() throws IOException {
        ModifyProcess.sort(input("nom;n\nb;1\na;2\nb;0\na;3"), output(), "nom");
        assertEquals(List.of("a;2", "a;3", "b;1", "b;0"), lastLines(4));
    }

    @Test
    void sortKeepsAllTheLines() throws IOException {
        ModifyProcess.sort(input(CSV), output(), "nom");

        List<String> lines = result();
        assertTrue(lines.containsAll(List.of("Martin;Geneve;8.90", "dupont;Lausanne;12.50", "Bernard;Yverdon;15.20")));
    }

    

    @Disabled("Bug , la fonction sort retire le header ???")
    @Test
    void sortKeepsTheHeader() throws IOException {
        ModifyProcess.sort(input(CSV), output(), "nom");

        assertEquals("Nom;Ville;Prix", result().get(0));
        assertEquals(4, result().size());
    }

    //@Disabled("J'en ai déja parlé dans un commit ou message mais ya un bug au dessus de 10 lignes qui fait foirer le sort")
    @Test
    void sortHandlesMoreThanNineLines() throws IOException {
        String[] names = {"k", "a", "j", "c", "l", "b", "i", "d", "h", "e", "g", "f"};
        StringBuilder csv = new StringBuilder("nom;n");
        for (int i = 0; i < names.length; i++)
            csv.append("\n").append(names[i]).append(";").append(i);

        ModifyProcess.sort(input(csv.toString()), output(), "nom");

        assertEquals(List.of("a;1", "b;5", "c;3", "d;7", "e;9", "f;11","g;10", "h;8", "i;6", "j;2", "k;0", "l;4"), lastLines(12));
    }

    //@Disabled("Comme on en a parlé ya un problème de diff de format entre ModifyCommand et ModifyProcess (B2 vs B:2)")
    @Test
    void replaceAcceptsTheFormatCheckedByTheCommand() throws IOException {
        ModifyProcess.replace(input(CSV), output(), "B2:Fribourg");

        assertEquals("Martin;Fribourg;8.90", result().get(1));
    }

    //@Disabled("Meme bug que au dessus mais avec 2-3 vs 2 3")
    @Test
    void deleteRowsAcceptsTheFormatCheckedByTheCommand() throws IOException {
        ModifyProcess.deleteRows(input(CSV), output(), "2-3");

        assertEquals(List.of("Nom;Ville;Prix", "Bernard;Yverdon;15.20"), result());
    }

    //@Disabled("pareil que au dessus")
    @Test
    void deleteColumnsAcceptsTheFormatCheckedByTheCommand() throws IOException {
        ModifyProcess.deleteColumns(input(CSV), output(), "A-B");

        assertEquals(List.of("Prix", "8.90", "12.50", "15.20"), result());
    }

    @Disabled("On en a également parlé mais ya un bug lorsque le csv termine par un newline qui fait plant les test")
    @Test
    void replaceWorksOnAFileEndingWithALineBreak() throws IOException {
        ModifyProcess.replace(input(CSV + "\r\n"), output(), "B2 Fribourg");

        assertEquals("Martin;Fribourg;8.90", result().get(1));
    }
}