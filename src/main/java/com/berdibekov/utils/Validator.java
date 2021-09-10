package com.berdibekov.utils;

import com.berdibekov.exceptions.ValidationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Validator {

    public boolean checkIsSorted(String filePath) {
        List<String> result = getLinesFromFile(filePath);
        for (int i = 1; i < result.size(); i++) {
            if (result.get(i).compareTo(result.get(i - 1)) < 0) {
                return false;
            }
        }
        return true;
    }

    public List<String> getLinesFromFile(String inputFile) {
        File file = new File(inputFile);
        List<String> lines = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                lines.add(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public void validate(String sourceFileName, String resultFileName) throws ValidationException {
        List<String> in = getLinesFromFile(sourceFileName);
        List<String> out = getLinesFromFile(resultFileName);

        if (!checkIsSorted(resultFileName)) {
            throw new ValidationException("output file is not sorted");
        }
        Map<String, Integer> inputLinesMap = new HashMap<>();
        in.forEach(s -> inputLinesMap.put(s, inputLinesMap.getOrDefault(s, 1)));
        Map<String, Integer> outputLinesMap = new HashMap<>();
        in.forEach(s -> outputLinesMap.put(s, outputLinesMap.getOrDefault(s, 1)));

        if (!inputLinesMap.equals(outputLinesMap)) {
            throw new ValidationException("The lines in the input file do not match the lines in the output file");
        }
    }
}
