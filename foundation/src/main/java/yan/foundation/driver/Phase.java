package yan.foundation.driver;

import yan.foundation.driver.error.ErrorCollector;
import yan.foundation.printer.PhasePrinter;

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

    protected PhasePrinter<Out> printer;

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
        if (printer != null && config.target.equals(printer.targetName())) {
            String text = printer.toString(output);
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
            return Optional.empty();
        }

        onSucceed(out);
        return Optional.of(out);
    }

    /**
     * Return if there is any error occurred in this phase.
     *
     * @return true if there is an error.
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