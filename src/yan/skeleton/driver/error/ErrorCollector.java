package yan.skeleton.driver.error;

import java.util.ArrayList;
import java.util.List;

public class ErrorCollector {

    // 单例模式

    public static ErrorCollector shared = new ErrorCollector();

    protected ErrorCollector() {
    }

    // 属性

    protected List<BaseError> errors = new ArrayList<>();
    private boolean _hasError = false;
    private boolean _hasWarning = false;

    // 方法

    public List<BaseError> getAllErrors() {
        return errors;
    }

    public boolean hasError() {
        return _hasError;
    }

    public boolean hasWarning() {
        return _hasWarning;
    }

    public void addError(BaseError error) {
        errors.add(error);
        if (error instanceof Warning) _hasWarning = true;
        else if(error instanceof Error) _hasError = true;
    }
}

