package yan.foundation.driver.log;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class DiagnosticLogger implements Logger {

    protected List<Diagnostic> diagnostics = new ArrayList<>();
    protected int numOfError = 0;
    protected int numOfWarning = 0;

    // 方法

    public void log(Diagnostic error) {
        diagnostics.add(error);
        if (error.is(Diagnostic.WARNING)) numOfWarning += 1;
        else if (error.is(Diagnostic.ERROR)) numOfError += 1;
    }

    public void clear() {
        diagnostics.clear();
        numOfError = 0;
        numOfWarning = 0;
    }

    public List<Diagnostic> getAllDiagnostics() { return diagnostics; }

    public int numOfErrors() { return numOfError; }

    public int numOfWarning() { return numOfWarning; }

    public boolean hasError() { return numOfError > 0; }

    public boolean hasWarning() { return numOfWarning > 0; }

    @Override
    public void flush(PrintStream err) {
        for (var diagnostic : diagnostics) {
            err.print(diagnostic.getMessage());
            err.print('\n');
        }
    }

}

