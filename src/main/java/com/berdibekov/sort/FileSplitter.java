package com.berdibekov.sort;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The purpose of this class is to divide a large text file into files small enough to be read entirely into memory
 * Divided files will be sorted by lines for future Merge sort https://en.wikipedia.org/wiki/Merge_sort .
 */
public class FileSplitter {
    /**
     *
     * @param fileName the path to the file to sort
     * @param maxSymbols max symbols to read to RAM from file at one time.
     * @return list of split files.
     */
    public LinkedList<FileNode> splitFile(String fileName, int maxSymbols) {
        String pathSplitTemp = Paths.get(fileName).toAbsolutePath().getParent().toString().concat("\\result");
        LinkedList<FileNode> fileNodes = new LinkedList<>();
        int splitFilesCount = 0;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            List<String> sortedLines = readNextNLines(bufferedReader, maxSymbols).stream().sorted(String::compareTo).collect(Collectors.toList());
            while (!sortedLines.isEmpty()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathSplitTemp + "\\" + splitFilesCount + ".txt", true))) {
                    System.out.println(splitFilesCount + ".txt");
                    for (String line : sortedLines) {
                        writer.append(line);
                        writer.newLine();
                    }
                    fileNodes.addLast(new FileNode(Integer.toString(splitFilesCount)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                splitFilesCount++;
                sortedLines = readNextNLines(bufferedReader, maxSymbols).stream().sorted(String::compareTo).collect(Collectors.toList());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileNodes;
    }


    private List<String> readNextNLines(BufferedReader bufferedReader, int maxSizeMb) {
        try {
            long byteReadCount = 0;
            List<String> lines = new ArrayList<>();
            String line = bufferedReader.readLine();
            while (line != null) {
                byteReadCount += line.length();
                lines.add(line);
                if (byteReadCount > maxSizeMb) {
                    break;
                }
                line = bufferedReader.readLine();
            }
            return lines;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
