package yan.foundation.driver.error;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ErrorCollector {

    // 单例模式

    public static ErrorCollector shared = new ErrorCollector();

    protected ErrorCollector() {
    }

    // 属性

    protected List<Unexpected> errors = new ArrayList<>();
    private int numOfError = 0;
    private int numOfWarning = 0;

    // 方法

    public List<Unexpected> getAllErrors() {
        return errors;
    }

    public boolean hasError() {
        return numOfError > 0;
    }

    public boolean hasWarning() {
        return numOfWarning > 0;
    }

    public void addError(Unexpected error) {
        errors.add(error);
        if (error instanceof Warning) numOfWarning += 1;
        else if (error instanceof Error) numOfError += 1;
    }

    public int numOfErrors() {
        return numOfError;
    }

    public void clean() {
        errors.clear();
        numOfError = 0;
        numOfWarning = 0;
    }

    public void flush(PrintWriter err) {
        for (var error : errors) {
            err.print(error);
            err.print('\n');
        }
    }
}

