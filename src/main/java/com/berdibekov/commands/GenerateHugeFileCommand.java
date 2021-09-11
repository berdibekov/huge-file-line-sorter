package com.berdibekov.commands;

import com.berdibekov.utils.HugeFileGenerator;
import lombok.SneakyThrows;
import picocli.CommandLine;

@CommandLine.Command(name = "generate", mixinStandardHelpOptions = true)
public class GenerateHugeFileCommand implements Runnable {

    @CommandLine.Parameters(index = "1", description = "max line length")
    private int maxLineLength;
    @CommandLine.Parameters(index = "2", description = "max lines number")
    private int maxLines;
    @CommandLine.Parameters(index = "0", description = "file name")
    private String fileName;

    @SneakyThrows
    @Override
    public void run() {
        System.out.println("Start generating file with random lines with max line length = " + maxLineLength +
                " lines in file = " + maxLines);
        HugeFileGenerator hugeFileGenerator = new HugeFileGenerator();
        hugeFileGenerator.generateHugeFile(fileName, maxLineLength, maxLines);
        System.out.println("File generated.");
    }
}
