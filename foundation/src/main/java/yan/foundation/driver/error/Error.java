package yan.foundation.driver.error;

public class Error extends Unexpected {
    public Error(int line, int column, String sourceName, String message, String context, String hint) {
        super(line, column, sourceName, message, context, hint);
    }

    @Override
    public String getType() {
        return "error";
    }
}
