package yan.foundation.driver.log;

import java.io.PrintStream;

public interface Logger {
    boolean hasError();

    boolean hasWarning();

    void flush(PrintStream err);
}
