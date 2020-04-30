package yan.foundation.driver.lang;

import java.io.PrintStream;

public class InterpreterTarget<In, Out> extends Target<In, Out> {
    public InterpreterTarget(String name, Phase<In, Out> phase, Formatter<Out> formatter) {
        super(name, phase, formatter);
    }

    public int interpret(In input, PrintStream out, PrintStream err) {
        Phase.logger.clear();
        Phase.isInterpreting = true;
        var output = phase.apply(input);

        output.ifPresent(o -> out.println(formatter.format(o)));
        if (Phase.logger.hasError()) {
            Phase.logger.flush(err);
            return ExitCode.PhaseError;
        }
        return ExitCode.Success;
    }
}
