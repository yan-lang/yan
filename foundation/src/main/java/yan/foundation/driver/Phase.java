package yan.foundation.driver;

import yan.foundation.driver.error.ErrorCollector;
import yan.foundation.utils.formatter.PhaseFormatter;

import java.util.Optional;

/**
 * Compilation of a Decaf program is processed phase-by-phase. Each phase transforms a kind of language representation
 * into another.
 *
 * @param <In>  input
 * @param <Out> output
 * @see Task
 */
public abstract class Phase<In, Out> implements Task<In, Out> {
    /**
     * Name.
     */
    public final String name;

    /**
     * Compiler configuration.
     */
    protected final BaseConfig config;

    /**
     * Global Error Collector
     */
    protected ErrorCollector errorCollector = ErrorCollector.shared;

    protected PhaseFormatter<? super Out> formatter;
    protected PhaseFormatter<? super Out> shellFormatter;

    public Phase(String name, BaseConfig config) {
        this.name = name;
        this.config = config;
    }

    /**
     * Entry of the actual transformation.
     *
     * @param input input
     * @return output
     */
    public abstract Out transform(In input);

    /**
     * A phase is said to be <em>successful</em>, if and only if no errors occur (i.e. {@code !hasError()}).
     * When a phase is successful, this method will be executed.
     *
     * @param output output of the transformation
     */
    protected void onSucceed(Out output) {
        if (formatter != null && config.target.equals(formatter.targetName())) {
            String text = formatter.toString(output);
            config.out.print(text);
        }
    }

    /**
     * Entry of running the phase.
     *
     * @param in input
     * @return output (if succeeds)
     */
    @Override
    public Optional<Out> apply(In in) {
        var out = transform(in);
        if (hasError()) {
            errorCollector.flush(config.err);
            return Optional.empty();
        }

        onSucceed(out);
        return Optional.of(out);
    }

    /**
     * Return if there is any yan.common.error occurred in this phase.
     *
     * @return true if there is an yan.common.error.
     */
    protected boolean hasError() {
        return errorCollector.hasError();
    }

    @Override
    public String toString() {
        return "Phase{" +
                "name='" + name + '\'' +
                '}';
    }
}
