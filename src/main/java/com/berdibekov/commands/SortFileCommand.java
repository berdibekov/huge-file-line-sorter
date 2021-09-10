package com.berdibekov.commands;

import com.berdibekov.sort.FileNode;
import com.berdibekov.sort.FileSplitter;
import picocli.CommandLine;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

@CommandLine.Command(name = "sort",mixinStandardHelpOptions = true)
public class SortFileCommand implements Runnable{

    @CommandLine.Parameters(paramLabel = "file name to sort")
    String inputFileName;

    @Override
    public void run() {
        Path inputFilePath = Paths.get(inputFileName).toAbsolutePath();
        File inputFile = inputFilePath.toFile();
        String outDirectoryPath = inputFile.getParentFile().getAbsolutePath().concat("\\result");
        File outDirectory = new File(outDirectoryPath);
        if (!outDirectory.exists()) {
            outDirectory.mkdirs();
        }
        System.out.println("Start splitting file into small ones ...");
        FileSplitter fileSplitter = new FileSplitter();
        LinkedList<FileNode> smallFiles = fileSplitter.splitFile(inputFilePath.toAbsolutePath().toString(), 50 * 1000000);
        System.out.println("Start merging small files into each other ...");
        FileNode sortedFileNode = FileNode.mergeAll(smallFiles, outDirectoryPath, 50 * 1000000);
        File sortedFile = new File(outDirectoryPath + "\\" + sortedFileNode.getName() + ".txt");
        String resultPath = sortedFile.getParent().concat("\\" + inputFile.getName().replaceFirst("[.][^.]+$", "") + ".sorted." + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd.hh.mm.ss")) + ".txt");

        boolean isRenamed = sortedFile.renameTo(new File(resultPath));
        if (isRenamed) {
            System.out.println("new FIle created");
        } else {
            System.err.println("not created");
        }
        System.out.println("Successful ,sorted file is at " + resultPath);
    }
}
