package com.berdibekov.sort;

import lombok.Data;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The purpose of this class is to Merge sort files witch small enough to be read entirely into memory https://en.wikipedia.org/wiki/Merge_sort .
 * File node provide static method for merging list of file nodes.
 */
@Data
public class FileNode {
    private String name;
    private String outDir;

    public FileNode(String name) {
        this.name = name;
    }

    /**
     * @param fileNodes  file node list to be merged to each other.
     * @param outDir     the folder where the result will be placed
     * @param maxSymbols max symbols to read to RAM from file at one time.
     * @return final node which is the result of merging all nodes.
     */
    public static FileNode mergeAll(LinkedList<FileNode> fileNodes, String outDir, int maxSymbols) {
        FileNode firstNode = fileNodes.removeFirst();
        FileNode nextNode = fileNodes.pollFirst();
        if (nextNode == null) {
            return firstNode;
        }
        fileNodes.addFirst(merge(firstNode, nextNode, maxSymbols, outDir));
        return mergeAll(fileNodes, outDir, maxSymbols);
    }

    /**
     * @param fileNode1  left node to be merged.
     * @param fileNode2  right node to be merged.
     * @param maxSymbols max symbols to read to RAM from file at one time.
     * @param outDir     the folder where the result will be placed.
     * @return node which is the result of the merging two nodes.
     */
    public static FileNode merge(FileNode fileNode1, FileNode fileNode2, int maxSymbols, String outDir) {
        System.out.println("merge " + fileNode1.name + " and " + fileNode2.name);
        FileNode child = new FileNode(fileNode1.getName() + "." + fileNode2.getName());

        try (BufferedReader readLeft = new BufferedReader(new FileReader(String.valueOf(Paths.get(outDir + "\\" + fileNode1.name + ".txt"))));
             BufferedReader readRight = new BufferedReader(new FileReader(String.valueOf(Paths.get(outDir + "\\" + fileNode2.name + ".txt"))));
             BufferedWriter writeChild = new BufferedWriter(new FileWriter(String.valueOf(Paths.get(outDir + "\\" + child.name + ".txt")), true))) {
            LinkedList<String> leftPayload = fileNode1.readNextChunk(readLeft, maxSymbols);
            LinkedList<String> rightPayload = fileNode2.readNextChunk(readRight, maxSymbols);
            long payloadCharCount = 0;
            List<String> payload = new ArrayList<>();
            while (!leftPayload.isEmpty() || !rightPayload.isEmpty()) {
                String minLine = retrieveMinLine(leftPayload, rightPayload);
                payloadCharCount += minLine.length();
                payload.add(minLine);
                if (payloadCharCount > maxSymbols) {
                    writeToFile(writeChild, payload);
                    payload = new LinkedList<>();
                }
                if (leftPayload.isEmpty()) {
                    leftPayload = fileNode1.readNextChunk(readLeft, maxSymbols);
                }
                if (rightPayload.isEmpty()) {
                    rightPayload = fileNode1.readNextChunk(readRight, maxSymbols);
                }
                if (leftPayload.isEmpty() && rightPayload.isEmpty()) {
                    writeToFile(writeChild, payload);
                    payload = new ArrayList<>();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("merged in " + child.name);
        deleteParentFiles(fileNode1, fileNode2, outDir);
        return child;
    }

    private static void deleteParentFiles(FileNode fileNode1, FileNode fileNode2, String outDir) {
        File parent1 = new File(outDir + "\\" + fileNode1.name + ".txt");
        File parent2 = new File(outDir + "\\" + fileNode2.name + ".txt");
        parent1.delete();
        parent2.delete();
    }

    private static String retrieveMinLine(LinkedList<String> leftPayload, LinkedList<String> rightPayload) {
        String minLine;
        if (leftPayload.isEmpty()) {
            minLine = rightPayload.removeFirst();
        } else if (rightPayload.isEmpty()) {
            minLine = leftPayload.removeFirst();
        } else if (leftPayload.getFirst().compareTo(rightPayload.getFirst()) < 0) {
            minLine = leftPayload.removeFirst();
        } else {
            minLine = rightPayload.removeFirst();
        }
        return minLine;
    }

    private static void writeToFile(BufferedWriter writeChild, List<String> payload) throws IOException {
        for (String s : payload) {
            writeChild.append(s);
            writeChild.newLine();
        }
    }

    private LinkedList<String> readNextChunk(BufferedReader bufferedReader, int maxSizeMb) throws IOException {
        long byteReadCount = 0;
        LinkedList<String> lines = new LinkedList<>();
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
    }
}
