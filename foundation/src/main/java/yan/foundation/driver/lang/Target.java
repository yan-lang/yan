package yan.foundation.driver.lang;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Objects;

/**
 * {@code Compile Target}类似于Maven里的Build Target，一个target可能包含好几个Phase。
 * 常见的Compiler Target包括lex（词法分析），parse（语法分析）等。
 *
 * @param <In>  Target执行任务的输入
 * @param <Out> Target执行任务的输出
 */
public class Target<In, Out> {
    public String name;
    public Phase<In, Out> phase;
    public Formatter<Out> cformatter;
    public Formatter<Out> iformatter;
    public Compatibility compatibility;

    Target(Builder<In, Out> builder) {
        this.name = builder.name;
        this.phase = builder.phase;
        this.cformatter = builder.cformatter;
        this.iformatter = builder.iformatter;
        this.compatibility = builder.compatibility;
    }

    public int compile(In input, File output) {
        var out = phase.apply(input);
        if (out.isPresent()) {
            try {
                PrintStream writer = new PrintStream(output);
                writer.print(cformatter.format(out.get()));
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

    public int interpret(In input, PrintStream out, PrintStream err) {
        Phase.logger.clear();
        var output = phase.apply(input);
        if (output.isPresent()) {
            out.println(iformatter.format(output.get()));
            return ExitCode.Success;
        } else {
            Phase.logger.flush(err);
            return ExitCode.PhaseError;
        }
    }

    public boolean isCompilerCompatible() {
        return compatibility == Compatibility.COMPILER || compatibility == Compatibility.BOTH;
    }

    public boolean isInterpreterCompatible() {
        return compatibility == Compatibility.INTERPRETER || compatibility == Compatibility.BOTH;
    }

    public enum Compatibility {
        INTERPRETER, COMPILER, BOTH;

        public boolean has(Compatibility compatibility) {
            if (this == BOTH) return true;
            else return this == compatibility;
        }
    }

    public static class Builder<In, Out> {
        private String name;
        private Phase<In, Out> phase;
        private Formatter<Out> cformatter;
        private Formatter<Out> iformatter;
        private Compatibility compatibility;

        public Builder<In, Out> name(String name) {
            this.name = name;
            return this;
        }

        public Builder<In, Out> phase(Phase<In, Out> phase) {
            this.phase = phase;
            return this;
        }

        public Builder<In, Out> cformatter(Formatter<Out> cformatter) {
            this.cformatter = cformatter;
            return this;
        }

        public Builder<In, Out> iformatter(Formatter<Out> iformatter) {
            this.iformatter = iformatter;
            return this;
        }

        public Builder<In, Out> compatibility(Compatibility compatibility) {
            this.compatibility = compatibility;
            return this;
        }

        public Target<In, Out> build() {
            Objects.requireNonNull(name);
            Objects.requireNonNull(phase);
            Objects.requireNonNull(compatibility);
            if (compatibility.has(Compatibility.COMPILER)) { Objects.requireNonNull(cformatter); }
            if (compatibility.has(Compatibility.INTERPRETER)) { Objects.requireNonNull(iformatter); }
            return new Target<>(this);
        }
    }
}
