package yan.foundation.driver.error;

import java.util.ArrayList;
import java.util.List;

public class ErrorCollector {

    // 单例模式

    public static ErrorCollector shared = new ErrorCollector();

    protected ErrorCollector() {
    }

    // 属性

    protected List<BaseError> errors = new ArrayList<>();
    private int _numOfError = 0;
    private int _numOfWarning = 0;

    // 方法

    public List<BaseError> getAllErrors() {
        return errors;
    }

    public boolean hasError() {
        return _numOfError > 0;
    }

    public boolean hasWarning() {
        return _numOfWarning > 0;
    }

    public void addError(BaseError error) {
        errors.add(error);
        if (error instanceof Warning) _numOfWarning += 1;
        else if (error instanceof Error) _numOfError += 1;
    }

    public int numOfErrors() {
        return _numOfError;
    }

}

