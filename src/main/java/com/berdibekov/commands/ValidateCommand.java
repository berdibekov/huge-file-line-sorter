package com.berdibekov.commands;

import com.berdibekov.utils.Validator;
import picocli.CommandLine;

@CommandLine.Command(name = "validate", mixinStandardHelpOptions = true,
        description = "the program checks whether the lines of the output file are sorted lines of the source file")
public class ValidateCommand implements Runnable {
    @CommandLine.Parameters(index = "0", description = "source file")
    private String sourceFileName;
    @CommandLine.Parameters(index = "1", description = "converted")
    private String resultFileName;

    @Override
    public void run() {
        System.out.println("Start validation ...");
        Validator validator = new Validator();
        try {
            validator.validate(sourceFileName, resultFileName);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        System.out.println("Everything is OK");
    }
}
