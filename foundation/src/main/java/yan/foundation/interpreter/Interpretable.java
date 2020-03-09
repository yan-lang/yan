package yan.foundation.interpreter;

import java.io.PrintWriter;

public interface Interpretable {
    Object execute(String statement, PrintWriter out) throws Exception;

    default String getName() {
        return this.getClass().getSimpleName();
    }

    default String getVersion() {
        return "1.0";
    }

    default String getExtension() {
        return "txt";
    }

}
