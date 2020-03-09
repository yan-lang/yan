package yan.foundation.driver;

import yan.foundation.interpreter.Interpretable;

import java.io.PrintWriter;

public abstract class InterpretableLanguage<Tree> extends Language<Tree> implements Interpretable {

    public InterpretableLanguage(BaseConfig config) {
        super(config);
    }

    @Override
    public Object execute(String statement, PrintWriter out) throws Exception {
        config.out = out;
        config.target = getDefaultCompilerTarget();
        Phase<String, ?> task = (Phase<String, ?>) target2Phase.get(config.target);
        try {
            Object output = task.apply(statement);
        } catch (RuntimeException e) {
            e.printStackTrace(out);
        }
        // TODO: check if output is a R-Value, if it is, return the value.
        return null;
    }

}
