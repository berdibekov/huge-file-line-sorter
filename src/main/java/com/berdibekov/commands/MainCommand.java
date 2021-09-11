package com.berdibekov.commands;

import picocli.CommandLine;

@CommandLine.Command(name = "fs", subcommands = {SortFileCommand.class, GenerateHugeFileCommand.class, ValidateCommand.class},
        mixinStandardHelpOptions = true)
public class MainCommand implements Runnable{

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec spec;

    @Override
    public void run() {
        throw new CommandLine.ParameterException(spec.commandLine(), "Missing required sub command");
    }
}
