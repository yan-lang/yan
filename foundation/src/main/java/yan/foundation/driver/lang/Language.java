package yan.foundation.driver.lang;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

/**
 * Language
 */
public abstract class Language {

    abstract public List<CompilerTarget<Code, ?>> getCompilerTargets();

    abstract public List<InterpreterTarget<Code, ?>> getInterpreterTargets();

    abstract public String version();

    abstract public String name();

    abstract public String extension();

    /**
     * Tell whether this language is interpretable.
     *
     * <p>The default implementation return {@code !getInterpreterTargets().isEmpty()}. Override this method
     * if you want to run interpreter even if there is no interpreter targets.</p>
     *
     * @return true if it is, otherwise false.
     */
    public boolean interpretable() {
        return !getInterpreterTargets().isEmpty();
    }

    public int compile(File input, File output, String targetName) {
        Code code = readCode(input);
        if (code == null) return ExitCode.InvalidInputFile;
        var target = getCompilerTargets().stream()
                                         .filter(t -> t.name.equals(targetName)).findFirst().orElseThrow();
        return target.compile(code, output);
    }

    public int interpret(Code input, String targetName) {
        return interpret(input, targetName, System.out, System.err);
    }

    public int interpret(Code input, String targetName, PrintStream out, PrintStream err) {
        var target = getInterpreterTargets().stream()
                                            .filter(t -> t.name.equals(targetName)).findFirst().orElseThrow();
        return target.interpret(input, out, err);
    }

    private Code readCode(File input) {
        try {
            var reader = new FileInputStream(input);
            var content = new String(reader.readAllBytes());
            return new Code(input.getName(), content);
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
            return null;
        }
    }
}
