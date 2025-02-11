import org.example.Main;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @TempDir
    Path tempDir;

    private File createTempFile(String content) throws IOException {
        File tempFile = File.createTempFile("test", ".txt", tempDir.toFile());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(content);
        }
        return tempFile;
    }

    private String getFileContent(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString().trim();
    }

    @Test
    void testMainWithSingleInputFile() throws IOException {
        File inputFile = createTempFile("123\n456.78\nhello");
        Main.main(new String[]{inputFile.getAbsolutePath(), "-o", tempDir.toString()});

        File integersFile = new File(tempDir.toFile(), "integers.txt");
        File floatsFile = new File(tempDir.toFile(), "floats.txt");
        File stringsFile = new File(tempDir.toFile(), "strings.txt");

        assertTrue(integersFile.exists());
        assertTrue(floatsFile.exists());
        assertTrue(stringsFile.exists());

        assertEquals("123", getFileContent(integersFile));
        assertEquals("456.78", getFileContent(floatsFile));
        assertEquals("hello", getFileContent(stringsFile));
    }

    @Test
    void testMainWithPrefix() throws IOException {
        File inputFile = createTempFile("123\n456.78\nhello");

        Main.main(new String[]{inputFile.getAbsolutePath(), "-o", tempDir.toString(), "-p", "test"});

        File integersFile = new File(tempDir.toFile(), "test_integers.txt");
        File floatsFile = new File(tempDir.toFile(), "test_floats.txt");
        File stringsFile = new File(tempDir.toFile(), "test_strings.txt");

        assertTrue(integersFile.exists());
        assertTrue(floatsFile.exists());
        assertTrue(stringsFile.exists());
    }

    @Test
    void testMainWithShortStats() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            File inputFile = createTempFile("123\n456.78\nhello");
            Main.main(new String[]{inputFile.getAbsolutePath(), "-s"});

            String output = outContent.toString();
            assertTrue(output.contains("Краткая статистика:"));
            assertTrue(output.contains("Целые числа: 1"));
            assertTrue(output.contains("Вещественные числа: 1"));
            assertTrue(output.contains("Строки: 1"));
        } catch (IOException e) {
            fail("Test failed with IOException: " + e.getMessage());
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void testMainWithFullStats() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            File inputFile = createTempFile("123\n456.78\nhello");
            Main.main(new String[]{inputFile.getAbsolutePath(), "-f"});

            String output = outContent.toString();
            assertTrue(output.contains("Полная статистика:"));
            assertTrue(output.contains("Статистика целых чисел:"));
            assertTrue(output.contains("Статистика вещественных чисел:"));
            assertTrue(output.contains("Статистика строк:"));
        } catch (IOException e) {
            fail("Test failed with IOException: " + e.getMessage());
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void testErrorHandling() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errContent));

        try {
            Main.main(new String[]{"-o", "output"});
            assertTrue(errContent.toString().contains("Ошибка: не указаны входные файлы"));

            errContent.reset();

            Main.main(new String[]{"-o"});
            assertTrue(errContent.toString().contains("Ошибка: отсутствует аргумент после параметра -o"));

            errContent.reset();

            Main.main(new String[]{"-p"});
            assertTrue(errContent.toString().contains("Ошибка: отсутствует аргумент после параметра -p"));
            errContent.reset();
            Main.main(new String[]{"input.txt", "-s", "-f"});
            assertTrue(errContent.toString().contains("Ошибка: нельзя одновременно использовать аргументы"));
        } finally {
            System.setErr(originalErr);
        }
    }
}
