package yan.foundation.driver.lang;

import yan.foundation.driver.log.DiagnosticLogger;

import java.io.PrintStream;
import java.util.Optional;
import java.util.function.Function;

// Modified from decaf

/**
 * A compiler phase accepts one argument and may produce a result.
 * It can be regarded as a "partial" function.
 *
 * @param <T> Input
 * @param <R> Output
 */
public abstract class Phase<T, R> implements Function<T, Optional<R>> {
    /**
     * Shared std out for all phases.
     */
    public static PrintStream out = System.out;

    /**
     * Shared std err for all phases.
     */
    public static PrintStream err = System.err;

    /**
     * Shared diagnostic logger for all phases.
     */
    public static DiagnosticLogger logger = new DiagnosticLogger();

    /**
     * Whether the phase is being interpreting.
     * <p>Why we need this field? Well, phase sometimes need be executed specially
     * when it is interpreted. For example, in the semantic analyze phase,
     * we need to track global symbol table between interpretation.</p>
     */
    public static boolean isInterpreting = false;

    /**
     * The name of this phase.
     */
    public String name;

    /**
     * When a phase is in panic mode, it return {@link Optional#empty()} if there is any error.
     * If not, it will still return value if return value of {@link Phase#transform(Object)} is not null.
     * <p>Default value: true</p>
     */
    protected boolean panicMode = true;

    public Phase() { this.name = this.getClass().getSimpleName(); }

    public Phase(String name) { this.name = name; }

    public Phase(boolean panicMode) { this.panicMode = panicMode; }

    public Phase(String name, boolean panicMode) {
        this.name = name;
        this.panicMode = panicMode;
    }

    /**
     * Entry of the actual transformation.
     *
     * @param input input
     * @return output
     */
    public abstract R transform(T input);

    /**
     * Entry of running the phase.
     *
     * @param t input
     * @return output (if succeeds)
     */
    @Override
    public Optional<R> apply(T t) {
        R out = transform(t);
        if ((logger.hasError() && panicMode) || out == null)
            return Optional.empty();
        return Optional.of(out);
    }

    /**
     * Pipe two phases. This will return a function which does "this" first, if succeeds, continue do {@code next} with
     * the previous result as input; or else exits and returns {@link Optional#empty}.
     * <p>
     * In terms of monad, this is just a Kleisli composition.
     *
     * @param next the next function
     * @param <V>  output type of next phase
     * @return the piped (Kleisli-composed) function
     */
    public <V> Phase<T, V> pipe(Phase<R, V> next) {
        return new Phase<>(Phase.this.name + "->" + next.name) {
            @Override
            public V transform(T input) {
                return apply(input).orElse(null);
            }

            @Override
            public Optional<V> apply(T t) {
                return Phase.this.apply(t).flatMap(next);
            }
        };
    }
}
