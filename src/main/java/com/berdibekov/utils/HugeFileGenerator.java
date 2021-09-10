package com.berdibekov.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class HugeFileGenerator {

    private final Set<String> lines = new HashSet<>();

    public void generateHugeFile(String fileName, int maxLineLength, int maxLines) throws IOException {
        try (FileWriter fileWriter = new FileWriter(fileName);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            for (int i = 0; i < maxLines; i++) {
                String newLine = generateString(maxLineLength);
                if (lines.add(newLine)) {
                    bufferedWriter.append(newLine);
                }
                if (i % 100000 == 0) {
                    System.out.println(i);
                }
            }
        }
    }

    private String generateString(int maxLength) {
        Random r = new Random();
        int length = r.nextInt(maxLength) + 1;
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            char c = (char) (r.nextInt(26) + 'a');
            text[i] = c;
        }
        return new String(text) + "\r\n";
    }
}
