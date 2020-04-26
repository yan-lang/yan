package yan.foundation.driver.lang;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class CompilerTarget<In, Out> extends Target<In, Out> {
    public CompilerTarget(String name, Phase<In, Out> phase, Formatter<Out> formatter) {
        super(name, phase, formatter);
    }

    public int compile(In input, File output) {
        Phase.logger.clear();
        Phase.isInterpreting = false;
        var out = phase.apply(input);
        if (out.isPresent()) {
            try {
                PrintStream writer = new PrintStream(output);
                writer.print(formatter.format(out.get()));
                writer.close();
                return ExitCode.Success;
            } catch (FileNotFoundException e) {
                System.err.println(e.getLocalizedMessage());
                return ExitCode.ErrorCreatingFile;
            }
        } else {
            Phase.logger.flush(System.err);
            return ExitCode.PhaseError;
        }
    }
}
