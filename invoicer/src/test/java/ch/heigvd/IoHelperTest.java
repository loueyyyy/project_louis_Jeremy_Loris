package ch.heigvd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import ch.heigvd.utils.IOhelper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import ch.heigvd.utils.IOhelper;


class IOhelperTest {

    @TempDir
    Path tmp;

    

    @Test
    void csvExtensionAcceptsCsvFiles() {
        assertTrue(IOhelper.csvExtension("data.csv"));
        assertTrue(IOhelper.csvExtension("folder/data.csv"));
    }

    @Test
    void csvExtensionIgnoresTheCase() {
        assertTrue(IOhelper.csvExtension("DATA.CSV"));
        assertTrue(IOhelper.csvExtension("data.Csv"));
    }

    @Test
    void csvExtensionRejectsOtherFiles() {
        assertFalse(IOhelper.csvExtension("data.txt"));
        assertFalse(IOhelper.csvExtension("data.csv.txt"));
        assertFalse(IOhelper.csvExtension("csv"));
        assertFalse(IOhelper.csvExtension(""));
    }

    @Test
    void csvExtensionRejectsNull() {
        assertFalse(IOhelper.csvExtension(null));
    }

    @Test
    void texExtensionAcceptsTexFilesWhateverTheCase() {
        assertTrue(IOhelper.texExtension("invoice.tex"));
        assertTrue(IOhelper.texExtension("INVOICE.TEX"));
    }

    @Test
    void texExtensionRejectsOtherFiles() {
        assertFalse(IOhelper.texExtension("invoice.pdf"));
        assertFalse(IOhelper.texExtension("invoice.tex.csv"));
        assertFalse(IOhelper.texExtension("tex"));
        assertFalse(IOhelper.texExtension(null));
    }

    

    @Test
    void inputFileCheckRejectsNullAndBlankPaths() {
        assertEquals(1, IOhelper.inputFileCheck(null));
        assertEquals(1, IOhelper.inputFileCheck(""));
        assertEquals(1, IOhelper.inputFileCheck("   "));
    }

    @Test
    void inputFileCheckRejectsAMissingFile() {
        Path missing = tmp.resolve("missing.csv");

        assertEquals(1, IOhelper.inputFileCheck(missing.toString()));
        assertFalse(Files.exists(missing));
    }

    @Test
    void inputFileCheckRejectsAFolder() {
        assertEquals(1, IOhelper.inputFileCheck(tmp.toString()));
    }

    @Test
    void inputFileCheckAcceptsAReadableFile() throws IOException {
        Path file = tmp.resolve("data.csv");
        Files.writeString(file, "a;b", StandardCharsets.UTF_8);

        assertEquals(0, IOhelper.inputFileCheck(file.toString()));
    }

    

    @Test
    void outputFileCheckRejectsNullAndBlankPaths() {
        assertEquals(1, IOhelper.outputFileCheck(null));
        assertEquals(1, IOhelper.outputFileCheck(""));
        assertEquals(1, IOhelper.outputFileCheck("   "));
    }

    @Test
    void outputFileCheckCreatesTheFile() {
        Path file = tmp.resolve("out.csv");

        assertEquals(0, IOhelper.outputFileCheck(file.toString()));
        assertTrue(Files.isRegularFile(file));
    }

    @Test
    void outputFileCheckCreatesTheMissingFolders() {
        Path file = tmp.resolve("a").resolve("b").resolve("out.csv");

        assertEquals(0, IOhelper.outputFileCheck(file.toString()));
        assertTrue(Files.isRegularFile(file));
    }

    @Test
    void outputFileCheckKeepsTheContentOfAnExistingFile() throws IOException {
        Path file = tmp.resolve("out.csv");
        Files.writeString(file, "data", StandardCharsets.UTF_8);

        assertEquals(0, IOhelper.outputFileCheck(file.toString()));
        assertEquals("data", Files.readString(file, StandardCharsets.UTF_8));
    }

    @Test
    void outputFileCheckRejectsAFolder() {
        assertEquals(1, IOhelper.outputFileCheck(tmp.toString()));
    }
}