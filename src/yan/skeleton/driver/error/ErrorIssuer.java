package yan.skeleton.driver.error;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Decaf error issuer. The error must be a subclass of {@link BaseError}.
 */
public interface ErrorIssuer {
    ArrayList<BaseError> errors = new ArrayList<>();

    /**
     * Add an error.
     *
     * @param error Decaf error
     */
    default void issue(BaseError error) {
        errors.add(error);
    }

    /**
     * Has any error been added?
     *
     * @return true/false
     */
    default boolean hasError() {
        return !errors.isEmpty();
    }

    /**
     * Print out error messages, sorted by their error positions.
     *
     * @param to where to print
     */
    default void printErrors(PrintStream to) {
        errors.forEach(to::println);
    }
}
