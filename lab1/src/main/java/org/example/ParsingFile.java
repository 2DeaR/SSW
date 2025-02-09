package org.example;

import java.util.Set;

public final class ParsingFile {
    public static void parse(String line, Set<Integer> integers, Set<Double> floats, Set<String> strings) {
        try {
            integers.add(Integer.parseInt(line));
        } catch (NumberFormatException e1) {
            try {
                floats.add(Double.parseDouble(line));
            } catch (NumberFormatException e2) {
                strings.add(line);
            }
        }
    }
}
