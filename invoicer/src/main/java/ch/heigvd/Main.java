package ch.heigvd;

import ch.heigvd.cli.InvoiceCommand;
import ch.heigvd.cli.ModifyCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
    name = "invoicer",
    mixinStandardHelpOptions = true,
    version = "Invoicer 1.0",
    description = "A simple application to create invoices from a CSV file or create a new modifiedCSV file.",
    subcommands = {ModifyCommand.class, InvoiceCommand.class}
)

public class Main implements Runnable
{

    @Override
    public void run() {
        CommandLine.usage(this, System.out);
    }

    public static void main( String[] args ){
    CommandLine cmd = new CommandLine(new Main());
    int exitCode = cmd.execute(args);
    System.exit(exitCode);
    }
}
