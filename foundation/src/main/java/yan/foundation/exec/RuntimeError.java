package yan.foundation.exec;

public class RuntimeError extends RuntimeException {
    public RuntimeError(String message) {
        super("Runtime error: " + message);
    }
}
