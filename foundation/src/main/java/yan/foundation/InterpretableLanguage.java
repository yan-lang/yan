package yan.foundation;

import yan.foundation.driver.BaseConfig;
import yan.foundation.driver.Phase;
import yan.foundation.driver.error.ErrorCollector;
import yan.foundation.interpreter.Interpretable;

import java.io.PrintWriter;

public abstract class InterpretableLanguage<TopLevel> extends Language<TopLevel> implements Interpretable {

    public InterpretableLanguage(BaseConfig config) {
        super(config);
    }

    public InterpretableLanguage() {
        super();
    }

    @Override
    public Object execute(String statement, PrintWriter out) throws Exception {
        config.out = out;
        config.err = out;
        config.target = getDefaultCompilerTarget();
        Phase<String, ?> task = (Phase<String, ?>) target2Task.get(config.target);
        // clear errors
        ErrorCollector.shared.clean();
        try {
            Object output = task.apply(statement);
        } catch (RuntimeException e) {
            e.printStackTrace(out);
        }
        // TODO: check if output is a R-Value, if it is, return the value.
        return null;
    }

}
