package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        if (args.length == 0) {
            System.err.println("Ошибка: укажите хотя бы один входной файл.");
            return;
        }

        String outputPath = "";
        String prefix = "";
        boolean addToExistingFile = false;
        boolean shortStats = false;
        boolean fullStats = false;

        List<String> inputFiles = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-o" -> {
                    if (i + 1 < args.length) {
                        outputPath = args[++i];
                    } else {
                        System.err.println("Ошибка: отсутствует аргумент после параметра -o.");
                        return;
                    }
                }
                case "-p" -> {
                    if (i + 1 < args.length) {
                        prefix = args[++i];
                    } else {
                        System.err.println("Ошибка: отсутствует аргумент после параметра -p.");
                        return;
                    }
                }
                case "-a" -> addToExistingFile = true;
                case "-s" -> shortStats = true;
                case "-f" -> fullStats = true;
                default -> inputFiles.add(args[i]);
            }
        }

        if (shortStats && fullStats) {
            System.err.println("Ошибка: нельзя одновременно использовать аргументы -s (краткая статистика) и -f (полная статистика).");
            return;
        }

        if (inputFiles.isEmpty()) {
            System.err.println("Ошибка: не указаны входные файлы.");
            return;
        }

        Set<Integer> integers = new HashSet<>();
        Set<Double> floats = new HashSet<>();
        Set<String> strings = new HashSet<>();

        for (String inputFile : inputFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    ParsingFile.parse(line, integers, floats, strings);
                }
            } catch (FileNotFoundException e) {
                System.err.println("Ошибка: файл не найден - " + inputFile);
            } catch (IOException e) {
                System.err.println("Ошибка: ошибка ввода-вывода при чтении файла - " + inputFile);
            }
        }

        if (!outputPath.isEmpty()) {
            File outputDir = new File(outputPath);
            if (!outputDir.exists() && !outputDir.mkdirs()) {
                System.err.println("Ошибка: не удалось создать директорию " + outputPath);
                return;
            }
        }

        if (!integers.isEmpty()) writeToFile("integers", integers, outputPath, prefix, addToExistingFile);
        if (!floats.isEmpty()) writeToFile("floats", floats, outputPath, prefix, addToExistingFile);
        if (!strings.isEmpty()) writeToFile("strings", strings, outputPath, prefix, addToExistingFile);

        if (shortStats) {
            System.out.println("Краткая статистика:");
            System.out.println("Целые числа: " + integers.size());
            System.out.println("Вещественные числа: " + floats.size());
            System.out.println("Строки: " + strings.size());
        }

        if (fullStats) {
            System.out.println("Полная статистика:");
            System.out.println("Целые числа: " + integers.size());
            System.out.println("Вещественные числа: " + floats.size());
            System.out.println("Строки: " + strings.size());

            if (!integers.isEmpty()) {
                System.out.println("Статистика целых чисел:");
                printNumberStats(integers);
            }

            if (!floats.isEmpty()) {
                System.out.println("Статистика вещественных чисел:");
                printNumberStats(floats);
            }

            if (!strings.isEmpty()) {
                System.out.println("Статистика строк:");
                printStringStats(strings);
            }
        }
    }

    private static void writeToFile(String dataType, Set<?> data, String outputPath, String prefix, boolean addToExistingFile) {
        String fileName = (prefix.isEmpty() ? "" : prefix + "_") + dataType + ".txt";
        if (!outputPath.isEmpty()) {
            fileName = outputPath + File.separator + fileName;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, addToExistingFile))) {
            for (Object item : data) {
                writer.write(item.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Ошибка: не удалось записать в файл " + fileName);
        }
    }

    private static void printNumberStats(Set<? extends Number> numbers) {
        if (numbers.isEmpty()) return;

        double min = Double.POSITIVE_INFINITY, max = Double.NEGATIVE_INFINITY, sum = 0;
        for (Number num : numbers) {
            double val = num.doubleValue();
            if (val < min) min = val;
            if (val > max) max = val;
            sum += val;
        }

        double avg = sum / numbers.size();
        System.out.println("Мин: " + min + ", Макс: " + max + ", Сумма: " + sum + ", Среднее: " + avg);
    }

    private static void printStringStats(Set<String> strings) {
        if (strings.isEmpty()) return;

        String shortest = null, longest = null;
        for (String str : strings) {
            if (shortest == null || str.length() < shortest.length()) shortest = str;
            if (longest == null || str.length() > longest.length()) longest = str;
        }

        System.out.println("Самая короткая строка: " + shortest + ", Самая длинная строка: " + longest);
    }
}
