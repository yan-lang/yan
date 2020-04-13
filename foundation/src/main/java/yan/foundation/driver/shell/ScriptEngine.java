package yan.foundation.driver.shell;

import java.io.PrintStream;
import java.util.List;

public interface ScriptEngine {
    int execute(String statement, PrintStream out, PrintStream err) throws Exception;

    void setTarget(String targetName);

    String getTarget();

    List<String> getTargets();

    String getDefaultTarget();

    String name();

    String version();

    String extension();
}
